// code by jph
package ch.alpine.tensor.ext;

import java.util.Collections;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class ArgMinTest extends TestCase {
  public void testDocumentation() {
    assertEquals(ArgMin.of(Tensors.vector(3, 4, 1, 2, 3)), 2);
    assertEquals(ArgMin.of(Tensors.vector(1, 4, 1, 2, 3)), 0);
  }

  public void testMin() {
    assertEquals(1, ArgMin.of(Tensors.vectorDouble(3., 0.6, 8, 0.6, 100)));
    assertEquals(2, ArgMin.of(Tensors.vectorDouble(3, 3., 0.6, 8, 0.6, 8)));
  }

  public void testMinComparatorIncr() {
    assertEquals(1, ArgMin.of(Tensors.vectorDouble(3., 0.6, 8, 0.6, 100)));
    assertEquals(2, ArgMin.of(Tensors.vectorDouble(3, 3., 0.6, 8, 0.6, 8)));
  }

  public void testMinComparatorDecr() {
    assertEquals(4, ArgMin.of(Tensors.vectorDouble(3., 0.6, 8, 0.6, 100), Collections.reverseOrder()));
    assertEquals(3, ArgMin.of(Tensors.vectorDouble(3, 3., 0.6, 8, 0.6, 8), Collections.reverseOrder()));
  }

  public void testComparatorNullFail() {
    AssertFail.of(() -> ArgMin.of(Tensors.empty(), null));
  }

  public void testScalar() {
    AssertFail.of(() -> ArgMin.of(RealScalar.ONE));
  }

  public void testFailMatrix() {
    AssertFail.of(() -> ArgMin.of(HilbertMatrix.of(6)));
  }
}