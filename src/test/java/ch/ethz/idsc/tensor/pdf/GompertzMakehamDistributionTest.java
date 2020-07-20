// code by jph
package ch.ethz.idsc.tensor.pdf;

import java.io.IOException;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.qty.UnitConvert;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GompertzMakehamDistributionTest extends TestCase {
  public void testPDF() throws ClassNotFoundException, IOException {
    Distribution distribution = //
        Serialization.copy(GompertzMakehamDistribution.of(RealScalar.of(3), RealScalar.of(0.2)));
    PDF pdf = PDF.of(distribution);
    Scalar prob = pdf.at(RealScalar.of(0.35));
    assertTrue(Chop._13.close(prob, RealScalar.of(1.182515740643019)));
    assertEquals(pdf.at(RealScalar.of(4.35)), RealScalar.ZERO);
  }

  public void testCDF() {
    Distribution distribution = //
        GompertzMakehamDistribution.of(RealScalar.of(3), RealScalar.of(0.2));
    CDF cdf = CDF.of(distribution);
    Scalar prob = cdf.p_lessEquals(RealScalar.of(0.35));
    assertTrue(Chop._13.close(prob, RealScalar.of(0.3103218390514517)));
    assertEquals(cdf.p_lessEquals(RealScalar.of(4.35)), RealScalar.ONE);
    assertEquals(CDF.of(distribution).p_lessThan(RealScalar.ZERO), RealScalar.ZERO);
    assertEquals(CDF.of(distribution).p_lessEquals(RealScalar.ZERO), RealScalar.ZERO);
  }

  public void testRandomVariate() {
    GompertzMakehamDistribution gmd = (GompertzMakehamDistribution) //
    GompertzMakehamDistribution.of(RealScalar.of(3), RealScalar.of(0.2));
    assertTrue(Scalars.lessThan(gmd.randomVariate(0), RealScalar.of(3)));
    assertTrue(Scalars.isZero(gmd.randomVariate(Math.nextDown(1.0))));
  }

  public void testQuantity() {
    Distribution distribution = GompertzMakehamDistribution.of(Quantity.of(0.3, "m^-1"), RealScalar.of(0.1));
    Scalar rand = RandomVariate.of(distribution);
    assertTrue(rand instanceof Quantity);
    UnitConvert.SI().to(Unit.of("in")).apply(rand);
    {
      Scalar prob = PDF.of(distribution).at(Quantity.of(1, "m"));
      QuantityMagnitude.SI().in(Unit.of("in^-1")).apply(prob);
    }
    {
      CDF cdf = CDF.of(distribution);
      Scalar prob = cdf.p_lessEquals(Quantity.of(10, "m"));
      assertTrue(prob instanceof DoubleScalar);
      assertTrue(NumberQ.of(prob));
    }
  }

  public void testQuantityPDF() {
    Distribution distribution = GompertzMakehamDistribution.of(Quantity.of(0.3, "m^-1"), RealScalar.of(0.1));
    {
      Scalar prob = PDF.of(distribution).at(Quantity.of(-1, "m"));
      assertTrue(prob instanceof Quantity);
      assertTrue(Scalars.isZero(prob));
      QuantityMagnitude.SI().in(Unit.of("in^-1")).apply(prob);
    }
    assertEquals(CDF.of(distribution).p_lessThan(Quantity.of(-2, "m^1*s^0")), RealScalar.ZERO);
  }

  public void testToString() {
    Distribution distribution = GompertzMakehamDistribution.of(Quantity.of(0.3, "m^-1"), RealScalar.of(0.1));
    String string = distribution.toString();
    assertTrue(string.startsWith(GompertzMakehamDistribution.class.getSimpleName()));
  }

  public void testPdfUnitFail() {
    Distribution distribution = GompertzMakehamDistribution.of(Quantity.of(0.3, "m^-1"), RealScalar.of(0.1));
    PDF pdf = PDF.of(distribution);
    assertEquals(pdf.at(Quantity.of(0.0, "m")), Quantity.of(0.03, "m^-1"));
    try {
      pdf.at(Quantity.of(-1, "m^2"));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      pdf.at(Quantity.of(+1, "m^2"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testCdfUnitFail() {
    Distribution distribution = GompertzMakehamDistribution.of(Quantity.of(0.3, "m^-1"), RealScalar.of(0.1));
    CDF cdf = CDF.of(distribution);
    Chop._10.requireClose(cdf.p_lessEquals(Quantity.of(+0.1, "m")), RealScalar.of(0.003040820706232905));
    assertEquals(cdf.p_lessEquals(Quantity.of(+0.0, "m")), RealScalar.ZERO);
    assertEquals(cdf.p_lessEquals(Quantity.of(-0.1, "m")), RealScalar.ZERO);
    try {
      cdf.p_lessEquals(Quantity.of(-1, "m^2"));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      cdf.p_lessEquals(Quantity.of(+1, "m^2"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFail() {
    try {
      GompertzMakehamDistribution.of(RealScalar.of(0), RealScalar.of(0.2));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      GompertzMakehamDistribution.of(RealScalar.of(3), RealScalar.of(0));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      GompertzMakehamDistribution.of(RealScalar.of(1e-300), RealScalar.of(1e-300));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
