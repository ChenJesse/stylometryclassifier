package models
import edu.stanford.nlp.simple._

/**
  * Created by jessechen on 3/27/17.
  */

class Novel(val title: String, segmentLength: Int) {
  var segments: Seq[Segment] = Seq()

  def getSegments = segments

  def loadSegments() = {
    val novelText = scala.io.Source.fromFile("app/resources/training/" + title + ".txt").mkString
    val document: Document = new Document(novelText)
    val sentences: List[Sentence] = document.sentences().toArray.toList.asInstanceOf[List[Sentence]]
    segments = sentences.sliding(segmentLength, segmentLength).map(listSentence => new Segment(listSentence)).toList
  }
}