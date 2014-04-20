package authentikat.jwt

import authentikat.json.SimpleJsonSerializer
import org.json4s._

sealed trait JwtClaimsSet {
  def asJsonString: String
}

case class JwtClaimsSetMap(claims: Map[String, Any]) extends JwtClaimsSet {
  def asJsonString: String = {
    SimpleJsonSerializer(claims.map(x => (x._1, x._2)).toSeq)
  }
}

case class JwtClaimsSetJvalue(jvalue: JValue) extends JwtClaimsSet  {
  import org.json4s.jackson.JsonMethods._
  implicit val formats = org.json4s.DefaultFormats

  def asJsonString: String = {
    compact(jvalue)
  }

  def asValues: Map[String, String] = {
    jvalue.extract[Map[String, String]]
  }
}

case class JwtClaimsSetJsonString(json: String) extends JwtClaimsSet  {
  def asJsonString: String = {
    json
  }
}