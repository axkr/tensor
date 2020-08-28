// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.num.GaussScalar;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import junit.framework.TestCase;

public class ArcTanTest extends TestCase {
  public void testReal() {
    Scalar s = RealScalar.of(-3);
    Scalar r = ArcTan.FUNCTION.apply(s);
    assertEquals(r, ArcTan.of(s));
    // -1.5707963267948966192 + 1.7627471740390860505 I
    assertEquals(r, Scalars.fromString("-1.2490457723982544"));
  }

  public void testRealZero() {
    assertEquals(ArcTan.of(RealScalar.ZERO, RealScalar.ZERO), RealScalar.ZERO);
    assertEquals(ArcTan.of(0, 0), RealScalar.ZERO);
  }

  public void testNumber() {
    assertEquals(ArcTan.of(2, 0), RealScalar.ZERO);
    assertEquals(ArcTan.of(-3, 0), RealScalar.of(Math.PI));
    assertEquals(ArcTan.of(0, 4), RealScalar.of(Math.PI / 2));
    assertEquals(ArcTan.of(0, -5), RealScalar.of(-Math.PI / 2));
  }

  public void testComplexReal() {
    Scalar r = ArcTan.of(ComplexScalar.of(2, 3), RealScalar.of(12));
    // 1.39519 - 0.247768 I
    assertEquals(r, Scalars.fromString("1.3951860877095887-0.24776768676598088*I"));
  }

  public void testComplex() {
    Scalar s = ComplexScalar.of(5, -7);
    Scalar r = ArcTan.FUNCTION.apply(s);
    assertEquals(r, ArcTan.of(s));
    // 1.50273 - 0.0944406 I
    assertEquals(r, Scalars.fromString("1.502726846368326-0.09444062638970714*I"));
  }

  public void testComplex2() {
    Scalar x = ComplexScalar.of(4, -1);
    Scalar y = ComplexScalar.of(1, 2);
    Scalar r = ArcTan.of(x, y);
    // 0.160875 + 0.575646 I
    assertEquals(r, Scalars.fromString("0.1608752771983211+0.5756462732485114*I"));
  }

  public void testComplexZeroP() {
    Scalar x = RealScalar.ZERO;
    Scalar y = ComplexScalar.of(1, 2);
    Scalar r = ArcTan.of(x, y);
    assertEquals(r, DoubleScalar.of(Math.PI / 2));
  }

  public void testComplexZeroN() {
    Scalar x = RealScalar.ZERO;
    Scalar y = ComplexScalar.of(-1, 2);
    Scalar r = ArcTan.of(x, y);
    assertEquals(r, DoubleScalar.of(-Math.PI / 2));
  }

  public void testCornerCases() {
    assertEquals(ArcTan.of(RealScalar.of(-5), RealScalar.ZERO), Pi.VALUE);
    assertEquals(ArcTan.of(RealScalar.ZERO, RealScalar.ZERO), RealScalar.ZERO);
  }

  // Mathematica doesn't do this:
  // ArcTan[Quantity[12, "Meters"], Quantity[4, "Meters"]] is not evaluated
  public void testQuantity() {
    Scalar qs1 = Quantity.of(12, "m");
    Scalar qs2 = Quantity.of(4, "m");
    {
      assertFalse(qs1 instanceof RealScalar);
      Scalar res = ArcTan.of(qs1, qs2);
      assertTrue(res instanceof RealScalar);
      Chop._10.requireClose(res, RealScalar.of(0.32175055439664219340));
    }
  }

  public void testQuantityZeroX() {
    Scalar qs0 = Quantity.of(0, "m");
    Scalar qs1 = Quantity.of(12, "m");
    {
      Scalar res = ArcTan.of(qs0, qs1);
      assertTrue(res instanceof RealScalar);
      Chop._10.requireClose(res, RealScalar.of(Math.PI / 2));
    }
    {
      Scalar res = ArcTan.of(qs1, qs0);
      assertTrue(res instanceof RealScalar);
      Chop._10.requireZero(res);
    }
  }

  public void testAntiSymmetry() {
    Distribution distribution = NormalDistribution.standard();
    Distribution scaling = UniformDistribution.of(0.1, 2);
    for (int count = 0; count < 10; ++count) {
      Scalar x = RandomVariate.of(distribution);
      Scalar y = RandomVariate.of(distribution);
      Scalar lambda = RandomVariate.of(scaling);
      Scalar v1 = ArcTan.of(x, y);
      Scalar v2 = ArcTan.of(x, y.negate());
      Scalar v3 = ArcTan.of(x.multiply(lambda), y.multiply(lambda));
      assertEquals(v1, v2.negate());
      Chop._10.requireClose(v1, v3);
    }
  }

  public void testDoubleNaNFail() {
    try {
      ArcTan.FUNCTION.apply(ComplexScalar.of(Double.NaN, Double.NaN));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testQuantityFail() {
    try {
      ArcTan.of(Quantity.of(12, "m"), Quantity.of(4, "s"));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      ArcTan.of(Quantity.of(12, "m"), RealScalar.of(4));
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      ArcTan.of(RealScalar.of(12), Quantity.of(4, "s"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testGaussScalarFail() {
    try {
      ArcTan.of(RealScalar.of(5), GaussScalar.of(1, 7));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
