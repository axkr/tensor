// code by gjoel
package ch.alpine.tensor.img;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.red.Mean;

/** the implementation is consistent with Mathematica.
 * 
 * <p>{@link MeanFilter} requires fewer operations for computation than {@link MedianFilter}.
 * 
 * <p>The concept of averaging values from a square block is also using in {@link ImageResize}.
 * However, for images the mean filter is applied to each of the RGBA channels separately.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/MeanFilter.html">MeanFilter</a> */
public enum MeanFilter {
  ;
  /** Example:
   * <pre>
   * MeanFilter[{-3, 3, 6, 0, 0, 3, -3, -9}, 1] == {0, 2, 3, 2, 1, 0, -3, -6}
   * </pre>
   * 
   * @param tensor of array structure with rank at least 1
   * @param radius non-negative integer
   * @return filtered version of input tensor with same {@link Dimensions};
   * for radius == 0 the function returns a copy of the given tensor
   * @throws Exception if given tensor is a scalar, or not of array form
   * @throws Exception if given radius is negative */
  public static Tensor of(Tensor tensor, int radius) {
    return TensorExtract.of(tensor, radius, Mean::of);
  }
}
