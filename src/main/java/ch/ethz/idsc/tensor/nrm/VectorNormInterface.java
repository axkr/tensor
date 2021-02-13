// code by jph
package ch.ethz.idsc.tensor.nrm;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** interface defines a norm for vectors */
@FunctionalInterface
public interface VectorNormInterface {
  /** @param vector
   * @return norm of vector
   * @throws Exception if input is not a vector */
  Scalar ofVector(Tensor vector);
}
