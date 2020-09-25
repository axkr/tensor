// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.MatrixQ;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.lie.LeviCivitaTensor;
import junit.framework.TestCase;

public class SquareMatrixQTest extends TestCase {
  public void testMatrix() {
    assertTrue(SquareMatrixQ.of(IdentityMatrix.of(10)));
    assertTrue(SquareMatrixQ.of(IdentityMatrix.of(10).unmodifiable()));
    assertFalse(SquareMatrixQ.of(Array.zeros(3, 4)));
  }

  public void testOthers() {
    assertFalse(SquareMatrixQ.of(UnitVector.of(10, 3)));
    assertFalse(SquareMatrixQ.of(LeviCivitaTensor.of(3)));
    assertFalse(SquareMatrixQ.of(RealScalar.ONE));
  }

  public void testEmpty() {
    assertFalse(SquareMatrixQ.of(Tensors.empty()));
  }

  public void testEmptyNested() {
    Tensor tensor = Tensors.fromString("{{}}");
    assertTrue(MatrixQ.of(tensor));
    assertFalse(SquareMatrixQ.of(tensor));
    assertTrue(SquareMatrixQ.of(Tensors.fromString("{{1}}")));
  }

  public void testRequire() {
    SquareMatrixQ.require(IdentityMatrix.of(10));
  }

  public void testNonArray() {
    assertFalse(SquareMatrixQ.of(Tensors.fromString("{{1, 2}, {{3}, 4}}")));
  }

  public void testRequireScalar() {
    try {
      SquareMatrixQ.require(RealScalar.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRequireVector() {
    try {
      SquareMatrixQ.require(Range.of(3, 10));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRequireMatrixNonSquare() {
    assertFalse(SquareMatrixQ.of(HilbertMatrix.of(3, 4)));
    try {
      SquareMatrixQ.require(HilbertMatrix.of(3, 4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRequireRank3() {
    assertFalse(SquareMatrixQ.of(LeviCivitaTensor.of(3)));
    try {
      SquareMatrixQ.require(LeviCivitaTensor.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
