package models
import breeze.linalg.DenseVector

/**
  * Created by jessechen on 4/23/17.
  */
class Name(name: String) extends Vectorizable {
  val dimension = 2000

  def vectorize(): DenseVector[Double] = {
      val vectorArray = Array.fill[Double](dimension)(0)
      for (i <- 0 to 7) {
        var featureString = "prefix" + name.slice(0, i)
        vectorArray(Math.abs(featureString.hashCode) % dimension) = 1.0
        featureString = "suffix" + name.slice(name.length - i, name.length)
        vectorArray(Math.abs(featureString.hashCode) % dimension) = 1.0
      }
      DenseVector(vectorArray)
  }
}
