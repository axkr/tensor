// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.Tensor;

/** {@link Flatten} is for convenience to wrap the Stream<Tensor>
 * returned by Tensor::flatten into a {@link Tensor}.
 * 
 * <p>{@link Flatten} undoes the work of {@link Partition}.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Flatten.html">Flatten</a> */
public enum Flatten {
  ;
  /** Flatten[{{a, b, c}, {{d}, e}}] == {a, b, c, d, e}
   * 
   * @param tensor
   * @return */
  public static Tensor of(Tensor tensor) {
    return of(tensor, -1);
  }

  /** Remark: in the special case of level == 0, the
   * function returns an exact copy of the input tensor.
   * 
   * @param tensor
   * @param level
   * @return */
  public static Tensor of(Tensor tensor, int level) {
    return Tensor.of(tensor.flatten(level));
  }
}