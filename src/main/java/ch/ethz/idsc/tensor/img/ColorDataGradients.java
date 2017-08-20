// code by jph
package ch.ethz.idsc.tensor.img;

import java.util.Objects;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.io.ResourceData;

/** inspired by Mathematica::ColorData["Gradients"] */
public enum ColorDataGradients implements ColorDataFunction {
  /** classic is default */
  CLASSIC("classic.csv"), //
  HUE("hue.csv"), // <- cyclic
  /** hsluv is hue with brightness equalized, see hsluv.org */
  HSLUV("hsluv.csv"), // <- cyclic
  SUNSET("sunset.csv"), //
  RAINBOW("rainbow.csv"), //
  CMYK_REVERSED("cmyk_reversed.csv"), //
  THERMOMETER("thermometer.csv"), //
  PASTEL("pastel.csv"), //
  GRAYSCALE("grayscale.csv"), //
  /** the tensor library is made in Switzerland
   * the alpine color scheme was added August 1st */
  ALPINE("alpine.csv"), //
  COPPER("copper.csv"), //
  PINK("pink.csv"), //
  ;
  // ---
  private final ColorDataFunction colorDataFunction;

  private ColorDataGradients(String string) {
    Tensor tensor = ResourceData.of("/colorscheme/" + string);
    colorDataFunction = ColorDataGradient.of(Objects.isNull(tensor) ? Array.zeros(2, 4) : tensor);
    if (Objects.isNull(tensor))
      System.err.println("fail to load " + string);
  }

  @Override
  public Tensor apply(Scalar scalar) {
    return colorDataFunction.apply(scalar);
  }
}