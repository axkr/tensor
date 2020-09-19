// code by jph
package ch.ethz.idsc.tensor;

import java.math.BigInteger;

import ch.ethz.idsc.tensor.io.StringScalar;
import ch.ethz.idsc.tensor.num.Divisible;
import ch.ethz.idsc.tensor.num.GaussScalar;

/** collection of useful static functions related to {@link Scalar} */
public enum Scalars {
  ;
  /** parses given string to an instance of {@link Scalar}
   * 
   * <p>Examples:
   * <pre>
   * "7/9" -> RationalScalar.of(7, 9)
   * "3.14" -> DoubleScalar.of(3.14)
   * "1E-20" -> DoubleScalar.of(1E-20)
   * "(3+2)*I/(-1+4)+8-I" -> ComplexScalar.of(8, 2/3) == "8+2/3*I"
   * "9.81[m*s^-2]" -> Quantity.of(9.81, "m*s^-2")
   * </pre>
   * 
   * If the parsing logic encounters an inconsistency, the return type
   * is a {@link StringScalar} that holds the input string.
   * 
   * <p>Scalar types that are not supported include {@link GaussScalar}.
   * 
   * @param string
   * @return scalar */
  public static Scalar fromString(String string) {
    try {
      return QuantityParser.of(string);
    } catch (Exception exception) {
      // ---
    }
    return StringScalar.of(string);
  }

  /** @param s1
   * @param s2
   * @return canonic/native comparison of input scalars
   * @see Double#compare(double, double)
   * @see Integer#compare(int, int) */
  public static int compare(Scalar s1, Scalar s2) {
    @SuppressWarnings("unchecked")
    Comparable<Scalar> comparable = (Comparable<Scalar>) s1;
    return comparable.compareTo(s2);
  }

  /** @param s1
   * @param s2
   * @return true if s1 < s2 */
  public static boolean lessThan(Scalar s1, Scalar s2) {
    return compare(s1, s2) < 0;
  }

  /** @param s1
   * @param s2
   * @return true if s1 <= s2 */
  public static boolean lessEquals(Scalar s1, Scalar s2) {
    return compare(s1, s2) <= 0;
  }

  /** @param scalar
   * @return true if given scalar equals scalar.zero() */
  public static boolean isZero(Scalar scalar) {
    return scalar.equals(scalar.zero());
  }

  /** @param scalar
   * @return true if given scalar does not equal scalar.zero() */
  public static boolean nonZero(Scalar scalar) {
    return !scalar.equals(scalar.zero());
  }

  /** bi-predicate that tests if m divides n, i.e. "m|n"
   * 
   * @param m in exact precision
   * @param n in exact precision
   * @return true if m divides n, else false
   * @see Divisible */
  public static boolean divides(Scalar m, Scalar n) {
    return Divisible.of(n, m);
  }

  /***************************************************/
  /** exact conversion to primitive type {@code int}
   * 
   * <p>function succeeds if given scalar is
   * <ul>
   * <li>instance of {@link RationalScalar}, with
   * <li>numerator sufficiently small to encode as {@code int}, and
   * <li>denominator == 1
   * </ul>
   * 
   * @param scalar
   * @return int value that equals given scalar
   * @throws Exception if exact conversion is not possible
   * @see IntegerQ */
  public static int intValueExact(Scalar scalar) {
    return bigIntegerValueExact(scalar).intValueExact();
  }

  /** exact conversion to primitive type {@code long}
   * 
   * <p>function succeeds if given scalar is
   * <ul>
   * <li>instance of {@link RationalScalar}, with
   * <li>numerator sufficiently small to encode as {@code long}, and
   * <li>denominator == 1
   * </ul>
   * 
   * @param scalar
   * @return long value that equals given scalar
   * @throws Exception if exact conversion is not possible
   * @see IntegerQ */
  public static long longValueExact(Scalar scalar) {
    return bigIntegerValueExact(scalar).longValueExact();
  }

  /** exact conversion to type {@code BigInteger}
   * 
   * <p>function succeeds if given scalar is instance of
   * {@link RationalScalar} with denominator == 1.
   * 
   * @param scalar
   * @return BigInteger that equals given scalar
   * @throws Exception if exact conversion is not possible
   * @see IntegerQ */
  public static BigInteger bigIntegerValueExact(Scalar scalar) {
    RationalScalar rationalScalar = (RationalScalar) scalar;
    if (rationalScalar.isInteger())
      return rationalScalar.numerator();
    throw TensorRuntimeException.of(scalar);
  }
}
