// code by jph
package ch.ethz.idsc.tensor.nrm;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Hypot computes
 * <code>sqrt(<i>a</i><sup>2</sup>&nbsp;+<i>b</i><sup>2</sup>)</code>
 * for a and b as {@link RealScalar}s
 * without intermediate overflow or underflow.
 * 
 * <p>Hypot also operates on vectors.
 * 
 * <p>Hypot is inspired by {@link Math#hypot(double, double)} */
public enum Hypot {
  ;
  /** @param a
   * @param b
   * @return Sqrt[a * a + b * b] */
  public static Scalar of(Scalar a, Scalar b) {
    Scalar ax = Abs.FUNCTION.apply(a);
    Scalar ay = Abs.FUNCTION.apply(b);
    final Scalar min;
    final Scalar max;
    if (Scalars.lessThan(ax, ay)) {
      min = ax;
      max = ay;
    } else {
      min = ay;
      max = ax;
    }
    if (Scalars.isZero(min))
      return max; // if min == 0 return max
    // valid at this point: 0 < min <= max
    Scalar r1 = min.divide(max);
    Scalar r2 = r1.multiply(r1);
    return max.multiply(Sqrt.FUNCTION.apply(r2.one().add(r2)));
  }

  /** @param a
   * @return Sqrt[a^2 + 1] */
  public static Scalar withOne(Scalar a) {
    Scalar ax = Abs.FUNCTION.apply(a);
    Scalar ay = a.one();
    final Scalar min;
    final Scalar max;
    if (Scalars.lessThan(ax, ay)) {
      min = ax;
      max = ay;
    } else {
      min = ay;
      max = ax;
    }
    Scalar r1 = min.divide(max);
    Scalar r2 = r1.multiply(r1);
    return max.multiply(Sqrt.FUNCTION.apply(r2.one().add(r2)));
  }

  /** function computes the 2-Norm of a vector
   * without intermediate overflow or underflow
   * 
   * <p>the empty vector Hypot[{}] results in an error, since
   * Mathematica::Norm[{}] == Norm[{}] is undefined also.
   * 
   * <p>The disadvantage of the implementation is that
   * a numerical output is returned even in cases where
   * a rational number is the exact result.
   * 
   * @param vector
   * @return 2-norm of vector
   * @throws Exception if vector is empty, or vector contains NaN */
  public static Scalar ofVector(Tensor vector) {
    Tensor abs = vector.map(Abs.FUNCTION);
    Scalar max = (Scalar) abs.stream().reduce(Max::of).get();
    if (Scalars.isZero(max))
      return max;
    abs = abs.divide(max);
    return max.multiply(Sqrt.FUNCTION.apply((Scalar) abs.dot(abs)));
  }
}
