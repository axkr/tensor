// code by jph
package ch.ethz.idsc.tensor.pdf;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.red.Variance;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class PascalDistributionTest extends TestCase {
  public void testPDF() {
    Scalar p = RationalScalar.of(2, 3);
    PascalDistribution distribution = (PascalDistribution) PascalDistribution.of(5, p);
    PDF pdf = PDF.of(distribution);
    Scalar scalar = pdf.at(RealScalar.of(5));
    assertEquals(scalar, Power.of(p, 5));
    assertTrue(Scalars.lessEquals(RealScalar.of(43), distribution.inverse_cdf().lastEntry().getValue()));
  }

  public void testCDF() {
    Scalar p = RationalScalar.of(2, 3);
    Distribution distribution = PascalDistribution.of(5, p);
    CDF pdf = CDF.of(distribution);
    Scalar probability = pdf.p_lessEquals(RealScalar.of(14));
    assertEquals(probability, RationalScalar.of(4763648, 4782969));
    ExactScalarQ.require(probability);
  }

  public void testMean() {
    Distribution distribution = PascalDistribution.of(5, RationalScalar.of(2, 3));
    Scalar mean = Mean.of(distribution);
    Scalar var = Variance.of(distribution);
    assertEquals(mean, RationalScalar.of(15, 2));
    assertEquals(var, RationalScalar.of(15, 4));
    ExactScalarQ.require(mean);
    ExactScalarQ.require(var);
  }

  public void testVariance() {
    PascalDistribution distribution = (PascalDistribution) PascalDistribution.of(11, RationalScalar.of(5, 17));
    Scalar mean = Mean.of(distribution);
    Scalar var = Variance.of(distribution);
    assertEquals(mean, RationalScalar.of(187, 5));
    assertEquals(var, RationalScalar.of(2244, 25));
    ExactScalarQ.require(mean);
    ExactScalarQ.require(var);
    assertTrue(Scalars.lessEquals(RealScalar.of(172), distribution.inverse_cdf().lastEntry().getValue()));
  }

  public void testRandomVariate() {
    Scalar p = RationalScalar.of(3, 4);
    Distribution distribution = PascalDistribution.of(5, p);
    Tensor tensor = RandomVariate.of(distribution, 2300);
    Tensor mean = Mean.of(tensor);
    Scalar diff = Mean.of(distribution).subtract(mean);
    assertTrue(Scalars.lessThan(Abs.of(diff), RealScalar.of(0.2)));
    ExactScalarQ.require(diff);
  }

  public void testInverseCdf() {
    Scalar p = RationalScalar.of(1, 5);
    PascalDistribution distribution = (PascalDistribution) PascalDistribution.of(5, p);
    InverseCDF inverseCDF = InverseCDF.of(distribution);
    Scalar quantile = inverseCDF.quantile(RealScalar.of(0.999));
    assertTrue(Scalars.lessThan(quantile, distribution.inverse_cdf().lastEntry().getValue()));
    assertTrue(Scalars.isZero(distribution.p_equals(3)));
    assertTrue(Scalars.isZero(distribution.p_equals(4)));
    assertTrue(Scalars.nonZero(distribution.p_equals(5)));
  }

  public void testFailN() {
    AssertFail.of(() -> PascalDistribution.of(0, RealScalar.of(0.2)));
    AssertFail.of(() -> PascalDistribution.of(-3, RealScalar.of(0.2)));
  }

  public void testFailP() {
    AssertFail.of(() -> PascalDistribution.of(3, RealScalar.of(-0.2)));
    AssertFail.of(() -> PascalDistribution.of(3, RealScalar.of(1.2)));
  }
}
