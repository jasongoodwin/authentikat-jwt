package authentikat.jws

import java.security.{ PublicKey, Signature, PrivateKey }

import org.apache.commons.codec.binary.Base64

private object RsaAlgorithmString {
  def apply(rsaAlgorithm: RsaAlgorithm): String = {
    rsaAlgorithm match {
      case RS256 => "SHA256withRSA"
      case RS384 => "SHA384withRSA"
      case RS512 => "SHA512withRSA"
    }
  }
}

/**
 * RSA Signer uses a private key for signing and a public key for verification.
 * @param privateKey
 * @param algorithm
 */

class RsaShaSigner(privateKey: PrivateKey, algorithm: RsaAlgorithm) extends PrivateKeyJwsSigner(privateKey) {
  override val algorithmString = RsaAlgorithmString(algorithm)
}

class RsaShaVerifier(publicKey: PublicKey, algorithm: RsaAlgorithm) extends PublicKeyJwsVerifier(publicKey) {
  val algorithmString = RsaAlgorithmString(algorithm)
}