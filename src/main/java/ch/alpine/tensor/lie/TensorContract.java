// code by jph
package ch.alpine.tensor.lie;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.Trace;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/TensorContract.html">TensorContract</a> */
public enum TensorContract {
  ;
  /** @param tensor
   * @param d0
   * @param d1
   * @return contraction of tensor along dimensions d0 and d1 */
  public static Tensor of(Tensor tensor, int d0, int d1) {
    return Trace.of(tensor, d0, d1);
  }
}
