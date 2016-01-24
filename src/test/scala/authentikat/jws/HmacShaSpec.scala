package authentikat.jws

import authentikat.jws.JwsAlgorithm.HS256
import org.scalatest.{ FunSpec, Matchers }

class HmacShaSpec extends FunSpec with Matchers {

  describe("HmacShaSigner") {
    it("Should produce data for HMAC SHA 256") {
      val signer = new HmacShaSigner("secretKey", HS256)
      val signature = HexToString(signer("someData"))
      signature should equal(HexToString(Array[Byte](34, 43, -124, 2, 46, -78, 28, 3, 6, -75, 107, 20, -84, -34, -27, -90, 35, 35, 20, -89, 37, 106, 27, 94, 25, 78, -25, 96, -74, -2, 98, -96)))
    }
  }

  describe("HmacShaValidator") {
    it("Should validate for HMAC SHA 256") {
      import HexToString._

      val signer = new HmacShaSigner("secretKey", HS256)
      val signature = signer("someData")

      val validator = new HmacShaVerifier("secretKey", HS256)
      validator("someData", signature) should equal(true)
    }
  }

}
