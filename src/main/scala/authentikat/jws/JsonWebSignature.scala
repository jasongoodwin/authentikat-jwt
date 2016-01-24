package authentikat.jws

import org.apache.commons.codec.binary.Hex

/**
 * The byte arrays need a string representation in JWT.
 * Implicitly converts between formats
 */
object HexToString {
  implicit def apply(bytes: Array[Byte]): String = {
    Hex.encodeHexString(bytes)
  }
}

/**
 * Json Web Algorithms for Encrypting JWS.
 * These generate a one way hash (of claims) with a secret key.
 * Note there is an incomplete set of hashing implementations here.
 * http://tools.ietf.org/html/draft-ietf-jose-json-web-algorithms-25
 */

package object JwsAlgorithm {
  sealed trait Algorithm
  case object `None` extends Algorithm

  sealed trait HmacAlgoritm extends Algorithm
  case object HS256 extends HmacAlgoritm
  case object HS384 extends HmacAlgoritm
  case object HS512 extends HmacAlgoritm

  sealed trait RsaAlgorithm extends Algorithm
  case object RS256 extends RsaAlgorithm
  case object RS384 extends RsaAlgorithm
  case object RS512 extends RsaAlgorithm

  //  sealed trait UnimplementedAlgorithm
  //  case object ES256 extends UnimplementedAlgorithm //Recommended implementation
  //  case object ES384 extends UnimplementedAlgorithm
  //  case object ES512 extends UnimplementedAlgorithm
  //  case object PS256 extends UnimplementedAlgorithm
  //  case object PS384 extends UnimplementedAlgorithm
  //  case object PS512 extends UnimplementedAlgorithm
}

trait JwsSigner {
  /**
   * Produces signature from private key
   * @param data
   * @return signature
   */
  def apply(data: String): Array[Byte]
}

trait JwsVerifier {
  /**
   * Verifies jwt token
   * @param data
   * @return
   */
  def apply(data: String, signatureData: String): Boolean
}

