// code by jph
package ch.ethz.idsc.tensor.qty;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class QuantityMagnitudeTest extends TestCase {
  public void testSimple() {
    Scalar scalar = QuantityMagnitude.SI().in(Unit.of("K*m^2")).apply(Quantity.of(2, "K*km^2"));
    assertEquals(scalar, RealScalar.of(2_000_000));
  }

  public void testRad() {
    QuantityMagnitude quantityMagnitude = QuantityMagnitude.SI();
    Scalar q = Quantity.of(360, "deg");
    Unit unit = Unit.of("rad");
    Scalar scalar = quantityMagnitude.in(unit).apply(q);
    assertTrue(Chop._12.close(scalar, RealScalar.of(Math.PI * 2)));
  }

  public void testFailConversion() {
    QuantityMagnitude quantityMagnitude = QuantityMagnitude.SI();
    Scalar quantity = Quantity.of(360, "kg");
    Unit unit = Unit.of("m");
    try {
      quantityMagnitude.in(unit).apply(quantity);
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailNull() {
    try {
      new QuantityMagnitude(null);
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }
}
