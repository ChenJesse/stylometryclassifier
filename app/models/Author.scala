package models

/**
  * Created by jessechen on 4/28/17.
  */
trait Author {
  val name: String
}
case object Tolkien extends Author {
  val name = "Tolkien"
}
case object Martin extends Author {
  val name = "Martin"
}
