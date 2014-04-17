package authentikat.jwt

import spray.json._
import spray.json.DefaultJsonProtocol._

object JsonWebToken {

  private implicit val jsonFormat = jsonFormat3(JwtHeader)
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
        val header = new String(base64Decoder.decodeBuffer(sections(0)), "UTF-8").asJson.convertTo[JwtHeader]
        val claims = JwtClaimsSet(new String(base64Decoder.decodeBuffer(sections(1)), "UTF-8").asJson.convertTo[Map[String, String]])
        val signature = sections(2)

        Some(header, claims, signature)
      case _ =>
        None
    }
  }

  def validate(jwt: String, key: String): Boolean = {
    val sections = jwt.split("\\.")
    val header = new String(base64Decoder.decodeBuffer(sections(0)), "UTF-8").asJson.convertTo[JwtHeader]
    val encryptedClaims = base64Encoder.encode(JsonWebSignature(header.alg.getOrElse("none"), sections(1), key))

    sections(2) == encryptedClaims
  }

}

