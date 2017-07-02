// code by jph
package ch.ethz.idsc.tensor.red;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.AbsSquared;

/* package */ class Norm2Squared extends RankAdapter<Scalar> {
  @Override
  public Scalar ofScalar(Scalar scalar) {
    return AbsSquared.FUNCTION.apply(scalar);
  }

  @Override
  public Scalar ofVector(Tensor vector) {
    return vector.flatten(0) //
        .map(Scalar.class::cast) //
        .map(AbsSquared.FUNCTION) //
        .reduce(Scalar::add) //
        .get();
  }

  @Override
  public Scalar ofMatrix(Tensor matrix) {
    Scalar value = Norm._2.of(matrix);
    return value.multiply(value);
  }
}
