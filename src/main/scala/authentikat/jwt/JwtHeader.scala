package authentikat.jwt

import scala.Some
import authentikat.json.JsonSerializer

//TODO there should by an apply method in companion object to allow nulls to be used instead of Option on created. Breaks spray-json. Need to get off of Spray json and onto something faster anyways.

case class JwtHeader(alg: Option[String] = None,
                     typ: Option[String] = None,
                     cty: Option[String] = Some("JWT")) {

  def asJsonString: String = {
    val toSerialize =
      alg.map(x => ("alg", x)).toSeq ++
      typ.map(x => ("typ", x)).toSeq ++
      cty.map(x => ("cty", x))

    JsonSerializer(toSerialize)
  }
}
