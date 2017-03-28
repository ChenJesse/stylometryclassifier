package models

/**
  * Created by jessechen on 3/27/17.
  */

case object Twitter extends Source
case object Reddit extends Source
case object CNN extends Source
case object FOX extends Source

class Article (text: String, date: String, source: Source) {

}

object Article {
  def apply(s: String, date: String) = new Article(s, date, Twitter)
}
