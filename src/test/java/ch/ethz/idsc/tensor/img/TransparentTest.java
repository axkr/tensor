// code by jph
package ch.ethz.idsc.tensor.img;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.alg.Array;
import junit.framework.TestCase;

public class TransparentTest extends TestCase {
  public void testSimple() {
    assertEquals(Transparent.rgba(), Array.zeros(4));
    Transparent.rgba().set(RealScalar.ONE, 3);
    assertEquals(Transparent.rgba(), Array.zeros(4));
  }

  public void testPackageVisibility() {
    assertFalse(Modifier.isPublic(Transparent.class.getModifiers()));
  }
}
