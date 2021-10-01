// code by jph
package ch.alpine.tensor.ext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/Integers.html">Integers</a> */
public enum Integers {
  ;
  /** @param value non-negative
   * @return value
   * @throws Exception if given value is negative */
  public static int requirePositiveOrZero(int value) {
    if (0 <= value) // non-negative, greater or equals zero
      return value;
    throw new IllegalArgumentException(Integer.toString(value));
  }

  /** @param value strictly positive
   * @return value
   * @throws Exception if given value is negative or zero */
  public static int requirePositive(int value) {
    if (0 < value) // strictly positive
      return value;
    throw new IllegalArgumentException(Integer.toString(value));
  }

  /** intended for use in reductive algorithms, for instance in the addition of
   * two vectors, where the length of the two vectors have to be equal.
   * 
   * inspired by junit's assertEquals
   * 
   * @param lhs
   * @param rhs
   * @return the identical value of lhs and rhs
   * @throws Exception if lhs and rhs are not equal */
  public static int requireEquals(int lhs, int rhs) {
    if (lhs == rhs)
      return lhs;
    throw new IllegalArgumentException(Integer.toString(lhs) + " != " + Integer.toString(rhs));
  }

  /** @param value
   * @return whether given value is an even number */
  public static boolean isEven(int value) {
    return (value & 1) == 0;
  }

  /** @param value strictly positive
   * @return true if value is a power of 2, e.g. 1, 2, 4, 8, 16, etc.
   * @throws Exception if given value is negative or zero */
  public static boolean isPowerOf2(int value) {
    return 0 == (requirePositive(value) & (value - 1));
  }

  // ---
  /** @param sigma
   * @return whether sigma encodes a permutation for instance {2, 0, 1, 3} */
  public static boolean isPermutation(int[] sigma) {
    return sigma.length == Arrays.stream(sigma) //
        .filter(index -> 0 <= index && index < sigma.length).distinct().count();
  }

  /** @param sigma
   * @return
   * @throws Exception if sigma does not encode a permutation */
  public static int[] requirePermutation(int[] sigma) {
    if (isPermutation(sigma))
      return sigma;
    throw new IllegalArgumentException(sigma.length <= 16 //
        ? Arrays.stream(sigma) //
            .mapToObj(Integer::toString) //
            .collect(Collectors.joining(" "))
        : "");
  }

  /** Examples:
   * <pre>
   * parity[{0, 1}] == 0
   * parity[{1, 0}] == 1
   * parity[{2, 0, 1}] == 0
   * </pre>
   * 
   * @param sigma a permutation of the list {0, 1, ..., sigma.length - 1}
   * @return 0 if sigma is even permutation, or 1 if sigma is odd permutation
   * @throws Exception if given sigma is not a permutation */
  public static int parity(int[] sigma) {
    requirePermutation(sigma);
    int parity = 0;
    for (int index = 0; index < sigma.length; ++index)
      while (sigma[index] != index) {
        int value = sigma[index];
        sigma[index] = sigma[value];
        sigma[value] = value;
        parity ^= 1;
      }
    return parity;
  }

  /** Remark: for variable size input use {@link Arrays#asList(Object...)} for example
   * Arrays.asList(18, 7, 79);
   * 
   * @param array
   * @return list with elements {array[0], array[1], ...} */
  public static List<Integer> asList(int[] array) {
    return Arrays.stream(array).boxed().collect(Collectors.toList());
  }
}
