// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.DecimalScalar;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;

/** consistent with Mathematica:
 * <pre>
 * Round[+11.5] == +12
 * Round[-11.5] == -12
 * </pre>
 * 
 * not consistent with java.lang.Math::round which rounds -11.5 to -11.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Round.html">Round</a> */
public enum Round implements ScalarUnaryOperator {
  function;
  // ---
  // display pi as "3.1416"
  public static final ScalarUnaryOperator MATLAB = Round.toMultipleOf(DecimalScalar.of(0.0001));

  // ---
  @Override
  public Scalar apply(Scalar scalar) {
    if (scalar instanceof RoundingInterface) {
      RoundingInterface roundingInterface = (RoundingInterface) scalar;
      return roundingInterface.round();
    }
    throw TensorRuntimeException.of(scalar);
  }

  /** for best results, the parameter increment should be a instance of
   * {@link DecimalScalar}, or {@link RationalScalar}
   * Examples:
   * DecimalScalar.of(0.1), or RationalScalar.of(1, 10)
   * 
   * <p>if instead increment is a {@link DoubleScalar} the return value
   * may suffer from numeric round off error in the style of "3.4000000000000004"
   * 
   * @param increment
   * @return */
  public static ScalarUnaryOperator toMultipleOf(Scalar increment) {
    Scalar inverse = increment.invert();
    return scalar -> function.apply(scalar.multiply(inverse)).multiply(increment);
  }

  /** rounds all entries of tensor to nearest integers, with
   * ties rounding to positive infinity.
   * 
   * @param tensor
   * @return tensor with all entries replaced by their rounded values */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(Round.function);
  }
}
