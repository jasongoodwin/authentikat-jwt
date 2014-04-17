package authentikat.jwt

import spray.json._
import spray.json.DefaultJsonProtocol._
import scala.Some

//TODO there should by an apply method in companion object to allow nulls to be used instead of Option on created. Breaks spray-json. Need to get off of Spray json and onto something faster anyways.

case class JwtHeader(alg: Option[String] = None,
                     typ: Option[String] = None,
                     cty: Option[String] = Some("JWT")) {
  def asJsonString: String = {
    implicit val jsonFormat = jsonFormat3(JwtHeader)
    this.toJson.toString
  }
}
