package authentikat.jwt

import spray.json._
import spray.json.DefaultJsonProtocol._
import authentikat.jwt.JwtHeader

object JsonWebToken {
  private val base64Encoder = new sun.misc.BASE64Encoder
  private val base64Decoder = new sun.misc.BASE64Decoder

  /**
   * Produces a JWT.
   * TODO make another apply with no alg or key (eg alg=NONE). Take algorithm Type in addition to string.
   * @param header
   * @param claims
   * @param algorithm
   * @param key
   * @return
   */

  def apply(header: JwtHeader, claims: JwtClaimsSet, algorithm: String, key: String): String = {
    val encodedHeader = base64Encoder.encode(header.asJsonString.getBytes("UTF-8"))
    val encodedClaims = base64Encoder.encode(claims.asJsonString.getBytes("UTF-8"))
    val encryptedClaims = JsonWebSignature(algorithm, encodedClaims, key).toString
    Seq(encodedHeader, encodedClaims, encryptedClaims).mkString(".")
  }

  def unapply(jwt: String): Option[(JwtHeader, JwtClaimsSet, String)] = {
    val sections = jwt.split("\\.")



    sections.length match {
      case 3 =>
        implicit val jsonFormat = jsonFormat3(JwtHeader)

        //TODO memoize the encoded strings. only decode and render if no hit
        val header = new String(base64Decoder.decodeBuffer(sections(0)), "UTF-8").asJson.convertTo[JwtHeader]
        val claims = JwtClaimsSet(new String(base64Decoder.decodeBuffer(sections(1)), "UTF-8").asJson.convertTo[Map[String, String]])
        val signature = sections(2)

        Some(header, claims, signature)
      case _ =>
        None
    }
  }
}

