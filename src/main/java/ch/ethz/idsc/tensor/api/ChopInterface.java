// code by jph
package ch.ethz.idsc.tensor.api;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Chop;

/** interface may be implemented by {@link Scalar}
 * to support the chop towards zero function
 * 
 * the interface is optional: the identity function is
 * used if the interface is not implemented. */
@FunctionalInterface
public interface ChopInterface {
  /** @param threshold
   * @return {@link Scalar#zero()} if Scalar has numeric precision and
   * absolute value is strictly below threshold defined by {@link Chop#threshold()} */
  Scalar chop(Chop chop);
}
