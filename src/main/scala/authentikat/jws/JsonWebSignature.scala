package authentikat.jws

import java.security.{ PublicKey, PrivateKey, Signature }

import org.apache.commons.codec.binary.{ Base64, Hex }

/**
 * Json Web Algorithms for Encrypting JWS.
 * These generate a one way hash (of claims) with a secret key.
 * Note there is an incomplete set of hashing implementations here.
 * http://tools.ietf.org/html/draft-ietf-jose-json-web-algorithms-25
 */

sealed trait Algorithm
case object NONE extends Algorithm

sealed trait HmacAlgorithm extends Algorithm
case object HS256 extends HmacAlgorithm
case object HS384 extends HmacAlgorithm
case object HS512 extends HmacAlgorithm

sealed trait RsaAlgorithm extends Algorithm
case object RS256 extends RsaAlgorithm
case object RS384 extends RsaAlgorithm
case object RS512 extends RsaAlgorithm

sealed trait EcdsaAlgorithm extends Algorithm
case object ES256 extends EcdsaAlgorithm
case object ES384 extends EcdsaAlgorithm
case object ES512 extends EcdsaAlgorithm

sealed trait RsassaPssAlgorithm extends Algorithm
case object PS256 extends RsassaPssAlgorithm
case object PS384 extends RsassaPssAlgorithm
case object PS512 extends RsassaPssAlgorithm

trait JwsSigner {
  /**
   * Produces signature from private key
   * @param data
   * @return signature
   */
  def apply(data: String): Array[Byte]
}

protected abstract class PrivateKeyJwsSigner(privateKey: PrivateKey) extends JwsSigner {
  val algorithmString: String
  val maybePssSpec: Option[java.security.spec.PSSParameterSpec] = None

  override def apply(data: String): Array[Byte] = {
    val signature = Signature.getInstance(algorithmString)
    signature.initSign(privateKey)
    signature.update(data.getBytes("UTF-8"))
    maybePssSpec.foreach(pssSpec => signature.setParameter(pssSpec))
    signature.sign()
  }
}

trait JwsVerifier {
  /**
   * Verifies jwt token
   * @param data
   * @return
   */
  def apply(data: String, signatureData: String): Boolean
}

protected abstract class PublicKeyJwsVerifier(publicKey: PublicKey) extends JwsVerifier {
  val algorithmString: String
  val maybePssSpec: Option[java.security.spec.PSSParameterSpec] = None

  def apply(data: String, signatureData: Array[Byte]): Boolean = {
    val signature = Signature.getInstance(algorithmString)
    signature.initVerify(publicKey)
    signature.update(data.getBytes("UTF-8"))
    maybePssSpec.foreach(pssSpec => signature.setParameter(pssSpec))
    signature.verify(signatureData)
  }

  def apply(data: String, signatureData: String): Boolean = {
    apply(data, Base64.decodeBase64(signatureData.getBytes("UTF-8")))
  }
}

