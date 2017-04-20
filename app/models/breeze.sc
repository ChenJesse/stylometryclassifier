import breeze.linalg.{*, DenseMatrix, DenseVector, Transpose}
import breeze.numerics.log

val newW = DenseVector(5, 3, 2)
val oldW = DenseVector(4, 2, 1)

val x = List(List(1, 2, 3), List(1, 2, 3), List(1, 2, 3), List(1, 2, 3))
val y = List(List(4, 5, 36), List(4, 5, 36), List(4, 5, 36), List(4, 5, 36))
val z = x.zip(y)
val (a, b) = z.unzip

x.toArray.flatten

var mat = new DenseMatrix(3, 4, x.toArray.flatten)
mat = mat.t

val array = Array.fill[Double](20)(0)
array(0) = 5
array