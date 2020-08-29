// code by jph
package ch.ethz.idsc.tensor.num;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Abs;

/* package */ enum StaticHelper {
  ;
  /** Quote from BigInteger:
   * "certainty a measure of the uncertainty that the caller is
   * willing to tolerate: if the call returns {@code true}
   * the probability that this BigInteger is prime exceeds
   * (1 - 1/2<sup>{@code certainty}</sup>). The execution time of
   * this method is proportional to the value of this parameter." */
  private static final int CERTAINTY = 20;
  private static final int MEMO_SIZE = 512;
  private static final Map<BigInteger, BigInteger> MEMO = //
      new LinkedHashMap<BigInteger, BigInteger>(MEMO_SIZE * 4 / 3, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<BigInteger, BigInteger> eldest) {
          return MEMO_SIZE < size();
        }
      };

  /** @param bigInteger
   * @return bigInteger
   * @throws Exception if given bigInteger is not a prime */
  public static BigInteger assertIsProbablePrime(BigInteger bigInteger) {
    if (!MEMO.containsKey(bigInteger)) {
      if (!bigInteger.isProbablePrime(CERTAINTY))
        throw new IllegalArgumentException("not prime: " + bigInteger);
      MEMO.put(bigInteger, bigInteger);
    }
    return bigInteger;
  }

  /***************************************************/
  // LONGTERM function does not result in Mathematica standard for all input
  public static Scalar normalForm(Scalar scalar) {
    if (scalar instanceof RealScalar)
      return Abs.FUNCTION.apply(scalar);
    return scalar;
  }
}
