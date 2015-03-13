package controllers

import models.{Geolocation, Ping}
import org.joda.time.DateTime
import play.api.{Logger, Play}
import play.api.mvc.{Action, Controller}
import services.Salesforce
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

object Application extends Controller {

  def sendLoc = Action.async(parse.urlFormEncoded) { request =>
    Logger.info(request.body.toString())
    val bikeRentalId = request.body("bike_rental_id").head
    val geo = Geolocation(request.body("lat").head.toDouble, request.body("lon").head.toDouble)
    val dateTime = Try {
      DateTime.parse(request.body("time").head)
    } getOrElse {
      DateTime.now()
    }
    val ping = Ping(geo, dateTime)
    Salesforce(Play.current).updatePing(bikeRentalId, ping).map { r =>
      Logger.info(r.toString)
      Ok
    }
  }

}
