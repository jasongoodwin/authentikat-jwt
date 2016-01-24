package authentikat.jws

import authentikat.jws.JwsAlgorithm._
import org.apache.commons.codec.binary.Base64
import org.scalatest.{ FunSpec, Matchers }

class RsaShaSpec extends FunSpec with Matchers {

  //TODO Get RSA keys from bytes?
  //  Array[Byte] =  privateKeyBytes;
  //  byte[] publicKeyBytes;
  //  val kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
  //  val privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
  //  val publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

  //  val keyGen = KeyPairGenerator.getInstance("RSA")
  //  keyGen.initialize(256)
  //  val keyPair = keyGen.genKeyPair()
  //
  //  val publicKey = keyPair.getPublic()
  //  val privateKey = keyPair.getPrivate()
  //

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

  describe("RsaShaValidator") {

    describe("RS256") {
      it("Should validate signature using private Key") {
        val signer = new RsaShaSigner(privateKey, RS256)
        val signature = Base64.encodeBase64(signer("someData"))

        val validator = new RsaShaVerifier(publicKey, RS256)
        validator("someData", new String(signature)) should equal(true)
      }
    }

    describe("RS384") {
      it("Should validate signature using private Key") {
        val signer = new RsaShaSigner(privateKey, RS384)
        val signature = Base64.encodeBase64(signer("someData"))

        val validator = new RsaShaVerifier(publicKey, RS384)
        validator("someData", new String(signature)) should equal(true)
      }
    }

    describe("RS512") {
      it("Should validate signature using private Key") {
        val signer = new RsaShaSigner(privateKey, RS512)
        val signature = Base64.encodeBase64(signer("someData"))

        val validator = new RsaShaVerifier(publicKey, RS512)
        validator("someData", new String(signature)) should equal(true)
      }
    }
  }

}
