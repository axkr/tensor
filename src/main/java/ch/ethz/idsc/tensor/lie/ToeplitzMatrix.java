// code by jph
package ch.ethz.idsc.tensor.lie;

import ch.ethz.idsc.tensor.ScalarQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;

/** Quote from Wikipedia:
 * a Toeplitz matrix or diagonal-constant matrix, named after Otto Toeplitz,
 * is a matrix in which each descending diagonal from left to right is constant
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/ToeplitzMatrix.html">ToeplitzMatrix</a> */
public enum ToeplitzMatrix {
  ;
  /** For example:
   * ToeplitzMatrix.of(Tensors.vector(1, 2, 3, 4, 5)) gives the 3x3 matrix
   * <pre>
   * [ 3 4 5 ]
   * [ 2 3 4 ]
   * [ 1 2 3 ]
   * </pre>
   * 
   * @param vector with odd number of entries
   * @return
   * @throws Exception if given vector has even length, or is not a vector */
  public static Tensor of(Tensor vector) {
    ScalarQ.thenThrow(vector);
    if (vector.length() % 2 == 0)
      throw TensorRuntimeException.of(vector);
    int n = (vector.length() + 1) / 2;
    int semi = n - 1;
    return Tensors.matrix((i, j) -> vector.Get(semi - i + j), n, n);
  }
}
