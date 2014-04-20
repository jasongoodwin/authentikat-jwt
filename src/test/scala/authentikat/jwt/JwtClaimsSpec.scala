package authentikat.jwt

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import org.json4s.jackson.JsonMethods._

class JwtClaimsSpec extends FunSpec with ShouldMatchers {

  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._

  describe("JwtClaimsSet") {
    it("asSimpleMap Should return a failure on complex hierarchies.") {
      val jvalueTree = render("Hey" -> ("Hey" -> "foo"))
      val tryMap = JwtClaimsSetJvalue(jvalueTree).asSimpleMap

      tryMap.isFailure should be(true)
    }

    it("asSimpleMap Should succeed on flat hierarchies.") {
      val jvalueTree = render("Hey" -> "foo")
      val tryMap = JwtClaimsSetJvalue(jvalueTree).asSimpleMap

      tryMap.isFailure should be(false)
      tryMap.get should equal(Map("Hey" -> "foo"))
    }
  }
}
