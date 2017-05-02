import classifiers.BinaryLabel.{LabelA, LabelB}
import classifiers.{ClassifierWrapper, LogisticRegressionClassifier, Regularization}
import edu.stanford.nlp.simple.{Document, Sentence}
import models.{Author, Martin, Segment, Tolkien}
import org.scalatestplus.play.PlaySpec

/**
  * Created by jessechen on 4/30/17.
  */
class SegmentVectorizationSpec extends PlaySpec {
  "Segment vectorization" should {

    "should parse sentences and split them correctly" in {
      val novelText =
        """“No,” she told him. “Your place is here. There must always be a Stark in Winterfell.” She looked at Ser Rodrik with his great white whiskers, at Maester Luwin in his grey robes, at young Greyjoy, lean and dark and impetuous. Who to send? Who would be believed? Then she knew. Catelyn struggled to push back the blankets, her bandaged fingers as stiff and unyielding as stone. She climbed out of bed. “I must go myself.”""".stripMargin
      val document: Document = new Document(novelText)
      val sentences = document.sentences().toArray.toList.asInstanceOf[List[Sentence]]

      sentences.length mustBe 10

      val segments = sentences.sliding(2, 2).map(listSentence => new Segment(listSentence)).toList

      segments.length mustBe 5
      segments.flatMap(segment => segment.sentences) mustBe sentences
      println(segments.head.sentences)
      println(segments.head.sentences.map(_.posTags()))
      segments.flatMap(segment => segment.sentences.map(_.toString)).mkString(" ") mustBe novelText
    }

    "should be able to parse a novel correctly" in {
      case object TestMartin extends Author {
        val name = "Martin"
        override val novelData = Seq("agameofthrones")
      }

      TestMartin.loadNovels
      TestMartin.novels.head.loadSegments()
      TestMartin.novels.head.title mustBe "agameofthrones"
      TestMartin.novels.head.segments.length mustBe 2485

      val reconstructedText = TestMartin.novels.head.segments.flatMap(_.sentences.map(_.toString)).mkString(" ")
      val text = scala.io.Source.fromFile("app/resources/training/agameofthrones.txt").mkString
      println(reconstructedText.length)
      println(text.length)
    }

    //TAKES TOO LONG TO RUN, APPROX 20 MIN
//    "should be able to classify Tolkien vs Martin with reasonably training error" in {
//      Tolkien.loadNovels
//      Martin.loadNovels
//      Tolkien.novels.foreach(novel => novel.loadSegments())
//      Martin.novels.foreach(novel => novel.loadSegments())
//      val segmentsA = Tolkien.novels.flatMap(novel => novel.segments)
//      val segmentsB = Martin.novels.flatMap(novel => novel.segments)
//      val labels = segmentsA.map(_ => LabelA) ++ segmentsB.map(_ => LabelB)
//      val classifier = new ClassifierWrapper[Segment](
//        new LogisticRegressionClassifier(Segment.defaultDimension, Option(Regularization()))
//      )
//      classifier.train(segmentsA ++ segmentsB, labels)
//      println(classifier.test(segmentsA ++ segmentsB, labels))
//    }
  }
}
