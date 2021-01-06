// code by jph
package ch.ethz.idsc.tensor;

import ch.ethz.idsc.tensor.io.StringScalar;
import ch.ethz.idsc.tensor.num.GaussScalar;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class ScalarQTest extends TestCase {
  public void testScalar() {
    assertTrue(ScalarQ.of(Quantity.of(3, "m")));
    assertTrue(ScalarQ.of(GaussScalar.of(3, 11)));
    assertTrue(ScalarQ.of(StringScalar.of("IDSC")));
  }

  public void testVector() {
    assertFalse(ScalarQ.of(Tensors.vector(1, 2, 3)));
  }

  public void testThenThrow() {
    ScalarQ.thenThrow(Tensors.vector(1, 2, 3));
    AssertFail.of(() -> ScalarQ.thenThrow(RealScalar.ONE));
  }
}
