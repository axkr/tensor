// code by jph
package ch.ethz.idsc.tensor.nrm;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.SingularValueDecomposition;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.red.Min;
import ch.ethz.idsc.tensor.sca.Sqrt;

public enum MatrixNorm2 {
  ;
  /** uses SVD for matrices
   * 
   * @param matrix
   * @return 2-norm of given matrix */
  public static Scalar of(Tensor matrix) {
    Tensor normal = matrix.length() < Unprotect.dimension1(matrix) //
        ? Transpose.of(matrix)
        : matrix;
    return SingularValueDecomposition.of(normal).values().stream() // values are non-negative
        .map(Scalar.class::cast) //
        .reduce(Max::of).get();
  }

  /** References:
   * "Matrix Computations", 4th Edition
   * Section 2.3.2 Some Matrix Norm Properties
   * 
   * Wikipedia:
   * https://en.wikipedia.org/wiki/Matrix_norm
   * 
   * @param matrix
   * @return upper bound to 2-norm of given matrix up to numerical precision */
  public static Scalar bound(Tensor matrix) {
    Scalar n1 = MatrixNorm1.of(matrix);
    Scalar ni = MatrixNormInfinity.of(matrix);
    return Min.of( //
        Sqrt.FUNCTION.apply(n1.multiply(ni)), // Hoelder's inequality
        FrobeniusNorm.of(matrix));
  }
}
