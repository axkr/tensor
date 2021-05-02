// code by jph
package ch.alpine.tensor.itp;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** suggested base class for implementations of {@link Interpolation} */
public abstract class AbstractInterpolation implements Interpolation {
  @Override // from Interpolation
  public final Scalar Get(Tensor index) {
    return (Scalar) get(index);
  }

  @Override // from Interpolation
  public final Scalar At(Scalar index) {
    return (Scalar) at(index);
  }
}
