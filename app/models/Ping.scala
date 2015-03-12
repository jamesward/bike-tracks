package models

import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Ping(bikeRentalId: String, location: Geolocation, moment: DateTime)

object Ping {

  implicit val jsonWrites: Writes[Ping] = (
    (__ \ "Rental_Bike__c").write[String] ~
    (__ \ "Location__Latitude__s").write[Double] ~
    (__ \ "Location__Longitude__s").write[Double] ~
    (__ \ "Moment__c").write[DateTime]
  ) { ping =>
    (ping.bikeRentalId, ping.location.lat, ping.location.lon, ping.moment)
  }

}