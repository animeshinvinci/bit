package bitcoin.model

import spray.json.DefaultJsonProtocol

/**
  * For bitcoin.model in play-scala-rest-api-example
  * Created by whereby[Tao Zhou](187225577@qq.com) on 2018/9/16
  */
/**
  * A custom version of the Spray DefaultJsonProtocol with a modified field naming strategy
  */
trait SnakifiedSprayJsonSupport extends DefaultJsonProtocol {
  import reflect._

  /**
    * This is the most important piece of code in this object!
    * It overrides the default naming scheme used by spray-json and replaces it with a scheme that turns camelcased
    * names into snakified names (i.e. using underscores as word separators).
    */
  override protected def extractFieldNames(classTag: ClassTag[_]) = {
    import java.util.Locale

    def snakify(name: String) = PASS2.replaceAllIn(PASS1.replaceAllIn(name, REPLACEMENT), REPLACEMENT).toLowerCase(Locale.US)

    super.extractFieldNames(classTag).map { snakify(_) }
  }

  private val PASS1 = """([A-Z]+)([A-Z][a-z])""".r
  private val PASS2 = """([a-z\d])([A-Z])""".r
  private val REPLACEMENT = "$1_$2"
}