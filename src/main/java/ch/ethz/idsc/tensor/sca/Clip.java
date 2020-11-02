// code by jph
package ch.ethz.idsc.tensor.sca;

import java.util.Objects;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.qty.Quantity;

/** Clip encodes a closed interval in the ordered set of real numbers.
 * 
 * <p>Example:
 * <pre>
 * Clip clip = Clips.interval(5, 10);
 * clip.apply(3) == 5
 * clip.apply(5) == 5
 * clip.apply(6) == 6
 * clip.apply(10) == 10
 * clip.apply(20) == 10
 * </pre>
 * 
 * <p>{@code Clip} also works for intervals defined by {@link Quantity}.
 * 
 * <p>An instance of {@link Clip} is immutable.
 * {@link Clip} implements {@link #hashCode()}, and {@link #equals(Object)}.
 * {@link Clip} does not implement {@link #toString()}.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Clip.html">Clip</a> */
public interface Clip extends ScalarUnaryOperator {
  /** @param tensor
   * @return tensor with all entries of given tensor applied to clip function */
  <T extends Tensor> T of(T tensor);

  /** @param scalar
   * @return true if given scalar is invariant under this clip */
  boolean isInside(Scalar scalar);

  /** Remark: Functionality inspired by {@link Objects#requireNonNull(Object)}
   * 
   * @param scalar
   * @return scalar that is guaranteed to be invariant under this clip
   * @throws Exception if given scalar is not invariant under this clip */
  Scalar requireInside(Scalar scalar);

  /** @param scalar
   * @return true if given scalar is not invariant under this clip */
  boolean isOutside(Scalar scalar);

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
   * If the given scalar is outside the clip interval, the return value is either 0, or 1. */
  Scalar rescale(Scalar scalar);

  /** @return lower bound of clip interval */
  Scalar min();

  /** @return upper bound of clip interval */
  Scalar max();

  /** @return difference between upper and lower bound of clip interval */
  Scalar width();

  @Override // from Object
  int hashCode();

  @Override // from Object
  boolean equals(Object object);
}
