package authentikat.jws

import authentikat.jws.JwsAlgorithm._
import org.apache.commons.codec.binary.Base64
import org.scalatest.{ FunSpec, Matchers }

class RsaShaSpec extends FunSpec with Matchers {

  val publicKey = KeyReader.getPublic("src/test/resources/public_key.der")
  val privateKey = KeyReader.getPrivate("src/test/resources/private.der")

  describe("RsaShaSigner") {

    describe("RS256") {
      it("Should produce signature from privateKey") {
        val signer = new RsaShaSigner(privateKey, RS512)
        val signature = signer("someData")
        signature.length should be(256)
      }
    }

    describe("RS384") {
      it("Should produce signature from privateKey") {
        val signer = new RsaShaSigner(privateKey, RS384)
        val signature = signer("someData")
        signature.length should be(256)
      }
    }

    describe("RS512") {
      it("Should produce signature from privateKey") {
        val signer = new RsaShaSigner(privateKey, RS512)
        val signature = signer("someData")
        signature.length should be(256)
      }
    }
  }

  describe("RsaShaVerifier") {

    describe("RS256") {
      it("Should verify valid signature using private Key") {
        val signer = new RsaShaSigner(privateKey, RS256)
        val signature = Base64.encodeBase64(signer("someData"))

        val verifier = new RsaShaVerifier(publicKey, RS256)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify invalid signature using private Key") {
        fail("not tested...")
      }
    }

    describe("RS384") {
      it("Should verify valid signature using private Key") {
        val signer = new RsaShaSigner(privateKey, RS384)
        val signature = Base64.encodeBase64(signer("someData"))

        val verifier = new RsaShaVerifier(publicKey, RS384)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify invalid signature using private Key") {
        fail("not tested...")
      }
    }

    describe("RS512") {
      it("Should verify valid signature using private Key") {
        val signer = new RsaShaSigner(privateKey, RS512)
        val signature = Base64.encodeBase64(signer("someData"))

        val verifier = new RsaShaVerifier(publicKey, RS512)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify invalid signature using private Key") {
        fail("not tested...")
      }
    }
  }

}
