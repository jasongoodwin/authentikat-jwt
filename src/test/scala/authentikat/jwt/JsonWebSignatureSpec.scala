package authentikat.jwt

import JsonWebSignature._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class JsonWebSignatureSpec extends AnyFunSpec with Matchers {

  describe("JsonWebSignature") {
    it("Should generate nothing for algo = none ") {
      JsonWebSignature(NONE, "someData", "secretKey") should equal(Array.empty[Byte])
    }

    it("Should generate data for HMAC SHA 256 algorithm") {
      JsonWebSignature(HS256, "someData", "secretKey") should equal(Array[Byte](34, 43, -124, 2, 46, -78, 28, 3, 6, -75, 107, 20, -84, -34, -27, -90, 35, 35, 20, -89, 37, 106, 27, 94, 25, 78, -25, 96, -74, -2, 98, -96))
    }

    it("Should generate data for HMAC SHA 384 algorithm") {
      JsonWebSignature(HS384, "someData", "secretKey") should equal(Array[Byte](81, 49, -2, -38, 57, 77, 115, -10, 18, 56, 113, -16, -56, -35, -5, 60, 60, -23, -24, -94, -127, -23, 28, 78, -112, 111, 72, -25, -102, 34, 21, 47, -109, 16, -105, -11, -18, 28, 56, 0, -46, -108, -1, -124, 51, 13, -92, 3))
    }

    it("Should generate data for HMAC SHA 512 algorithm") {
      JsonWebSignature(HS512, "someData", "secretKey") should equal(Array[Byte](14, -21, 125, 54, -13, 89, -58, -64, 16, -15, 38, -84, -38, -25, 41, -79, 2, 78, -128, 98, 54, -68, -15, -58, 43, -27, -44, 108, -114, -86, -64, 14, -95, 114, 68, -112, -40, 15, 51, 31, 95, -125, -82, -82, -93, -7, -81, 43, 32, 82, -70, 100, -104, 24, 12, 96, -61, 85, 72, 57, 31, -89, -23, 120))
    }
  }

}
