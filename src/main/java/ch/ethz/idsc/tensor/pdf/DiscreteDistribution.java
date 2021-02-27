// code by jph
package ch.ethz.idsc.tensor.pdf;

import ch.ethz.idsc.tensor.Scalar;

/** functionality for a discrete probability distribution
 * 
 * BernoulliDistribution
 * BinomialDistribution
 * DiscreteUniformDistribution
 * EmpiricalDistribution
 * GeometricDistribution
 * HypergeometricDistribution
 * NegativeBinomialDistribution
 * PascalDistribution
 * PoissonBinomialDistribution
 * PoissonDistribution */
public interface DiscreteDistribution extends Distribution, PDF, RandomVariateInterface {
  /** @return lowest value a random variable from this distribution may attain */
  int lowerBound();

  /** @param n
   * @return P(X == n), i.e. probability of random variable X == n */
  Scalar p_equals(int n);
}
