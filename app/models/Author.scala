package models

/**
  * Created by jessechen on 4/28/17.
  */
trait Author {
  val name: String
  val novelDataTrain: Seq[String] = Seq()
  val novelDataValidate: Seq[String] = Seq()
  var novelTrain: Seq[Novel] = Seq()
  var novelValidate: Seq[Novel] = Seq()
  var novels: Seq[Novel] = Seq()
  val segmentLength = 10
  def loadNovels = {
    if (novels.isEmpty) {
      novelTrain = novelDataTrain.map { title => new Novel(title, segmentLength) }
      novelValidate = novelDataValidate.map { title => new Novel(title, segmentLength) }
      novels = novelTrain ++ novelValidate
    }
  }
}
case object Tolkien extends Author {
  val name = "Tolkien"
  override val novelDataTrain = Seq(
    "thefellowshipofthering",
    "thetwotowers"
  )
  override val novelDataValidate: Seq[String] = Seq(
    "thereturnoftheking"
  )
}
case object Martin extends Author {
  val name = "Martin"
  override val novelDataTrain = Seq(
    "agameofthrones"
//    "aclashofkings"
  )
  override val novelDataValidate: Seq[String] = Seq(
    "astormofswords"
  )
}

case object Collins extends Author {
  val name = "Collins"
  override val novelDataTrain = Seq(
    "hungergames",
    "catchingfire"
  )
  override val novelDataValidate: Seq[String] = Seq(
    "mockingjay"
  )
}

case object Roth extends Author {
  val name = "Roth"
  override val novelDataTrain = Seq(
    "divergent",
    "insurgent"
  )
  override val novelDataValidate: Seq[String] = Seq(
    "allegiant"
  )
}