// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class MatrixQTest extends TestCase {
  public void testEmpty() {
    assertFalse(MatrixQ.of(Tensors.fromString("{}")));
    assertTrue(MatrixQ.of(Tensors.fromString("{{}}")));
    assertTrue(MatrixQ.of(Tensors.fromString("{{}, {}}")));
  }

  public void testScalar() {
    assertFalse(MatrixQ.of(RealScalar.ONE));
    assertFalse(MatrixQ.of(ComplexScalar.I));
  }

  public void testVector() {
    assertFalse(MatrixQ.of(Tensors.vector(2, 3, 1)));
  }

  public void testMatrix() {
    assertTrue(MatrixQ.of(Tensors.fromString("{{1}}")));
    assertTrue(MatrixQ.of(Tensors.fromString("{{1, 1, 3}, {7, 2, 9}}")));
    assertFalse(MatrixQ.of(Tensors.fromString("{{1, 1}, {7, 2, 9}}")));
  }

  public void testMatrixSize() {
    assertTrue(MatrixQ.ofSize(Tensors.fromString("{{1}}"), 1, 1));
    assertFalse(MatrixQ.ofSize(Tensors.fromString("{{1}}"), 1, 2));
    assertFalse(MatrixQ.ofSize(Tensors.fromString("{{1}}"), 2, 1));
    assertTrue(MatrixQ.ofSize(Tensors.fromString("{{1, 1, 3}, {7, 2, 9}}"), 2, 3));
    MatrixQ.requireSize(Tensors.fromString("{{1, 1, 3}, {7, 2, 9}}"), 2, 3);
    assertFalse(MatrixQ.ofSize(Tensors.fromString("{{1, 1}, {7, 2, 9}}"), 2, 2));
    assertTrue(MatrixQ.ofSize(HilbertMatrix.of(3, 4), 3, 4));
    assertTrue(MatrixQ.ofSize(HilbertMatrix.of(2, 7), 2, 7));
    MatrixQ.requireSize(HilbertMatrix.of(2, 7), 2, 7);
    assertFalse(MatrixQ.ofSize(HilbertMatrix.of(2, 7), 2, 6));
    assertFalse(MatrixQ.ofSize(HilbertMatrix.of(2, 7), 3, 7));
  }

  public void testArrayWithDimensions() {
    Tensor tensor = Tensors.fromString("{{1, 2}, {3, {4}}, {5, 6}}");
    assertFalse(MatrixQ.ofSize(tensor, 3, 2));
  }

  public void testAd() {
    assertFalse(MatrixQ.of(Array.zeros(3, 3, 3)));
  }

  public void testElseThrow() {
    try {
      MatrixQ.require(Tensors.vector(1, 2, 3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRequireNullThrow() {
    try {
      MatrixQ.require(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testOfNullThrow() {
    try {
      MatrixQ.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRequireSize() {
    MatrixQ.requireSize(IdentityMatrix.of(3), 3, 3);
    try {
      MatrixQ.requireSize(IdentityMatrix.of(3), 3, 4);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
