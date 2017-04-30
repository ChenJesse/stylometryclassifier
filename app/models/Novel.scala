package models

import epic.preprocess.MLSentenceSegmenter

/**
  * Created by jessechen on 3/27/17.
  */

class Novel(title: String, segmentLength: Int, author: Author) {
  var segments: Seq[Segment] = Seq()

  def loadSegments(): Unit = {
    val novelText = scala.io.Source.fromFile("app/resources/training/" + title).mkString
    val sentenceSplitter = MLSentenceSegmenter.bundled().get
    val tokenizer = new epic.preprocess.TreebankTokenizer()
    val sentences: IndexedSeq[IndexedSeq[String]] = sentenceSplitter(novelText).map(tokenizer).toIndexedSeq
    segments = sentences.sliding(segmentLength, segmentLength).toList.map(segment => new Segment(segment))
  }
}