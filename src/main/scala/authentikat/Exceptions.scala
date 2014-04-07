package authentikat

sealed abstract class JwtException(message: String, cause: Throwable = null) extends Exception(message, cause)
case class JwtEncodeException(message: String, cause: Throwable = null) extends JwtException(message, cause)
case class JwtDecodeException(message: String, cause: Throwable = null) extends JwtException(message, cause)
