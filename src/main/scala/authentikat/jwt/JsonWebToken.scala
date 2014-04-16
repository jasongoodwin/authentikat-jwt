package authentikat.jwt

object JsonWebToken {
  private val base64Encoder = new sun.misc.BASE64Encoder
  private val base64Decoder = new sun.misc.BASE64Decoder

  /**
   * Produce a jwt
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

}

