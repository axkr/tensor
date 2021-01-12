// code by jph
package ch.ethz.idsc.tensor.red;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class VectorNormTest extends TestCase {
  public void testOdd() {
    Tensor tensor = Tensors.vector(2.3, 1.0, 3.2);
    Scalar n = VectorNorm.with(1.5).ofVector(tensor);
    // 4.7071
    assertEquals(n, RealScalar.of(4.707100665786122));
  }

  public void testNormP() {
    Scalar n = VectorNorm.with(1.23).ofVector(Tensors.vector(1, 2, 3));
    assertEquals(n, RealScalar.of(4.982125211204371));
  }

  public void testNormalize() {
    VectorNormInterface vni = VectorNorm.with(2.6);
    Tensor nrm = Normalize.with(vni::ofVector).apply(Tensors.vector(1, 2, 3));
    Chop._15.requireClose(vni.ofVector(nrm), RealScalar.ONE);
  }

  public void testNormalize2() {
    Distribution distribution = NormalDistribution.standard();
    VectorNormInterface vni = VectorNorm.with(3.4);
    Tensor vector = RandomVariate.of(distribution, 1000);
    Tensor result = Normalize.with(vni::ofVector).apply(vector);
    Scalar norm = vni.ofVector(result);
    Chop._15.requireClose(norm, RealScalar.ONE);
  }

  public void testQuantity1() {
    Scalar qs1 = Quantity.of(-3, "m");
    Scalar qs2 = Quantity.of(4, "m");
    Tensor vec = Tensors.of(qs1, RealScalar.ZERO, qs2);
    Scalar lhs = VectorNorm.with(RationalScalar.of(7, 3)).ofVector(vec);
    Scalar rhs = Quantity.of(4.774145448367236, "m");
    Chop._13.requireClose(lhs, rhs);
  }

  public void testQuantity2() {
    Scalar qs1 = Quantity.of(-3, "m");
    Scalar qs2 = Quantity.of(4, "m");
    Tensor vec = Tensors.of(qs1, RealScalar.ZERO, qs2);
    Scalar lhs = VectorNorm.with(Math.PI).ofVector(vec); // the result has unit [m^1.0]
    Scalar rhs = Quantity.of(4.457284396597481, "m");
    Chop._13.requireClose(lhs, rhs);
  }

  public void testToString() {
    VectorNormInterface vectorNormInterface = VectorNorm.with(3);
    System.out.println(vectorNormInterface.toString());
  }

  public void testNormPFail() {
    AssertFail.of(() -> VectorNorm.with(0.99));
  }

  public void testMatrixFail() {
    VectorNormInterface vectorNormInterface = VectorNorm.with(2.6);
    AssertFail.of(() -> vectorNormInterface.ofVector(IdentityMatrix.of(2)));
  }

  public void testScalarFail() {
    VectorNormInterface vectorNormInterface = VectorNorm.with(2.6);
    AssertFail.of(() -> vectorNormInterface.ofVector(RealScalar.of(12)));
  }
}
