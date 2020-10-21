// code by jph
package ch.ethz.idsc.tensor.sca.win;

import java.util.Map;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Tally;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class TukeyWindowTest extends TestCase {
  public void testSmall() {
    Tensor tensor = Tensors.of(RationalScalar.of(-1, 6), RealScalar.ZERO, RealScalar.of(0.01), RationalScalar.of(1, 6));
    Tensor mapped = tensor.map(TukeyWindow.FUNCTION);
    Map<Tensor, Long> map = Tally.of(mapped);
    assertEquals(map.get(RealScalar.ONE).longValue(), tensor.length());
  }

  public void testNumerical() {
    ScalarUnaryOperator scalarUnaryOperator = TukeyWindow.FUNCTION;
    assertEquals(scalarUnaryOperator.apply(RealScalar.of(0.12)), RealScalar.ONE);
    Scalar scalar = scalarUnaryOperator.apply(RealScalar.of(0.22));
    Chop._12.requireClose(scalar, RealScalar.of(0.9381533400219317)); // mathematica
  }

  public void testSemiExact() {
    Scalar scalar = TukeyWindow.FUNCTION.apply(RealScalar.of(0.5));
    assertTrue(Scalars.isZero(scalar));
    ExactScalarQ.require(scalar);
  }

  public void testOutside() {
    Scalar scalar = TukeyWindow.FUNCTION.apply(RealScalar.of(-0.52));
    assertEquals(scalar, RealScalar.ZERO);
    ExactScalarQ.require(scalar);
  }

  public void testOf() {
    Tensor tensor = RandomVariate.of(NormalDistribution.standard(), 2, 3);
    assertEquals(TukeyWindow.of(tensor), tensor.map(TukeyWindow.FUNCTION));
  }

  public void testQuantityFail() {
    AssertFail.of(() -> TukeyWindow.FUNCTION.apply(Quantity.of(0, "s")));
    AssertFail.of(() -> TukeyWindow.FUNCTION.apply(Quantity.of(2, "s")));
  }
}
