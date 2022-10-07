package ch.alpine.tensor.tmp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.N;

class TimeSeriesBinaryOperatorTest {
  @Test
  void testMathematica() {
    Tensor p1 = Tensors.fromString("{{1, 3}, {4, 3}, {5, 6}, {7, 5}, {10, 2}}");
    TimeSeries ts1 = TimeSeries.path(p1, ResamplingMethods.LINEAR_INTERPOLATION);
    assertEquals(ts1.resamplingMethod(), ResamplingMethods.LINEAR_INTERPOLATION);
    assertEquals(ts1.path(), p1);
    TimeSeries ts2 = TimeSeries.path( //
        Tensors.fromString("{{2, 1}, {3, 2}, {6, 3}, {8, 2}, {10, 4}, {11, 3}}"), //
        ResamplingMethods.LINEAR_INTERPOLATION);
    Clip clip = Clips.intersection(ts1.domain(), ts2.domain());
    assertEquals(clip, Clips.interval(2, 10));
    BinaryOperator<TimeSeries> binaryOperator = TimeSeriesBinaryOperator.of(Entrywise.max(), null);
    {
      TimeSeries timeSeries = binaryOperator.apply(ts1, ts2);
      assertEquals(timeSeries.path(), Tensors.fromString( //
          "{{2, 3}, {3, 3}, {4, 3}, {5, 6}, {6, 11/2}, {7, 5}, {8, 4}, {10, 4}}"));
    }
  }

  @Test
  void testExactAndInexact() {
    Tensor p1 = Tensors.fromString("{{1, 3}, {4, 3}, {5, 6}, {7, 5}, {10, 2}}");
    TimeSeries ts1 = TimeSeries.path(p1, ResamplingMethods.LINEAR_INTERPOLATION);
    TimeSeries ts2 = TimeSeries.path(p1.map(N.DOUBLE), ResamplingMethods.LINEAR_INTERPOLATION);
    TimeSeries timeSeries = TsKeywise.plus(ts1, ts2);
    assertEquals(timeSeries.size(), 5);
  }

  @Test
  void testEmpty() {
    Tensor p1 = Tensors.fromString("{{1, 3}, {4, 3}, {5, 6}, {7, 5}, {10, 2}}");
    TimeSeries ts1 = TimeSeries.path(p1, ResamplingMethods.LINEAR_INTERPOLATION);
    assertTrue(ts1.containsKey(RealScalar.ONE));
    assertFalse(ts1.containsKey(RealScalar.of(2)));
    TimeSeries ts2 = TimeSeries.empty(ResamplingMethods.HOLD_LO);
    assertTrue(TsKeywise.plus(ts1, ts2).isEmpty());
    assertTrue(TsKeywise.plus(ts2, ts1).isEmpty());
    assertTrue(TsKeywise.plus(ts2, ts2).isEmpty());
    TimeSeries timeSeries = ts1.block(Clips.interval(2, 7), true);
    assertEquals(timeSeries.size(), 3);
  }

  @Test
  void testNonOverlap() {
    Tensor p1 = Tensors.fromString("{{1, 3}, {4, 3}, {5, 6}}");
    Tensor p2 = Tensors.fromString("{{7, 5}, {10, 2}}");
    TimeSeries ts1 = TimeSeries.path(p1, ResamplingMethods.LINEAR_INTERPOLATION);
    TimeSeries ts2 = TimeSeries.path(p2, ResamplingMethods.HOLD_LO);
    assertTrue(TsKeywise.plus(ts1, ts2).isEmpty());
    assertTrue(TsKeywise.plus(ts2, ts1).isEmpty());
  }
}
