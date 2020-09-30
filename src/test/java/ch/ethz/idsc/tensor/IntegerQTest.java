// code by jph
package ch.ethz.idsc.tensor;

import junit.framework.TestCase;

public class IntegerQTest extends TestCase {
  public void testPositive() {
    assertTrue(IntegerQ.of(Scalars.fromString("9/3")));
    assertTrue(IntegerQ.of(Scalars.fromString("-529384765923478653476593847659876237486")));
    assertTrue(IntegerQ.of(ComplexScalar.of(-2, 0)));
  }

  public void testNegative() {
    assertFalse(IntegerQ.of(Scalars.fromString("9.0")));
    assertFalse(IntegerQ.of(ComplexScalar.of(2, 3)));
    assertFalse(IntegerQ.of(Scalars.fromString("abc")));
  }

  public void testRequire() {
    Scalar scalar = RealScalar.of(2);
    assertTrue(IntegerQ.require(scalar) == scalar);
  }

  public void testRequireFail() {
    try {
      IntegerQ.require(RealScalar.of(0.2));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNullFail() {
    try {
      IntegerQ.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
