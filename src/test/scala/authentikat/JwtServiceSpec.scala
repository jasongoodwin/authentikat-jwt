import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class JwtServiceSpec extends FunSpec with ShouldMatchers{

  describe("JwtEncode") {
    it("should fail on invalid input") {
//      intercept[IllegalArgumentException]{
//        JwtEncode("bad input", PlainText)
//      }
    }
  }

}

//
//object JwtEncode {
//
//  val base64Encoder = new sun.misc.BASE64Encoder
//
//
//
//}
//
//
//object JwtDecode {
//
//  val base64Decoder = new sun.misc.BASE64Decoder
//
//  def apply(input: String) {
//    throw new JwtEncodeException("Problem parsing input")
//  }
//
//}
