package models

/**
  * Created by jessechen on 4/25/17.
  */
abstract class Text(var text: String) extends Vectorizable {
  def sanitize(): String = {
    val sanitizedText = text.toLowerCase.trim()
      .replaceAll('\"'.toString, "")
      .replaceAll("[", "")
      .replaceAll("]", "")
      .replaceAll("(", "")
      .replaceAll(")", "")
    val stopWords = scala.io.Source.fromFile("app/resources/stopwords.txt").mkString
      .split('\n').map(word => " " + word + " ")
    stopWords.foldLeft(sanitizedText)((sanitizedText, stopWord) => sanitizedText.replaceAll(stopWord, " "))
  }
}
