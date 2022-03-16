// code by jph
package ch.alpine.tensor.sca;

import java.math.BigDecimal;

import ch.alpine.tensor.DecimalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.api.RoundingInterface;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.qty.Quantity;

/** Examples:
 * <pre>
 * Floor[+3.9] == +3
 * Floor[-8.2] == -9
 * </pre>
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Floor.html">Floor</a>
 * 
 * @see RoundingInterface
 * @see Ceiling
 * @see Round */
public enum Floor implements ScalarUnaryOperator {
  FUNCTION;

  public static final ScalarUnaryOperator _1 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.1")));
  public static final ScalarUnaryOperator _2 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.01")));
  public static final ScalarUnaryOperator _3 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.001")));
  public static final ScalarUnaryOperator _4 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.0001")));
  public static final ScalarUnaryOperator _5 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.00001")));
  public static final ScalarUnaryOperator _6 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.000001")));
  public static final ScalarUnaryOperator _7 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.0000001")));
  public static final ScalarUnaryOperator _8 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.00000001")));
  public static final ScalarUnaryOperator _9 = Floor.toMultipleOf(DecimalScalar.of(new BigDecimal("0.000000001")));

  /** @param scalar instance if {@link RealScalar}
   * @return best integer scalar approximation to floor of scalar
   * @throws TensorRuntimeException if scalar is Infinity, or NaN */
  @Override
  public Scalar apply(Scalar scalar) {
    if (scalar instanceof RoundingInterface roundingInterface)
      return roundingInterface.floor();
    throw TensorRuntimeException.of(scalar);
  }

  /** Examples:
   * <pre>
   * Floor[+4.0] == +4
   * Floor[+4.2] == +4
   * Floor[+4.9] == +4
   * Floor[-7.2] == -8
   * </pre>
   * 
   * @param scalar
   * @return
   * @throws Exception if output is outside encodable range
   * {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE}
   * @throws Exception if input scalar is instance of {@link Quantity} */
  public static int intValueExact(Scalar scalar) {
    return Scalars.intValueExact(FUNCTION.apply(scalar));
  }

  /** see documentation {@link #intValueExact(Scalar)}
   * 
   * @param scalar
   * @return */
  public static long longValueExact(Scalar scalar) {
    return Scalars.longValueExact(FUNCTION.apply(scalar));
  }

  /** @param tensor
   * @return tensor with all entries replaced by their floor */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(FUNCTION);
  }

  /** @param increment non-zero
   * @return */
  public static ScalarUnaryOperator toMultipleOf(Scalar increment) {
    return scalar -> FUNCTION.apply(scalar.divide(increment)).multiply(increment);
  }
}
