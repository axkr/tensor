// code by jph
package ch.alpine.tensor.io;

import java.io.File;
import java.util.concurrent.TimeUnit;

import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.usr.TestFile;
import junit.framework.TestCase;

public class AnimationWriterTest extends TestCase {
  public void testColor() throws Exception {
    File file = TestFile.withExtension("gif");
    try (AnimationWriter animationWriter = new GifAnimationWriter(file, 100, TimeUnit.MILLISECONDS)) {
      animationWriter.write(Array.zeros(3, 4));
      animationWriter.write(Array.zeros(3, 4));
    }
    assertTrue(file.isFile());
    assertTrue(file.delete());
  }

  public void testFailExtension() {
    try (AnimationWriter animationWriter = new GifAnimationWriter(null, 100, TimeUnit.MILLISECONDS)) { // extension unknown
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
