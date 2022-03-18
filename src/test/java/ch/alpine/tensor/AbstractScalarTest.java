// code by jph
package ch.alpine.tensor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.usr.AssertFail;

public class AbstractScalarTest {
  @Test
  public void testMap() {
    Tensor c = Tensors.fromString("{{1}, {4}, {4}}");
    Tensor a = Tensors.vector(1, 2, 3);
    Tensor b = Tensors.vector(4).unmodifiable();
    a = a.map(s -> b);
    a.set(RealScalar.ONE, 0, 0); // requires copy() in AbstractScalar::map
    assertEquals(a, c);
  }

  @Test
  public void testSet() {
    Tensor a = Tensors.vector(1, 2, 3);
    Tensor b = Tensors.vector(4).unmodifiable();
    a.set(s -> b, Tensor.ALL);
    a.set(RealScalar.ONE, 0, 0);
    Tensor c = Tensors.fromString("{{1}, {4}, {4}}");
    assertEquals(a, c);
  }

  @Test
  public void testSetAll() {
    Tensor matrix = HilbertMatrix.of(5);
    matrix.set(Tensor::negate, Tensor.ALL, 1);
    matrix.set(Tensor::negate, 3, Tensor.ALL);
    matrix.set(Tensor::negate, 1);
    matrix.set(Tensor::negate, Tensor.ALL, 3);
    SymmetricMatrixQ.require(matrix);
  }

  @Test
  public void testGet1Fail() {
    assertEquals(Pi.VALUE.get(), Pi.VALUE);
    AssertFail.of(() -> Pi.VALUE.get(Arrays.asList(0)));
    AssertFail.of(() -> Pi.VALUE.get(Arrays.asList(0, 0)));
    AssertFail.of(() -> Pi.VALUE.get(Arrays.asList(-1)));
    AssertFail.of(() -> Pi.VALUE.get(Arrays.asList(-1, 0)));
    AssertFail.of(() -> RealScalar.ONE.Get(1));
    AssertFail.of(() -> RealScalar.ONE.get(new int[] { 1 }));
  }

  @Test
  public void testGet2Fail() {
    AssertFail.of(() -> RationalScalar.HALF.Get(1, 4));
    AssertFail.of(() -> Pi.TWO.get(new int[] { 1, 2 }));
  }

  @Test
  public void testSetFail() {
    AssertFail.of(() -> RealScalar.ONE.set(RealScalar.ZERO));
    AssertFail.of(() -> RealScalar.ONE.set(s -> RealScalar.ZERO));
  }

  @Test
  public void testSetListFail() {
    AssertFail.of(() -> Pi.VALUE.set(RealScalar.ZERO, Integers.asList(new int[] {})));
    AssertFail.of(() -> Pi.VALUE.set(RealScalar.ZERO, Integers.asList(new int[] { 2 })));
    AssertFail.of(() -> Pi.VALUE.set(RealScalar.ZERO::add, Integers.asList(new int[] {})));
    AssertFail.of(() -> Pi.VALUE.set(RealScalar.ZERO::add, Integers.asList(new int[] { 2 })));
  }

  @Test
  public void testAppendFail() {
    AssertFail.of(() -> RealScalar.ONE.append(RealScalar.ZERO));
  }

  @Test
  public void testExtractFail() {
    AssertFail.of(() -> RealScalar.ONE.extract(1, 2));
  }

  @Test
  public void testBlockEmpty() {
    assertEquals(Pi.VALUE.block(Arrays.asList(), Arrays.asList()), Pi.VALUE);
  }

  @Test
  public void testBlockFail() {
    AssertFail.of(() -> RealScalar.ONE.block(Arrays.asList(1), Arrays.asList(1)));
  }
}
