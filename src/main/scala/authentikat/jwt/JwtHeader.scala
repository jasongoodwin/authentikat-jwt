package authentikat.jwt

import spray.json._
import spray.json.DefaultJsonProtocol._
import scala.Some

case class JwtHeader(algorithm: String = null,
                     mimeType: String = null,
                     contentType: String = "JWT") {

  private case class JwtHeader(alg: Option[String], typ: Option[String], cty: Option[String]= Some("JWT"))

  def asJsonString: String = {
    implicit val jsonFormat = jsonFormat3(JwtHeader)
    val header = JwtHeader(Option(algorithm), Option(mimeType))
    header.toJson.toString
  }
}
