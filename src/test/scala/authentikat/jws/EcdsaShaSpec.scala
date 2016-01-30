package authentikat.jws

import java.security._

import authentikat.jws._
import org.apache.commons.codec.binary.Base64
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.scalatest.{ FunSpec, Matchers }
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

class EcdsaShaSpec extends FunSpec with Matchers {
  Security.addProvider(new BouncyCastleProvider()) //Need to add bouncy castle provider for algorithm. 

  def genKeyPair: (PublicKey, PrivateKey) = {

    val ecSpec = ECNamedCurveTable.getParameterSpec("prime192v1");
    val g = KeyPairGenerator.getInstance("ECDSA", "BC");
    g.initialize(ecSpec, new SecureRandom());
    val pair = g.generateKeyPair()

    val fact = KeyFactory.getInstance("ECDSA", "BC")
    val publicKey = fact.generatePublic(new X509EncodedKeySpec(pair.getPublic().getEncoded()))
    val privateKey = fact.generatePrivate(new PKCS8EncodedKeySpec(pair.getPrivate().getEncoded()))
    (publicKey, privateKey)
  }

  val (publicKey, privateKey) = genKeyPair
  val (wrongPublicKey, wrongPrivateKey) = genKeyPair

  describe("EcdsaShaSigner") {

    describe("ES256") {
      it("Should produce signature from privateKey") {
        val signer = new EcdsaShaSigner(privateKey, ES256)
        val signature = signer("someData")
        signature.length > 0 should be(true)
      }
    }

    describe("ES384") {
      it("Should produce signature from privateKey") {
        val signer = new EcdsaShaSigner(privateKey, ES384)
        val signature = signer("someData")
        signature.length > 0 should be(true)
      }
    }

    describe("ES512") {
      it("Should produce signature from privateKey") {
        val signer = new EcdsaShaSigner(privateKey, ES512)
        val signature = signer("someData")
        signature.length > 0 should be(true)
      }
    }
  }

  describe("EcdsaShaVerifier") {

    describe("given ES256 signature") {
      val signer = new EcdsaShaSigner(privateKey, ES256)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new EcdsaShaVerifier(publicKey, ES256)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new EcdsaShaVerifier(wrongPublicKey, ES256)
        verifier("someData", new String(signature)) should equal(false)
      }
    }

    describe("piven ES384 signature") {
      val signer = new EcdsaShaSigner(privateKey, ES384)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new EcdsaShaVerifier(publicKey, ES384)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new EcdsaShaVerifier(wrongPublicKey, ES384)
        verifier("someData", new String(signature)) should equal(false)
      }
    }

    describe("given ES512 signature") {
      val signer = new EcdsaShaSigner(privateKey, ES512)
      val signature = Base64.encodeBase64(signer("someData"))

      it("Should verify valid signature using matching private Key") {
        val verifier = new EcdsaShaVerifier(publicKey, ES512)
        verifier("someData", new String(signature)) should equal(true)
      }

      it("Should not verify signature using wrong private Key") {
        val verifier = new EcdsaShaVerifier(wrongPublicKey, ES512)
        verifier("someData", new String(signature)) should equal(false)
      }
    }
  }

}
