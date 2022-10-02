// code by jph
package ch.alpine.tensor.prc;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

public interface RandomProcess {
  Scalar eval(TimeSeries timeSeries, Scalar x);

  Tensor path();
}
