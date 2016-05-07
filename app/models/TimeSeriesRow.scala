package models

import play.api.libs.json._

case class TimeSeriesRow(id: Long, name: String, label:String , value:String)

object TimeSeriesRow extends ((Long, String, String,String) => TimeSeriesRow) {
  
  implicit val timeSeriesFormat = Json.format[TimeSeriesRow]
}
