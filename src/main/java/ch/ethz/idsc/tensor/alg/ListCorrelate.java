// code by jph
package ch.ethz.idsc.tensor.alg;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Unprotect;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/ListCorrelate.html">ListCorrelate</a> */
public enum ListCorrelate {
  ;
  /** <pre>
   * ListCorrelate[{x, y}, {a, b, c, d, e, f}] ==
   * {a x + b y, b x + c y, c x + d y, d x + e y, e x + f y}
   * </pre>
   * 
   * @param kernel
   * @param tensor
   * @return correlation of kernel with tensor
   * @throws Exception if dimensions of kernel and tensor are unsuitable for convolution,
   * for instance if tensor is a {@link Scalar} */
  public static Tensor of(Tensor kernel, Tensor tensor) {
    List<Integer> mask = Dimensions.of(kernel);
    List<Integer> size = Dimensions.of(tensor);
    Tensor refs = Unprotect.references(tensor);
    List<Integer> dimensions = IntStream.range(0, mask.size()) //
        .mapToObj(index -> size.get(index) - mask.get(index) + 1) //
        .collect(Collectors.toList());
    if (dimensions.stream().anyMatch(i -> i <= 0))
      throw TensorRuntimeException.of(kernel, tensor);
    return Array.of(index -> kernel.pmul(refs.block(index, mask)).flatten(-1) //
        .reduce(Tensor::add).get(), dimensions);
  }
}