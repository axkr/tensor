// code by jph
package ch.alpine.tensor.fft;

import java.util.Arrays;
import java.util.stream.IntStream;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.io.ImageFormat;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.win.BlackmanWindow;
import ch.alpine.tensor.sca.win.DirichletWindow;
import ch.alpine.tensor.sca.win.GaussianWindow;
import ch.alpine.tensor.sca.win.ParzenWindow;
import ch.alpine.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class TensorSpectrogramTest extends TestCase {
  public void testDefault() {
    Tensor vector = Tensor.of(IntStream.range(0, 2000) //
        .mapToDouble(i -> Math.cos(i * 0.25 + (i / 20.0) * (i / 20.0))) //
        .mapToObj(RealScalar::of));
    Tensor image = TensorSpectrogram.of(vector, DirichletWindow.FUNCTION, ColorDataGradients.VISIBLE_SPECTRUM);
    ImageFormat.of(image);
    assertEquals(Dimensions.of(image), Arrays.asList(32, 93, 4));
    assertEquals(Dimensions.of(SpectrogramArray.half_abs(vector, DirichletWindow.FUNCTION)), Arrays.asList(32, 93));
    Tensor tensor = SpectrogramArray.of(vector).map(Abs.FUNCTION);
    assertEquals(Dimensions.of(tensor), Arrays.asList(93, 64));
  }

  public void testQuantity() {
    Tensor signal = Tensors.vector(1, 2, 1, 4, 3, 2, 3, 4, 3, 4);
    Tensor vector = signal.map(s -> Quantity.of(s, "m"));
    Tensor array1 = TensorSpectrogram.of(signal, GaussianWindow.FUNCTION, ColorDataGradients.VISIBLE_SPECTRUM);
    Tensor array2 = TensorSpectrogram.of(vector, GaussianWindow.FUNCTION, ColorDataGradients.VISIBLE_SPECTRUM);
    assertEquals(array1, array2);
  }

  public void testScalarFail() {
    AssertFail.of(() -> TensorSpectrogram.of(RealScalar.ONE, ParzenWindow.FUNCTION, ColorDataGradients.VISIBLE_SPECTRUM));
  }

  public void testMatrixFail() {
    AssertFail.of(() -> TensorSpectrogram.of(HilbertMatrix.of(32), BlackmanWindow.FUNCTION, ColorDataGradients.VISIBLE_SPECTRUM));
  }
}