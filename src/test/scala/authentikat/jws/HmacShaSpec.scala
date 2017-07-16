package authentikat.jws

import org.apache.commons.codec.binary.Base64
import org.scalatest.{ FunSpec, Matchers }

class HmacShaSpec extends FunSpec with Matchers {

  describe("HmacShaSigner") {
    describe("HMAC SHA 256") {
      it("Should produce signature") {
        val signer = new HmacShaSigner("secretKey", HS256)
        val signature = Base64.encodeBase64URLSafe(signer("someData"))
        signature.length should equal(43)
      }
    }

    describe("HMAC SHA 384") {
      it("Should produce signature") {
        val signer = new HmacShaSigner("secretKey", HS384)
        val signature = Base64.encodeBase64URLSafe(signer("someData"))
        signature.length should equal(64)
      }
    }

    describe("HMAC SHA 512") {
      it("Should produce signature") {
        val signer = new HmacShaSigner("secretKey", HS512)
        val signature = Base64.encodeBase64URLSafe(signer("someData"))
        signature.length should equal(86)
      }
    }
  }

  describe("HmacShaVerifier") {
    describe("given HMAC SHA 256 signature") {
      val signer = new HmacShaSigner("secretKey", HS256)
      val signature = Base64.encodeBase64URLSafeString(signer("someData"))

      it("Should verify with correct secret") {
        val verifier = new HmacShaVerifier("secretKey", HS256)
        verifier("someData", signature) should equal(true)
      }

      it("Should not verify with incorrect secret") {
        val verifier = new HmacShaVerifier("badSecretKey", HS256)
        verifier("someData", signature) should equal(false)
      }
    }

    describe("given HMAC SHA 384 signature") {
      val signer = new HmacShaSigner("secretKey", HS384)
      val signature = Base64.encodeBase64URLSafeString(signer("someData"))

      it("Should verify with correct secret") {
        val verifier = new HmacShaVerifier("secretKey", HS384)
        verifier("someData", signature) should equal(true)
      }

      it("Should not verify with incorrect secret") {
        val verifier = new HmacShaVerifier("badSecretKey", HS384)
        verifier("someData", signature) should equal(false)
      }
    }

    describe("given HMAC SHA 512 signature") {
      val signer = new HmacShaSigner("secretKey", HS512)
      val signature = Base64.encodeBase64URLSafeString(signer("someData"))

      it("Should verify with correct secret") {
        val verifier = new HmacShaVerifier("secretKey", HS512)
        verifier("someData", signature) should equal(true)
      }

      it("Should not verify with incorrect secret") {
        val verifier = new HmacShaVerifier("badSecretKey", HS512)
        verifier("someData", signature) should equal(false)
      }
    }
  }
}
