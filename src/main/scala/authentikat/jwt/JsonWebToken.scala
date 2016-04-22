package authentikat.jwt

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString
import org.apache.commons.codec.binary.Base64.decodeBase64
import util.control.Exception.allCatch

object JsonWebToken {
  import JsonWebSignature.HexToString._

  @deprecated(message = "please specify the key as a byte array", since = "0.4.3")
  def apply(header: JwtHeader, claims: JwtClaimsSet, key: String): String = apply(header, claims, key.getBytes)

  /**
   * Produces a JWT.
   * @param header
   * @param claims
   * @param key
   * @return
   */
  def apply(header: JwtHeader, claims: JwtClaimsSet, key: Array[Byte]): String = {
    val encodedHeader = encodeBase64URLSafeString(header.asJsonString.getBytes("UTF-8"))
    val encodedClaims = encodeBase64URLSafeString(claims.asJsonString.getBytes("UTF-8"))

    val signingInput = encodedHeader + "." + encodedClaims

    val encodedSignature: String = encodeBase64URLSafeString(
        JsonWebSignature(header.algorithm.getOrElse("none"), signingInput, key))

    signingInput + "." + encodedSignature
  }

  /**
   * Extractor method
   * @param jwt
   * @return
   */

  def unapply(jwt: String): Option[(JwtHeader, JwtClaimsSetJValue, String)] = {
    jwt.split("\\.") match {
      case Array(providedHeader, providedClaims, providedSignature) =>
        import org.json4s.DefaultFormats
        implicit val formats = DefaultFormats

        val headerJsonString = new String(decodeBase64(providedHeader), "UTF-8")
        val header = JwtHeader.fromJsonStringOpt(headerJsonString)
        val optClaimsSet = allCatch opt {
          parse(new String(decodeBase64(providedClaims), "UTF-8"))
        }

        if(header.isEmpty || optClaimsSet.isEmpty)
          None
        else {
          val claimsSet = JwtClaimsSetJValue(optClaimsSet.get)

          val signature = providedSignature

          Some(header.get, claimsSet, signature)
        }
      case _ =>
        None
    }
  }


  @deprecated(message = "please specify the key as a byte array", since = "0.4.3")
  def validate(jwt: String, key: String): Boolean = validate(jwt, key.getBytes)

  /**
   * Validate a JWT claims set against a secret key.
   * Validates an un-parsed jwt as parsing it before validating it is probably un-necessary.
   * Note this does NOT validate exp or other validation claims - it only validates the claims against the hash.
   * @param jwt
   * @param key
   * @return
   */
  def validate(jwt: String, key: Array[Byte]): Boolean = {

    import org.json4s.DefaultFormats
    implicit val formats = DefaultFormats

    jwt.split("\\.") match {
      case Array(providedHeader, providedClaims, providedSignature) =>

        val headerJsonString = new String(decodeBase64(providedHeader), "UTF-8")
        val header = JwtHeader.fromJsonStringOpt(headerJsonString).getOrElse(JwtHeader(None, None, None))

        val signature = encodeBase64URLSafeString(
          JsonWebSignature(header.algorithm.getOrElse("none"), providedHeader + "." + providedClaims, key))

        providedSignature.contentEquals(signature)
      case _ =>
        false
    }
  }

}
