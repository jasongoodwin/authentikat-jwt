package authentikat.jwt

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class JwtClaimsSpec extends AnyFunSpec with Matchers {

  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._

  describe("JwtClaimsSet") {
    it("asSimpleMap Should return a failure on complex hierarchies.") {
      val jvalueTree = render("Hey" -> ("Hey" -> "foo"))
      val tryMap = JwtClaimsSetJValue(jvalueTree).asSimpleMap

      tryMap.isFailure should be(true)
    }

    it("asSimpleMap Should succeed on flat hierarchies.") {
      val jvalueTree = render("Hey" -> "foo")
      val tryMap = JwtClaimsSetJValue(jvalueTree).asSimpleMap

      tryMap.isFailure should be(false)
      tryMap.get should equal(Map("Hey" -> "foo"))
    }
  }
}
