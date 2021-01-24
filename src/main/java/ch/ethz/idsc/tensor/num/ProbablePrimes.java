// code by jph
package ch.ethz.idsc.tensor.num;

import java.math.BigInteger;
import java.util.function.Function;

import ch.ethz.idsc.tensor.ext.Cache;

/* package */ enum ProbablePrimes {
  ;
  /** Quote from BigInteger:
   * "certainty a measure of the uncertainty that the caller is
   * willing to tolerate: if the call returns {@code true}
   * the probability that this BigInteger is prime exceeds
   * (1 - 1/2<sup>{@code certainty}</sup>). The execution time of
   * this method is proportional to the value of this parameter." */
  private static final int CERTAINTY = 20;
  private static final int MAX_SIZE = 768;
  private static final Function<BigInteger, BigInteger> CACHE = Cache.of(ProbablePrimes::require, MAX_SIZE);

  public static BigInteger of(BigInteger bigInteger) {
    return CACHE.apply(bigInteger);
  }

  /** @param bigInteger
   * @return */
  public static boolean isMember(BigInteger bigInteger) {
    return bigInteger.isProbablePrime(CERTAINTY);
  }

  /** @param bigInteger
   * @return bigInteger
   * @throws Exception if given bigInteger is not a prime */
  private static BigInteger require(BigInteger bigInteger) {
    if (isMember(bigInteger))
      return bigInteger;
    throw new IllegalArgumentException("not prime: " + bigInteger);
  }
}
