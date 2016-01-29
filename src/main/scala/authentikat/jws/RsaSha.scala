package authentikat.jws

import java.security.{ PublicKey, Signature, PrivateKey }

import JwsAlgorithm._
import org.apache.commons.codec.binary.Base64

private object AlgorithmString {
  def apply(rsaAlgorithm: RsaAlgorithm): String = {
    rsaAlgorithm match {
      case RS256 => "SHA256withRSA"
      case RS384 => "SHA384withRSA"
      case RS512 => "SHA512withRSA"
      case x => throw new java.security.NoSuchAlgorithmException(x + " is not a valid RSA SHA-2 signing algorithm")
    }
  }
}

/**
 * RSA Signer uses a private key for signing and a public key for validation.
 * @param privateKey
 * @param algorithm
 */

class RsaShaSigner(privateKey: PrivateKey, algorithm: JwsAlgorithm.RsaAlgorithm) extends JwsSigner {

  val algorithmString = AlgorithmString(algorithm)

  def apply(data: String): Array[Byte] = {
    val signature = Signature.getInstance(algorithmString)
    signature.initSign(privateKey)
    signature.update(data.getBytes("UTF-8"))
    signature.sign()
  }
}

class RsaShaVerifier(publicKey: PublicKey, algorithm: JwsAlgorithm.RsaAlgorithm) extends JwsVerifier {

  val algorithmString = AlgorithmString(algorithm)

  def apply(data: String, signatureData: Array[Byte]): Boolean = {
    val sign = Signature.getInstance(algorithmString)
    sign.initVerify(publicKey)
    sign.update(data.getBytes("UTF-8"))
    sign.verify(signatureData)
  }

  def apply(data: String, signatureData: String): Boolean = {
    apply(data, Base64.decodeBase64(signatureData.getBytes("UTF-8")))
  }
}