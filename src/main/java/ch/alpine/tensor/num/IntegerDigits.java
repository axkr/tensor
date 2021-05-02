// code by jph
package ch.alpine.tensor.num;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;

import ch.alpine.tensor.IntegerQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** IntegerDigits is consistent with Mathematica::IntegerDigits except for input zero:
 * <pre>
 * Tensor-Lib.::IntegerDigits[0] == {}
 * Mathematica::IntegerDigits[0] == {0}
 * </pre>
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/IntegerDigits.html">IntegerDigits</a>
 * 
 * @see IntegerQ */
public enum IntegerDigits {
  ;
  private static final ScalarTensorFunction BASE_10 = base(BigInteger.TEN);

  /** Example:
   * <pre>
   * IntegerDigits[752] == {7, 5, 2}
   * </pre>
   * 
   * @param scalar
   * @return vector with integer digits of given scalar
   * @throws Exception if given scalar is not an integer */
  public static Tensor of(Scalar scalar) {
    return BASE_10.apply(scalar);
  }

  /** Example:
   * <pre>
   * IntegerDigits.base(2).apply(11) == {1, 0, 1, 1}
   * </pre>
   * 
   * @param base
   * @return
   * @throws Exception if base is less than +2 */
  public static ScalarTensorFunction base(long base) {
    if (base < 2)
      throw new IllegalArgumentException(Long.toString(base));
    return base(BigInteger.valueOf(base));
  }

  private static ScalarTensorFunction base(BigInteger base) {
    return scalar -> Tensor.of(of(Scalars.bigIntegerValueExact(scalar), base).stream().map(RealScalar::of));
  }

  private static Deque<BigInteger> of(BigInteger bigInteger, BigInteger base) {
    bigInteger = bigInteger.abs();
    Deque<BigInteger> deque = new ArrayDeque<>();
    while (!bigInteger.equals(BigInteger.ZERO)) {
      BigInteger[] bigIntegers = bigInteger.divideAndRemainder(base);
      bigInteger = bigIntegers[0];
      deque.push(bigIntegers[1]);
    }
    return deque;
  }
}
