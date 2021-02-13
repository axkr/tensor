// code by jph
package ch.ethz.idsc.tensor.nrm;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class NormalizeFailTest extends TestCase {
  public void testEmpty() {
    AssertFail.of(() -> VectorNorm2.NORMALIZE.apply(Tensors.empty()));
  }

  public void testZeros() {
    AssertFail.of(() -> VectorNorm2.NORMALIZE.apply(Array.zeros(10)));
  }

  public void testFail1() {
    TensorUnaryOperator normalize = VectorNorm1.NORMALIZE;
    AssertFail.of(() -> normalize.apply(Tensors.vector(0, 0, 0, 0)));
  }

  public void testNormalizePositiveInfinity() {
    Tensor vector = Tensors.of(DoubleScalar.POSITIVE_INFINITY, RealScalar.ONE);
    AssertFail.of(() -> VectorNorm2.NORMALIZE.apply(vector));
    AssertFail.of(() -> NormalizeUnlessZero.with(VectorNorm2::of).apply(vector));
  }

  public void testNormalizeNegativeInfinity() {
    Tensor vector = Tensors.of(DoubleScalar.NEGATIVE_INFINITY, RealScalar.ONE, DoubleScalar.POSITIVE_INFINITY);
    AssertFail.of(() -> VectorNorm2.NORMALIZE.apply(vector));
  }

  public void testNormalizeNaN() {
    Tensor vector = Tensors.of(RealScalar.ONE, DoubleScalar.INDETERMINATE, RealScalar.ONE);
    AssertFail.of(() -> VectorNorm2.NORMALIZE.apply(vector));
  }

  public void testScalarFail() {
    AssertFail.of(() -> VectorNorm2.NORMALIZE.apply(RealScalar.ONE));
  }

  public void testMatrixFail() {
    AssertFail.of(() -> VectorNorm2.NORMALIZE.apply(Tensors.fromString("{{1, 2}, {3, 4, 5}}")));
    AssertFail.of(() -> VectorNorm2.NORMALIZE.apply(HilbertMatrix.of(3)));
  }
}
