// code by jph
package ch.alpine.tensor.lie.r2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.usr.AssertFail;

public class Det2DTest {
  @Test
  public void testSimple() {
    assertEquals(Det2D.of(Tensors.vector(1, 0), Tensors.vector(0, 1)), RealScalar.ONE);
    assertEquals(Det2D.of(Tensors.vector(1, 1), Tensors.vector(0, 1)), RealScalar.ONE);
    assertEquals(Det2D.of(Tensors.vector(1, 2), Tensors.vector(0, 1)), RealScalar.ONE);
  }

  @Test
  public void testMore() {
    assertEquals(Det2D.of(Tensors.vector(0, 1), Tensors.vector(1, 1)), RealScalar.ONE.negate());
  }

  @Test
  public void testArea() {
    Scalar det = Det2D.of(UnitVector.of(2, 0), UnitVector.of(2, 1));
    assertEquals(det, RealScalar.ONE);
    assertTrue(ExactScalarQ.of(det));
  }

  @Test
  public void testFailP() {
    AssertFail.of(() -> Det2D.of(Tensors.vector(1, 0), Tensors.vector(0, 1, 0)));
  }

  @Test
  public void testFailQ() {
    AssertFail.of(() -> Det2D.of(Tensors.vector(1, 0, 0), Tensors.vector(0, 1)));
  }
}
