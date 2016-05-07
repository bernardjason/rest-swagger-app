package models

import play.api.libs.json._

 case class TimeSeriesLabelValue(label:String, value:String)
 
 object TimeSeriesLabelValue  extends ((String,String) => TimeSeriesLabelValue) {
  
  implicit val personFormat = Json.format[TimeSeriesLabelValue]
}