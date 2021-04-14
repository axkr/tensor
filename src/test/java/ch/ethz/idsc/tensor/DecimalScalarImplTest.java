// code by jph
package ch.ethz.idsc.tensor;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.num.GaussScalar;
import ch.ethz.idsc.tensor.sca.Ceiling;
import ch.ethz.idsc.tensor.sca.Floor;
import ch.ethz.idsc.tensor.sca.Imag;
import ch.ethz.idsc.tensor.sca.Real;
import ch.ethz.idsc.tensor.sca.Round;
import ch.ethz.idsc.tensor.sca.Sqrt;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class DecimalScalarImplTest extends TestCase {
  public void testZero() {
    assertEquals(RealScalar.ZERO, DecimalScalar.of(BigDecimal.ZERO));
  }

  public void testAddMultiply() {
    BigDecimal d = BigDecimal.ONE;
    Scalar sc1 = DecimalScalar.of(d);
    Scalar sc2 = sc1.add(sc1);
    Scalar sc2c = sc1.add(sc1);
    Scalar sc4 = sc2.multiply(sc2);
    Scalar r23 = RationalScalar.of(2, 3);
    assertEquals(sc2, sc2c);
    Scalar sc4pr23 = sc4.add(r23);
    Scalar sc4mr23 = sc4.multiply(r23);
    assertTrue(sc4pr23 instanceof DecimalScalar);
    assertTrue(sc4mr23 instanceof DecimalScalar);
  }

  public void testDouble() {
    BigDecimal d = BigDecimal.ONE;
    Scalar sc1 = DecimalScalar.of(d);
    Scalar sc2 = sc1.add(sc1);
    Scalar sc4 = sc2.multiply(sc2);
    Scalar r23 = DoubleScalar.of(2 / 3.);
    Scalar sc4pr23 = sc4.add(r23);
    Scalar sc4mr23 = sc4.multiply(r23);
    assertTrue(sc4pr23 instanceof DoubleScalar);
    assertTrue(sc4mr23 instanceof DoubleScalar);
  }

  public void testReciprocal() {
    BigDecimal d = BigDecimal.ONE;
    Scalar sc1 = DecimalScalar.of(d);
    Scalar sc2 = sc1.add(sc1);
    DecimalScalar sc3 = (DecimalScalar) sc2.add(sc1);
    Scalar s13 = sc3.reciprocal();
    Scalar r13 = RationalScalar.of(1, 3);
    Scalar d13 = DoubleScalar.of(1. / 3);
    assertEquals(r13, s13);
    assertEquals(s13, r13);
    assertEquals(d13, s13);
    assertEquals(s13, d13);
  }

  public void testDivide() {
    BigDecimal d = BigDecimal.ONE;
    Scalar sc1 = DecimalScalar.of(d);
    Scalar sc2 = sc1.add(sc1);
    Scalar sc3 = sc2.add(sc1);
    Scalar s23 = sc2.divide(sc3);
    Scalar r23 = RationalScalar.of(2, 3);
    Scalar d23 = DoubleScalar.of(Math.nextUp(2. / 3));
    Tolerance.CHOP.requireClose(r23, s23);
    Tolerance.CHOP.requireClose(s23, r23);
    Tolerance.CHOP.requireClose(d23, s23);
    Tolerance.CHOP.requireClose(s23, d23);
  }

  public void testDivide2() {
    Scalar s = DecimalScalar.of(new BigDecimal("123.345"));
    Scalar d = s.divide(RationalScalar.of(2, 7));
    assertEquals(d.toString(), "431.7075");
  }

  public void testSqrt() {
    // Mathematica N[Sqrt[2], 50] gives
    // ................1.4142135623730950488016887242096980785696718753769
    String expected = "1.414213562373095048801688724209698";
    Scalar sc1 = DecimalScalar.of(BigDecimal.ONE);
    DecimalScalar sc2 = (DecimalScalar) sc1.add(sc1);
    Scalar root2 = Sqrt.FUNCTION.apply(sc2);
    assertTrue(root2.toString().startsWith(expected));
  }

  public void testSqrtNeg() {
    // Mathematica N[Sqrt[2], 50] gives
    // ................1.4142135623730950488016887242096980785696718753769
    String expected = "1.414213562373095048801688724209698";
    Scalar sc1 = DecimalScalar.of(BigDecimal.ONE);
    DecimalScalar sc2 = (DecimalScalar) sc1.add(sc1).negate();
    Scalar root2 = Sqrt.FUNCTION.apply(sc2);
    assertEquals(Real.of(root2), RealScalar.ZERO);
    assertTrue(Imag.of(root2).toString().startsWith(expected));
  }

  public void testZero1() {
    assertEquals(RealScalar.of(BigDecimal.ONE).hashCode(), BigDecimal.ONE.hashCode());
  }

  public void testRound() {
    assertEquals(Round.of(DecimalScalar.of(new BigDecimal("12.1"))), RealScalar.of(12));
    assertEquals(Round.of(DecimalScalar.of(new BigDecimal("12.99"))), RealScalar.of(13));
    assertEquals(Round.of(DecimalScalar.of(new BigDecimal("25"))), RealScalar.of(25));
    assertTrue(Round.of(DecimalScalar.of(new BigDecimal("12.99"))) instanceof RationalScalar);
  }

  public void testFloor() {
    assertEquals(Floor.of(DecimalScalar.of(new BigDecimal("12.99"))), RealScalar.of(12));
    assertEquals(Floor.of(DecimalScalar.of(new BigDecimal("25"))), RealScalar.of(25));
    assertTrue(Floor.of(DecimalScalar.of(new BigDecimal("12.99"))) instanceof RationalScalar);
  }

  public void testCeiling() {
    assertEquals(Ceiling.of(DecimalScalar.of(new BigDecimal("12.1"))), RealScalar.of(13));
    assertEquals(Ceiling.of(DecimalScalar.of(new BigDecimal("25"))), RealScalar.of(25));
    assertTrue(Ceiling.of(DecimalScalar.of(new BigDecimal("12.99"))) instanceof RationalScalar);
  }

  public void testCompare0() {
    Scalar a = DecimalScalar.of(new BigDecimal("0.1"));
    Scalar b = DecimalScalar.of(new BigDecimal("0.2"));
    assertTrue(Scalars.lessThan(a, b));
    assertFalse(Scalars.lessThan(b, a));
  }

  public void testCompare1() {
    Scalar dec = DecimalScalar.of(new BigDecimal("0.1"));
    Scalar alt = DoubleScalar.of(0.01);
    assertTrue(Scalars.lessThan(alt, dec));
    assertFalse(Scalars.lessThan(dec, alt));
  }

  public void testCompare2() {
    Scalar dec = DecimalScalar.of(new BigDecimal("0.1"));
    Scalar alt = RationalScalar.of(1, 100);
    assertTrue(Scalars.lessThan(alt, dec));
    assertFalse(Scalars.lessThan(dec, alt));
  }

  public void testCompare3() {
    assertTrue(Scalars.lessThan(DecimalScalar.of(new BigDecimal("-3")), RealScalar.ZERO));
    assertFalse(Scalars.lessThan(DecimalScalar.of(new BigDecimal("3")), RealScalar.ZERO));
    assertFalse(Scalars.lessThan(RealScalar.ZERO, DecimalScalar.of(new BigDecimal("-3"))));
    assertTrue(Scalars.lessThan(RealScalar.ZERO, DecimalScalar.of(new BigDecimal("3"))));
  }

  public void testEquals() {
    Scalar rs1 = RealScalar.ONE;
    Scalar ds1 = DecimalScalar.of(new BigDecimal("1.0000"));
    assertEquals(ds1, rs1);
    assertEquals(rs1, ds1);
  }

  public void testEqualsSpecial() {
    final Scalar ds1 = DecimalScalar.of(new BigDecimal("1.0234", MathContext.DECIMAL128));
    assertTrue(ds1 instanceof DecimalScalar);
    assertFalse(ds1.equals(null));
    assertFalse(ds1.equals(ComplexScalar.of(1, 2)));
    assertFalse(ds1.equals("hello"));
    assertFalse(ds1.equals(GaussScalar.of(6, 7)));
  }

  private static final class ObjectExtension {
    @Override
    public boolean equals(Object obj) {
      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }
  }

  public void testEqualsTrue() {
    Object object = new ObjectExtension();
    final Scalar ds1 = DecimalScalar.of(new BigDecimal("1.0234", MathContext.DECIMAL128));
    assertTrue(ds1.equals(object));
  }

  public void testNullFail() {
    AssertFail.of(() -> DecimalScalar.of((BigDecimal) null));
    AssertFail.of(() -> DecimalScalar.of((String) null, 10));
  }
}
