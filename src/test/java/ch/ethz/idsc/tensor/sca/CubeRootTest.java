// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class CubeRootTest extends TestCase {
  public void testSimple() {
    Scalar scalar = CubeRoot.FUNCTION.apply(RealScalar.of(27));
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(3));
  }

  public void testQuantity() {
    Scalar input = Quantity.of(2, "m^3");
    Scalar scalar = CubeRoot.FUNCTION.apply(input);
    Tolerance.CHOP.requireClose(scalar, Quantity.of(1.2599210498948732, "m"));
    Tolerance.CHOP.requireClose(Times.of(scalar, scalar, scalar), input);
  }

  public void testNegative() {
    Scalar input = Quantity.of(-2, "m^3");
    Scalar scalar = CubeRoot.FUNCTION.apply(input);
    Tolerance.CHOP.requireClose(scalar, Quantity.of(-1.2599210498948731648, "m"));
  }

  public void testZero() {
    Scalar scalar = CubeRoot.FUNCTION.apply(RealScalar.ZERO);
    assertEquals(scalar, RealScalar.ZERO);
    ExactScalarQ.require(scalar);
  }

  public void testOf() {
    Tensor tensor = Tensors.vector(-27, -8, -1, 0, 1, 8, 27).map(CubeRoot.FUNCTION);
    assertEquals(tensor, Range.of(-3, 4));
  }

  public void testComplexFail() {
    Scalar scalar = ComplexScalar.of(12, 23);
    AssertFail.of(() -> CubeRoot.FUNCTION.apply(scalar));
  }
}
