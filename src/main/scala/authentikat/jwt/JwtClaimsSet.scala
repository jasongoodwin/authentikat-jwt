package authentikat.jwt

import spray.json._

//TODO needs to handle data types for eg iso-8601 date formatting (see commit history). Recommend type classes with implicit conversions

case class JwtClaimsSet(claims: Map[String, String]) {

  def asJsonString: String = {
    import DefaultJsonProtocol._
    claims.toJson.compactPrint
  }

  def asBase64EncodedJson: String = {
    this.asJsonString
  }
}
