import com.typesafe.config.ConfigFactory
import smoke._

object AuthentikatApp extends App {
  val smoke = new Authentikat

}

class Authentikat extends Smoke with StaticAssets {

  override val smokeConfig = ConfigFactory.load().getConfig("smoke")
  override val executionContext = scala.concurrent.ExecutionContext.global

  override val publicFolder: String = smokeConfig.getString("static-assets.public-dir")
  override val cacheAssets: Boolean = smokeConfig.getBoolean("static-assets.cache-assets")
  override val cacheAssetsPreload: Boolean = smokeConfig.getBoolean("static-assets.cache-assets-preload")

  onRequest {
    case GET(Path("/example")) ⇒ reply {
      Thread.sleep(1000)
      Response(Ok, body = "It took me a second to build this response.\n")
    }
    case GET(Path(path)) ⇒ reply(responseFromAsset(path))
    case _               ⇒ reply(Response(NotFound))
  }

  after { response ⇒
    val headers = response.headers ++ Seq(
      "Server" -> "Authenticat",
      "Connection" -> "Close")
    Response(response.status, headers, response.body)
  }
}
