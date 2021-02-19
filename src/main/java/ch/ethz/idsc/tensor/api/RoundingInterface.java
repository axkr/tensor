// code by jph
package ch.ethz.idsc.tensor.api;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Ceiling;
import ch.ethz.idsc.tensor.sca.Floor;
import ch.ethz.idsc.tensor.sca.Round;

/** An implementation of {@link Scalar} may implement {@link RoundingInterface}
 * to support the use of {@link Ceiling}, {@link Floor}, and {@link Round}.
 * 
 * Examples of are {@link RealScalar}, and {@link Quantity}. */
public interface RoundingInterface {
  /** @return equal or higher scalar with integer components */
  Scalar ceiling();

  /** @return equal or lower scalar with integer components */
  Scalar floor();

  /** @return closest scalar with integer components */
  Scalar round();
}
