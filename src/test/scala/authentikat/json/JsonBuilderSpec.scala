package authentikat.json

import authentikat.jwt._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import java.text.SimpleDateFormat

class JsonBuilderSpec  extends FunSpec with ShouldMatchers  {

  describe("JsonBuilder") {
    it("should produce json with strings") {
      val stuffToSerialize = Seq(("key1", "value1"), ("key2", "value2"))
      JsonBuilder.toJsonString(stuffToSerialize) should equal ("""{"key1":"value1","key2":"value2"}""")
    }

    it("should produce json with dates formatted as ISO8601") {
      val date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2011-01-01 00:00:00")
      val expectedDateString = "2011-01-01T05:00Z"

      val stuffToSerialize = Seq(("key1", "value1"), ("key2", date))
      JsonBuilder.toJsonString(stuffToSerialize) should equal ("""{"key1":"value1","key2":2011-01-01T05:00Z}""")
    }

    it("should produce json with numbers") {
      val stuffToSerialize = Seq(("key1", 53.23), ("answer to life", 42))
      JsonBuilder.toJsonString(stuffToSerialize) should equal ("""{"key1":53.23,"answer to life":42}""")
    }
  }
}
