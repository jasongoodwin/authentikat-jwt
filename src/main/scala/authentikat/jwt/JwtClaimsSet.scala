package authentikat.jwt

import authentikat.json.JsonSerializer

case class JwtClaimsSet(claims: Map[String, Any]) {

  def asJsonString: String = {
    JsonSerializer(claims.map(x => (x._1, x._2)).toSeq)
  }

  def asBase64EncodedJson: String = {
    this.asJsonString
  }
}
