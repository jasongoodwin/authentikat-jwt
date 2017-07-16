package authentikat.jws

import org.apache.commons.codec.binary.Base64
import org.scalatest.{ FunSpec, Matchers }

class RsaShaSpec extends FunSpec with Matchers {

  val publicKey = KeyReader.getPublic("src/test/resources/public_key.der")
  val privateKey = KeyReader.getPrivate("src/test/resources/private_key.der")

  val wrongPublicKey = KeyReader.getPublic("src/test/resources/bad_public_key.der")
  val wrongPrivateKey = KeyReader.getPrivate("src/test/resources/bad_private_key.der")

  //TODO - check the output against another source
  describe("RsaShaSigner") {

    describe("RS256") {
      it("Should produce signature from privateKey") {
        val signer = new RsaShaSigner(privateKey, RS256)
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

    describe("given RS256 signature") {
      val signer = new RsaShaSigner(privateKey, RS256)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new RsaShaVerifier(publicKey, RS256)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new RsaShaVerifier(wrongPublicKey, RS256)
        verifier("someData", new String(signature)) should equal(false)
      }
    }

    describe("given RS384 signature") {
      val signer = new RsaShaSigner(privateKey, RS384)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new RsaShaVerifier(publicKey, RS384)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new RsaShaVerifier(wrongPublicKey, RS384)
        verifier("someData", new String(signature)) should equal(false)
      }
    }

    describe("given RS512 signature") {
      val signer = new RsaShaSigner(privateKey, RS512)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new RsaShaVerifier(publicKey, RS512)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new RsaShaVerifier(wrongPublicKey, RS512)
        verifier("someData", new String(signature)) should equal(false)
      }
    }
  }

}
