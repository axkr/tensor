// code by jph
package ch.alpine.tensor.pdf;

import ch.alpine.tensor.Scalar;

/** The triangular distribution is a special case of a {@link TrapezoidalDistribution}
 * 
 * <p>inspired by
 * <a href="https://en.wikipedia.org/wiki/Triangular_distribution">Triangular_distribution</a> */
public enum TriangularDistribution {
  ;
  /** @param a
   * @param b
   * @param c
   * @return
   * @throws Exception unless a <= b <= c and a < c */
  public static Distribution of(Scalar a, Scalar b, Scalar c) {
    return TrapezoidalDistribution.of(a, b, b, c);
  }
}
