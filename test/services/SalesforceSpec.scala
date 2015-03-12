package services

import models.{Geolocation, Ping}
import org.joda.time.DateTime
import org.scalatestplus.play._
import play.api.test.Helpers._
import org.scalatestplus.play.{PlaySpec, OneAppPerSuite}


class SalesforceSpec extends PlaySpec with OneAppPerSuite {

  "Salesforce" must {
    "insert a ping" in {
      val ping = Ping("asdf", Geolocation(-122.403405, 37.785143), DateTime.now())

      val json = await(Salesforce(app).insertPing(ping))

      (json \ "success").asOpt[Boolean] mustBe Some(true)
    }
  }

}