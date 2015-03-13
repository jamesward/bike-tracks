package services

import models.Ping
import play.api.Application
import play.api.libs.json.{JsObject, Json, JsValue}
import play.api.libs.ws.{WSResponse, WSRequestHolder, WS}
import play.api.http.{Status, HeaderNames}
import play.api.mvc.Results.EmptyContent
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class Salesforce(implicit app: Application) {

  lazy val authFuture: Future[Auth] = {
    val params = Map(
      "grant_type" -> "password",
      "client_id" -> app.configuration.getString("salesforce.consumer.key").get,
      "client_secret" -> app.configuration.getString("salesforce.consumer.secret").get,
      "username" -> app.configuration.getString("salesforce.username").get,
      "password" -> app.configuration.getString("salesforce.password").get
    )

    WS.
      url("https://login.salesforce.com/services/oauth2/token").
      withQueryString(params.toSeq:_*).
      post(EmptyContent()).
      flatMap { response =>
        response.status match {
          case Status.OK =>
            Future.successful(Auth((response.json \ "access_token").as[String], (response.json \ "instance_url").as[String]))
          case _ =>
            Future.failed(new IllegalStateException(s"Auth Denied: ${response.body}"))
        }
      }
  }


  def ws(path: String): Future[WSRequestHolder] = {
    authFuture.map { auth =>
      WS.
        url(s"${auth.instance}/services/data/v32.0/sobjects/$path").
        withHeaders(HeaderNames.AUTHORIZATION -> s"Bearer ${auth.token}")
    }
  }

  def updatePing(bikeRentalId: String, ping: Ping): Future[Unit] = {
    ws(s"Rental_Bike__c/$bikeRentalId").flatMap {
      _.patch(Json.toJson(ping)).flatMap { response =>
        response.status match {
          case Status.NO_CONTENT =>
            Future.successful(Unit)
          case _ =>
            Future.failed(new IllegalStateException(response.json.toString()))
        }
      }
    }
  }

}

object Salesforce {
  def apply(implicit app: Application) = new Salesforce()
}

case class Auth(token: String, instance: String)