package authentikat.jwt

import org.json4s._
import org.json4s.jackson.JsonMethods._

object JsonWebToken {

  private val base64Encoder = new sun.misc.BASE64Encoder
  private val base64Decoder = new sun.misc.BASE64Decoder

  /**
   * Produces a JWT.
   * @param header
   * @param claims
   * @param key
   * @return
   */

  def apply(header: JwtHeader, claims: JwtClaimsSet, key: String): String = {
    val encodedHeader = base64Encoder.encode(header.asJsonString.getBytes("UTF-8"))
    val encodedClaims = base64Encoder.encode(claims.asJsonString.getBytes("UTF-8"))
    val encryptedClaims = base64Encoder.encode(JsonWebSignature(header.alg.getOrElse("none"), encodedClaims, key))

    Seq(encodedHeader, encodedClaims, encryptedClaims).mkString(".")
  }

  def unapply(jwt: String): Option[(JwtHeader, JwtClaimsSet, String)] = {
    val sections = jwt.split("\\.")

    sections.length match {
      case 3 =>
        import org.json4s.DefaultFormats
        implicit val formats = DefaultFormats


        val headerJsonString = new String(base64Decoder.decodeBuffer(sections(0)), "UTF-8")
        val header = JwtHeader.fromJsonString(headerJsonString)

        val claims = JwtClaimsSet(parse(new String(base64Decoder.decodeBuffer(sections(1)), "UTF-8")).extract[Map[String, String]])
        val signature = sections(2)

        Some(header, claims, signature)
      case _ =>
        None
    }
  }

  def validate(jwt: String, key: String): Boolean = {

    import org.json4s.DefaultFormats
    implicit val formats = DefaultFormats

    val sections = jwt.split("\\.")

    val headerJsonString = new String(base64Decoder.decodeBuffer(sections(0)), "UTF-8")
    val header = JwtHeader.fromJsonString(headerJsonString)

    val encryptedClaims = base64Encoder.encode(JsonWebSignature(header.alg.getOrElse("none"), sections(1), key))

    sections(2) == encryptedClaims
  }

}

