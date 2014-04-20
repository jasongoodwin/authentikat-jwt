package authentikat.jwt

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import java.util.{Date, TimeZone}
import java.text.SimpleDateFormat

class JsonWebTokenSpec extends FunSpec with ShouldMatchers {

  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._


  describe("JsonWebToken") {
    val header = JwtHeader("HS256")
    val claims = JwtClaimsSetMap(Map("Hey" -> "foo"))
    val jvalueClaims = render("Hey" -> ("Hey" -> "foo") )

    it("should have three parts for a token created with claims map claims") {
      val result = JsonWebToken.apply(header, claims, "secretkey")
      result.split("\\.").length should equal(3)
    }

    it("should have three parts for a token created with a jvalue claims") {
      val result = JsonWebToken.apply(header, JwtClaimsSetJvalue(jvalueClaims), "secretkey")
      result.split("\\.").length should equal(3)
    }

    it("should have three parts for a token created with a string claims") {
      val result = JsonWebToken.apply(header, JwtClaimsSetJvalue("{\"json\":42}"), "secretkey")
      result.split("\\.").length should equal(3)
    }

    it("should produce the same results for all three claims types") {
      val expectedResult = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJIZXkiOiJmb28ifQ==.e89b48f1e2016b78b805c1430d5a139e62bba9237ff3e34bad56dae6499b2647"

      val res1 = JsonWebToken.apply(header, JwtClaimsSetMap(Map("Hey" -> "foo")), "secretkey")
      val res2 = JsonWebToken.apply(header, JwtClaimsSetJvalue(("Hey" -> "foo")), "secretkey")
      val res3 = JsonWebToken.apply(header, JwtClaimsSetJsonString("{\"Hey\":\"foo\"}"), "secretkey")

      res1 should equal(expectedResult)
      res2 should equal(expectedResult)
      res3 should equal(expectedResult)
    }

    it("should be extracted by extractor") {
      val jwt = JsonWebToken.apply(header, claims, "secretkey")
      val result = jwt match {
        case JsonWebToken(x, y, z) =>
          true
        case x =>
          false
      }
      result should equal(true)
    }

    it("extracted claims set should be jvalue ast regardless of input type") {

      val jwt = JsonWebToken.apply(header, claims, "secretkey")
      val result = jwt match {
        case JsonWebToken(x, y, z) =>
          Some(y)
        case x =>
          None
      }

      result.get should equal(JwtClaimsSetJvalue(("Hey" -> "foo")))
    }

    it("should validate a token successfully if same key is used") {
      val jwt = JsonWebToken.apply(header, claims, "secretkey")
      JsonWebToken.validate(jwt, "secretkey") should equal(true)
    }

    it("should fail to validate a token if different key is used") {
      val jwt = JsonWebToken.apply(header, claims, "secretkey")
      JsonWebToken.validate(jwt, "here be dragons") should equal(false)
    }
  }

  describe("JwtHeader") {
    it("should render to json as per spec") {
      val header = JwtHeader("algorithm", "contentType")
      val expectedJson = "{\"alg\":\"algorithm\",\"cty\":\"contentType\",\"typ\":\"JWT\"}"

      header.asJsonString should equal(expectedJson)
    }
  }

  describe("JwtClaimsSet") {

    val date = new Date
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    val dateIso8601 = dateFormat.format(date)

    val claimsSet = JwtClaimsSetMap(Map("privateClaim" -> "foo", "iss" -> "Issuer", "exp" -> date))
    val claimsSetJsonString = claimsSet.asJsonString

    it("should contain private claims") {
      claimsSetJsonString should include("\"privateClaim\":\"foo\"")
    }

    it("should contain iss (Issuer) claim") {
      claimsSetJsonString should include("\"iss\":\"Issuer\"")
    }

    it("should contain exp (Expiration time) claim as ISO8601 date") {
      claimsSetJsonString should include("\"exp\":\"" + dateIso8601 + "\"")
    }
  }
}
