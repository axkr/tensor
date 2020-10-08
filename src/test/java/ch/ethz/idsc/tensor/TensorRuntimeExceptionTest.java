// code by jph
package ch.ethz.idsc.tensor;

import java.io.IOException;

import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.opt.Pi;
import junit.framework.TestCase;

public class TensorRuntimeExceptionTest extends TestCase {
  public void testFull() throws ClassNotFoundException, IOException {
    Exception exception = Serialization.copy(TensorRuntimeException.of(Tensors.vector(1, 2), Tensors.vector(9, 3)));
    assertEquals(exception.getMessage(), "{1, 2}; {9, 3}");
  }

  public void testFullScalar() {
    Exception exception = TensorRuntimeException.of(Tensors.vector(1, 2), RationalScalar.HALF, Tensors.empty());
    assertEquals(exception.getMessage(), "{1, 2}; 1/2; {}");
  }

  public void testShort() {
    Exception exception = TensorRuntimeException.of(Array.zeros(20, 10, 5), RealScalar.ONE);
    assertEquals(exception.getMessage(), "T[20, 10, 5]; 1");
  }

  public void testEmpty() {
    Exception exception = TensorRuntimeException.of();
    assertEquals(exception.getMessage(), "");
  }

  public void testSerializable() throws Exception {
    Exception exception = Serialization.copy(TensorRuntimeException.of(RealScalar.ONE));
    assertEquals(exception.getMessage(), "1");
  }

  public void testMessage() {
    Exception exception = TensorRuntimeException.of(Pi.VALUE);
    assertEquals(exception.getMessage(), "3.141592653589793");
  }

  public void testNull() {
    Exception exception = TensorRuntimeException.of(Tensors.vector(4, 7, 1, 1), null, RealScalar.ONE);
    assertEquals(exception.getMessage(), "{4, 7, 1, 1}; null; 1");
  }
}
