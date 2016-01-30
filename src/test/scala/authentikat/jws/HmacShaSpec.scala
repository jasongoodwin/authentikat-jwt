package authentikat.jws

import org.scalatest.{ FunSpec, Matchers }

class HmacShaSpec extends FunSpec with Matchers {

  describe("HmacShaSigner") {

    describe("HMAC SHA 256") {
      it("Should produce signature") {
        val signer = new HmacShaSigner("secretKey", HS256)
        val signature = HexToString(signer("someData"))
        signature.length should equal(64)
        signature should equal(HexToString(Array[Byte](34, 43, -124, 2, 46, -78, 28, 3, 6, -75, 107, 20, -84, -34, -27, -90, 35, 35, 20, -89, 37, 106, 27, 94, 25, 78, -25, 96, -74, -2, 98, -96)))
      }
    }

    describe("HMAC SHA 384") {
      it("Should produce signature") {
        val signer = new HmacShaSigner("secretKey", HS384)
        val signature = HexToString(signer("someData"))
        signature.size should equal(96)
      }
    }

    describe("HMAC SHA 512") {
      it("Should produce signature") {
        val signer = new HmacShaSigner("secretKey", HS512)
        val signature = HexToString(signer("someData"))
        signature.size should equal(128)
      }
    }
  }

  describe("HmacShaVerifier") {
    describe("given HMAC SHA 256 signature") {
      val signer = new HmacShaSigner("secretKey", HS256)
      val signature = signer("someData")

      it("Should verify with correct secret") {
        import HexToString._
        val verifier = new HmacShaVerifier("secretKey", HS256)
        verifier("someData", signature) should equal(true)
      }

      it("Should not verify with incorrect secret") {
        import HexToString._
        val verifier = new HmacShaVerifier("badSecretKey", HS256)
        verifier("someData", signature) should equal(false)
      }
    }

    describe("given HMAC SHA 384 signature") {
      val signer = new HmacShaSigner("secretKey", HS384)
      val signature = signer("someData")

      it("Should verify with correct secret") {
        import HexToString._
        val verifier = new HmacShaVerifier("secretKey", HS384)
        verifier("someData", signature) should equal(true)
      }

      it("Should not verify with incorrect secret") {
        import HexToString._
        val verifier = new HmacShaVerifier("badSecretKey", HS384)
        verifier("someData", signature) should equal(false)
      }
    }

    describe("given HMAC SHA 512 signature") {
      val signer = new HmacShaSigner("secretKey", HS512)
      val signature = signer("someData")

      it("Should verify with correct secret") {
        import HexToString._
        val verifier = new HmacShaVerifier("secretKey", HS512)
        verifier("someData", signature) should equal(true)
      }

      it("Should not verify with incorrect secret") {
        import HexToString._
        val verifier = new HmacShaVerifier("badSecretKey", HS512)
        verifier("someData", signature) should equal(false)
      }
    }
  }
}
