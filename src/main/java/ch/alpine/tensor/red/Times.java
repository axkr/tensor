// code by jph
package ch.alpine.tensor.red;

import java.util.stream.Stream;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.alg.Dimensions;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/Times.html">Times</a> */
public enum Times {
  ;
  /** function computes the product of a sequence of {@link Scalar}s
   * 
   * @param scalars
   * @return product of scalars, or {@link RealScalar#ONE} if no scalars are present */
  public static Scalar of(Scalar... scalars) {
    return Stream.of(scalars).reduce(Scalar::multiply).orElse(RealScalar.ONE);
  }

  /** The return value has {@link Dimensions} of input tensor reduced by 1.
   * 
   * <p>For instance
   * <pre>
   * pmul({ 3, 4, 2 }) == 3 * 4 * 2 == 24
   * pmul({ { 1, 2, 3 }, { 4, 5, 6 } }) == { 4, 10, 18 }
   * </pre>
   * 
   * <p>For an empty list, the result is RealScalar.ONE. This is consistent with
   * Mathematica::Times @@ {} == 1
   * 
   * <p>implementation is consistent with MATLAB::prod
   * pmul([]) == 1
   * 
   * @param tensor
   * @return total pointwise product of tensor entries at first level, or 1 if tensor is empty
   * @throws TensorRuntimeException if input tensor is a scalar */
  public static Tensor pmul(Tensor tensor) {
    return tensor.stream().reduce(Tensor::pmul).orElse(RealScalar.ONE);
  }
}
