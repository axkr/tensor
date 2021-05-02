// code by jph
package ch.alpine.tensor.img;

import java.util.stream.IntStream;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.itp.MappedInterpolation;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/ImageResize.html">ImageResize</a>
 * 
 * @see MappedInterpolation */
public enum ImageResize {
  ;
  /** function uses nearest neighbor interpolation
   * 
   * @param tensor
   * @param factor positive integer
   * @return */
  public static Tensor nearest(Tensor tensor, int factor) {
    return nearest(tensor, factor, factor);
  }

  /** function uses nearest neighbor interpolation
   * 
   * @param tensor
   * @param fx positive scaling along x axis
   * @param fy positive scaling along y axis
   * @return
   * @throws Exception if either fx or fy is zero or negative */
  public static Tensor nearest(Tensor tensor, int fx, int fy) {
    int dim0 = tensor.length();
    int dim1 = Unprotect.dimension1(tensor);
    // precomputation of indices
    int[] ix = IntStream.range(0, dim0 * fx).map(i -> i / fx).toArray();
    int[] iy = IntStream.range(0, dim1 * fy).map(i -> i / fy).toArray();
    return Tensors.matrix((i, j) -> tensor.get(ix[i], iy[j]), //
        dim0 * Integers.requirePositive(fx), //
        dim1 * Integers.requirePositive(fy));
  }
}
