package authentikat.jwt

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import java.util.{Date, TimeZone}
import java.text.SimpleDateFormat
import org.scalatest.Matchers

class JsonWebTokenSpec extends FunSpec with Matchers {

  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._

  describe("JsonWebToken") {
    val header = JwtHeader("HS256")
    val claims = JwtClaimsSetMap(Map("Hey" -> "foo"))
    val jvalueClaims = render("Hey" -> ("Hey" -> "foo"))
    val secretKey = Array[Byte](115, 101, 99, 114, 101, 116, 107, 101, 121)

    it("should have three parts for a token created with claims map claims") {
      val result = JsonWebToken.apply(header, claims, secretKey)
      result.split("\\.").length should equal(3)
    }

    it("should have three parts for a token created with a jvalue claims") {
      val result = JsonWebToken.apply(header, JwtClaimsSetJValue(jvalueClaims), secretKey)
      result.split("\\.").length should equal(3)
    }

    it("should have three parts for a token created with a string claims") {
      val result = JsonWebToken.apply(header, JwtClaimsSetJValue("{\"json\":42}"), secretKey)
      result.split("\\.").length should equal(3)
    }

    it("should produce the same results for all three claims types") {
      val expectedResult = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJIZXkiOiJmb28ifQ.fTW9f2w5okSpa7u64d6laQQbpBdgoTFvIPcx5gi70R8"

      val res1 = JsonWebToken.apply(header, JwtClaimsSetMap(Map("Hey" -> "foo")), secretKey)
      val res2 = JsonWebToken.apply(header, JwtClaimsSetJValue(("Hey" -> "foo")), secretKey)
      val res3 = JsonWebToken.apply(header, JwtClaimsSetJsonString("{\"Hey\":\"foo\"}"), secretKey)

      res1 should equal(expectedResult)
      res2 should equal(expectedResult)
      res3 should equal(expectedResult)
    }

    it("should be extracted by extractor") {
      val jwt = JsonWebToken.apply(header, claims, secretKey)
      val result = jwt match {
        case JsonWebToken(x, y, z) =>
          true
        case x =>
          false
      }
      result should equal(true)
    }

    it("extracted claims set should be jvalue") {

      val jwt = JsonWebToken.apply(header, claims, secretKey)
      val result = jwt match {
        case JsonWebToken(x, y, z) =>
          Some(y)
        case x =>
          None
      }

      result.get should equal(JwtClaimsSetJValue(("Hey" -> "foo")))
    }

    it("should validate a token successfully if same key is used") {
      val jwt = JsonWebToken.apply(header, claims, secretKey)
      JsonWebToken.validate(jwt, secretKey) should equal(true)
    }

    it("should fail to validate a token if different key is used") {
      val jwt = JsonWebToken.apply(header, claims, secretKey)
      JsonWebToken.validate(jwt, "here be dragons".getBytes) should equal(false)
    }

    it("should report validation failure and not crash if the token is incorrectly formatted") {
      JsonWebToken.validate("", secretKey) should equal(false)
    }

    it("should report a validation failure and not crash if the token components are incorrectly formatted") {
      JsonWebToken.validate("..", secretKey) should equal(false)
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
