package authentikat.jws

import java.security.spec.{ MGF1ParameterSpec, PSSParameterSpec }
import java.security.{ Security, PublicKey, PrivateKey }

import org.bouncycastle.jce.provider.BouncyCastleProvider

/**
 * Need to pass bouncy castle to java security for PSS
 */
object PssProviderLoad {
}

private object PssAlgorithmString {
  def apply(pssAlgorithm: RsassaPssAlgorithm): (String, PSSParameterSpec) = {
    pssAlgorithm match {
      case PS256 => ("SHA256withRSAandMGF1", new PSSParameterSpec("SHA256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1))
      case PS384 => ("SHA384withRSAandMGF1", new PSSParameterSpec("SHA384", "MGF1", MGF1ParameterSpec.SHA384, 48, 1))
      case PS512 => ("SHA512withRSAandMGF1", new PSSParameterSpec("SHA512", "MGF1", MGF1ParameterSpec.SHA512, 64, 1))
    }
  }
}

class PssShaSigner(privateKey: PrivateKey, algorithm: RsassaPssAlgorithm) extends PrivateKeyJwsSigner(privateKey) {
  val (jcaAlg, pssSpec) = PssAlgorithmString(algorithm)
  override val algorithmString = jcaAlg
  override val maybePssSpec = Some(pssSpec)
}

class PssShaVerifier(publicKey: PublicKey, algorithm: RsassaPssAlgorithm) extends PublicKeyJwsVerifier(publicKey) {
  val (jcaAlg, pssSpec) = PssAlgorithmString(algorithm)
  override val algorithmString = jcaAlg
  override val maybePssSpec = Some(pssSpec)
}