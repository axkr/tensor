// code by jph
package ch.ethz.idsc.tensor.red;

import ch.ethz.idsc.tensor.Tensor;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/Standardize.html">Standardize</a> */
public enum Standardize {
  ;
  /** @param vector
   * @return result with Mean[result] == 0 and Variance[result] == 1
   * @throws Exception if input does not have sufficient elements, or is not a vector */
  public static Tensor ofVector(Tensor vector) {
    Tensor nmean = Mean.of(vector).negate();
    return Tensor.of(vector.stream().map(nmean::add)) //
        .divide(StandardDeviation.ofVector(vector)); // StandardDeviation subtracts the mean internally
  }
}
