// code by jph
package ch.ethz.idsc.tensor.usr;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.Norm;
import ch.ethz.idsc.tensor.nrm.Normalize;
import ch.ethz.idsc.tensor.nrm.VectorNorm;
import ch.ethz.idsc.tensor.nrm.VectorNormInterface;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.red.Min;

/* package */ enum NormalizeDemo {
  ;
  public static void main(String[] args) {
    Distribution distribution = NormalDistribution.standard();
    VectorNormInterface norm = VectorNorm.with(3.4);
    norm = Norm._2;
    Scalar min = RealScalar.ONE;
    Scalar max = RealScalar.ONE;
    for (int c = 0; c < 10000; ++c) {
      Tensor vector = RandomVariate.of(distribution, 1000);
      Tensor result = Normalize.with(norm::ofVector).apply(vector);
      Scalar value = norm.ofVector(result);
      min = Min.of(min, value);
      max = Max.of(max, value);
    }
    System.out.println(min + " " + max);
  }
}
