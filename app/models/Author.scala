package models

/**
  * Created by jessechen on 4/28/17.
  */
trait Author {
  val name: String
  val novelData: Seq[(String, Int)]
  val novels = novelData.map { case (title: String, chapters: Int) => new Novel(title, chapters, Tolkien) }
}
case object Tolkien extends Author {
  val name = "Tolkien"
  val novelData = Seq(
    ("The Fellowship of the Ring", 22),
    ("The Two Towers", 21),
    ("The Return of the King", 19)
  )
}
case object Martin extends Author {
  val name = "Martin"
  val novelData = Seq(
    ("A Game of Thrones", 73),
    ("A Clash of Kings", 70),
    ("A Storm of Swords", 82),
    ("A Feast for Crows", 46),
    ("A Dance with Dragons", 73)
  )
}
