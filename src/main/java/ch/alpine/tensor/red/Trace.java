// code by jph
package ch.alpine.tensor.red;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;

/** Trace of a matrix or tensor along two dimensions with the same size.
 * 
 * <p>Trace is identical to Mathematica::Tr except for few special cases:
 * <ul>
 * <li>Mathematica also defines the function for vectors Mathematica::Tr[{1, 2, 3}].
 * In the tensor library computing the trace of a vector throws an exception:
 * Trace.of[{1, 2, 3}] == undefined
 * <li>Mathematica::Tr[{{}}] == 0 whereas
 * Tensor::Trace[{{}}, 0, 1] throws an exception.
 * <li>Mathematica::Tr[{}] == 0 whereas
 * Tensor::Trace[{}, 0, 1] throws an exception.
 * </ul>
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Tr.html">Tr</a> */
public enum Trace {
  ;
  /** @param tensor
   * @param d0
   * @param d1 != d0
   * @return stream of slices of tensor along dimensions d0 and d1 */
  public static Stream<Tensor> stream(Tensor tensor, int d0, int d1) {
    if (d0 == d1)
      throw new IllegalArgumentException(d0 + "==" + d1);
    List<Integer> dimensions = Dimensions.of(tensor);
    int l0 = dimensions.get(d0);
    int l1 = dimensions.get(d1);
    if (l0 != l1)
      throw new IllegalArgumentException(l0 + "!=" + l1);
    Integer[] index = Stream.generate(() -> Tensor.ALL) //
        .limit(Math.max(d0, d1) + 1) //
        .toArray(Integer[]::new);
    return IntStream.range(0, l0) //
        .mapToObj(count -> {
          index[d0] = count;
          index[d1] = count;
          return tensor.get(index);
        });
  }

  /** to compute the trace, the tensor has to have equal dimensions at d0 and d1, i.e.
   * <code>Dimensions.of(tensor).get(d0) == Dimensions.of(tensor).get(d1)</code>
   * 
   * <p>For a square matrix, the trace is the sum of all diagonal entries.
   * 
   * @param tensor
   * @param d0
   * @param d1
   * @return trace of tensor along dimensions d0 and d1,
   * i.e. the sum of all slices along dimensions d0 and d1 */
  public static Tensor of(Tensor tensor, int d0, int d1) {
    return stream(tensor, d0, d1).reduce(Tensor::add).orElseThrow();
  }

  /** @param matrix
   * @return sum of diagonal elements of given matrix
   * @throws Exception if input is not a matrix */
  public static Scalar of(Tensor matrix) {
    return (Scalar) of(matrix, 0, 1);
  }
}
