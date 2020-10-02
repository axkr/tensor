// code by jph
package ch.ethz.idsc.tensor.img;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.Random;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class ColorFormatTest extends TestCase {
  public void testRandom() {
    Random random = new SecureRandom();
    for (int index = 0; index < 10; ++index) {
      int red = random.nextInt(256);
      int green = random.nextInt(256);
      int blue = random.nextInt(256);
      int alpha = random.nextInt(256);
      Color color = new Color(red, green, blue, alpha);
      Tensor vector = ColorFormat.toVector(color);
      assertEquals(red, vector.Get(0).number());
      assertEquals(green, vector.Get(1).number());
      assertEquals(blue, vector.Get(2).number());
      assertEquals(alpha, vector.Get(3).number());
      Color color2 = ColorFormat.toColor(vector);
      assertEquals(color, color2);
      Tensor vector2 = ColorFormat.toVector(color2);
      assertEquals(vector, vector2);
    }
  }

  public void testFailValue() {
    ColorFormat.toColor(Tensors.vector(0, 0, 0, 255.9));
    AssertFail.of(() -> ColorFormat.toColor(Tensors.vector(0, 0, 0, 256)));
  }

  public void testFailLength() {
    AssertFail.of(() -> ColorFormat.toColor(Array.zeros(3)));
    AssertFail.of(() -> ColorFormat.toColor(Array.zeros(5)));
  }
}
