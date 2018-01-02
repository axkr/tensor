// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.qty.Quantity;

/** Clip encodes a closed interval in the ordered set of real numbers.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Clip.html">Clip</a> */
public class Clip implements ScalarUnaryOperator {
  private static final Clip UNIT = function(0, 1);
  private static final Clip ABSOLUTE_ONE = function(-1, 1);

  /** clip function that maps to the unit interval [0, 1] */
  public static Clip unit() {
    return UNIT;
  }

  /** clip function that clips scalars to the interval [-1, 1] */
  public static Clip absoluteOne() {
    return ABSOLUTE_ONE;
  }

  /** @param min
   * @param max
   * @return function that clips the input to the closed interval [min, max] */
  public static Clip function(Number min, Number max) {
    return function(RealScalar.of(min), RealScalar.of(max));
  }

  /** @param min
   * @param max
   * @return function that clips the input to the closed interval [min, max] */
  public static Clip function(Scalar min, Scalar max) {
    if (Scalars.lessThan(max, min))
      throw TensorRuntimeException.of(min, max);
    return new Clip(min, max);
  }

  // ---
  private final Scalar min;
  private final Scalar max;
  private final Scalar width;

  private Clip(Scalar min, Scalar max) {
    this.min = min;
    this.max = max;
    width = max.subtract(min);
  }

  @Override
  public Scalar apply(Scalar scalar) {
    boolean cmpMin = Scalars.lessThan(scalar, min);
    boolean cmpMax = Scalars.lessThan(max, scalar);
    if (cmpMin)
      return min;
    if (cmpMax)
      return max;
    return scalar;
  }

  @SuppressWarnings("unchecked")
  public <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(this);
  }

  /** @param scalar
   * @return true if given scalar is invariant under this clip */
  public boolean isInside(Scalar scalar) {
    return apply(scalar).equals(scalar);
  }

  /** @param scalar
   * @return true if given scalar is invariant under this clip */
  public void isInsideElseThrow(Scalar scalar) {
    if (isOutside(scalar))
      throw TensorRuntimeException.of(min, max, scalar);
  }

  /** @param scalar
   * @return true if given scalar is not invariant under this clip */
  public boolean isOutside(Scalar scalar) {
    return !isInside(scalar);
  }

  /** If max - min > 0, the given scalar is divided by width.
   * If max == min the result is always RealScalar.ZERO.
   * 
   * <p>When using Clip with {@link Quantity}s, all three scalars
   * min, max, and the given scalar, must be of identical unit.
   * Rescale always returns a {@link RealScalar}.
   * 
   * @param scalar
   * @return value in interval [0, 1] relative to position of scalar in clip interval.
   * If the clip interval width is zero, the return value is zero.
   * If the given scalar is outside the clip interval, the return value is outside [0, 1]. */
  public Scalar rescale(Scalar scalar) {
    return Scalars.isZero(width) ? RealScalar.ZERO : apply(scalar).subtract(min).divide(width);
  }

  /** @return lower bound of clip interval */
  public Scalar min() {
    return min;
  }

  /** @return upper bound of clip interval */
  public Scalar max() {
    return max;
  }

  /** @return difference between upper and lower bound of clip interval */
  public Scalar width() {
    return width;
  }
}