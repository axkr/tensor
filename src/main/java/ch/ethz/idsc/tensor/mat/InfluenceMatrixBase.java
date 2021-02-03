// code by jph
package ch.ethz.idsc.tensor.mat;

import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** base for a class that implements the {@link InfluenceMatrix} interface */
/* package */ abstract class InfluenceMatrixBase implements InfluenceMatrix {
  @Override // from InfluenceMatrix
  public final Tensor leverages() {
    return Diagonal.of(matrix());
  }

  @Override // from InfluenceMatrix
  public final Tensor leverages_sqrt() {
    return leverages().map(Sqrt.FUNCTION);
  }

  @Override // from InfluenceMatrix
  public final Tensor residualMaker() {
    AtomicInteger atomicInteger = new AtomicInteger();
    // I-X^+.X is projector on ker X
    return Tensor.of(matrix().stream() //
        .map(Tensor::negate) // copy
        .map(row -> {
          int index = atomicInteger.getAndIncrement();
          row.set(scalar -> scalar.add(((Scalar) scalar).one()), index);
          return row; // by ref
        }));
  }

  @Override // from InfluenceMatrix
  public final Tensor kernel(Tensor vector) {
    return vector.subtract(image(vector));
  }

  @Override // from Object
  public final String toString() {
    return String.format("%s[%d]", InfluenceMatrix.class.getSimpleName(), length());
  }

  /** @return Length[design] */
  protected abstract int length();
}
