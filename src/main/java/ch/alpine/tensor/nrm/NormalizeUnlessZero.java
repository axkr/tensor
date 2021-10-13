// code by jph
package ch.alpine.tensor.nrm;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** @throws Exception if input is not a vector, or is empty
 * @throws Exception if vector contains Infinity, or NaN */
public class NormalizeUnlessZero implements TensorUnaryOperator {
  /** Example:
   * <pre>
   * NormalizeUnlessZero.with(Vector2Norm::of)
   * </pre>
   * 
   * @param tensorScalarFunction
   * @return operator that normalizes vectors using given tensorScalarFunction unless given vector has length 0 */
  public static TensorUnaryOperator with(TensorScalarFunction tensorScalarFunction) {
    return new NormalizeUnlessZero(tensorScalarFunction);
  }

  // ---
  private final TensorScalarFunction tensorScalarFunction;
  private final Normalize normalize;

  private NormalizeUnlessZero(TensorScalarFunction tensorScalarFunction) {
    this.tensorScalarFunction = tensorScalarFunction;
    normalize = (Normalize) Normalize.with(tensorScalarFunction);
  }

  @Override
  public Tensor apply(Tensor vector) {
    Scalar norm = tensorScalarFunction.apply(vector); // throws exception if input is not a vector
    return Scalars.isZero(norm) //
        ? vector.copy()
        : normalize.normalize(vector, norm);
  }

  @Override // from Object
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), tensorScalarFunction);
  }
}
