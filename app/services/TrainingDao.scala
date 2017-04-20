package services

import java.util.concurrent.TimeUnit

import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection
import play.modules.reactivemongo.json._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by jessechen on 3/31/17.
  */

@Singleton
class TrainingDao @Inject() (reactiveMongoAPI: ReactiveMongoApi) {
  def testConnection(): Unit = {
    val collection = Await.result(reactiveMongoAPI.database.map(_.collection[JSONCollection]("testcol")), Duration(5, TimeUnit.SECONDS))
    print(collection.find(Json.obj("name" -> "jesse")))
//    collection.insert(Json.obj("name" -> "jesse"))
  }
}
