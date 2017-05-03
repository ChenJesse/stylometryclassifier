package services

import java.util.concurrent.TimeUnit

import breeze.linalg.DenseVector
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{Await, Future}
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

    document
  }

  def getClassifierParams(collectionName: String): Future[Option[(DenseVector[Double], Double)]] = {
    try {
      val cursor = getCollection(collectionName).find(Json.obj()).cursor[JsObject]()
      val futureJson = cursor.collect[List]()
      futureJson.map { json =>
        if (json.nonEmpty) {
          (json.head \ "isTrained").as[Boolean] match {
            case true => Some(DenseVector((json.head \ "w").as[List[Double]].toArray), (json.head \ "b").as[Double])
            case false => None
          }
        } else {
          None
        }
      }
    } catch {
      case _: Throwable => Future(None)
    }
  }

  def persistClassifierParams(w: DenseVector[Double], b: Double, collectionName: String): Boolean = {
    try {
      val collection = getCollection(collectionName)
      collection.remove(Json.obj())
      collection.insert(Json.obj("isTrained" -> true, "w" -> w.toArray, "b" -> b))
      true
    } catch {
      case _: Throwable => false
    }
  }

  def clearClassifierParams(name: String) = getCollection(name).remove(Json.obj())

  def getDefaultCollection(): JSONCollection =
    Await.result(reactiveMongoAPI.database.map(_.collection[JSONCollection]("default")), Duration(5, TimeUnit.SECONDS))

  def getCollection(name: String) =
    Await.result(reactiveMongoAPI.database.map(_.collection[JSONCollection](name)), Duration(5, TimeUnit.SECONDS))

}
