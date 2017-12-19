package authentikat.jwt

import authentikat.jws.{ JwsSigner, JwsVerifier }
import org.apache.commons.codec.binary.Base64.{ decodeBase64, encodeBase64URLSafeString }
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.control.Exception.allCatch

object JsonWebToken {

  /**
   * Produces a JWT.
   * @param header
   * @param claims
   * @return
   */

  def apply(header: JwtHeader, claims: JwtClaimsSet, signer: JwsSigner): String = {
    val encodedHeader = encodeBase64URLSafeString(header.asJsonString.getBytes("UTF-8"))
    val encodedClaims = encodeBase64URLSafeString(claims.asJsonString.getBytes("UTF-8"))

    val signingInput = encodedHeader + "." + encodedClaims

    signingInput + "." + encodeBase64URLSafeString(signer.apply(signingInput))
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

        if (header.isEmpty || optClaimsSet.isEmpty)
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
}
