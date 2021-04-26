// code by jph
package ch.ethz.idsc.tensor.fft;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Entrywise;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class FourierTest extends TestCase {
  public void test2() {
    Tensor vector = Tensors.fromString("{1 + 2*I, 3 + 11*I}");
    Tensor expect = Tensors.fromString("{2.828427124746190 + 9.19238815542512*I, -1.414213562373095 - 6.36396103067893*I}");
    Tolerance.CHOP.requireClose(Fourier.of(vector), expect);
    Tolerance.CHOP.requireClose(InverseFourier.of(Fourier.of(vector)), vector);
  }

  public void test2Quantity() {
    Tensor vector = Tensors.fromString("{1 + 2*I[m], 3 + 11*I[m]}");
    Tensor expect = Tensors.fromString("{2.828427124746190 + 9.19238815542512*I[m], -1.414213562373095 - 6.36396103067893*I[m]}");
    Tolerance.CHOP.requireClose(Fourier.of(vector), expect);
    Tolerance.CHOP.requireClose(InverseFourier.of(Fourier.of(vector)), vector);
  }

  public void test4() {
    Tensor vector = Tensors.vector(1, 2, 0, 0);
    Tensor tensor = Fourier.of(vector);
    Tensor expect = Tensors.fromString("{1.5, 0.5 + I, -0.5, 0.5 - I}");
    Tolerance.CHOP.requireClose(FourierMatrix.of(vector.length()).dot(vector), expect);
    Tolerance.CHOP.requireClose(vector.dot(FourierMatrix.of(vector.length())), expect);
    Tolerance.CHOP.requireClose(tensor, expect);
    Tensor backed = Fourier.of(expect);
    Tolerance.CHOP.requireClose(backed, Tensors.vector(1, 0, 0, 2));
  }

  public void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 0; n < 7; ++n)
      for (int count = 0; count < 10; ++count) {
        Tensor vector = Entrywise.with(ComplexScalar::of).apply( //
            RandomVariate.of(distribution, 1 << n), //
            RandomVariate.of(distribution, 1 << n));
        Tensor result = Fourier.of(vector);
        Tensor dotmat = vector.dot(FourierMatrix.of(vector.length()));
        Tolerance.CHOP.requireClose(dotmat, result);
      }
  }

  public void testFailScalar() {
    AssertFail.of(() -> Fourier.of(RealScalar.ONE));
  }

  public void testFailEmpty() {
    AssertFail.of(() -> Fourier.of(Tensors.empty()));
  }

  public void test3Fail() {
    Tensor vector = Tensors.vector(1, 2, 0);
    AssertFail.of(() -> Fourier.of(vector));
  }

  public void testFailMatrix() {
    AssertFail.of(() -> Fourier.of(HilbertMatrix.of(4)));
  }
}
