// code by jph
package ch.ethz.idsc.tensor.api;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** interface may be implemented by {@link Scalar} to support
 * the computation of the square root in {@link Sqrt} */
@FunctionalInterface
public interface SqrtInterface {
  /** @return scalar that satisfies scalar * scalar == this, or
   * @throws Exception if such an element does not exist */
  Scalar sqrt();
}
