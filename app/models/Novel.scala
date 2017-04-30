package models
import edu.stanford.nlp.simple._

/**
  * Created by jessechen on 3/27/17.
  */

class Novel(title: String, segmentLength: Int, author: Author) {
  var segments: Seq[Segment] = Seq()

  def loadSegments(): Unit = {
    val novelText = scala.io.Source.fromFile("app/resources/training/" + title).mkString
    val document: Document = new Document(novelText)
    val sentences: List[Sentence] = document.sentences().toArray.asInstanceOf[List[Sentence]]
    segments = sentences.sliding(2, 2).map(listSentence => new Segment(listSentence)).toList
  }
}