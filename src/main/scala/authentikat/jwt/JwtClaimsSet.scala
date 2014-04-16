package authentikat.jwt

import spray.json._
import java.text.SimpleDateFormat
import java.util.TimeZone

case class JwtClaimsSet(claims: Map[String, String]) {

  def asJsonString: String = {
    import DefaultJsonProtocol._
    claims.toJson.compactPrint
  }

  def asBase64EncodedJson: String = {
    this.asJsonString
  }
}

/**
 * Should be used for dates.
 */
object JwtClaimsSet {
  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
  dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
}

