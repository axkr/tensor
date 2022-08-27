// code by jph
package ch.alpine.tensor.sca.bes;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/BesselI.html">BesselI</a> */
public enum BesselI {
  ;
  public static Scalar _0(Scalar x) {
    return RealScalar.of(Bessel.i0(x.number().doubleValue()));
  }

  public static Scalar _0(Number x) {
    return _0(RealScalar.of(x));
  }

  public static Scalar _1(Scalar x) {
    return RealScalar.of(Bessel.i1(x.number().doubleValue()));
  }

  public static Scalar _1(Number x) {
    return _1(RealScalar.of(x));
  }
}
