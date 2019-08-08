// code by gjoel
package ch.ethz.idsc.tensor.img;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.red.Mean;

/** the implementation is consistent with Mathematica.
 * 
 * <p>For images apply the mean filter to each of the RGB channels separately.
 * 
 * <p>{@link MeanFilter} requires less operations for computation than {@link MedianFilter}.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/MeanFilter.html">MeanFilter</a> */
public enum MeanFilter {
  ;
  /** Example:
   * <pre>
   * MeanFilter.of({-3, 3, 6, 0, 0, 3, -3, -9}, 2) == {0, 2, 3, 2, 1, 0, -3, -6}
   * </pre>
   * 
   * @param tensor of arbitrary rank but not a scalar
   * @param radius non-negative integer
   * @return filtered version of input tensor with same {@link Dimensions};
   * for radius == 0 the function returns a copy of the given tensor
   * @throws Exception if given tensor is a scalar
   * @throws Exception if given radius is negative */
  public static Tensor of(Tensor tensor, int radius) {
    return TensorExtract.of(tensor, radius, Mean::of);
  }
}
