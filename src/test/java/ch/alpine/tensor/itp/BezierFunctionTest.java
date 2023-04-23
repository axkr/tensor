// code by jph
package ch.alpine.tensor.itp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactTensorQ;

class BezierFunctionTest {
  @Test
  void testSimple() {
    Tensor control = Tensors.fromString("{{0, 1}, {1, 0}, {2, 1}}");
    ScalarTensorFunction scalarTensorFunction = new BezierFunction(LinearBinaryAverage.INSTANCE, control);
    Tensor tensor = scalarTensorFunction.apply(RationalScalar.of(1, 4));
    assertEquals(tensor, Tensors.fromString("{1/2, 5/8}"));
    ExactTensorQ.require(tensor);
  }

  @Test
  void testFailEmpty() {
    assertThrows(Exception.class, () -> new BezierFunction(LinearBinaryAverage.INSTANCE, Tensors.empty()));
  }

  @Test
  void testFailScalar() {
    assertThrows(Exception.class, () -> new BezierFunction(LinearBinaryAverage.INSTANCE, RealScalar.ZERO));
  }

  @Test
  void testFailNull() {
    assertThrows(Exception.class, () -> new BezierFunction(null, Tensors.vector(1, 2, 3)));
  }
}
