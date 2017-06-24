// code by jph
package ch.ethz.idsc.tensor.pdf;

import java.util.Random;

import ch.ethz.idsc.tensor.IntegerQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;

/** functionality and suggested base class for a discrete probability distribution */
public abstract class AbstractDiscreteDistribution implements DiscreteDistribution, //
    MeanInterface, PDF, RandomVariateInterface {
  @Override // from RandomVariateInterface
  public final Scalar randomVariate(Random random) {
    return randomVariate(RealScalar.of(random.nextDouble()));
  }

  /** @param reference in the half-open interval [0, 1)
   * @return */
  public abstract Scalar randomVariate(Scalar reference);

  @Override // from PDF
  public final Scalar at(Scalar x) {
    if (!IntegerQ.of(x))
      return RealScalar.ZERO;
    int n = Scalars.intValueExact(x);
    return p_equals(n);
  }

  @Override // from DiscreteDistribution
  public final Scalar p_equals(int n) {
    if (n < lowerBound())
      return RealScalar.ZERO;
    return protected_p_equals(n);
  }

  /** @param n with n >= lowerBound()
   * @return P(X == n), i.e. probability of random variable X == n */
  protected abstract Scalar protected_p_equals(int n);
}
