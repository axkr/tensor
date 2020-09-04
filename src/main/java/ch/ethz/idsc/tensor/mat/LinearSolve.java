// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.Partition;
import ch.ethz.idsc.tensor.alg.TensorRank;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/LinearSolve.html">LinearSolve</a> */
public enum LinearSolve {
  ;
  /** gives solution to linear system of equations.
   * scalar entries are required to implement
   * Comparable<Scalar> for pivoting.
   * 
   * @param matrix square of {@link Scalar}s that implement absolute value abs()
   * @param b tensor with first dimension identical to size of matrix
   * @return x with matrix.dot(x) == b
   * @throws TensorRuntimeException if matrix m is singular */
  public static Tensor of(Tensor matrix, Tensor b) {
    return GaussianElimination.of(matrix, b, Pivots.ARGMAX_ABS);
  }

  /** method only checks for non-zero
   * and doesn't use Scalar::abs().
   * 
   * @param matrix square
   * @param b tensor with first dimension identical to size of matrix
   * @param pivot
   * @return x with matrix.dot(x) == b
   * @throws TensorRuntimeException if given matrix is singular */
  public static Tensor of(Tensor matrix, Tensor b, Pivot pivot) {
    return GaussianElimination.of(matrix, b, pivot);
  }

  /** function for matrix not necessarily invertible, or square
   * 
   * Example:
   * <pre>
   * Tensor matrix = Tensors.fromString("{{1}, {1}, {-1}}");
   * Tensor b = Tensors.vector(2, 2, -2);
   * LinearSolve.any(matrix, b) == {2}
   * </pre>
   * 
   * @param matrix with exact precision scalars
   * @param b vector
   * @return x with matrix.x == b
   * @throws TensorRuntimeException if such an x does not exist */
  public static Tensor any(Tensor matrix, Tensor b) {
    if (!ExactTensorQ.of(matrix)) // LONGTERM explore options for machine scalars
      throw TensorRuntimeException.of(matrix, b);
    switch (TensorRank.of(b)) {
    case 1:
      return vector(matrix, b);
    default:
      break;
    }
    throw TensorRuntimeException.of(matrix, b);
  }

  // helper function
  private static Tensor vector(Tensor matrix, Tensor b) {
    int cols = Unprotect.dimension1(matrix);
    Tensor r = RowReduce.of(Join.of(1, matrix, Partition.of(b, 1)));
    Tensor x = Array.zeros(cols);
    int j = 0;
    int c0 = 0;
    while (c0 < cols) {
      if (Scalars.nonZero(r.Get(j, c0))) { // use chop for numeric input?
        x.set(r.Get(j, cols), c0);
        ++j;
      }
      ++c0;
    }
    for (; j < matrix.length(); ++j)
      if (Scalars.nonZero(r.Get(j, cols)))
        throw TensorRuntimeException.of(matrix, b);
    return x;
  }
}
