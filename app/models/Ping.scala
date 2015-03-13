package models

import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Ping(location: Geolocation, moment: DateTime)

object Ping {

  implicit val jsonWrites: Writes[Ping] = (
    (__ \ "Ping_Location__Latitude__s").write[Double] ~
    (__ \ "Ping_Location__Longitude__s").write[Double] ~
    (__ \ "Ping_Moment__c").write[DateTime]
  ) { ping =>
    (ping.location.lat, ping.location.lon, ping.moment)
  }

}