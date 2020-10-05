// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class InverseFourierTest extends TestCase {
  public void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 0; n < 7; ++n)
      for (int count = 0; count < 10; ++count) {
        Tensor real = RandomVariate.of(distribution, 1 << n);
        Tensor imag = RandomVariate.of(distribution, 1 << n);
        Tensor vector = real.add(imag.multiply(ComplexScalar.I));
        Chop._10.requireClose(InverseFourier.of(Fourier.of(vector)), vector);
      }
  }

  public void testFailScalar() {
    AssertFail.of(() -> InverseFourier.of(RealScalar.ONE));
  }

  public void testFailEmpty() {
    AssertFail.of(() -> InverseFourier.of(Tensors.empty()));
  }

  public void testFailMatrix() {
    AssertFail.of(() -> InverseFourier.of(HilbertMatrix.of(4)));
  }
}
