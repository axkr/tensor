// code by jph
package ch.alpine.tensor.mat.ev;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.HermitianMatrixQ;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.NullSpace;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.UnitaryMatrixQ;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.red.Entrywise;
import junit.framework.TestCase;

public class JacobiComplexTest extends TestCase {
  private static void _check(Tensor matrix, Eigensystem eigensystem) {
    Tensor v = eigensystem.vectors();
    UnitaryMatrixQ.require(v);
    Tensor diagon = DiagonalMatrix.with(eigensystem.values());
    Tensor m1 = BasisTransform.ofMatrix(diagon, v);
    Tolerance.CHOP.requireClose(m1, matrix);
    Tensor m2 = BasisTransform.of(diagon, 1, v);
    Tolerance.CHOP.requireClose(m2, matrix);
  }

  public void testHermitian() {
    Distribution distribution = TriangularDistribution.with(0, 1);
    for (int n = 1; n < 6; ++n) {
      Tensor real = Symmetrize.of(RandomVariate.of(distribution, n, n));
      Tensor imag = TensorWedge.of(RandomVariate.of(distribution, n, n));
      Tensor matrix = Entrywise.with(ComplexScalar::of).apply(real, imag);
      HermitianMatrixQ.require(matrix);
      JacobiComplex jacobiComplex = new JacobiComplex(matrix, Tolerance.CHOP);
      Tensor h = jacobiComplex.package_H();
      Tolerance.CHOP.requireClose(DiagonalMatrix.with(Diagonal.of(h)), h);
      Tolerance.CHOP.requireClose( //
          BasisTransform.ofMatrix(h, jacobiComplex.vectors()), //
          matrix);
      _check(matrix, jacobiComplex);
    }
  }

  public void testComplex() {
    Tensor matrix = Tensors.fromString("{{2, 3-3*I}, {3+3*I, 5}}");
    HermitianMatrixQ.require(matrix);
    Scalar det = Det.of(matrix);
    assertEquals(det, RealScalar.of(-8));
    Tensor ns8 = NullSpace.of(matrix.subtract(IdentityMatrix.of(2).multiply(RealScalar.of(8))));
    assertEquals(ns8.length(), 1);
    Tensor ns1 = NullSpace.of(matrix.subtract(IdentityMatrix.of(2).multiply(RealScalar.of(-1))));
    assertEquals(ns1.length(), 1);
    Eigensystem eigensystem = Eigensystem.ofHermitian(matrix);
    Tolerance.CHOP.requireClose(eigensystem.values(), Tensors.vector(8, -1));
    _check(matrix, eigensystem);
  }

  public void testComplexUnits() {
    Tensor matrix = Tensors.fromString("{{2[m], 3-3*I[m]}, {3+3*I[m], 5[m]}}");
    Eigensystem eigensystem = Eigensystem.ofHermitian(matrix);
    _check(matrix, eigensystem);
  }

  public void testRealComparison() {
    Distribution distribution = TriangularDistribution.with(0, 1);
    for (int n = 1; n < 6; ++n) {
      Tensor matrix = Symmetrize.of(RandomVariate.of(distribution, n, n));
      Eigensystem e1 = Eigensystem.ofHermitian(matrix);
      Eigensystem e2 = Eigensystem.ofSymmetric(matrix);
      _check(matrix, e1);
      _check(matrix, e2);
      Tolerance.CHOP.requireClose(e1.values(), e2.values());
    }
  }
}
