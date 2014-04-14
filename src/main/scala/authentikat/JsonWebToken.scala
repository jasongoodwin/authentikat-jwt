package authentikat

import java.util.{TimeZone, Date}
import spray.json._
import java.text.SimpleDateFormat

package object jwt {

  object JsonWebToken {
    private val base64Encoder = new sun.misc.BASE64Encoder
    private val base64Decoder = new sun.misc.BASE64Decoder

    def apply(header: JwtHeader, claims: JwtClaimsSet, algorithm: String, key: String): String = {
      val encodedHeader = base64Encoder.encode(header.asJsonString.getBytes("UTF-8"))
      val encodedClaims = base64Encoder.encode(claims.asJsonString.getBytes("UTF-8"))
      val encryptedClaims = JsonWebSignature(algorithm, encodedClaims, key).toString
      Seq(encodedHeader, encodedClaims, encryptedClaims).mkString(".")
    }
  }

  case class JwtHeader(algorithm: String = null,
                       mimeType: String = null,
                       contentType: String = "JWT") {

    private case class JwtHeader(alg: Option[String], typ: Option[String], cty: Option[String]= Some("JWT"))

    def asJsonString: String = {
      import DefaultJsonProtocol._
      implicit val jsonFormat = jsonFormat3(JwtHeader)

      val header = JwtHeader(Option(algorithm), Option(mimeType))
      header.toJson.toString
    }
  }

  type JwtPrivateClaimsSet = scala.collection.immutable.Map[String, String]

  private case class JwtPublicClaimsSet(iss: Option[String],
                                        sub: Option[String],
                                        aud: Option[String],
                                        exp: Option[String],
                                        nbf: Option[String],
                                        iat: Option[String],
                                        jti: Option[String])


  case class JwtClaimsSet(privateClaims: JwtPrivateClaimsSet,
                          issuer: String = null,
                          subject: String = null,
                          audience: String = null,
                          expirationTime: Date = null,
                          notBefore: Date = null,
                          issuedAt: Date = null,
                          jwtId: String = null) {


    private val publicClaims = new JwtPublicClaimsSet(
      Option(issuer),
      Option(subject),
      Option(audience),
      Option(expirationTime).map{x => JwtClaimsSet.dateFormat.format(x)},
      Option(notBefore).map{x => JwtClaimsSet.dateFormat.format(x)},
      Option(issuedAt).map{x => JwtClaimsSet.dateFormat.format(x)},
      Option(jwtId))

    def asJsonString: String = {
      import DefaultJsonProtocol._
      implicit val jsonFormat = jsonFormat7(JwtPublicClaimsSet)

      val pc = privateClaims.toJson
      JsObject(pc.asJsObject.fields ++ publicClaims.toJson.asJsObject.fields).toString
    }

    def asBase64EncodedJson: String = {
      this.asJsonString
    }
  }

  object JwtClaimsSet {
    private val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
  }

}

