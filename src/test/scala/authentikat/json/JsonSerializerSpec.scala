package authentikat.json

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import java.text.SimpleDateFormat
import org.scalatest.Matchers

class JsonSerializerSpec extends FunSpec with Matchers {

  describe("JsonBuilder") {
    it("should produce json with strings") {
      val stuffToSerialize = Seq(("key1", "value1"), ("key2", "value2"))

      SimpleJsonSerializer(stuffToSerialize) should equal( """{"key1":"value1","key2":"value2"}""")
    }

    it("should produce json with dates formatted as ISO8601") {
      val date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z").parse("2011-01-01 00:00:00 -0500")
      val stuffToSerialize = Seq(("key1", "value1"), ("key2", date))

      SimpleJsonSerializer(stuffToSerialize) should equal( """{"key1":"value1","key2":"2011-01-01T05:00Z"}""")
    }

    it("should produce json with numbers") {
      val stuffToSerialize = Seq(("key1", 53.23), ("answer to life", 42))

      SimpleJsonSerializer(stuffToSerialize) should equal( """{"key1":53.23,"answer to life":42}""")
    }
  }
}
