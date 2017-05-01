package models

import breeze.linalg.DenseVector
import edu.stanford.nlp.simple._

/**
  * Created by jessechen on 4/29/17.
  */
class Segment(val sentences: List[Sentence]) extends Vectorizable {
  val dimension = 57

  /**
    * Vectorize on:
    * type-token ratio (size of vocab of sample / number of unique words
    * Word length frequencies (1 - 20)
    * Distribution of parts of speech
    * Frequency of certain pronouns (he, she, it, there, here, is)
    * @return a feature vector representing the segment
    */
  def vectorize(): DenseVector[Double] = {
    var vectorList = List[Double]()
    val words = sentences.flatMap(sentence => sentence.words().toArray.toList.asInstanceOf[List[String]])

    val uniqueWordMap = words.foldLeft(Map.empty[String, Int]) { case (map, word) =>
      map + (word -> (map.getOrElse(word, 0) + 1))
    }
    vectorList = vectorList ++ List[Double](uniqueWordMap.keySet.size.toFloat / words.length.toFloat)

    val countWordMap = words.foldLeft(Map.empty[Int, Int]) { case (map, word) =>
      map + (word.length -> (map.getOrElse(word.length, 0) + 1))
    }
    vectorList = vectorList ++ (1 to 20).map(num => countWordMap.getOrElse(num, 0).toDouble)

    val partsOfSpeech = sentences.flatMap(sentence => sentence.posTags().toArray.toList.asInstanceOf[List[String]])
    val partsOfSpeechMap = partsOfSpeech.foldLeft(Map.empty[String, Int]) { case (map, pos) =>
      map + (pos -> (map.getOrElse(pos, 0) + 1))
    }
    vectorList = vectorList ++ Segment.partsOfSpeech.map(part => partsOfSpeechMap.getOrElse(part, 0).toDouble)

    DenseVector[Double](vectorList.toArray)
  }
}

object Segment {
  val partsOfSpeech = scala.io.Source.fromFile("app/resources/pos.txt").mkString.split("\n")
}
