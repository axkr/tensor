// code by jph
package ch.ethz.idsc.tensor.io;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.itp.Interpolation;
import ch.ethz.idsc.tensor.itp.LinearInterpolation;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class ResourceDataTest extends TestCase {
  private static void _checkColorscheme(Interpolation interpolation) {
    AssertFail.of(() -> interpolation.get(Tensors.vector(256)));
  }

  public void testColorschemeClassic() {
    Tensor tensor = ResourceData.of("/colorscheme/classic.csv");
    assertNotNull(tensor);
    assertEquals(Dimensions.of(tensor), Arrays.asList(256, 4));
    Interpolation interpolation = LinearInterpolation.of(tensor);
    assertEquals(interpolation.get(Tensors.vector(255)), Tensors.vector(255, 237, 237, 255));
    _checkColorscheme(interpolation);
  }

  public void testHue() {
    Tensor tensor = ResourceData.of("/colorscheme/hue.csv");
    assertNotNull(tensor);
    assertEquals(Dimensions.of(tensor), Arrays.asList(7, 4));
    Interpolation interpolation = LinearInterpolation.of(tensor);
    assertEquals(interpolation.get(Tensors.vector(0)), Tensors.vector(255, 0, 0, 255));
    _checkColorscheme(interpolation);
  }

  public void testPrimes() {
    Tensor primes = ResourceData.of("/number/primes.vector");
    List<Integer> dimensions = Dimensions.of(primes);
    assertEquals(dimensions.size(), 1);
    assertTrue(500 < dimensions.get(0));
    assertEquals(primes.Get(5), Scalars.fromString("13"));
  }

  public void testPrimesLines() {
    Tensor linesp = Tensor.of(ResourceData.lines("/number/primes.vector").stream().map(Scalars::fromString));
    Tensor vector = ResourceData.of("/number/primes.vector");
    assertEquals(linesp, vector);
  }

  public void testCsvGz() {
    Tensor actual = ResourceData.of("/io/mathematica23.csv.gz");
    Tensor expected = Tensors.fromString("{{123/875+I, 9.3}, {-9, 5/8123123123123123, 1010101}}");
    assertEquals(expected, actual);
  }

  public void testBufferedImagePng() {
    BufferedImage bufferedImage = ResourceData.bufferedImage("/io/image/rgba15x33.png");
    assertEquals(bufferedImage.getWidth(), 15);
    assertEquals(bufferedImage.getHeight(), 33);
    assertEquals(bufferedImage.getType(), BufferedImage.TYPE_4BYTE_ABGR);
  }

  public void testBufferedImageJpg() {
    BufferedImage bufferedImage = ResourceData.bufferedImage("/io/image/rgb15x33.jpg");
    assertEquals(bufferedImage.getWidth(), 15);
    assertEquals(bufferedImage.getHeight(), 33);
    assertEquals(bufferedImage.getType(), BufferedImage.TYPE_3BYTE_BGR);
  }

  public void testJpg() {
    Tensor image = ResourceData.of("/io/image/rgb15x33.jpg");
    assertEquals(Dimensions.of(image), Arrays.asList(33, 15, 4));
  }

  public void testBufferedImageBmp() {
    BufferedImage bufferedImage = ResourceData.bufferedImage("/io/image/rgb7x11.bmp");
    assertEquals(bufferedImage.getWidth(), 7);
    assertEquals(bufferedImage.getHeight(), 11);
    assertEquals(bufferedImage.getType(), BufferedImage.TYPE_3BYTE_BGR);
  }

  public void testBufferedImageBmpNull() {
    assertNull(ResourceData.bufferedImage("/doesnotexist.jpg"));
  }

  public void testBmp() {
    Tensor image = ResourceData.of("/io/image/rgb7x11.bmp");
    assertEquals(Dimensions.of(image), Arrays.asList(11, 7, 4));
    assertEquals(image.get(10, 4), Tensors.vector(0, 7, 95, 255));
  }

  public void testFailNull() {
    assertNull(ResourceData.of("/number/exists.fail"));
    assertNull(ResourceData.of("/number/exists.fail.bmp"));
  }

  public void testObjectNull() {
    assertNull(ResourceData.object("/number/exists.fail"));
  }

  public void testPropertiesFailNull() {
    assertNull(ResourceData.properties("/number/exists.properties"));
  }

  public void testUnknownExtension() {
    assertNull(ResourceData.of("/io/extension.unknown"));
  }

  public void testCorruptContent() {
    assertNull(ResourceData.of("/io/corrupt.png"));
  }

  public void testLines() {
    List<String> lines = ResourceData.lines("/io/basic.mathematica");
    assertEquals(lines.size(), 7);
  }

  public void testLinesNull() {
    List<String> lines = ResourceData.lines("/io/doesnotexist");
    assertNull(lines);
  }
}
