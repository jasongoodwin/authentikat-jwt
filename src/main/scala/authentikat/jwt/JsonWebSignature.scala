package authentikat.jwt

import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac
import org.apache.commons.codec.binary.Hex

/**
 * Json Web Algorithms for Encrypting JWS.
 * These generate a one way hash (of claims) with a secret key.
 * Note there is an incomplete set of hashing implementations here.
 * http://tools.ietf.org/html/draft-ietf-jose-json-web-algorithms-25
 */

object JsonWebSignature {

  object HexToString {
    implicit def converter(bytes: Array[Byte]): String = {
      Hex.encodeHexString(bytes)
    }
  }

  def apply(algorithm: String, data: String, key: String): Array[Byte] = {
    algorithm match {
      case "HS256" => apply(HS256, data, key)
      case "HS384" => apply(HS384, data, key)
      case "HS512" => apply(HS512, data, key)
      case "none"  => apply(NONE, data, key)
      case x       => throw new UnsupportedOperationException(x + " is an unknown or unimplemented JWT algo key")
    }
  }

  def apply(algorithm: Algorithm, data: String, key: String = null): Array[Byte] = {
    algorithm match {
      case HS256 => HmacSha("HmacSHA256", data, key)
      case HS384 => HmacSha("HmacSHA384", data, key)
      case HS512 => HmacSha("HmacSHA512", data, key)
      case NONE  => Array.empty[Byte]
      case x     => throw new UnsupportedOperationException(s"$x is an unknown or unimplemented JWT algo key")
    }
  }

  private case object HmacSha {
    def apply(algorithm: String, data: String, key: String): Array[Byte] = {

      val _key = Option(key).getOrElse(throw new IllegalArgumentException("Missing key for JWT encryption via " + algorithm))
      val mac: Mac = Mac.getInstance(algorithm)
      val secretKey: SecretKeySpec = new SecretKeySpec(_key.getBytes, algorithm)
      mac.init(secretKey)
      mac.doFinal(data.getBytes)
    }
  }

  abstract class Algorithm

  case object NONE extends Algorithm

  case object HS256 extends Algorithm

  case object HS384 extends Algorithm

  case object HS512 extends Algorithm

  //  private sealed abstract class UnimplementedAlgorithm extends Algorithm
  //  private case object RS256 extends UnimplementedAlgorithm //Recommended implementation
  //  private case object RS384 extends UnimplementedAlgorithm
  //  private case object RS512 extends UnimplementedAlgorithm
  //  private case object ES256 extends UnimplementedAlgorithm //Recommended+ implementation
  //  private case object ES384 extends UnimplementedAlgorithm
  //  private case object ES512 extends UnimplementedAlgorithm
  //  private case object PS256 extends UnimplementedAlgorithm
  //  private case object PS384 extends UnimplementedAlgorithm
  //  private case object PS512 extends UnimplementedAlgorithm
}

