package authentikat

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import authentikat.jwt._
import java.util.{TimeZone, Calendar, Date}
import java.text.SimpleDateFormat

class JsonWebTokenSpec  extends FunSpec with ShouldMatchers {

  describe("JsonWebToken") {
    it("should have three parts") {

    }
  }

  describe("JwtHeader") {
    it("should render to json as per spec") {
      val header = JwtHeader("algorithm", "mimeType")
      val expectedJson = "{\"alg\":\"algorithm\",\"typ\":\"mimeType\",\"cty\":\"JWT\"}"

      header.asJsonString should equal(expectedJson)
    }
  }

  describe("JwtClaimsSet") {

    val date = new Date
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    val dateIso8601 = dateFormat.format(date)
    
    val claimsSet = JwtClaimsSet(Map("privateClaim" -> "foo"), "Issuer", "Subject", "Audience", date, date, date, "JWT ID")
    val claimsSetJsonString = claimsSet.asJsonString

    it("should contain private claims") {
      claimsSetJsonString should include("\"privateClaim\":\"foo\"")
    }

    it("should contain iss (Issuer) claim") {
      claimsSetJsonString should include("\"iss\":\"Issuer\"")
    }

    it("should contain sub (Subject) claim") {
      claimsSetJsonString should include("\"sub\":\"Subject\"")
    }

    it("should contain aud (Audience) claim") {
      claimsSetJsonString should include("\"aud\":\"Audience\"")
    }

    it("should contain exp (Expiration Time) claim") {
      claimsSetJsonString should include("\"exp\":\"" + dateIso8601 + "\"")
    }

    it("should contain nbf (Not Before) claim") {
      claimsSetJsonString should include("\"nbf\":\"" + dateIso8601 + "\"")
    }

    it("should contain iat (Issued At) claim") {
      claimsSetJsonString should include("\"iat\":\"" + dateIso8601 + "\"")
    }

    it("should contain jti (JWT ID) claim") {
      claimsSetJsonString should include("\"jti\":\"JWT ID\"")
    }
  }
}
