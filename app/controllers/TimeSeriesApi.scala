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

@Api(value = "/timeseries", description = "Operations for timeseries use and population")
@Singleton
class TimeSeriesApi @Inject() (timeSeries: TimeSeriesOperations) extends Controller {

  val accessControlAllowOrigin = ("Access-Control-Allow-Origin", "*")

  @ApiOperation(nickname = "getByName", value = 
      "get information series for a timeseries by name."+ 
      "The list is returned in the order that it was inserted into the database",
    notes = "Returns a list of timeseries points",
    responseContainer = "List", response = classOf[models.TimeSeriesRow],
    httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid")))
  def getByName(@ApiParam(name = "name", required = true, defaultValue = "motivation") name: String) =
    Action.async {
      timeSeries.list(name).map { timeSeriresRows =>
        Ok(Json.toJson(timeSeriresRows)).withHeaders(accessControlAllowOrigin)
      }
    }

  @ApiOperation(nickname = "postByName", value = "create entry for timeseries name",
    notes = "Returns created timeseries points, no checks are made of the data",
    response = classOf[models.TimeSeriesRow],
    httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      dataType = "models.TimeSeriesLabelValue",
      required = true,
      paramType = "body",
      value = "label value pair object")))
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid")))
  def postByName(@ApiParam(name = "name", required = true, defaultValue = "energy") name: String) = Action.async(BodyParsers.parse.json) { implicit request =>

    val create = request.body.validate[models.TimeSeriesLabelValue]

    create.fold(
      errors => {
        Future.successful(InternalServerError(JsError.toJson(errors)))
      },
      row => {
        timeSeries.create(name, row.label, row.value).map(created =>
          Ok(Json.toJson(created)).withHeaders(accessControlAllowOrigin))
      })

  }
}


