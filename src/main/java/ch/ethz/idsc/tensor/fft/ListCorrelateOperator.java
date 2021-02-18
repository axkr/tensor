// code by jph
package ch.ethz.idsc.tensor.fft;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Integers;

/* package */ class ListCorrelateOperator implements TensorUnaryOperator {
  private static final long serialVersionUID = 5649389581423571244L;
  // ---
  private final Tensor kernel;
  private final List<Integer> mask;

  public ListCorrelateOperator(Tensor kernel) {
    this.kernel = kernel;
    mask = Dimensions.of(kernel);
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor tensor) {
    List<Integer> size = Dimensions.of(tensor);
    if (mask.size() != size.size())
      throw TensorRuntimeException.of(kernel, tensor);
    List<Integer> dimensions = IntStream.range(0, mask.size()) //
        .map(index -> size.get(index) - mask.get(index) + 1) //
        .mapToObj(Integers::requirePositive) //
        .collect(Collectors.toList());
    Tensor refs = Unprotect.references(tensor);
    return Array.of(index -> kernel.pmul(refs.block(index, mask)).flatten(-1) //
        .reduce(Tensor::add).get(), dimensions);
  }
}