// code by jph
package ch.alpine.tensor.tmp;

import java.util.NavigableMap;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

public interface ResamplingMethod {
  /** inserts value in given map at key
   * 
   * the resampling methods may determine that the insertion of
   * the given (key, value)-pair is redundant and not alter the map
   * 
   * @param navigableMap
   * @param key
   * @param value */
  void insert(NavigableMap<Scalar, Tensor> navigableMap, Scalar key, Tensor value);

  /** @param navigableMap
   * @param x
   * @return result of resampling method at location x taking account the keys in the vicinity of x
   * and their corresponding values
   * @throws Exception if parameter x is is outside the interval [firstKey(), lastKey()] */
  Tensor evaluate(NavigableMap<Scalar, Tensor> navigableMap, Scalar x);

  /** @param navigableMap
   * @return given navigableMap but potentially with some entries removed */
  default NavigableMap<Scalar, Tensor> pack(NavigableMap<Scalar, Tensor> navigableMap) {
    return navigableMap;
  }
}