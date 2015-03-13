package services

import models.{Geolocation, Ping}
import org.joda.time.DateTime
import org.scalatestplus.play._
import play.api.test.Helpers._
import org.scalatestplus.play.{PlaySpec, OneAppPerSuite}


class SalesforceSpec extends PlaySpec with OneAppPerSuite {

  "Salesforce" must {
    "update the ping" in {
      val ping = Ping(Geolocation(37.785143, -122.403405), DateTime.now())

      try {
        await(Salesforce(app).updatePing("a00j00000041nvH", ping))
      }
      catch {
        case e: Exception => fail("This should not fail")
      }
    }
  }

}