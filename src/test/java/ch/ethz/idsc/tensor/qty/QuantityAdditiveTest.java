// code by jph
package ch.ethz.idsc.tensor.qty;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.num.GaussScalar;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class QuantityAdditiveTest extends TestCase {
  private static void _checkPlusSymmetry(Scalar s1, Scalar s2) {
    Scalar r1 = s1.add(s2);
    Scalar r2 = s2.add(s1);
    assertEquals(r1.toString(), r2.toString());
  }

  public void testPlusSymmetry() {
    _checkPlusSymmetry(Quantity.of(0, "m"), Quantity.of(0, "kg"));
    _checkPlusSymmetry(Quantity.of(0, "m"), Quantity.of(2, "kg"));
    _checkPlusSymmetry( //
        Quantity.of(ComplexScalar.of(0, 0), "m"), //
        RealScalar.of(0));
    _checkPlusSymmetry( //
        Quantity.of(ComplexScalar.of(1, 3), "m"), //
        RealScalar.of(0));
    _checkPlusSymmetry(Quantity.of(0, "s"), RealScalar.of(0.0));
    _checkPlusSymmetry(Quantity.of(0, "s"), RealScalar.of(2));
    _checkPlusSymmetry(Quantity.of(2, "s"), RealScalar.of(0));
  }

  public void testPlusUnits1() {
    Scalar s1 = Quantity.of(0, "m");
    Scalar s2 = Quantity.of(0, "kg");
    _checkPlusSymmetry(s1, s2);
    assertEquals(s1.add(s2).toString(), RealScalar.ZERO.toString());
  }

  public void testPlusUnits2() {
    Scalar s1 = Quantity.of(0, "m"); //
    Scalar s2 = Quantity.of(0.0, "kg");
    _checkPlusSymmetry(s1, s2);
    assertEquals(s1.add(s2).toString(), RealScalar.of(0.0).toString());
  }

  public void testPlusUnits3() {
    Scalar s1 = Quantity.of(0, "m"); //
    Scalar s2 = Quantity.of(0.0, "m");
    _checkPlusSymmetry(s1, s2);
    assertEquals(s1.add(s2).toString(), s2.toString()); // result in numeric precision
  }

  public void testPlusUnits4() {
    Scalar s1 = Quantity.of(0, "m"); //
    Scalar s2 = Quantity.of(0.0, "");
    _checkPlusSymmetry(s1, s2);
    assertEquals(s1.add(s2).toString(), s2.toString()); // result in numeric precision
  }

  public void testPlusUnits5() {
    Scalar s1 = Quantity.of(0.0, "m"); //
    Scalar s2 = RealScalar.ZERO;
    _checkPlusSymmetry(s1, s2);
    assertEquals(s1.add(s2).toString(), RealScalar.of(0.0).toString()); // result in numeric precision
  }

  public void testPlusMix() {
    Scalar s1 = Quantity.of(0, "m"); //
    Scalar s2 = Quantity.of(2, "kg");
    _checkPlusSymmetry(s1, s2);
    assertEquals(s1.add(s2).toString(), s2.toString()); // result in numeric precision
  }

  public void testPlusMix2() {
    Scalar s1 = Quantity.of(3, "m"); //
    Scalar s2 = Quantity.of(0, "kg");
    _checkPlusSymmetry(s1, s2);
    assertEquals(s1.add(s2).toString(), s1.toString()); // result in numeric precision
  }

  public void testPlusMix3() {
    Scalar s1 = Quantity.of(0, "m"); //
    Scalar s2 = Quantity.of(0, "kg");
    _checkPlusSymmetry(s1, s2);
    assertEquals(s1.add(s2).toString(), "0"); // result in numeric precision
  }

  public void testPlusMixFail() {
    Scalar s1 = Quantity.of(1.0, "m"); //
    Scalar s2 = GaussScalar.of(0, 7);
    AssertFail.of(() -> s1.add(s2));
  }

  public void testPlusMixZeroFail() {
    Scalar s1 = Quantity.of(0.0, "m"); //
    Scalar s2 = GaussScalar.of(0, 7);
    AssertFail.of(() -> s1.add(s2));
  }

  public void testComplex() {
    Scalar s1 = ComplexScalar.of(1, 2);
    Scalar s2 = Quantity.of(0, "m*s");
    assertEquals(s1, s1.add(s2));
  }

  public void testAddDifferent() {
    Scalar s1 = Quantity.of(200, "g"); //
    Scalar s2 = Quantity.of(1, "kg");
    Scalar sum = Total.of(Tensors.of(s1, s2).map(UnitSystem.SI())).Get();
    assertEquals(sum, Scalars.fromString("6/5[kg]"));
  }

  public void testPlusFail() {
    AssertFail.of(() -> Quantity.of(2, "m").add(Quantity.of(2, "kg")));
    try {
      _checkPlusSymmetry( //
          Quantity.of(ComplexScalar.of(1, 2), "m"), //
          Quantity.of(2, "kg"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
