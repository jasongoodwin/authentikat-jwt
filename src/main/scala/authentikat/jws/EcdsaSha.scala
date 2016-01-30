package authentikat.jws

package authentikat.jws

import java.security.{ PublicKey, Signature, PrivateKey }

import org.apache.commons.codec.binary.Base64

private object EcdsaAlgorithmString {
  def apply(ecdsaAlgorithm: EcdsaAlgorithm): String = {
    ecdsaAlgorithm match {
      case ES256 => "SHA256withECDSA"
      case ES384 => "SHA384withECDSA"
      case ES512 => "SHA512withECDSA"
    }
  }
}

/**
 * Ecdsa Signer uses a private key for signing and a public key for verification.
 * @param privateKey
 * @param algorithm
 */

class EcdsaShaSigner(privateKey: PrivateKey, algorithm: EcdsaAlgorithm) extends PrivateKeyJwsSigner(privateKey) {
  val algorithmString = EcdsaAlgorithmString(algorithm)
}

class EcdsaShaVerifier(publicKey: PublicKey, algorithm: EcdsaAlgorithm) extends PublicKeyJwsVerifier(publicKey) {
  val algorithmString = EcdsaAlgorithmString(algorithm)
}