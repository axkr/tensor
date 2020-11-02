// code by jph
package ch.ethz.idsc.tensor.usr;

import java.util.Objects;
import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/FoldList.html">FoldList</a> */
/* package */ enum FoldListTry {
  ;
  private static class FoldDigest implements TensorUnaryOperator {
    private static final long serialVersionUID = 6038751468674830689L;
    private final BinaryOperator<Tensor> binaryOperator;
    private Tensor next;

    public FoldDigest(BinaryOperator<Tensor> binaryOperator) {
      this.binaryOperator = binaryOperator;
    }

    @Override
    public Tensor apply(Tensor tensor) {
      return next = Objects.isNull(next) //
          ? tensor.copy()
          : binaryOperator.apply(next, tensor);
    }
  }

  /** <pre>
   * FoldList[f, {a, b, c, ...}] gives {a, f[a, b], f[f[a, b], c], ...}
   * </pre>
   * 
   * @param binaryOperator
   * @param tensor must not be a {@link Scalar}
   * @return see description above */
  public static Tensor of(BinaryOperator<Tensor> binaryOperator, Tensor tensor) {
    return Tensor.of(tensor.stream().map(new FoldDigest(binaryOperator)));
  }
}
