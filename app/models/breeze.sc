import breeze.linalg.{*, DenseMatrix, DenseVector}
import breeze.numerics.log

val newW = DenseVector(5, 3, 2)
val oldW = DenseVector(4, 2, 1)
newW - oldW