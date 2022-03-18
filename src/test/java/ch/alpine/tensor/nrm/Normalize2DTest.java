// code by jph
package ch.alpine.tensor.nrm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.usr.AssertFail;

public class Normalize2DTest {
  private static Tensor unlessZero(Scalar x, Scalar y) {
    return NormalizeUnlessZero.with(Vector2Norm::of).apply(Tensors.of(x, y));
  }

  @Test
  public void testUp() {
    double eps = Math.nextUp(0.0);
    assertEquals(unlessZero(RealScalar.of(eps), RealScalar.ZERO), Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 0)));
    assertEquals(unlessZero(RealScalar.ZERO, RealScalar.of(eps)), Vector2Norm.NORMALIZE.apply(Tensors.vector(0, 1)));
    assertEquals(unlessZero(RealScalar.of(eps), RealScalar.of(eps)), Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 1)));
  }

  @Test
  public void testDown() {
    double eps = Math.nextDown(0.0);
    Tensor vec = unlessZero(RealScalar.of(eps), RealScalar.ZERO);
    assertEquals(vec, Tensors.vector(-1, 0));
    assertEquals(unlessZero(RealScalar.ZERO, RealScalar.of(-eps)), Tensors.vector(0, 1));
  }

  @Test
  public void testZero() {
    Tensor res = unlessZero(RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(res, Array.zeros(2));
  }

  @Test
  public void testUp2() {
    double eps = Math.nextUp(0.0);
    Tensor vec = unlessZero(RealScalar.of(eps), RealScalar.of(eps));
    Chop._12.requireClose(Vector2Norm.of(vec), RealScalar.ONE);
  }

  @Test
  public void testFail() {
    Tensor vector = Tensors.vectorDouble(0.0, 0.0);
    NormalizeUnlessZero.with(Vector2Norm::of).apply(vector);
    AssertFail.of(() -> Vector2Norm.NORMALIZE.apply(vector));
  }

  @Test
  public void testNumberQFail1() {
    AssertFail.of(() -> unlessZero(DoubleScalar.POSITIVE_INFINITY, RealScalar.ZERO));
    AssertFail.of(() -> unlessZero(DoubleScalar.INDETERMINATE, RealScalar.ZERO));
  }

  @Test
  public void testNumberQFail2() {
    AssertFail.of(() -> unlessZero(RealScalar.ZERO, DoubleScalar.POSITIVE_INFINITY));
    AssertFail.of(() -> unlessZero(RealScalar.ZERO, DoubleScalar.INDETERMINATE));
  }
}
