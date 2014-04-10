import org.scalatest.{ FunSpec, BeforeAndAfterAll }

import scala.concurrent.Await
import scala.concurrent.duration._

import smoke._
import smoke.test._

class AppSpec extends FunSpec with BeforeAndAfterAll {

  val app = new Authentikat

  override def afterAll { app.shutdown() }

  describe("GET /example") {
    it("should respond with 200") {
      val request = TestRequest("/example")
      val response = Await.result(app.application(request), 2 seconds)
      assert(response.status === Ok)
    }
  }

  describe("POST /unknown-path") {
    it("should respond with 404") {
      val request = TestRequest("/unknown-path", "POST")
      val response = Await.result(app.application(request), 2 seconds)
      assert(response.status === NotFound)
    }
  }
}
