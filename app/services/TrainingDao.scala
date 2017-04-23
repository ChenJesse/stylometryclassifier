package services

import java.util.concurrent.TimeUnit

import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by jessechen on 3/31/17.
  */

@Singleton
class TrainingDao @Inject() (reactiveMongoAPI: ReactiveMongoApi) {
  def testConnection(): Option[JsObject] = {
    val collection = Await.result(reactiveMongoAPI.database.map(_.collection[JSONCollection]("testcol")), Duration(5, TimeUnit.SECONDS))
    collection.remove(Json.obj("article" -> "Lorem ipsum"))
    collection.insert(Json.obj("article" -> "Lorem ipsum"))
    val document = Await.result(collection.find(Json.obj()).one[JsObject], Duration(5, TimeUnit.SECONDS))

    return document
  }

  def isTrained(): Boolean = {
    false
  }
}
