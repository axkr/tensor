// code by jph
package ch.ethz.idsc.tensor.api;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Arg;

/** interface may be implemented by {@link Scalar}
 * to support the computation of the complex argument in {@link Arg} */
@FunctionalInterface
public interface ArgInterface {
  /** @return argument of this number in the complex plane */
  Scalar arg();
}
