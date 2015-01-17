package authentikat.jwt

import authentikat.json.SimpleJsonSerializer

import org.json4s._
import org.json4s.jackson.JsonMethods._
import util.control.Exception.allCatch

case class JwtHeader(algorithm: Option[String],
                     contentType: Option[String],
                     typ: Option[String]) {

  def asJsonString: String = {
    val toSerialize =
      algorithm.map(x => ("alg", x)).toSeq ++
        contentType.map(x => ("cty", x)).toSeq ++
        typ.map(x => ("typ", x)).toSeq

    SimpleJsonSerializer(toSerialize)
  }
}

object JwtHeader {

  import org.json4s.DefaultFormats

  implicit val formats = DefaultFormats

  def apply(algorithm: String = null, contentType: String = null, typ: String = "JWT"): JwtHeader = {
    JwtHeader(Option(algorithm), Option(contentType), Option(typ))
  }

  def fromJsonString(jsonString: String): JwtHeader = {
    val ast = parse(jsonString)

    val alg = (ast \ "alg").extract[Option[String]]
    val cty = (ast \ "cty").extract[Option[String]]
    val typ = (ast \ "typ").extract[Option[String]]

    JwtHeader(alg, cty, typ)
  }

  def fromJsonStringOpt(jsonString: String): Option[JwtHeader] = allCatch opt {
    fromJsonString(jsonString)
  }
}
