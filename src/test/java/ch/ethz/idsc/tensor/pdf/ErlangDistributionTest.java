// code by jph
package ch.ethz.idsc.tensor.pdf;

import java.io.IOException;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityUnit;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class ErlangDistributionTest extends TestCase {
  public void testPdf() throws ClassNotFoundException, IOException {
    Distribution distribution = Serialization.copy(ErlangDistribution.of(3, RealScalar.of(1.8)));
    PDF pdf = PDF.of(distribution);
    Scalar p = pdf.at(RealScalar.of(3.2));
    Chop._06.requireClose(p, RealScalar.of(0.0940917));
    assertEquals(pdf.at(RealScalar.of(0)), RealScalar.ZERO);
    assertEquals(pdf.at(RealScalar.of(-0.12)), RealScalar.ZERO);
  }

  public void testMean() {
    Distribution distribution = ErlangDistribution.of(5, Quantity.of(10, "m"));
    Scalar mean = Expectation.mean(distribution);
    assertEquals(mean, Scalars.fromString("1/2[m^-1]"));
  }

  public void testVariance() {
    Distribution distribution = ErlangDistribution.of(5, Quantity.of(10, "m"));
    Scalar var = Expectation.variance(distribution);
    assertEquals(var, Scalars.fromString("1/20[m^-2]"));
  }

  public void testQuantityPDF() {
    Distribution distribution = ErlangDistribution.of(4, Quantity.of(6, "m"));
    PDF pdf = PDF.of(distribution);
    {
      Scalar prob = pdf.at(Quantity.of(1.2, "m^-1"));
      assertEquals(QuantityUnit.of(prob), Unit.of("m"));
    }
    {
      Scalar prob = pdf.at(Quantity.of(-1.2, "m^-1"));
      assertTrue(prob instanceof Quantity);
      assertEquals(QuantityUnit.of(prob), Unit.of("m"));
    }
  }

  public void testToString() {
    Distribution distribution = ErlangDistribution.of(5, Quantity.of(10, "m"));
    assertEquals(distribution.toString(), "ErlangDistribution[5, 10[m]]");
  }

  public void testFail() {
    AssertFail.of(() -> ErlangDistribution.of(0, RealScalar.of(1.8)));
  }
}
