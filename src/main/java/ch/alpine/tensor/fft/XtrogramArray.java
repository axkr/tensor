// code by ob, jph
package ch.alpine.tensor.fft;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.PadRight;
import ch.alpine.tensor.alg.Partition;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.AbsSquared;
import ch.alpine.tensor.sca.Re;
import ch.alpine.tensor.sca.exp.Log;
import ch.alpine.tensor.sca.win.DirichletWindow;
import ch.alpine.tensor.sca.win.HannWindow;
import ch.alpine.tensor.sca.win.WindowFunctions;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/SpectrogramArray.html">SpectrogramArray</a>
 * <a href="https://reference.wolfram.com/language/ref/CepstrogramArray.html">CepstrogramArray</a>
 * 
 * @see WindowFunctions */
public class XtrogramArray implements Serializable {
  public static final XtrogramArray SPECTROGRAM = new XtrogramArray(Fourier.FORWARD::transform);
  public static final XtrogramArray CEPSTROGRAM_Power = new XtrogramArray(vector -> {
    return Fourier.INVERSE.transform(Fourier.FORWARD.transform(vector) //
        .map(AbsSquared.FUNCTION) //
        .map(Log.FUNCTION)).map(AbsSquared.FUNCTION);
  });
  public static final XtrogramArray CEPSTROGRAM_Real = new XtrogramArray(vector -> {
    return Fourier.INVERSE.transform(Fourier.FORWARD.transform(vector) //
        .map(Abs.FUNCTION) //
        .map(Log.FUNCTION)).map(Re.FUNCTION);
  });
  public static final XtrogramArray CEPSTROGRAM_Real1 = new XtrogramArray(vector -> {
    return Fourier.INVERSE.transform(Fourier.FORWARD.transform(vector) //
        .map(Abs.FUNCTION) //
        .map(RealScalar.of(1E-12)::add) //
        .map(Log.FUNCTION)) //
        .map(Re.FUNCTION);
  });
  private final TensorUnaryOperator process;

  public XtrogramArray(TensorUnaryOperator process) {
    this.process = Objects.requireNonNull(process);
  }

  /** @param vector
   * @param window for instance {@link DirichletWindow#FUNCTION}
   * @return */
  public final Tensor of(Tensor vector, ScalarUnaryOperator window) {
    int windowLength = StaticHelper.default_windowLength(vector.length());
    return of(windowLength, StaticHelper.default_offset(windowLength), window).apply(vector);
  }

  /** Mathematica default
   * 
   * @param vector
   * @return
   * @throws Exception if input is not a vector */
  public final Tensor of(Tensor vector) {
    return of(vector, DirichletWindow.FUNCTION);
  }

  /** @param vector
   * @param window for instance {@link HannWindow#FUNCTION}
   * @return truncated and transposed spectrogram array for visualization
   * @throws Exception if input is not a vector */
  public final Tensor half_abs(Tensor vector, ScalarUnaryOperator window) {
    Tensor tensor = of(vector, window);
    int half = Unprotect.dimension1Hint(tensor) / 2;
    return Tensors.vector(i -> tensor.get(Tensor.ALL, half - i - 1).map(Abs.FUNCTION), half);
  }

  /** @param windowLength
   * @param offset positive and not greater than windowLength
   * @return */
  public final TensorUnaryOperator of(int windowLength, int offset) {
    return of(windowLength, offset, DirichletWindow.FUNCTION);
  }

  /** @param windowDuration
   * @param samplingFrequency
   * @param offset positive
   * @param window for instance {@link DirichletWindow#FUNCTION}
   * @return */
  public final TensorUnaryOperator of( //
      Scalar windowDuration, Scalar samplingFrequency, int offset, ScalarUnaryOperator window) {
    return of(StaticHelper.windowLength(windowDuration, samplingFrequency), offset, window);
  }

  /** @param windowDuration
   * @param samplingFrequency
   * @param window for instance {@link DirichletWindow#FUNCTION}
   * @return spectrogram operator with default offset */
  public final TensorUnaryOperator of( //
      Scalar windowDuration, Scalar samplingFrequency, ScalarUnaryOperator window) {
    int windowLength = StaticHelper.windowLength(windowDuration, samplingFrequency);
    return of(windowLength, StaticHelper.default_offset(windowLength), window);
  }

  /** @param windowLength
   * @param offset
   * @param window for instance {@link DirichletWindow#FUNCTION}
   * @return */
  public final TensorUnaryOperator of(int windowLength, int offset, ScalarUnaryOperator window) {
    return new BasetrogramArray(windowLength, offset, window);
  }

  private class BasetrogramArray implements TensorUnaryOperator {
    private final int windowLength;
    private final int offset;
    private final Tensor weights;

    public BasetrogramArray(int windowLength, int offset, ScalarUnaryOperator window) {
      if (offset <= 0 || windowLength < offset)
        throw new IllegalArgumentException("windowLength=" + windowLength + " offset=" + offset);
      // ---
      this.windowLength = windowLength;
      this.offset = offset;
      weights = StaticHelper.weights(windowLength, window);
    }

    @Override // from TensorUnaryOperator
    public Tensor apply(Tensor vector) {
      Scalar zero = vector.Get(0).zero();
      int highestOneBit = Integer.highestOneBit(windowLength);
      TensorUnaryOperator padding = windowLength == highestOneBit //
          ? t -> t //
          : PadRight.with(zero, highestOneBit * 2);
      return Tensor.of(Partition.stream(vector, windowLength, offset) //
          .map(Times.operator(weights)) //
          .map(padding) //
          .map(process));
    }

    @Override // from Object
    public String toString() {
      return MathematicaFormat.concise("BasetrogramArray", windowLength, offset);
    }
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("XtrogramArray", process);
  }
}
