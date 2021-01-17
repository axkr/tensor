// code by jph
package ch.ethz.idsc.tensor.pdf;

import java.util.NavigableMap;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class PoissonDistributionTest extends TestCase {
  static Tensor values(PDF pdf, int length) {
    return Tensors.vector(i -> pdf.at(RealScalar.of(i)), length);
  }

  public void testSingle() {
    Distribution distribution = PoissonDistribution.of(RealScalar.of(2));
    PDF pdf = PDF.of(distribution);
    assertTrue(pdf.at(RealScalar.ZERO).toString().startsWith("0.13533"));
    assertTrue(pdf.at(RealScalar.ONE).toString().startsWith("0.27067"));
    assertTrue(pdf.at(RealScalar.of(2)).toString().startsWith("0.27067"));
    assertTrue(pdf.at(RealScalar.of(3)).toString().startsWith("0.18044"));
  }

  public void testConvergence() {
    Distribution distribution = PoissonDistribution.of(RealScalar.of(2));
    PDF pdf = PDF.of(distribution);
    Tensor prob = values(pdf, 16);
    Scalar scalar = Total.ofVector(prob);
    assertTrue(Scalars.lessThan(RealScalar.of(0.9999), scalar));
    assertTrue(Scalars.lessThan(scalar, RealScalar.ONE));
  }

  public void testValues() {
    Distribution distribution = PoissonDistribution.of(RealScalar.of(3));
    PDF pdf = PDF.of(distribution);
    pdf.at(RealScalar.of(30));
    Tensor prob = values(pdf, 30);
    Scalar sum = Total.ofVector(prob);
    assertEquals(sum, RealScalar.ONE);
  }

  public void testPDF() {
    Distribution distribution = PoissonDistribution.of(RealScalar.of(10.5));
    CDF cdf = CDF.of(distribution);
    Scalar scalar = cdf.p_lessThan(RealScalar.of(50));
    assertEquals(Chop._12.of(scalar.subtract(RealScalar.ONE)), RealScalar.ZERO);
  }

  public void testPDF2() {
    Distribution distribution = PoissonDistribution.of(RealScalar.of(1.5));
    CDF cdf = CDF.of(distribution);
    Scalar scalar = cdf.p_lessThan(RealScalar.of(50));
    assertEquals(Chop._12.of(scalar.subtract(RealScalar.ONE)), RealScalar.ZERO);
  }

  public void testInverseCDF() {
    InverseCDF inverseCDF = InverseCDF.of(PoissonDistribution.of(RealScalar.of(5.5)));
    Scalar x0 = inverseCDF.quantile(RealScalar.of(0.0));
    Scalar x1 = inverseCDF.quantile(RealScalar.of(0.1));
    Scalar x2 = inverseCDF.quantile(RealScalar.of(0.5));
    assertEquals(x0, RealScalar.ZERO);
    assertTrue(Scalars.lessThan(x1, x2));
  }

  public void testInverseCDFOne() {
    Distribution distribution = PoissonDistribution.of(RealScalar.of(5.5));
    EvaluatedDiscreteDistribution edd = (EvaluatedDiscreteDistribution) distribution;
    NavigableMap<Scalar, Scalar> navigableMap = edd.inverse_cdf();
    assertTrue(34 < navigableMap.size());
    assertTrue(navigableMap.size() < 38);
    InverseCDF inverseCDF = InverseCDF.of(distribution);
    assertTrue(Clips.interval(24, 26).isInside(inverseCDF.quantile(RealScalar.of(0.9999999989237532))));
    assertTrue(Clips.interval(32, 34).isInside(inverseCDF.quantile(RealScalar.of(0.9999999999999985))));
    assertTrue(Clips.interval(1900, 2000).isInside(inverseCDF.quantile(RealScalar.ONE)));
  }

  public void testToString() {
    Distribution distribution = PoissonDistribution.of(RealScalar.of(5.5));
    String string = distribution.toString();
    assertEquals(string, "PoissonDistribution[5.5]");
  }

  public void testQuantityFail() {
    AssertFail.of(() -> PoissonDistribution.of(Quantity.of(3, "m")));
  }

  public void testFailLambda() {
    AssertFail.of(() -> PoissonDistribution.of(RealScalar.ZERO));
    AssertFail.of(() -> PoissonDistribution.of(RealScalar.of(-0.1)));
  }

  public void testLarge() {
    Distribution distribution = PoissonDistribution.of(RealScalar.of(700));
    PDF pdf = PDF.of(distribution);
    assertTrue(Scalars.isZero(pdf.at(RealScalar.of(140.123))));
    assertTrue(Scalars.nonZero(pdf.at(RealScalar.of(1942))));
    assertTrue(Scalars.isZero(pdf.at(RealScalar.of(1945))));
    assertTrue(Scalars.isZero(pdf.at(RealScalar.of(10000000))));
    assertTrue(Scalars.isZero(pdf.at(RealScalar.of(-1))));
    assertTrue(Scalars.isZero(pdf.at(RealScalar.of(-10000000))));
    assertTrue(Scalars.isZero(pdf.at(RealScalar.of(-1000000.12))));
  }

  public void testNextDownOne() {
    for (int c = 1; c < 700; c += 3) {
      Scalar lambda = DoubleScalar.of(c * .5 + 300);
      AbstractDiscreteDistribution distribution = //
          (AbstractDiscreteDistribution) PoissonDistribution.of(lambda);
      Scalar scalar = distribution.quantile(RealScalar.of(Math.nextDown(1.0)));
      ExactScalarQ.require(scalar);
    }
  }
}
