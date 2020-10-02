// code by jph
package ch.ethz.idsc.tensor.red;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.lie.LeviCivitaTensor;
import ch.ethz.idsc.tensor.mat.FourierMatrix;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class FrobeniusTest extends TestCase {
  public void testVector() {
    Scalar norm = Frobenius.NORM.ofVector(Tensors.vector(3, 4));
    assertEquals(norm, RealScalar.of(5));
  }

  public void testBetween() {
    Tensor t1 = Tensors.fromString("{0, {1, 2}, 3}");
    Tensor t2 = Tensors.fromString("{2, {-1, 0}, 8}");
    Scalar d1 = Frobenius.between(t1, t2);
    Scalar d2 = Norm._2.between(Tensor.of(t1.flatten(-1)), Tensor.of(t2.flatten(-1)));
    assertEquals(d1, d2);
  }

  public void testMatrix() {
    Scalar norm = Frobenius.NORM.ofMatrix(IdentityMatrix.of(4));
    assertEquals(norm, RealScalar.of(2));
  }

  public void testMatrixComplex() {
    Scalar norm = Frobenius.NORM.ofMatrix(FourierMatrix.of(25));
    assertTrue(norm instanceof RealScalar);
  }

  public void testRank3() {
    Scalar expected = RealScalar.of(2.449489742783178);
    Scalar norm = Frobenius.of(LeviCivitaTensor.of(3));
    Chop._14.requireClose(norm, expected);
    Scalar scalar = Frobenius.of(Array.of(i -> RealScalar.ONE, 6));
    Chop._14.requireClose(scalar, expected);
  }

  public void testVectorFail() {
    AssertFail.of(() -> Frobenius.NORM.ofVector(RealScalar.ONE));
    AssertFail.of(() -> Frobenius.NORM.ofVector(HilbertMatrix.of(3, 4)));
  }

  public void testMatrixFail() {
    AssertFail.of(() -> Frobenius.NORM.ofMatrix(ComplexScalar.of(2, 3)));
    AssertFail.of(() -> Frobenius.NORM.ofMatrix(Tensors.vector(1, 2, 3)));
  }
}
