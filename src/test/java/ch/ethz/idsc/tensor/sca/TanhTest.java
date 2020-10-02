// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.num.GaussScalar;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class TanhTest extends TestCase {
  public void testReal() {
    Scalar i = RealScalar.of(2);
    Scalar c = Tanh.FUNCTION.apply(i);
    Scalar s = DoubleScalar.of(Math.tanh(2));
    assertEquals(c, Tanh.of(i));
    assertEquals(c, s);
  }

  public void testComplex() {
    Scalar c = Tanh.of(ComplexScalar.of(2, 3.));
    Scalar s = Scalars.fromString("0.965385879022133 - 0.009884375038322494*I"); // Mathematica
    Chop._13.requireClose(c, s);
  }

  public void testFail() {
    Scalar scalar = GaussScalar.of(3, 11);
    AssertFail.of(() -> Tanh.of(scalar));
  }
}
