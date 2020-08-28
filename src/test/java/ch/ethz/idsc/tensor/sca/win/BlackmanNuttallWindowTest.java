// code by jph
package ch.ethz.idsc.tensor.sca.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

// Mathematica gives the result for 1/4, 1/3, 1/2 in exact precision
public class BlackmanNuttallWindowTest extends TestCase {
  public void testSimple() {
    ScalarUnaryOperator windowFunction = BlackmanNuttallWindow.FUNCTION;
    Scalar scalar = windowFunction.apply(RationalScalar.HALF);
    Chop._10.requireClose(scalar, Scalars.fromString("907/2500000"));
  }

  public void testQuarter() {
    ScalarUnaryOperator windowFunction = BlackmanNuttallWindow.FUNCTION;
    Scalar scalar = windowFunction.apply(RationalScalar.of(1, 4));
    Chop._10.requireClose(scalar, Scalars.fromString("17733/78125"));
  }

  public void testThird() {
    ScalarUnaryOperator windowFunction = BlackmanNuttallWindow.FUNCTION;
    Scalar scalar = windowFunction.apply(RationalScalar.of(1, 3));
    Chop._10.requireClose(scalar, Scalars.fromString("122669/2000000"));
  }

  public void testOutside() {
    Scalar scalar = BlackmanNuttallWindow.FUNCTION.apply(RealScalar.of(-0.52));
    assertEquals(scalar, RealScalar.ZERO);
  }

  public void testOf() {
    Tensor tensor = RandomVariate.of(NormalDistribution.standard(), 2, 3);
    assertEquals(BlackmanNuttallWindow.of(tensor), tensor.map(BlackmanNuttallWindow.FUNCTION));
  }
}
