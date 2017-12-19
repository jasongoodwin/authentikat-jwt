package authentikat.jws

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString

/**
 * Hmac Sha-2 Uses shared key for both validation and signing
 * @param sharedKey
 * @param algorithm
 */

class HmacShaSigner(sharedKey: String, algorithm: HmacAlgorithm) extends JwsSigner {
  val algorithmString = algorithm match {
    case HS256 => "HmacSHA256"
    case HS384 => "HmacSHA384"
    case HS512 => "HmacSHA512"
  }

  def apply(data: String): Array[Byte] = {
    val secretKey: SecretKeySpec = new SecretKeySpec(sharedKey.getBytes("UTF-8"), algorithmString)
    val mac: Mac = Mac.getInstance(algorithmString)
    mac.init(secretKey)
    mac.doFinal(data.getBytes("UTF-8"))
  }
}

class HmacShaVerifier(sharedKey: String, algorithm: HmacAlgorithm) extends JwsVerifier {
  val signer = new HmacShaSigner(sharedKey, algorithm)

  def apply(data: String, signatureData: String): Boolean = {
    encodeBase64URLSafeString(signer(data)).equals(signatureData)
  }
}
