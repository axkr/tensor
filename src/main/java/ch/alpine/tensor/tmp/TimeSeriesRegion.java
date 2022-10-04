// code by jph
package ch.alpine.tensor.tmp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

public enum TimeSeriesRegion {
  ;
  public static List<Clip> of(TimeSeries series, Predicate<Scalar> predicate) {
    return of(series, Scalar.class::cast, predicate);
  }

  public static List<Clip> of(TimeSeries series, Function<Tensor, Scalar> function, Predicate<Scalar> predicate) {
    Clip domain = series.domain();
    List<Clip> list = new ArrayList<>();
    AtomicReference<Scalar> min = new AtomicReference<>();
    series.stream(domain, true).forEach(entry -> {
      if (predicate.test(function.apply(entry.value()))) {
        if (Objects.isNull(min.get()))
          min.set(entry.key());
      } else //
      if (Objects.nonNull(min.get())) {
        list.add(Clips.interval(min.get(), entry.key()));
        min.set(null);
      }
    });
    if (Objects.nonNull(min.get()))
      list.add(Clips.interval(min.get(), domain.max()));
    return list;
  }
}
