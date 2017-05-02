import edu.stanford.nlp.simple.{Document, Sentence}
import models.{Author, Segment}
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
    }
  }
}
