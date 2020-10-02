// code by jph
package ch.ethz.idsc.tensor.img;

import java.awt.Color;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class CyclicColorDataIndexedTest extends TestCase {
  public void testCustom() {
    Tensor tensor = Tensors.fromString("{{1, 2, 3, 4}, {5, 6, 7, 8}}");
    ColorDataIndexed colorDataIndexed = CyclicColorDataIndexed.of(tensor);
    assertEquals(colorDataIndexed.apply(RealScalar.of(1.9 - 20)), tensor.get(1));
    assertEquals(colorDataIndexed.apply(RealScalar.of(1.9)), tensor.get(1));
    assertEquals(colorDataIndexed.apply(RealScalar.of(3.9)), tensor.get(1));
    final Color ref0 = new Color(1, 2, 3, 4);
    assertEquals(colorDataIndexed.getColor(0), ref0);
    assertEquals(colorDataIndexed.getColor(2), ref0);
    assertEquals(colorDataIndexed.getColor(-2), ref0);
    assertEquals(colorDataIndexed.getColor(-12), ref0);
    final Color ref1 = new Color(5, 6, 7, 8);
    assertEquals(colorDataIndexed.getColor(1), ref1);
    assertEquals(colorDataIndexed.getColor(3), ref1);
    assertEquals(colorDataIndexed.getColor(-1), ref1);
    assertEquals(colorDataIndexed.getColor(-11), ref1);
  }

  public void testDerive() {
    Tensor tensor = Tensors.fromString("{{1, 2, 3, 4}, {5, 6, 7, 8}}");
    ColorDataIndexed colorDataIndexed = CyclicColorDataIndexed.of(tensor);
    colorDataIndexed = colorDataIndexed.deriveWithAlpha(255);
    final Color ref0 = new Color(1, 2, 3, 255);
    assertEquals(colorDataIndexed.getColor(0), ref0);
    final Color ref1 = new Color(5, 6, 7, 255);
    assertEquals(colorDataIndexed.getColor(1), ref1);
  }

  public void testFailEmpty() {
    AssertFail.of(() -> CyclicColorDataIndexed.of(Tensors.empty()));
  }

  public void testFailScalar() {
    AssertFail.of(() -> CyclicColorDataIndexed.of(RealScalar.ZERO));
  }

  public void testFailRGB() {
    Tensor tensor = Tensors.fromString("{{1, 2, 3}, {5, 6, 7}}");
    AssertFail.of(() -> CyclicColorDataIndexed.of(tensor));
  }
}
