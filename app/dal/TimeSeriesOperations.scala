package dal

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import models.TimeSeriesRow
import scala.concurrent.{ Future, ExecutionContext }
import scala.collection.mutable.ArrayBuffer


@Singleton
class TimeSeriesOperations @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  
  import dbConfig._
  import driver.api._

  class TimeSeriesTable(tag: Tag) extends Table[models.TimeSeriesRow](tag, "timeseries") {
    
    def id = column[Long]("id",O.PrimaryKey,O.AutoInc)
    def name = column[String]("name")
    def label = column[String]("label")
    def value = column[String]("value")

    override def * = (id,name,label,value) <> (models.TimeSeriesRow.tupled, models.TimeSeriesRow.unapply _)
  }
  
  private val timeSeries = TableQuery[TimeSeriesTable]
  
  def create(name: String, label: String , value: String): Future[TimeSeriesRow] = db.run {

    (timeSeries.map(p => (p.name, p.label,p.value))
      returning timeSeries.map(_.id)
     
      into ((entry, id) => { TimeSeriesRow(id,entry._1, entry._2,entry._3)} )
    ) .+=(name, label, value)
  }

  def list(name:String): Future[Seq[TimeSeriesRow]] = db.run {
    timeSeries.filter( _.name === name ).result
  }
  
}