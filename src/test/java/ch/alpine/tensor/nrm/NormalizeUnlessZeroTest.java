// code by jph
package ch.alpine.tensor.nrm;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class NormalizeUnlessZeroTest extends TestCase {
  public void testNormalizeNaN() {
    Tensor vector = Tensors.of(RealScalar.ONE, DoubleScalar.INDETERMINATE, RealScalar.ONE);
    AssertFail.of(() -> NormalizeUnlessZero.with(Vector2Norm::of).apply(vector));
  }

  public void testNormalizeTotal() {
    TensorUnaryOperator tensorUnaryOperator = NormalizeUnlessZero.with(Total::ofVector);
    assertTrue(tensorUnaryOperator.toString().startsWith("NormalizeUnlessZero"));
    Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(-1, 3, 2));
    assertEquals(tensor, Tensors.fromString("{-1/4, 3/4, 1/2}"));
    Tensor vector = Tensors.vector(-1, 3, -2);
    Tensor result = tensorUnaryOperator.apply(vector);
    assertEquals(vector, result);
    ExactTensorQ.require(result);
  }

  public void testQuantity() {
    TensorUnaryOperator tuo = NormalizeUnlessZero.with(Vector2Norm::of);
    Tensor one = Tensors.of(Quantity.of(2, "m"));
    assertEquals(tuo.apply(one), Tensors.vector(1));
    Tensor zer = Tensors.of(Quantity.of(0, "m"));
    assertEquals(tuo.apply(zer), Array.zeros(1));
  }

  public void testUnitless() {
    TensorUnaryOperator tuo = NormalizeUnlessZero.with(Vector2Norm::of);
    Tensor one = Tensors.of(Quantity.of(0, "m"), Quantity.of(0, "m"));
    assertEquals(tuo.apply(one), Array.zeros(2));
  }

  public void testMixedUnit1Fail() {
    TensorUnaryOperator tuo = NormalizeUnlessZero.with(Vector2Norm::of);
    Tensor one = Tensors.of(Quantity.of(0, "m"), Quantity.of(1, "s"));
    AssertFail.of(() -> tuo.apply(one));
  }

  public void testMixedUnit2Fail() {
    TensorUnaryOperator tuo = NormalizeUnlessZero.with(Vector2Norm::of);
    Tensor one = Tensors.of(Quantity.of(0, "m"), Quantity.of(0, "s"));
    AssertFail.of(() -> tuo.apply(one));
  }
}
