// code by jph
package ch.ethz.idsc.tensor;

import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class ExactTensorQTest extends TestCase {
  public void testAll() {
    assertTrue(ExactTensorQ.of(RationalScalar.HALF));
    assertTrue(ExactTensorQ.of(Tensors.vector(1, 2, 3)));
    assertTrue(ExactTensorQ.of(HilbertMatrix.of(3, 2)));
    assertFalse(ExactTensorQ.of(Tensors.vector(1, 1, 1.)));
  }

  public void testRequireAll() {
    ExactTensorQ.require(Tensors.fromString("{{9/8, 3/2[s]}, 1/2+3/4*I}"));
    AssertFail.of(() -> ExactTensorQ.require(Tensors.vector(1, 2, 3, 0.7)));
  }
}
