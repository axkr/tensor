// code by jph
package ch.alpine.tensor.api;

import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;

/** a {@link Scalar} may implement the interface to signal that the value is in exact precision.
 * For example, a {@link RationalScalar} implements the function isExactNumber() to return true.
 * 
 * <p>a {@link Scalar} that does not implement {@link ExactScalarQInterface} is assumed to
 * <em>not</em> represent an exact quantity by {@link ExactScalarQ}. */
@FunctionalInterface
public interface ExactScalarQInterface {
  /** @return true, if scalar is encoded in exact precision */
  boolean isExactScalar();
}
