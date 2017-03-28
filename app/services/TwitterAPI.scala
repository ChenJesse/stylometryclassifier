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
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

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

  var bearerToken: Option[String] = None

  def encodedKey = Base64.getEncoder.encodeToString((key._1 + ":" + key._2).getBytes(StandardCharsets.UTF_8))

  def authenticate = bearerToken match {
    case None =>
      bearerToken = Some(Await.result(ws.url(oauthRoute).withHeaders (
        "Authorization" -> ("Basic " + encodedKey),
        "Content-Type" -> "application/x-www-form-urlencoded;charset=UTF-8"
      ).post("grant_type=client_credentials").map {
        response =>
          println(response)
          (response.json \ "access_token").as[String]
      }, Duration(5, TimeUnit.SECONDS)))
    case Some(_) => Unit
  }

  def fetchArticles(): Future[Seq[Article]] = {
    authenticate
    ws.url(tweetRoute).withHeaders (
      "Authorization" -> ("Bearer " + bearerToken.getOrElse(""))
    ).withQueryString("q" -> "trump").get().map {
      response =>
        println(response)
        println(response.json)
        (response.json \ "statuses").validate[Seq[Article]] match {
        case s: JsSuccess[Seq[Article]] => s.get
        case _: JsError => Seq.empty
      }
    }
  }

  implicit val textReads: Reads[Article] = (
      (JsPath \ "text").read[String] and
      (JsPath \ "created_at").read[String]
    )(Article.apply _)
}
