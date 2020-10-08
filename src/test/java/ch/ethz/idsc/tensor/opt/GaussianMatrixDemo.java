// code by jph
package ch.ethz.idsc.tensor.opt;

import java.io.IOException;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.HomeDirectory;
import ch.ethz.idsc.tensor.img.ArrayPlot;
import ch.ethz.idsc.tensor.img.ColorDataGradients;
import ch.ethz.idsc.tensor.io.Export;

/* package */ enum GaussianMatrixDemo {
  ;
  public static void main(String[] args) throws IOException {
    Tensor tensor = GaussianMatrix.of(255);
    Export.of(HomeDirectory.Pictures(GaussianMatrix.class.getSimpleName() + ".png"), //
        ArrayPlot.of(tensor, ColorDataGradients.PARULA));
  }
}
