package authentikat

import java.util.Date

case class JsonWebTokenUnencoded(header: JwtHeader, claims: JwtClaimsSet)

object JsonWebToken {
  val base64Encoder = new sun.misc.BASE64Encoder
  val base64Decoder = new sun.misc.BASE64Decoder

  def apply(header: JwtHeader, claims: JwtClaimsSet) = {

  }
}

/**
 * JWT/JWS Header.
 * @param algorithm
 * @param mimeType
 */

case class JwtHeader(algorithm: String = null,
                      mimeType: String = null) {
  val contentType = "JWT"
}

object JwtHeader {
  object Keys {
    val contentType = "cty"
    val algorithm = "alg"
    val mimeType = "typ"
  }
}

/**
 * JWT Claims
 * @param privateClaims Any extra fields can be stored in here. Eg userName, userId for a user
 * @param issuer
 * @param subject
 * @param audience
 * @param expirationTime
 * @param notBefore
 * @param issuedAt
 * @param jwtId
 */

case class JwtClaimsSet(privateClaims: Map[String, String] = Map.empty[String, String],
                        issuer: Option[String],
                        subject: Option[String],
                        audience: Option[String],
                        expirationTime: Option[Date],
                        notBefore: Option[Date],
                        issuedAt: Option[Date],
                        jwtId: Option[String])

object JwtClaimsSet {
  object Keys {
    val issuerKey = "iss"
    val subjectKey = "sub"
    val audienceKey = "aud"
    val expirationTimeKey = "exp"
    val nodBeforeKey = "nfb"
    val issuedAt = "iat"
    val jwtId = "jti"
  }
}