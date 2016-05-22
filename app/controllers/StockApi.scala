package controllers

import scala.concurrent.Future
import dal.TimeSeriesOperations
import javax.inject.Inject
import javax.inject.Singleton
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsError
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.BodyParsers
import play.api.mvc.Controller
import scala.collection.mutable.ArrayBuffer
import models.TimeSeriesRow
import io.swagger.annotations._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.concurrent.Promise
import play.api.Play
import akka.actor._
import play.Logger;

@Api(value = "/stock", description = "simple api to get the price of a stock we are interested in")
@Singleton
class StockApi @Inject() (conf: play.api.Configuration, timeSeries: TimeSeriesOperations, ws: WSClient, system: ActorSystem) extends Controller {

  val stockApiUrl = "http://query.yahooapis.com/v1/public/yql"
  def yql(quote: String) = s"select * from yahoo.finance.quotes where symbol ='${quote}'"
  val env = "store://datatables.org/alltableswithkeys"

  import scala.concurrent.duration._

  val port = Play

  system.scheduler.schedule(10 seconds, 120 seconds) {
    Logger.info("Start stock read")

    val port = conf.getInt("http.port").getOrElse(9000)
    ws.url(s"http://0.0.0.0:${port}/stock/%5EFTSE").post("")
    ws.url(s"http://0.0.0.0:${port}/stock/BT-A.L").post("")
    Logger.info("end stock read")
  }

  @ApiOperation(value = "a wrapper api to populate the current stock value. It will call Yahoo's query.yahooapis.com to get the actual stock value",
    notes = "Return timeseries point",
    response = classOf[models.TimeSeriesRow],
    httpMethod = "POST")
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Opps")))
  def addStock(@ApiParam(name = "name", value = "examples are ^FTSE or BT-A.L", required = true, defaultValue = "^FTSE") name: String) =
    Action.async(BodyParsers.parse.empty) { implicit request =>

      val futureResponse: Future[WSResponse] = for {
        yahooResponse <- ws.url(stockApiUrl)
          .withQueryString("q" -> yql(name))
          .withQueryString("env" -> env)
          .withQueryString("format" -> "json")
          .get()
        timeSeriesResponse <- {
          Logger.info(s"${name} ${yahooResponse.body}")
          val dt = (yahooResponse.json \ "query" \ "created").as[String]
          val lastTradePriceOnly = (yahooResponse.json \ "query" \ "results" \ "quote" \ "LastTradePriceOnly").as[String]
          val store = Json.toJson(models.TimeSeriesLabelValue("" + dt, "" + lastTradePriceOnly))

          val url = routes.TimeSeriesApi.postByName(name).absoluteURL()

          routes.TimeSeriesApi.postByName("")
          ws.url(url).post(store)
        }
      } yield timeSeriesResponse

      futureResponse.map {
        r =>
          {
            Ok(r.json).as("application/json")
          }
      }
    }
}


