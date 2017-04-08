// code by jph
package ch.ethz.idsc.tensor;

import java.math.BigInteger;

/** an implementation is not required to support the representation of
 * Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, and Double.NaN */
public final class RationalScalar extends AbstractRealScalar {
  // private because BigFraction has package visibility
  private static RealScalar _of(BigFraction bigFraction) {
    return bigFraction.num.equals(BigInteger.ZERO) ? //
        ZeroScalar.get() : new RationalScalar(bigFraction);
  }

  public static RealScalar of(BigInteger num, BigInteger den) {
    // if (den.signum() == 0)
    // return DoubleScalar.of(num.signum() == 0 ? Double.NaN : //
    // (0 < num.signum() ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY));
    return _of(BigFraction.of(num, den));
  }

  public static RealScalar of(long num, long den) {
    // if (den == 0)
    // return DoubleScalar.of(num == 0 ? Double.NaN : //
    // (0 < num ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY));
    return _of(BigFraction.of(num, den));
  }

  private final BigFraction bigFraction;

  /** private constructor is only called from of(...)
   * 
   * @param value */
  private RationalScalar(BigFraction bigFraction) {
    this.bigFraction = bigFraction;
  }

  public BigInteger numerator() {
    return bigFraction.num;
  }

  /** @return positive number */
  public BigInteger denominator() {
    return bigFraction.den;
  }

  @Override // from Scalar
  public RealScalar invert() {
    return _of(bigFraction.invert());
  }

  @Override // from Scalar
  public RealScalar negate() {
    return _of(bigFraction.negate());
  }

  @Override // from Scalar
  public Scalar multiply(Scalar scalar) {
    if (scalar instanceof RationalScalar)
      return _of(bigFraction.multiply(((RationalScalar) scalar).bigFraction));
    return scalar.multiply(this);
  }

  @Override // from Scalar
  public Number number() {
    if (denominator().equals(BigInteger.ONE)) {
      BigInteger bigInteger = numerator();
      try {
        return bigInteger.intValueExact();
      } catch (Exception exception) {
        // ---
      }
      try {
        return bigInteger.longValueExact();
      } catch (Exception exception) {
        // ---
      }
      return bigInteger;
    }
    return bigFraction.doubleValue();
  }

  @Override // from AbstractScalar
  protected Scalar plus(Scalar scalar) {
    if (scalar instanceof RationalScalar)
      return _of(bigFraction.add(((RationalScalar) scalar).bigFraction));
    return scalar.add(this);
  }

  @Override // from AbstractRealScalar
  protected boolean isNonNegative() {
    return 0 <= bigFraction.num.signum();
  }

  @Override // from NInterface
  public Scalar n() {
    return DoubleScalar.of(bigFraction.doubleValue());
  }

  @Override // from RealScalar
  public int compareTo(Scalar scalar) {
    if (scalar instanceof RationalScalar) {
      RationalScalar rationalScalar = (RationalScalar) scalar;
      BigInteger lhs = numerator().multiply(rationalScalar.denominator());
      BigInteger rhs = rationalScalar.numerator().multiply(denominator());
      return lhs.compareTo(rhs);
    }
    if (scalar instanceof ZeroScalar)
      return signInt();
    @SuppressWarnings("unchecked")
    Comparable<Scalar> comparable = (Comparable<Scalar>) scalar;
    return -comparable.compareTo(this);
  }

  @Override // from AbstractScalar
  public int hashCode() {
    return bigFraction.hashCode();
  }

  @Override // from AbstractScalar
  public boolean equals(Object object) {
    if (object instanceof RationalScalar)
      return bigFraction.equals(((RationalScalar) object).bigFraction);
    return object == null ? false : object.equals(this);
  }

  @Override // from AbstractScalar
  public String toString() {
    return bigFraction.toCompactString();
  }
}
