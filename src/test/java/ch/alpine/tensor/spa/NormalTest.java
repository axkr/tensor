// code by jph
package ch.alpine.tensor.spa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.num.Pi;

public class NormalTest {
  @Test
  public void testSimple() {
    Tensor tensor = Tensors.fromString("{{1}, 2}");
    Tensor result = Normal.of(tensor);
    assertEquals(tensor, result);
  }

  @Test
  public void testScalar() {
    assertEquals(Normal.of(Pi.VALUE), Pi.VALUE);
  }

  @Test
  public void testMixed() {
    Tensor mixed = Tensors.of( //
        SparseArray.of(RealScalar.ZERO, 3), Tensors.vector(1, 2), SparseArray.of(RealScalar.ZERO, 3));
    assertTrue(mixed.get(0) instanceof SparseArray);
    assertEquals(Normal.of(mixed).toString(), "{{0, 0, 0}, {1, 2}, {0, 0, 0}}");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSerializable() throws ClassNotFoundException, IOException {
    Serialization.copy(new Normal((Function<Scalar, ? extends Tensor> & Serializable) s -> s.add(s)));
  }
}
