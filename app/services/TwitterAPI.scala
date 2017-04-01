package services

import java.nio.charset.StandardCharsets
import javax.inject._
import java.util.Base64
import java.util.concurrent.TimeUnit

import models.{Article, Twitter}
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
  private val oauthRoute = "https://api.twitter.com/oauth2/token"
  private val tweetRoute = "https://api.twitter.com/1.1/search/tweets.json"

  private val key = (
    configuration.underlying.getString("twitter.key"),
    configuration.underlying.getString("twitter.secret")
  )

  private var bearerToken: Option[String] = None

  def hasBearerToken = bearerToken match {
    case Some(_) => true
    case None => false
  }

  implicit val articleReads: Reads[Article] = (
    (JsPath \ "text").read[String] and
      (JsPath \ "created_at").read[String]
    )(Article.apply(_,_,Twitter))

  def encodedKey = Base64.getEncoder
    .encodeToString((key._1 + ":" + key._2)
    .getBytes(StandardCharsets.UTF_8))

  def authenticate(): Boolean = bearerToken match {
    case None =>
      val token: Option[String] = Await.result(ws.url(oauthRoute).withHeaders (
        "Authorization" -> ("Basic " + encodedKey),
        "Content-Type" -> "application/x-www-form-urlencoded;charset=UTF-8"
      ).post("grant_type=client_credentials").map {
        response =>
          response.status match {
            case 200 => Some((response.json \ "access_token").as[String])
            case _ => None
          }
      }, Duration(5, TimeUnit.SECONDS))
      token match {
        case Some(token) =>
          bearerToken = Some(token)
          true
        case None => false
      }
    case Some(_) => true
  }

  def fetchArticles(): Future[Seq[Article]] = {
    authenticate match {
      case true =>
        ws.url(tweetRoute).withHeaders (
          "Authorization" -> ("Bearer " + bearerToken.getOrElse(""))
        ).withQueryString("q" -> "trump").get().map {
          response =>
            (response.json \ "statuses").validate[Seq[Article]] match {
            case s: JsSuccess[Seq[Article]] => s.get
            case _: JsError => Seq.empty
          }
        }
      case false => Future(Seq.empty)
    }
  }
}
