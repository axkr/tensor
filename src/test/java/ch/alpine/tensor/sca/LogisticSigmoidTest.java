// code by jph
package ch.alpine.tensor.sca;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import junit.framework.TestCase;

public class LogisticSigmoidTest extends TestCase {
  public void testBasic() {
    assertEquals(LogisticSigmoid.of(RealScalar.ZERO), RealScalar.of(0.5));
    assertEquals(LogisticSigmoid.of(RealScalar.of(0.)), RealScalar.of(0.5));
    assertEquals(LogisticSigmoid.FUNCTION.apply(RealScalar.of(1e3)), RealScalar.ONE);
    assertEquals(LogisticSigmoid.FUNCTION.apply(RealScalar.of(-1e3)), RealScalar.ZERO);
  }

  public void testMathematica() {
    Scalar big = LogisticSigmoid.of(RealScalar.of(0.5));
    assertTrue(big.toString().startsWith("0.622459")); // from Mathematica
  }
}
