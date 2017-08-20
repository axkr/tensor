// code by jph
package ch.ethz.idsc.tensor;

import java.io.IOException;

import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.io.Put;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.utl.Stopwatch;
import ch.ethz.idsc.tensor.utl.UserHome;

enum DotMatMatDemo {
  ;
  public static void main(String[] args) throws IOException {
    Distribution distribution;
    distribution = NormalDistribution.of(1, 4);
    // distribution = PoissonDistribution.of(RealScalar.ONE);
    {
      int n = 100;
      Tensor a = RandomVariate.of(distribution, n, n);
      Tensor b = RandomVariate.of(distribution, n, n);
      a.dot(b);
      Parallelize.dot(a, b);
    }
    Tensor timing = Tensors.empty();
    for (int dim = 0; dim < 40; ++dim) {
      System.out.println(dim);
      Stopwatch s_ser = new Stopwatch();
      Stopwatch s_par = new Stopwatch();
      final int trials = 50;
      for (int count = 0; count < trials; ++count) {
        Tensor a = RandomVariate.of(distribution, dim, dim);
        Tensor b = RandomVariate.of(distribution, dim, dim);
        s_ser.start();
        Tensor cs = a.dot(b);
        s_ser.stop();
        s_par.start();
        Tensor cp = Parallelize.dot(a, b);
        s_par.stop();
        if (!Chop._12.close(cs, cp))
          throw TensorRuntimeException.of(cs);
      }
      timing.append(Tensors.vector(s_ser.total() / trials, s_par.total() / trials));
    }
    Put.of(UserHome.file("timing_matmat.txt"), Transpose.of(timing));
  }
}