// code by jph
package ch.alpine.tensor.itp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.tmp.ResamplingMethod;
import ch.alpine.tensor.tmp.TimeSeries;
import ch.alpine.tensor.tmp.TimeSeriesIntegrate;

class BSplineBasisTest {
  @RepeatedTest(4)
  void test(RepetitionInfo repetitionInfo) {
    int deg = repetitionInfo.getCurrentRepetition() - 1;
    ScalarUnaryOperator suo = BSplineBasis.of(deg);
    assertEquals(suo.toString(), "BSplineBasis[" + deg + "]");
    BSplineBasisDouble old = BSplineBasisDouble.of[deg];
    Tensor domain = RandomVariate.of(UniformDistribution.of(-4, 4), 10);
    Tensor s1 = domain.map(suo);
    Tensor s2 = domain.map(s -> RealScalar.of(old.at(s.number().doubleValue())));
    Tolerance.CHOP.requireClose(s1, s2);
  }

  @RepeatedTest(4)
  void testIntegrateUniform(RepetitionInfo repetitionInfo) {
    int deg = repetitionInfo.getCurrentRepetition();
    ScalarUnaryOperator suo = BSplineBasis.of(deg);
    TimeSeries timeSeries = TimeSeries.empty(ResamplingMethod.LINEAR_INTERPOLATION);
    Clip clip = Clips.absolute((deg + 1) * 0.5);
    Distribution distribution = UniformDistribution.of(clip);
    for (int count = 0; count < 100; ++count) {
      Scalar x = RandomVariate.of(distribution);
      timeSeries.insert(x, suo.apply(x));
    }
    TimeSeries integral = TimeSeriesIntegrate.of(timeSeries);
    Scalar x_hi = integral.keySet(clip, true).last();
    Tensor hi = integral.evaluate(x_hi);
    Chop._01.requireClose(hi, RealScalar.ONE);
    Tensor in = TimeSeriesIntegrate.of(timeSeries, timeSeries.domain());
    Chop._01.requireClose(in, RealScalar.ONE);
  }

  @RepeatedTest(4)
  void testIntegrateNormal(RepetitionInfo repetitionInfo) {
    int deg = repetitionInfo.getCurrentRepetition() + 1; // more gauss shaped
    ScalarUnaryOperator suo = BSplineBasis.of(deg);
    TimeSeries timeSeries = TimeSeries.empty(ResamplingMethod.LINEAR_INTERPOLATION);
    Clip clip = Clips.absolute((deg + 1) * 0.5);
    Distribution distribution = NormalDistribution.of(0, (deg + 1) * 0.25);
    for (int count = 0; count < 100; ++count) {
      Scalar x = RandomVariate.of(distribution);
      timeSeries.insert(x, suo.apply(x));
    }
    TimeSeries integral = TimeSeriesIntegrate.of(timeSeries);
    Scalar x_hi = integral.keySet(clip, true).last();
    Tensor hi = integral.evaluate(x_hi);
    Chop._01.requireClose(hi, RealScalar.ONE);
    Tensor in = TimeSeriesIntegrate.of(timeSeries, timeSeries.domain());
    Chop._01.requireClose(in, RealScalar.ONE);
  }
}
