package classifiers

/**
  * Created by jessechen on 4/23/17.
  */
trait Label {
  def toInt: Int
}

object BinaryLabel {
  case object LabelA extends Label {
    def toInt = 1
  }
  case object LabelB extends Label {
    def toInt = -1
  }

  def toInt(label: Label) = label match {
    case LabelA => LabelA.toInt
    case LabelB => LabelB.toInt
    case _ => throw new Exception
  }

  def toLabel(int: Int) = int match {
    case 1 => LabelA
    case -1 => LabelB
    case _ => throw new Exception
  }
}
