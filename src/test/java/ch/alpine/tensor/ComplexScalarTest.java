// code by jph
package ch.alpine.tensor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.lie.Quaternion;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.GaussScalar;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.exp.Exp;

public class ComplexScalarTest {
  @Test
  public void testSign() {
    Scalar scalar = ComplexScalar.of(4, 3);
    Scalar result = Sign.FUNCTION.apply(scalar);
    Tolerance.CHOP.requireClose(scalar, result.multiply(RealScalar.of(5)));
  }

  @Test
  public void testSignEps() {
    Scalar scalar = ComplexScalar.of(0, Double.MIN_VALUE);
    Scalar result = Sign.FUNCTION.apply(scalar);
    Tolerance.CHOP.requireClose(result, ComplexScalar.I);
  }

  @Test
  public void testSignEpsReIm() {
    Scalar scalar = ComplexScalar.of(Double.MIN_VALUE, Double.MIN_VALUE);
    Scalar result = Sign.FUNCTION.apply(scalar);
    Tolerance.CHOP.requireClose(result, ComplexScalar.of(0.7071067811865475, 0.7071067811865475));
  }

  @Test
  public void testSignEpsReImNeg() {
    Scalar scalar = ComplexScalar.of(Double.MIN_VALUE, -Double.MIN_VALUE);
    Scalar result = Sign.FUNCTION.apply(scalar);
    Tolerance.CHOP.requireClose(result, ComplexScalar.of(0.7071067811865475, -0.7071067811865475));
  }

  @Test
  public void testOne() {
    Scalar scalar = ComplexScalar.of(56, 217);
    assertEquals(scalar.one().multiply(scalar), scalar);
    assertEquals(scalar.multiply(scalar.one()), scalar);
  }

  @Test
  public void testConstructFail() {
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.of(RealScalar.ONE, ComplexScalar.I));
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.of(ComplexScalar.I, RealScalar.ONE));
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.of(Quaternion.ONE, RealScalar.ONE));
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.of(RealScalar.ONE, Quaternion.ONE));
  }

  @Test
  public void testNullFail() {
    assertThrows(NullPointerException.class, () -> ComplexScalar.of(RealScalar.ONE, null));
    assertThrows(NullPointerException.class, () -> ComplexScalar.of(null, RealScalar.ONE));
    assertThrows(NullPointerException.class, () -> ComplexScalar.of(null, RealScalar.ZERO));
  }

  @Test
  public void testPolarFail() {
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.fromPolar(RealScalar.ONE, ComplexScalar.I));
    assertThrows(ClassCastException.class, () -> ComplexScalar.fromPolar(ComplexScalar.I, RealScalar.ONE));
    assertThrows(ClassCastException.class, () -> ComplexScalar.fromPolar(ComplexScalar.I, ComplexScalar.I));
  }

  @Test
  public void testPolarQuantityFail() {
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.fromPolar(RealScalar.ONE, Quantity.of(1.3, "m")));
  }

  @Test
  public void testPolar() {
    assertInstanceOf(ComplexScalar.class, ComplexScalar.fromPolar(1, 3));
    assertInstanceOf(RealScalar.class, ComplexScalar.fromPolar(1, 0));
  }

  @Test
  public void testPolarNumberFail() {
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.fromPolar(-1, 3));
  }

  @Test
  public void testUnitExp() {
    Scalar theta = RealScalar.of(0.3);
    Tolerance.CHOP.requireClose( //
        Exp.FUNCTION.apply(theta.multiply(ComplexScalar.I)), //
        ComplexScalar.unit(theta));
  }

  @Test
  public void testGaussScalar() {
    Scalar scalar = ComplexScalar.of(GaussScalar.of(3, 7), GaussScalar.of(2, 7));
    Scalar invers = scalar.reciprocal();
    assertEquals(scalar.multiply(invers), GaussScalar.of(1, 7));
    assertEquals(invers.multiply(scalar), GaussScalar.of(1, 7));
  }

  @Test
  public void testGaussScalarCommute() {
    int p = 43;
    Scalar cs = ComplexScalar.of(GaussScalar.of(31, p), GaussScalar.of(22, p));
    Scalar gs = GaussScalar.of(16, p);
    assertEquals(cs.multiply(gs), gs.multiply(cs));
    assertEquals(cs.divide(gs), gs.under(cs));
    assertEquals(cs.under(gs), gs.divide(cs));
  }

  @Test
  public void testAsField() {
    // primes not resulting in field: 5,13,17
    // int c = 0;
    // primes resulting in field:
    for (int p : new int[] { 3, 7, 11, 19 }) { // also 23, 31, 43
      Scalar neutral = GaussScalar.of(1, p);
      for (int i = 0; i < p; ++i)
        for (int j = (0 < i ? 0 : 1); j < p; ++j) {
          Scalar scalar = ComplexScalar.of(GaussScalar.of(i, p), GaussScalar.of(j, p));
          // try {
          assertEquals(scalar.reciprocal().multiply(scalar), neutral);
          assertEquals(scalar.multiply(scalar.reciprocal()), neutral);
          assertEquals(scalar.divide(scalar), neutral);
          assertEquals(scalar.under(scalar), neutral);
          // } catch (Exception e) {
          // System.out.println(scalar);
          // ++c;
          // }
        }
    }
    // System.out.println(c + " of " + p * p);
  }

  @Test
  public void testUnitFail() {
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.unit(ComplexScalar.of(-1, 3)));
    assertThrows(TensorRuntimeException.class, () -> ComplexScalar.unit(Quantity.of(3, "s")));
  }
}
