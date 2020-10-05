// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class RotateLeftTest extends TestCase {
  public void testVector() {
    Tensor vector = Tensors.vector(0, 1, 2, 3, 4).unmodifiable();
    assertEquals(RotateLeft.of(vector, -6), Tensors.vector(4, 0, 1, 2, 3));
    assertEquals(RotateLeft.of(vector, -1), Tensors.vector(4, 0, 1, 2, 3));
    assertEquals(RotateLeft.of(vector, +0), Tensors.vector(0, 1, 2, 3, 4));
    assertEquals(RotateLeft.of(vector, +1), Tensors.vector(1, 2, 3, 4, 0));
    assertEquals(RotateLeft.of(vector, +2), Tensors.vector(2, 3, 4, 0, 1));
    assertEquals(RotateLeft.of(vector, +7), Tensors.vector(2, 3, 4, 0, 1));
    assertEquals(vector, Range.of(0, 5));
  }

  public void testReferences() {
    Tensor matrix = HilbertMatrix.of(3);
    Tensor tensor = RotateLeft.of(matrix, 1);
    matrix.set(s -> RealScalar.ONE, Tensor.ALL, 1);
    assertEquals(tensor, RotateLeft.of(HilbertMatrix.of(3), 1));
  }

  public void testEmpty() {
    assertEquals(RotateLeft.of(Tensors.empty(), +1), Tensors.empty());
    assertEquals(RotateLeft.of(Tensors.empty(), +0), Tensors.empty());
    assertEquals(RotateLeft.of(Tensors.empty(), -1), Tensors.empty());
  }

  public void testFailScalar() {
    AssertFail.of(() -> RotateLeft.of(RealScalar.ONE, 0));
  }

  public void testFailNull() {
    AssertFail.of(() -> RotateLeft.of(null, 0));
  }
}
