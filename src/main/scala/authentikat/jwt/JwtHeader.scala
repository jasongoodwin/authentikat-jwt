package authentikat.jwt

import authentikat.json.JsonSerializer

import org.json4s._
import org.json4s.jackson.JsonMethods._

case class JwtHeader(alg: Option[String],
                     typ: Option[String],
                     cty: Option[String]) {

  def asJsonString: String = {
    val toSerialize =
      alg.map(x => ("alg", x)).toSeq ++
        typ.map(x => ("typ", x)).toSeq ++
        cty.map(x => ("cty", x))

    JsonSerializer(toSerialize)
  }
}

object JwtHeader {

  import org.json4s.DefaultFormats

  implicit val formats = DefaultFormats

  def apply(alg: String = null, typ: String = null, cty: String = "JWT"): JwtHeader = {
    JwtHeader(Option(alg), Option(typ), Option(cty))
  }

  def fromJsonString(jsonString: String): JwtHeader = {
    val ast = parse(jsonString)

    val alg = (ast \ "alg").extract[Option[String]]
    val typ = (ast \ "typ").extract[Option[String]]
    val cty = (ast \ "cty").extract[Option[String]]

    JwtHeader(alg, typ, Option(cty.getOrElse("JWT")))
  }
}