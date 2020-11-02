// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.Quaternion;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.qty.Quantity;

/** Arg is consistent with Mathematica for {@link RealScalar}, {@link ComplexScalar},
 * {@link Quaternion}, and {@link Quantity}.
 *
 * <p>Arg gives the argument of a number in the complex plane.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Arg.html">Arg</a> */
public enum Arg implements ScalarUnaryOperator {
  FUNCTION;

  @Override
  public Scalar apply(Scalar scalar) {
    if (scalar instanceof ArgInterface) {
      ArgInterface argInterface = (ArgInterface) scalar;
      return argInterface.arg();
    }
    throw TensorRuntimeException.of(scalar);
  }

  /** @param tensor
   * @return tensor with all scalars replaced with their argument */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(FUNCTION);
  }
}
