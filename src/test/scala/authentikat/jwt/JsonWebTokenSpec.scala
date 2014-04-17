package authentikat.jwt

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class JsonWebTokenSpec  extends FunSpec with ShouldMatchers {

  describe("JsonWebToken") {
    val header = JwtHeader(Some("hai"))
    val claims = JwtClaimsSet(Map("Hey" -> "foo"))

    it("should have three parts") {
      val result = JsonWebToken.apply(header, claims, "HS256", "secretkey")
      result.split("\\.").length should equal(3)
    }

    it("should be extracted by extractor") {
      val jwt = JsonWebToken.apply(header, claims, "HS256", "secretkey")
      val result = jwt match {
        case JsonWebToken(x,y,z) =>
          true
        case x =>
          println(x)
          false
      }

      result should equal(true)
    }
  }

  describe("JwtHeader") {
    it("should render to json as per spec") {
      val header = JwtHeader(Some("algorithm"), Some("mimeType"))
      val expectedJson = "{\"alg\":\"algorithm\",\"typ\":\"mimeType\",\"cty\":\"JWT\"}"

      header.asJsonString should equal(expectedJson)
    }
  }

  describe("JwtClaimsSet") {

    //TODO deal with other data types...
//    val date = new Date
//    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
//    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
//    val dateIso8601 = dateFormat.format(date)
    
    val claimsSet = JwtClaimsSet(Map("privateClaim" -> "foo", "iss" -> "Issuer"))
    val claimsSetJsonString = claimsSet.asJsonString

    it("should contain private claims") {
      claimsSetJsonString should include("\"privateClaim\":\"foo\"")
    }

    it("should contain iss (Issuer) claim") {
      claimsSetJsonString should include("\"iss\":\"Issuer\"")
    }
  }
}
