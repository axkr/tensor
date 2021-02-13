// code by jph
package ch.ethz.idsc.tensor.red;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.nrm.Normalize;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sqrt;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class StandardDeviationTest extends TestCase {
  public void testSimple() {
    Scalar scalar = StandardDeviation.ofVector(Tensors.vector(1, 2, 6, 3, -2, 3, 10));
    assertEquals(scalar, Sqrt.of(RationalScalar.of(102, 7)));
  }

  public void testNormalize() {
    TensorUnaryOperator tensorUnaryOperator = Normalize.with(StandardDeviation::ofVector);
    Tensor tensor = Tensors.vector(1, 5, 3, 7, 5, 2);
    Tensor result = tensorUnaryOperator.apply(tensor);
    Chop._14.requireClose(StandardDeviation.ofVector(result), RealScalar.ONE);
  }

  public void testScalarFail() {
    AssertFail.of(() -> StandardDeviation.ofVector(RealScalar.ONE));
  }

  public void testMatrixFail() {
    AssertFail.of(() -> StandardDeviation.ofVector(HilbertMatrix.of(3)));
  }
}
