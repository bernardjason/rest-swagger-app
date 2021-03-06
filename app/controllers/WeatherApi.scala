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

@Api(value = "/weather", description = "simple api to get the weather we are interested in")
@Singleton
class WeatherApi @Inject() (conf: play.api.Configuration, timeSeries: TimeSeriesOperations, ws: WSClient, system: ActorSystem) extends Controller {

  val weatherApiUrl = "http://api.openweathermap.org/data/2.5/weather"
  val APPID = "13c4195b3e9f9898fea40601ab493c7f"

  import scala.concurrent.duration._

  val port = Play
  system.scheduler.schedule(10 seconds, 120 seconds) {
    Logger.info("Start weather api scheduled read")

    val port = conf.getInt("http.port").getOrElse(9000)
    ws.url(s"http://0.0.0.0:${port}/weather/London").post("")
    ws.url(s"http://0.0.0.0:${port}/weather/Darlington").post("")
    Logger.info("end weather api scheduled read")
  }

  @ApiOperation(value = "a wrapper api to populate the weather for a city. It will invoke api.openweathermap.org's API to get the actual value",
    notes = "Returns timeseries point",
    response = classOf[models.TimeSeriesRow],
    httpMethod = "POST")
  @ApiResponses(Array(new ApiResponse(code = 500, message = "Opps")))
  def addWeather(@ApiParam(name = "name", value = "examples are London or Darlington", required = true, defaultValue = "London") name: String) =
    Action.async(BodyParsers.parse.empty) { implicit request =>

      val futureResponse: Future[WSResponse] = for {
        openWeatherResponse <- ws.url(weatherApiUrl)
          .withQueryString("q" -> name)
          .withQueryString("units" -> "metric")
          .withQueryString("APPID" -> APPID)
          .get()
        timeSeriesResponse <- {
          Logger.info(s"${name} ${openWeatherResponse.body}")
          val temp = (openWeatherResponse.json \ "main" \ "temp").as[Float]
          val dt = (openWeatherResponse.json \ "dt").as[Long]
          val store = Json.toJson(models.TimeSeriesLabelValue("" + dt, "" + temp))

          val url = routes.TimeSeriesApi.postByName(name).absoluteURL()

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


