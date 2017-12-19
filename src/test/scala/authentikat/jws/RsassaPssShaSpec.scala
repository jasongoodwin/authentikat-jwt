package authentikat.jws

import java.security.Security

import org.apache.commons.codec.binary.Base64
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.scalatest.{ FunSpec, Matchers }

class RsassaPssShaSpec extends FunSpec with Matchers {
  Security.addProvider(new BouncyCastleProvider()) //Need to add bouncy castle provider for algorithm.

  val publicKey = KeyReader.getPublic("src/test/resources/public_key.der")
  val privateKey = KeyReader.getPrivate("src/test/resources/private_key.der")

  val wrongPublicKey = KeyReader.getPublic("src/test/resources/bad_public_key.der")
  val wrongPrivateKey = KeyReader.getPrivate("src/test/resources/bad_private_key.der")

  //TODO - check the output against another source
  describe("RsassaPssShaSigner") {

    describe("PS256") {
      it("Should produce signature from privateKey") {
        val signer = new PssShaSigner(privateKey, PS256)
        val signature = signer("someData")
        signature.length should be(256)
      }
    }

    describe("PS384") {
      it("Should produce signature from privateKey") {
        val signer = new PssShaSigner(privateKey, PS384)
        val signature = signer("someData")
        signature.length should be(256)
      }
    }

    describe("PS512") {
      it("Should produce signature from privateKey") {
        val signer = new PssShaSigner(privateKey, PS512)
        val signature = signer("someData")
        signature.length should be(256)
      }
    }
  }

  describe("RsassaPssShaVerifier") {

    describe("given PS256 signature") {
      val signer = new PssShaSigner(privateKey, PS256)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new PssShaVerifier(publicKey, PS256)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new PssShaVerifier(wrongPublicKey, PS256)
        verifier("someData", new String(signature)) should equal(false)
      }
    }

    describe("piven PS384 signature") {
      val signer = new PssShaSigner(privateKey, PS384)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new PssShaVerifier(publicKey, PS384)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new PssShaVerifier(wrongPublicKey, PS384)
        verifier("someData", new String(signature)) should equal(false)
      }
    }

    describe("given PS512 signature") {
      val signer = new PssShaSigner(privateKey, PS512)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new PssShaVerifier(publicKey, PS512)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new PssShaVerifier(wrongPublicKey, PS512)
        verifier("someData", new String(signature)) should equal(false)
      }
    }
  }

}
