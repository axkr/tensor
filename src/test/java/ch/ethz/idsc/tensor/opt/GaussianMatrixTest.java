// code by jph
package ch.ethz.idsc.tensor.opt;

import java.util.Arrays;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Reverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

/** [
 * [ 0.0113 0.0838 0.0113 ]
 * [ 0.0838 0.6193 0.0838 ]
 * [ 0.0113 0.0838 0.0113 ]
 * ] */
public class GaussianMatrixTest extends TestCase {
  private static void _check(int n) {
    Tensor matrix = GaussianMatrix.of(n);
    int size = 2 * n + 1;
    assertEquals(Dimensions.of(matrix), Arrays.asList(size, size));
    assertTrue(SymmetricMatrixQ.of(matrix));
    assertEquals(Reverse.of(matrix), matrix);
  }

  public void testSmall() {
    for (int index = 1; index < 5; ++index)
      _check(index);
  }

  public void testFail() {
    AssertFail.of(() -> GaussianMatrix.of(0));
    AssertFail.of(() -> GaussianMatrix.of(-1));
  }
}
