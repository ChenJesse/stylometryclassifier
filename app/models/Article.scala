package models

/**
  * Created by jessechen on 3/27/17.
  */

class Article(text: String, date: String, source: Source) {
  def getText = text
}

object Article {
  def apply(s: String, date: String, source: Source) = new Article(s, date, source)
}
