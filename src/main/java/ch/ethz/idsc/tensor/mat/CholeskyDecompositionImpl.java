// code by jph
// adapted from wikipedia - Cholesky decomposition
package ch.ethz.idsc.tensor.mat;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.sca.Conjugate;

/* package */ class CholeskyDecompositionImpl implements CholeskyDecomposition, Serializable {
  private static final long serialVersionUID = -4233167504993659845L;
  // ---
  private final Tensor l;
  private final Tensor d;

  /** @param A hermitian matrix */
  public CholeskyDecompositionImpl(Tensor A) {
    int n = A.length();
    l = IdentityMatrix.of(n);
    d = Array.zeros(n);
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < i; ++j) {
        Tensor lik = l.get(i).extract(0, j);
        Tensor ljk = l.get(j).extract(0, j).map(Conjugate.FUNCTION);
        Scalar value = A.Get(i, j).subtract(lik.dot(d.extract(0, j).pmul(ljk)));
        if (Scalars.nonZero(value))
          l.set(value.divide(d.Get(j)), i, j);
      }
      Tensor lik = l.get(i).extract(0, i);
      Tensor ljk = lik.map(Conjugate.FUNCTION); // variable name is deliberate
      d.set(A.get(i, i).subtract(lik.dot(d.extract(0, i).pmul(ljk))), i);
    }
  }

  @Override // from CholeskyDecomposition
  public Tensor getL() {
    return l;
  }

  @Override // from CholeskyDecomposition
  public Tensor diagonal() {
    return d;
  }

  @Override // from CholeskyDecomposition
  public Scalar det() {
    return Times.pmul(d).Get();
  }
}
