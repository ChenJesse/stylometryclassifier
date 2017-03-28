package models

/**
  * Created by jessechen on 3/27/17.
  */

case object Twitter extends Source
case object Reddit extends Source
case object CNN extends Source
case object FOX extends Source

class Article(text: String, date: String, source: Source) {
  def getText = text
}

object Article {
  def apply(s: String, date: String, source: Source) = new Article(s, date, Twitter)
}
