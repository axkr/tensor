// code by jph
package ch.ethz.idsc.tensor.sca;

import java.util.function.Function;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** http://www.milefoot.com/math/complex/functionsofi.htm
 * 
 * inspired by
 * <a href="https://reference.wolfram.com/language/ref/ArcCosh.html">ArcCosh</a> */
public enum ArcCosh implements Function<Scalar, Scalar> {
  function;
  // ---
  @Override
  public Scalar apply(Scalar scalar) {
    Scalar x2_o = Sqrt.function.apply(scalar.multiply(scalar).subtract(RealScalar.ONE));
    return Log.function.apply(scalar.add(x2_o)); // add or subtract
  }

  /** @param tensor
   * @return tensor with all scalars replaced with their arc cosh */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(ArcCosh.function);
  }
}
