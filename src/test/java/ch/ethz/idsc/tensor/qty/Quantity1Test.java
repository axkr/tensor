// code by jph
package ch.ethz.idsc.tensor.qty;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Sort;
import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class Quantity1Test extends TestCase {
  public void testSimple() {
    assertTrue(Quantity.fromString("-7[m*kg^-2]") instanceof Quantity);
    assertTrue(Quantity.fromString("3 [ m ]") instanceof Quantity);
    assertTrue(Quantity.fromString("3 [ m *rad ]") instanceof Quantity);
    assertFalse(Quantity.fromString(" 3  ") instanceof Quantity);
  }

  public void testParseFail() {
    try {
      Quantity.of(3.14, "[^2]");
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
    try {
      Quantity.of(3.14, "[m^2a]");
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
    try {
      Quantity.of(3.14, "[m^]");
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
    try {
      Quantity.of(3.14, "[m[^2]");
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
    try {
      Quantity.of(3.14, "[m]^2]");
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNestFail() {
    Scalar q1 = Quantity.of(3.14, "[m]");
    try {
      Quantity.of(q1, "[s]");
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }

  public void testValue() {
    Quantity quantity = (Quantity) Quantity.fromString("-7+3*I[kg^-2*m*s]");
    Scalar scalar = quantity.value();
    assertEquals(scalar, ComplexScalar.of(-7, 3));
  }

  public void testUnitString() {
    Quantity quantity = (Quantity) Quantity.fromString("-7+3*I[kg^-2*m*s]");
    String string = quantity.unitString();
    assertEquals(string, "[kg^-2*m*s]");
  }

  private static void _checkDivision(Scalar q1, Scalar q2) {
    assertEquals(q1.divide(q2), q2.under(q1));
    assertEquals(q2.divide(q1), q1.under(q2));
  }

  public void testDivisionUnder() {
    _checkDivision(Quantity.of(1, "[m]"), Quantity.of(2, "[s]"));
    _checkDivision(Quantity.of(1, "[m]"), DoubleScalar.of(2.0));
    _checkDivision(Quantity.of(1, "[m]"), RealScalar.of(2));
    double eps = Math.nextUp(0.0);
    _checkDivision(Quantity.of(eps, "[m]"), Quantity.of(2, "[s]"));
    _checkDivision(Quantity.of(eps, "[m]"), DoubleScalar.of(2.0));
    _checkDivision(Quantity.of(eps, "[m]"), RealScalar.of(2));
    // ---
    _checkDivision(Quantity.of(1, "[m]"), Quantity.of(eps, "[s]"));
    _checkDivision(Quantity.of(1, "[m]"), DoubleScalar.of(eps));
    _checkDivision(Quantity.of(1, "[m]"), RealScalar.of(eps));
    // ---
    _checkDivision(Quantity.of(0, "[m]"), Quantity.of(eps, "[s]"));
    _checkDivision(Quantity.of(0, "[m]"), DoubleScalar.of(eps));
    _checkDivision(Quantity.of(0.0, "[m]"), Quantity.of(eps, "[s]"));
    _checkDivision(Quantity.of(0.0, "[m]"), DoubleScalar.of(eps));
    // ---
    _checkDivision(Quantity.of(eps, "[m]"), Quantity.of(eps, "[s]"));
    _checkDivision(Quantity.of(eps, "[m]"), DoubleScalar.of(eps));
  }

  public void testDivision1() {
    Scalar quantity = Quantity.of(0, "[m]");
    Scalar eps = DoubleScalar.of(Math.nextUp(0.0));
    assertTrue(Scalars.isZero(quantity.divide(eps)));
  }

  public void testDivision2() {
    Scalar zero = DoubleScalar.of(0.0);
    Scalar eps = Quantity.of(Math.nextUp(0.0), "[m]");
    assertTrue(Scalars.isZero(zero.divide(eps)));
  }

  public void testDivision3() {
    Scalar s1 = ComplexScalar.of(1, 2);
    Scalar s2 = Quantity.of(3, "[m]");
    assertEquals(s1.divide(s2), s2.under(s1));
  }

  public void testEmptyFail() {
    try {
      Quantity.of(3.14, "[]"); // at the moment empty brackets are not supported
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }

  private void _checkCompareTo(Scalar s1, Scalar s2, int value) {
    int res1 = +Scalars.compare(s1, s2);
    int res2 = -Scalars.compare(s2, s1);
    assertEquals(res1, res2);
    assertEquals(res1, value);
  }

  public void testCompare() {
    _checkCompareTo(Quantity.of(2, "[m]"), Quantity.of(3, "[m]"), Integer.compare(2, 3));
    _checkCompareTo(Quantity.of(-2, "[kg]"), Quantity.of(0, "[m]"), Integer.compare(-2, 0));
    _checkCompareTo(Quantity.of(0, "[kg]"), Quantity.of(0, "[m]"), Integer.compare(0, 0));
    _checkCompareTo(Quantity.of(2, "[m]"), RealScalar.ZERO, Integer.compare(2, 0));
    _checkCompareTo(Quantity.of(0, "[kg]"), RealScalar.ONE, Integer.compare(0, 1));
  }

  public void testCompareFail() {
    try {
      _checkCompareTo(Quantity.of(2, "[m]"), Quantity.of(2, "[kg]"), Integer.compare(2, 2));
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }

  public void testSort() {
    Tensor vector = Tensors.of( //
        Quantity.of(0, "[m]"), Quantity.of(9, "[m]"), Quantity.of(-3, "[m]"), Quantity.of(0, "[s]"), RealScalar.ZERO);
    assertEquals(Sort.of(vector), Tensors.fromString("{-3[m], 0[m], 0[s], 0, 9[m]}", Quantity::fromString));
  }

  public void testSerializable() throws Exception {
    Quantity quantity = (Quantity) Quantity.fromString("-7+3*I[kg^-2*m*s]");
    Quantity copy = Serialization.copy(quantity);
    assertEquals(quantity, copy);
  }
}