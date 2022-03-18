// code by jph
package ch.alpine.tensor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.io.StringScalar;
import ch.alpine.tensor.num.GaussScalar;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.usr.AssertFail;

public class ScalarQTest {
  /** equivalent to the predicate {@code tensor.length() == Scalar.LENGTH}
   * 
   * @param tensor
   * @return true if given tensor is instance of {@link Scalar} */
  public static boolean of(Tensor tensor) {
    return tensor instanceof Scalar;
  }

  @Test
  public void testScalar() {
    assertTrue(of(Quantity.of(3, "m")));
    assertTrue(of(GaussScalar.of(3, 11)));
    assertTrue(of(StringScalar.of("IDSC")));
  }

  @Test
  public void testVector() {
    assertFalse(of(Tensors.vector(1, 2, 3)));
  }

  @Test
  public void testThenThrow() {
    ScalarQ.thenThrow(Tensors.vector(1, 2, 3));
    AssertFail.of(() -> ScalarQ.thenThrow(RealScalar.ONE));
  }
}
