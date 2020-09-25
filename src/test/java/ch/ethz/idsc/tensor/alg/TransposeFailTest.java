// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class TransposeFailTest extends TestCase {
  public void testScalarFail() {
    Tensor v = DoubleScalar.NEGATIVE_INFINITY;
    try {
      Transpose.of(v);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Transpose.of(v, new Integer[] { 2 });
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testVectorFail() {
    try {
      Transpose.of(Tensors.vector(2, 3, 4, 5));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testEmpty2() {
    Tensor empty2 = Tensors.fromString("{{}, {}}");
    assertEquals(Transpose.of(empty2), Tensors.empty());
    try {
      Transpose.of(Transpose.of(empty2));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRankFail() {
    Transpose.of(Array.zeros(1, 3, 2), 1, 2, 0);
    try {
      Transpose.of(Array.zeros(3, 3, 3), 1, 0);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Transpose.of(Array.zeros(3, 3, 2), 3, 2, 1, 0);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFail() {
    try {
      Transpose.nonArray(Array.zeros(2, 3), 1);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Transpose.nonArray(Array.zeros(2, 3), 2, 0);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      Transpose.nonArray(Array.zeros(2, 3), 0, -1);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFail2() {
    try {
      Transpose.of(Tensors.fromString("{{1, 2}, {3, 4, 5}}"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
