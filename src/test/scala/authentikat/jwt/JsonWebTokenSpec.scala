package authentikat.jwt

import java.text.SimpleDateFormat
import java.util.{ Date, TimeZone }

import authentikat.jws.JwsSigner
import org.scalatest.{ FunSpec, Matchers }

class FakeSigner extends JwsSigner {
  override def apply(data: String): Array[Byte] = {
    "MOCK".getBytes("UTF-8")
  }
}

class JsonWebTokenSpec extends FunSpec with Matchers {

  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._

  describe("JsonWebToken") {
    val header = JwtHeader("HS256")
    val claims = JwtClaimsSetMap(Map("Hey" -> "foo"))
    val jvalueClaims = render("Hey" -> ("Hey" -> "foo"))

    describe("when a signer is provided") {
      it("should have three parts for a token created with claims map claims") {
        val result = JsonWebToken(header, claims, new FakeSigner)
        result.split("\\.").length should equal(3)
      }

      it("should have three parts for a token created with a jvalue claims") {
        val result = JsonWebToken.apply(header, JwtClaimsSetJValue(jvalueClaims), new FakeSigner)
        result.split("\\.").length should equal(3)
      }

      it("should have three parts for a token created with a string claims") {
        val result = JsonWebToken.apply(header, JwtClaimsSetJValue("{\"json\":42}"), new FakeSigner)
        result.split("\\.").length should equal(3)
      }
    }

    it("should produce the same results regardless of claims type") {
      val expectedResult = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJIZXkiOiJmb28ifQ.MOCK"

      val res1 = JsonWebToken(header, JwtClaimsSetMap(Map("Hey" -> "foo")), new FakeSigner)
      val res2 = JsonWebToken(header, JwtClaimsSetJValue(("Hey" -> "foo")), new FakeSigner)
      val res3 = JsonWebToken(header, JwtClaimsSetJsonString("{\"Hey\":\"foo\"}"), new FakeSigner)

      res1 should equal(res2)
      res2 should equal(res3)
    }

    it("should be extracted by extractor") {
      val jwt = JsonWebToken.apply(header, claims, new FakeSigner)
      jwt match {
        case JsonWebToken(x, y, z) =>
        case _ => fail("didn't extract values")
      }
    }

    it("extracted claims set should be jvalue") {
      val jwt = JsonWebToken.apply(header, claims, new FakeSigner)
      val result = jwt match {
        case JsonWebToken(x, y, z) =>
          Some(y)
        case x =>
          None
      }

      result should equal(Some(JwtClaimsSetJValue("Hey" -> "foo")))
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
