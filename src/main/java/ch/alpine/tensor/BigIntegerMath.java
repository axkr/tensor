// code by jph
package ch.alpine.tensor;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;

/** implementation is standalone */
/* package */ enum BigIntegerMath {
  ;
  /** @param value
   * @return exact root of value
   * @throws IllegalArgumentException if value is not a square number */
  public static Optional<BigInteger> sqrt(BigInteger value) {
    BigInteger root = sqrtApproximation(value);
    if (root.multiply(root).equals(value))
      return Optional.of(root);
    return Optional.empty();
  }

  /** @param value
   * @return approximation to sqrt of value, exact root if input value is square number */
  // https://gist.github.com/JochemKuijpers/cd1ad9ec23d6d90959c549de5892d6cb
  private static BigInteger sqrtApproximation(BigInteger value) {
    BigInteger a = BigInteger.ONE;
    BigInteger b = value.shiftRight(5).add(BigInteger.valueOf(8));
    while (0 <= b.compareTo(a)) {
      BigInteger mid = a.add(b).shiftRight(1);
      if (0 < mid.multiply(mid).compareTo(value))
        b = mid.subtract(BigInteger.ONE);
      else
        a = mid.add(BigInteger.ONE);
    }
    return a.subtract(BigInteger.ONE);
  }

  /** @param limit
   * @param random
   * @return random number between 0, 1, ..., limit - 1 */
  public static BigInteger random(BigInteger limit, Random random) {
    BigInteger bigInteger;
    do {
      bigInteger = new BigInteger(limit.bitLength(), random);
    } while (0 <= bigInteger.compareTo(limit));
    return bigInteger;
  }
}
