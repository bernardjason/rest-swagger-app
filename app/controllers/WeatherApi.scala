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
class WeatherApi @Inject() (conf: play.api.Configuration, timeSeries: TimeSeriesOperations, ws: WSClient,system: ActorSystem) extends Controller {

  //val accessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")
  val weatherApiUrl="http://api.openweathermap.org/data/2.5/weather"

  import scala.concurrent.duration._

  val port = Play
  system.scheduler.schedule(60 seconds, 60 seconds) {
    Logger.info("Start weather api scheduled read")

    val port = conf.getInt("http.port").getOrElse(9000)
    ws.url(s"http://0.0.0.0:${port}/weather/London").post("")
    ws.url(s"http://0.0.0.0:${port}/weather/Darlington").post("")
    Logger.info("end weather api scheduled read")
  }
  
  @ApiOperation(nickname = "a wrapper api to populate the weather for a city", value = 
      "a wrapper api to populate the weather for a city",
    notes = "Returns a list of timeseries points",
    response = classOf[models.TimeSeriesRow],
    httpMethod = "POST")
  @ApiResponses(Array( new ApiResponse(code = 400, message = "Invalid")))
  def addWeather(@ApiParam(name = "name", required = true, defaultValue = "London") name:String) =
    Action.async(BodyParsers.parse.empty) { implicit request => 

    val futureResponse: Future[WSResponse] = for {
          openWeatherResponse <- ws.url(weatherApiUrl)
            .withQueryString("q" -> name )
            .withQueryString("units" -> "metric")
            .withQueryString("APPID" -> "13c4195b3e9f9898fea40601ab493c7f")
          .get()
          responseThree <- {
                Logger.info(s"${name} ${openWeatherResponse.body}")
                val temp = (openWeatherResponse.json \ "main"  \ "temp" ).as[Float]
                val dt = (openWeatherResponse.json \ "dt"   ).as[Long]
                val store = Json.toJson(models.TimeSeriesLabelValue(""+dt,""+temp))
               
                val url = routes.TimeSeriesApi.postByName(name).absoluteURL()

                routes.TimeSeriesApi.postByName("")
                ws.url(url).post(store)
          }
    } yield responseThree
    
   futureResponse.map{  
      r => {
        Ok(r.json).as("application/json")
      }
    }
    }
}


