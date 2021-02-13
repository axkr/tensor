// code by jph
package ch.ethz.idsc.tensor.nrm;

import java.io.Serializable;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Power;

/** p-Norm for vectors as well as schatten norm
 * 
 * implementation consistent with Mathematica
 * 
 * Important: For the special cases
 * <ul>
 * <li>p==1,
 * <li>p==2, or
 * <li>p==Infinity
 * </ul> */
public class VectorNorm implements Serializable {
  private static final long serialVersionUID = -2668949364257998603L;

  /** Hint: for enhanced precision, use p as instance of {@link RationalScalar} if possible
   * 
   * @param p exponent greater or equals 1
   * @return
   * @throws Exception if p is less than 1 */
  public static VectorNorm with(Scalar p) {
    return new VectorNorm(p);
  }

  /** @param p exponent greater or equals 1
   * @return
   * @throws Exception if p is less than 1 */
  public static VectorNorm with(Number p) {
    return with(RealScalar.of(p));
  }

  /***************************************************/
  private final ScalarUnaryOperator p_power;
  private final Scalar p;
  private final Scalar p_reciprocal;

  // constructor also called from SchattenNorm
  /* package */ VectorNorm(Scalar p) {
    if (Scalars.lessThan(p, RealScalar.ONE))
      throw TensorRuntimeException.of(p);
    p_power = Power.function(p);
    this.p = p;
    p_reciprocal = p.reciprocal();
  }

  public Scalar of(Tensor vector) {
    return Power.of(vector.stream() //
        .map(Scalar.class::cast) //
        .map(Abs.FUNCTION) //
        .map(p_power) //
        .reduce(Scalar::add).get(), //
        p_reciprocal);
  }

  @Override
  public String toString() {
    return String.format("%s[p=%s]", getClass().getSimpleName(), p);
  }
}
