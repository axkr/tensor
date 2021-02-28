// code by jph
package ch.ethz.idsc.tensor.sca;

import java.util.Objects;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.api.NInterface;

/* package */ class NDouble extends N {
  static final N INSTANCE = new NDouble();

  private NDouble() {
    // ---
  }

  @Override
  public Scalar apply(Scalar scalar) {
    if (scalar instanceof NInterface) {
      NInterface nInterface = (NInterface) scalar;
      return nInterface.n();
    }
    return Objects.requireNonNull(scalar);
  }
}
