package services

import java.nio.charset.StandardCharsets
import javax.inject._
import java.util.Base64
import java.util.concurrent.TimeUnit

import models.Article
import play.api.libs.json.{JsError, JsPath, JsSuccess, Reads}
import play.api.libs.ws.WSClient
import play.api.libs.functional.syntax._

import scala.concurrent.duration.Duration
import scala.concurrent.Await
import models._

/**
  * Created by jessechen on 3/27/17.
  */
@Singleton
class TwitterAPI @Inject() (configuration: play.api.Configuration, ws: WSClient) {
  val oauthRoute = "https://api.twitter.com/oauth2/token"
  val tweetRoute = "https://api.twitter.com/1.1/search/tweets.json"

  val key = (
    configuration.underlying.getString("twitter.key"),
    configuration.underlying.getString("twitter.secret")
  )

  var sessionCode: Option[String] = None

  def encode = "Basic " + Base64.getEncoder.encodeToString((key._1 + ":" + key._2).getBytes(StandardCharsets.UTF_8))

  def authenticate = sessionCode match {
    case None =>
      sessionCode = Some(Await.result(ws.url(oauthRoute).withHeaders (
        "Authorization" -> encode,
        "Content-Type" -> "application/x-www-form-urlencoded;charset=UTF-8"
      ).post().map {
        response => (response.json \ "access_token").as[String]
      }, Duration(100, TimeUnit.MILLISECONDS)))
    case Some(_) => Unit
  }

  def fetchArticles(): Seq[Article] = {
    authenticate
    Await.result(ws.url(tweetRoute).withHeaders (
      "Authorization" -> encode
    ).withQueryString("q" -> "trump").get().map {
      response =>
        response.json.validate[Seq[Article]] match {
        case s: JsSuccess[Seq[Article]] => s.get
        case _: JsError => throw Exception
      }
    }, Duration(100, TimeUnit.MILLISECONDS))
  }

  implicit val textReads: Reads[Article] = (
      (JsPath \ "text").read[String] and
      (JsPath \ "created_at").read[String]
    )(Article.apply _)

}
