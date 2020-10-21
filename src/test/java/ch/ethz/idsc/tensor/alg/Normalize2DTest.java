// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class Normalize2DTest extends TestCase {
  private static Tensor unlessZero(Scalar x, Scalar y) {
    return NormalizeUnlessZero.with(Norm._2::ofVector).apply(Tensors.of(x, y));
  }

  public void testUp() {
    double eps = Math.nextUp(0.0);
    assertEquals(unlessZero(RealScalar.of(eps), RealScalar.ZERO), Normalize.with(Norm._2).apply(Tensors.vector(1, 0)));
    assertEquals(unlessZero(RealScalar.ZERO, RealScalar.of(eps)), Normalize.with(Norm._2::ofVector).apply(Tensors.vector(0, 1)));
    assertEquals(unlessZero(RealScalar.of(eps), RealScalar.of(eps)), Normalize.with(Norm._2::ofVector).apply(Tensors.vector(1, 1)));
  }

  public void testDown() {
    double eps = Math.nextDown(0.0);
    Tensor vec = unlessZero(RealScalar.of(eps), RealScalar.ZERO);
    assertEquals(vec, Tensors.vector(-1, 0));
    assertEquals(unlessZero(RealScalar.ZERO, RealScalar.of(-eps)), Tensors.vector(0, 1));
  }

  public void testZero() {
    Tensor res = unlessZero(RealScalar.ZERO, RealScalar.ZERO);
    assertEquals(res, Array.zeros(2));
  }

  public void testUp2() {
    double eps = Math.nextUp(0.0);
    Tensor vec = unlessZero(RealScalar.of(eps), RealScalar.of(eps));
    Chop._12.requireClose(Norm._2.ofVector(vec), RealScalar.ONE);
  }

  public void testFail() {
    Tensor vector = Tensors.vectorDouble(0.0, 0.0);
    NormalizeUnlessZero.with(Norm._2::ofVector).apply(vector);
    TensorUnaryOperator tensorUnaryOperator = Normalize.with(Norm._2);
    AssertFail.of(() -> tensorUnaryOperator.apply(vector));
  }

  public void testNumberQFail1() {
    AssertFail.of(() -> unlessZero(DoubleScalar.POSITIVE_INFINITY, RealScalar.ZERO));
    AssertFail.of(() -> unlessZero(DoubleScalar.INDETERMINATE, RealScalar.ZERO));
  }

  public void testNumberQFail2() {
    AssertFail.of(() -> unlessZero(RealScalar.ZERO, DoubleScalar.POSITIVE_INFINITY));
    AssertFail.of(() -> unlessZero(RealScalar.ZERO, DoubleScalar.INDETERMINATE));
  }
}
