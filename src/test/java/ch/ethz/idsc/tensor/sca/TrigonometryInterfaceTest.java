// code by jph
package ch.ethz.idsc.tensor.sca;

import java.util.function.Function;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.num.GaussScalar;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.UnitSystem;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class TrigonometryInterfaceTest extends TestCase {
  private static void _check(Scalar value, ScalarUnaryOperator suo, Function<Double, Double> f) {
    Scalar scalar = UnitSystem.SI().apply(Quantity.of(value, "rad"));
    Scalar result = suo.apply(scalar);
    Scalar actual = RealScalar.of(f.apply(value.number().doubleValue()));
    assertEquals(result, actual);
  }

  public void testQuantity() {
    for (Tensor _value : Tensors.vector(-2.323, -1, -0.3, 0, 0.2, 1.2, 3., 4.456)) {
      Scalar value = _value.Get();
      _check(value, Sin::of, Math::sin);
      _check(value, Cos::of, Math::cos);
      _check(value, Sinh::of, Math::sinh);
      _check(value, Cosh::of, Math::cosh);
    }
  }

  public void testQuantityDegree() {
    Scalar scalar = UnitSystem.SI().apply(Quantity.of(180, "deg"));
    Chop._13.requireClose(Sin.of(scalar), RealScalar.ZERO);
    Chop._13.requireClose(Cos.of(scalar), RealScalar.ONE.negate());
  }

  public void testFails() {
    AssertFail.of(() -> Sin.of(Quantity.of(1.2, "m")));
    AssertFail.of(() -> Sin.of(GaussScalar.of(2, 7)));
  }
}
