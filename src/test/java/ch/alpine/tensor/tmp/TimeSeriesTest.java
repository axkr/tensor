// code by jph
package ch.alpine.tensor.tmp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

class TimeSeriesTest {
  @Test
  void testPack() {
    AtomicInteger atomicInteger = new AtomicInteger();
    TimeSeries timeSeries = TimeSeries.of(Stream.generate(() -> new TsEntry( //
        RealScalar.of(atomicInteger.getAndIncrement()), //
        Tensors.vector(1, 2, 3))).limit(11), //
        ResamplingMethods.HOLD_LO_SPARSE);
    assertEquals(timeSeries.path(), Tensors.fromString("{{0, {1, 2, 3}}, {10, {1, 2, 3}}}"));
  }

  @Test
  void testNoPack() {
    AtomicInteger atomicInteger = new AtomicInteger();
    TimeSeries timeSeries = TimeSeries.of(Stream.generate(() -> new TsEntry( //
        RealScalar.of(atomicInteger.getAndIncrement()), //
        Tensors.vector(1, 2, 3))).limit(11), //
        ResamplingMethods.LINEAR_INTERPOLATION);
    assertEquals(timeSeries.size(), 11);
  }

  @Test
  void testPath() {
    Tensor p1 = Tensors.fromString("{{1, {1,1}}, {4, {3,2}}}");
    TimeSeries timeSeries = TimeSeries.path(p1, ResamplingMethods.LINEAR_INTERPOLATION);
    Tensor path = timeSeries.path();
    path.set(r -> r.append(RealScalar.ZERO), 0);
    // System.out.println(path);
    // System.out.println(timeSeries.path());
  }

  @Test
  void testDimension() {
    Tensor p1 = Tensors.fromString("{{1, {1,1}}, {4, {3,2}}}").unmodifiable();
    TimeSeries timeSeries = TimeSeries.path(p1, ResamplingMethods.LINEAR_INTERPOLATION);
    Tensor path = timeSeries.path();
    path.set(r -> r.append(RealScalar.ZERO), 0, 1);
    assertEquals(timeSeries.path(), p1);
  }

  @Test
  void testFails() {
    Tensor p1 = Tensors.fromString("{{1, {1,1}}, {4, {3,2}, 3}}").unmodifiable();
    assertThrows(Exception.class, () -> TimeSeries.path(p1, ResamplingMethods.LINEAR_INTERPOLATION));
  }

  @Test
  void testNullFails() {
    assertThrows(Exception.class, () -> TimeSeries.path((Tensor) null, ResamplingMethods.LINEAR_INTERPOLATION));
    assertThrows(Exception.class, () -> TimeSeries.empty(null));
    Tensor p1 = Tensors.fromString("{{1, {1,1}}, {4, {3,2}}}").unmodifiable();
    TimeSeries.path(p1, ResamplingMethods.LINEAR_INTERPOLATION);
    assertThrows(Exception.class, () -> TimeSeries.path(p1.stream(), null));
  }
}
