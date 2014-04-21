package authentikat.jwt

import org.json4s._
import org.json4s.jackson.JsonMethods._

object JsonWebToken {
  import JsonWebSignature.HexToString._

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

    val encryptedClaims: String = JsonWebSignature(header.algorithm.getOrElse("none"), encodedClaims, key)

    Seq(encodedHeader,
      encodedClaims,
      encryptedClaims).mkString(".")
  }

  /**
   * Extractor method
   * @param jwt
   * @return
   */

  def unapply(jwt: String): Option[(JwtHeader, JwtClaimsSetJValue, String)] = {
    val sections = jwt.split("\\.")

    sections.length match {
      case 3 =>
        import org.json4s.DefaultFormats
        implicit val formats = DefaultFormats

        val headerJsonString = new String(base64Decoder.decodeBuffer(sections(0)), "UTF-8")
        val header = JwtHeader.fromJsonString(headerJsonString)

        val claimsSet = JwtClaimsSetJValue(parse(new String(base64Decoder.decodeBuffer(sections(1)), "UTF-8")))

        val signature = sections(2)

        Some(header, claimsSet, signature)
      case _ =>
        None
    }
  }

  /**
   * Validate a JWT claims set against a secret key.
   * Validates an un-parsed jwt as parsing it before validating it is probably un-necessary.
   * Note this does NOT validate exp or other validation claims - it only validates the claims against the hash.
   * @param jwt
   * @param key
   * @return
   */

  def validate(jwt: String, key: String): Boolean = {

    import org.json4s.DefaultFormats
    implicit val formats = DefaultFormats

    val sections = jwt.split("\\.")

    val headerJsonString = new String(base64Decoder.decodeBuffer(sections(0)), "UTF-8")
    val header = JwtHeader.fromJsonString(headerJsonString)

    val signature = JsonWebSignature(header.algorithm.getOrElse("none"), sections(1), key)

    sections(2).contentEquals(signature)
  }

}

