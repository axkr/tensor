// code by jph
package ch.alpine.tensor.num;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class RootsDegree1Test extends TestCase {
  public void testGaussScalar() {
    Tensor coeffs = Tensors.of(GaussScalar.of(4, 7), GaussScalar.of(5, 7));
    Tensor roots = Roots.of(coeffs);
    Tensor zeros = roots.map(Polynomial.of(coeffs));
    assertEquals(zeros, Tensors.of(GaussScalar.of(0, 7)));
    Chop.NONE.requireAllZero(zeros);
    Tolerance.CHOP.requireAllZero(zeros);
    assertEquals(roots, Tensors.of(GaussScalar.of(2, 7)));
  }
}
