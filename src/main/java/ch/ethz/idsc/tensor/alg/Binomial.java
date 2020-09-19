// code by jph
package ch.ethz.idsc.tensor.alg;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import ch.ethz.idsc.tensor.IntegerQ;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Gamma;

/** binomial coefficient implemented for integer input
 * <pre>
 * Gamma[n+1] / ( Gamma[m+1] Gamma[n-m+1] )
 * </pre>
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Binomial.html">Binomial</a> */
public class Binomial implements Serializable {
  /** @param n non-negative integer
   * @return binomial function that computes n choose k */
  public static Binomial of(Scalar n) {
    return of(Scalars.intValueExact(n));
  }

  /** @param n non-negative integer
   * @return binomial function that computes n choose k */
  public static Binomial of(int n) {
    return BinomialMemo.INSTANCE.lookup(Integers.requirePositiveOrZero(n));
  }

  /** <code>Mathematica::Binomial[n, m]</code>
   * 
   * @param n
   * @param m, and m <= n
   * @return binomial coefficient defined by n and m */
  public static Scalar of(Scalar n, Scalar m) {
    if (IntegerQ.of(n) && IntegerQ.of(m))
      return of(Scalars.intValueExact(n), Scalars.intValueExact(m));
    Scalar np1 = n.add(RealScalar.ONE);
    return Gamma.FUNCTION.apply(np1).divide( //
        Gamma.FUNCTION.apply(m.add(RealScalar.ONE)).multiply(Gamma.FUNCTION.apply(np1.subtract(m))));
  }

  /** <code>Mathematica::Binomial[n, m]</code>
   * 
   * @param n
   * @param m <= n
   * @return binomial coefficient defined by n and m */
  public static Scalar of(int n, int m) {
    if (n < m) {
      if (0 <= n)
        return RealScalar.ZERO;
      // LONGTERM this case is defined in Mathematica
      throw new IllegalArgumentException(String.format("Binomial[%d,%d]", n, m));
    }
    return BinomialMemo.INSTANCE.lookup(n).over(m);
  }

  /***************************************************/
  private static enum BinomialMemo {
    INSTANCE;

    private static final int MAX_SIZE = 384;
    private final Map<Integer, Binomial> map = //
        new LinkedHashMap<Integer, Binomial>(MAX_SIZE * 4 / 3, 0.75f, true) {
          @Override
          protected boolean removeEldestEntry(Map.Entry<Integer, Binomial> eldest) {
            return MAX_SIZE < size();
          }
        };

    public synchronized Binomial lookup(int n) {
      Binomial binomial = map.get(n);
      if (Objects.isNull(binomial))
        map.put(n, binomial = new Binomial(n));
      return binomial;
    }
  }

  /***************************************************/
  private final int n;
  private final Tensor row;

  private Binomial(int n) {
    this.n = n;
    int half = n / 2;
    Scalar x = RealScalar.ONE;
    row = Tensors.reserve(half + 1).append(x);
    for (int k = 1; k <= half; ++k)
      row.append(x = x.multiply(RationalScalar.of(n - k + 1, k)));
  }

  /** @param k
   * @return n choose k */
  public Scalar over(int k) {
    return 0 <= k //
        ? row.Get(Math.min(k, n - k))
        : RealScalar.ZERO;
  }
}
