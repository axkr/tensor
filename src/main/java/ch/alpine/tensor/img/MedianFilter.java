// code by gjoel
package ch.alpine.tensor.img;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.red.Median;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/MedianFilter.html">MedianFilter</a> */
public enum MedianFilter {
  ;
  /** Example:
   * <pre>
   * Tensor vector = Tensors.vector(0, 0, 1, 0, 0, 0, 0, 3, 3, 3, 0);
   * MedianFilter.of(vector, 1) == {0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3/2}
   * MedianFilter.of(vector, 2) == {0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3}
   * </pre>
   * 
   * @param tensor of arbitrary rank but not a scalar
   * @param radius non-negative integer
   * @return filtered version of input tensor with same {@link Dimensions};
   * for radius == 0 the function returns a copy of the given tensor
   * @throws Exception if given tensor is a scalar
   * @throws Exception if given radius is negative */
  public static Tensor of(Tensor tensor, int radius) {
    return ImageFilter.of(Unprotect.references(tensor), radius, MedianFilter::flatten);
  }

  // helper function
  private static Tensor flatten(Tensor tensor) {
    return Median.of(Flatten.of(tensor));
  }
}
