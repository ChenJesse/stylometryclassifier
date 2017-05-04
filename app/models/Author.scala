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
  def loadNovels = {
    if (novels.isEmpty) {
      novelTrain = novelDataTrain.map { title => new Novel(title, 10) }
      novelValidate = novelDataValidate.map { title => new Novel(title, 10) }
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