package models

/**
  * Created by jessechen on 4/28/17.
  */
trait Author {
  val name: String
  val novelData: Seq[String] = Seq()
  var novels: Seq[Novel] = Seq()
  def loadNovels = {
    novels = novelData.map { title => new Novel(title, 10) }
  }
}
case object Tolkien extends Author {
  val name = "Tolkien"
  override val novelData = Seq(
    "The Fellowship of the Ring",
    "The Two Towers",
    "The Return of the King"
  )
}
case object Martin extends Author {
  val name = "Martin"
  override val novelData = Seq(
    "A Game of Thrones",
    "A Clash of Kings",
    "A Storm of Swords",
    "A Feast for Crows",
    "A Dance with Dragons"
  )
}
