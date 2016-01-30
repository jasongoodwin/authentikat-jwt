package authentikat.jws

import org.apache.commons.codec.binary.Hex

/**
 * The byte arrays need to be a string representation in JWT.
 */
object HexToString {
  implicit def apply(bytes: Array[Byte]): String = {
    Hex.encodeHexString(bytes)
  }
}