// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.Tensor;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/Inverse.html">Inverse</a>
 * 
 * @see PseudoInverse */
public enum Inverse {
  ;
  /** @param matrix with square dimensions
   * @return inverse of given matrix
   * @throws Exception if given matrix is not invertible */
  public static Tensor of(Tensor matrix) {
    return LinearSolve.of(matrix, IdentityMatrix.of(matrix.length()));
  }

  /** function doesn't invoke Scalar::abs but pivots at the first non-zero column entry
   * 
   * @param matrix with square dimensions
   * @return inverse of given matrix
   * @throws Exception if given matrix is not invertible */
  public static Tensor of(Tensor matrix, Pivot pivot) {
    return LinearSolve.of(matrix, IdentityMatrix.of(matrix.length()), pivot);
  }
}
