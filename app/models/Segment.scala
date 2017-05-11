package models

import breeze.linalg.DenseVector
import edu.stanford.nlp.simple._

/**
  * Created by jessechen on 4/29/17.
  */
class Segment(val sentences: List[Sentence]) extends Vectorizable {
  val dimension = Segment.defaultDimension

  /**
    * Vectorize on:
    * type-token ratio (size of vocab of sample / number of unique words
    * Word length frequencies (1 - 20)
    * Distribution of parts of speech
    * Frequency of certain conjunctions, pronouns
    * @return a feature vector representing the segment
    */
  def vectorize(): DenseVector[Double] = {
    var vectorList = List[Double]()
    val words = sentences.flatMap(sentence => sentence.words().toArray.toList.asInstanceOf[List[String]])

    val uniqueWordMap = words.foldLeft(Map.empty[String, Int]) { case (map, word) =>
      map + (word.toLowerCase -> (map.getOrElse(word.toLowerCase, 0) + 1))
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

    vectorList = vectorList ++ Segment.commonConjunctions.map(conj => uniqueWordMap.getOrElse(conj, 0).toDouble)

    vectorList = vectorList ++ Segment.commonPronouns.map(pronoun => uniqueWordMap.getOrElse(pronoun, 0).toDouble)

    DenseVector[Double](vectorList.toArray)
  }
}

object Segment {
  def apply(text: String) = {
    val sentences = new Document(text).sentences().toArray.toList.asInstanceOf[List[Sentence]]
    new Segment(sentences)
  }

  val partsOfSpeech = scala.io.Source.fromFile("app/resources/pos.txt").mkString.split("\n").map(_.trim())
  val commonConjunctions = List("for", "and", "nor", "but", "or", "yet", "so")
  val commonPronouns = List("she", "he", "her", "him", "his", "hers")
  val defaultDimension = 70
}
