// code by jph
package ch.ethz.idsc.tensor.lie;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** QRDecomposition is not consistent with Mathematica.
 * 
 * <p>The tensor library factors a matrix m
 * {q, r} = Tensor::QRDecomposition[m] such that q.r == m.
 * whereas in Mathematica:
 * {q, r} = Mathematica::QRDecomposition[m] and then ConjugateTranspose[q].r == m.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/QRDecomposition.html">QRDecomposition</a> */
public interface QRDecomposition {
  /** householder reflections with highest numerical stability
   * 
   * @param matrix
   * @return qr-decomposition of given matrix */
  static QRDecomposition of(Tensor matrix) {
    return new QRDecompositionImpl(matrix, QRSignOperators.STABILITY);
  }

  /** @param matrix
   * @return qr-decomposition of matrix
   * @throws Exception for input that is not "almost"-orthogonal */
  static QRDecomposition of(Tensor matrix, QRSignOperator qrSignOperator) {
    return new QRDecompositionImpl(matrix, qrSignOperator);
  }

  /** @return orthogonal matrix */
  Tensor getInverseQ();

  /** @return upper triangular matrix */
  Tensor getR();

  /** @return orthogonal matrix */
  Tensor getQ();

  /** @return determinant of matrix */
  Scalar det();
}
