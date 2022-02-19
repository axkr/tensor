// code by jph
package ch.alpine.tensor.mat.pd;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.MatrixDotTranspose;
import ch.alpine.tensor.mat.sv.SingularValueDecomposition;
import ch.alpine.tensor.red.Times;

/* package */ class SvdSu extends PolarDecompositionSvd {
  public SvdSu(SingularValueDecomposition svd) {
    super(svd);
  }

  @Override // from PolarDecomposition
  public Tensor getPositiveSemidefinite() {
    return MatrixDotTranspose.of(Tensor.of(svd.getU().stream().map(row -> Times.of(svd.values(), row))), svd.getU());
  }
}
