// code by jph
package ch.alpine.tensor.mat.re;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.io.ResourceData;
import ch.alpine.tensor.lie.LeviCivitaTensor;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.num.GaussScalar;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Entrywise;
import ch.alpine.tensor.sca.N;
import ch.alpine.tensor.usr.AssertFail;

public class DetTest {
  @Test
  public void testEmpty() {
    AssertFail.of(() -> Det.of(Tensors.empty()));
  }

  @Test
  public void testEmptyMatrix() {
    Tensor m = Tensors.matrix(new Number[][] { {} });
    // this is consistent with Mathematica
    // Mathematica throws an exception
    AssertFail.of(() -> Det.of(m));
  }

  @Test
  public void testDet1() {
    Tensor m = Tensors.matrix(new Number[][] { //
        { +2, 3, 4 }, //
        { +0, 0, 1 }, //
        { -5, 3, 4 } });
    assertEquals(Det.of(m), RealScalar.of(-21));
  }

  @Test
  public void testDet2() {
    Tensor m = Tensors.matrix(new Number[][] { //
        { -2, 3, 4 }, //
        { +0, 0, 1 }, //
        { -5, 3, 4 } });
    assertEquals(Det.of(m), RealScalar.of(-9));
  }

  @Test
  public void testDet3() {
    Tensor m = Tensors.matrix(new Number[][] { //
        { -2, 3, +4 }, //
        { +0, 2, -1 }, //
        { -5, 3, +4 } });
    assertEquals(Det.of(m), RealScalar.of(33));
  }

  @Test
  public void testId() {
    for (int n = 1; n < 10; ++n)
      assertEquals(Det.of(IdentityMatrix.of(n)), RealScalar.ONE);
  }

  @Test
  public void testReversedId() {
    Tensor actual = Tensors.vector(0, 1, -1, -1, 1, 1, -1, -1, 1, 1, -1, -1, 1, 1, -1, -1, 1, 1);
    for (int n = 1; n < 10; ++n) {
      Tensor mat = Reverse.of(IdentityMatrix.of(n));
      Scalar det = Det.of(mat);
      assertEquals(det, actual.Get(n));
    }
  }

  @Test
  public void testDet4() {
    Tensor m = Tensors.matrix(new Number[][] { //
        { -2, 3, +4, 0 }, //
        { +0, 2, -1, 2 }, //
        { -5, 3, +4, 1 }, //
        { +0, 2, -1, 0 } //
    });
    assertEquals(Det.of(m), RealScalar.of(-66));
    m.set(RealScalar.of(9), 3, 0);
    assertEquals(Det.of(m), RealScalar.of(33));
  }

  @Test
  public void testNonSquare() {
    Tensor m = Tensors.matrix(new Number[][] { //
        { -2, 3, +4, 0 }, //
        { +0, 2, -1, 2 }, //
    });
    AssertFail.of(() -> Det.of(m));
  }

  @Test
  public void testNonSquare2() {
    Tensor m = Tensors.matrix(new Number[][] { //
        { -2, 3, +4 }, //
        { +0, 2, -1 }, //
        { -5, 3, +4 }, //
        { +0, 2, -1 } //
    });
    AssertFail.of(() -> Det.of(m));
  }

  @Test
  public void testComplex1() {
    Tensor re = Tensors.matrix(new Number[][] { //
        { 0, 0, 3 }, //
        { -2, 0, 0 }, //
        { -3, 0, 2 } //
    });
    Tensor im = Tensors.matrix(new Number[][] { //
        { 0, 0, -2 }, //
        { -1, 9, 0 }, //
        { 8, 0, 1 } //
    });
    Tensor matrix = Entrywise.with(ComplexScalar::of).apply(re, im);
    assertEquals(Det.of(matrix), ComplexScalar.of(270, -63));
  }

  @Test
  public void testComplex2() {
    Tensor re = Tensors.matrix(new Number[][] { //
        { 5, 0, 3 }, //
        { -2, 0, 0 }, //
        { -3, 0, 2 } //
    });
    Tensor im = Tensors.matrix(new Number[][] { //
        { -9, 0, -2 }, //
        { -1, 9, 0 }, //
        { 8, 0, 1 } //
    });
    Tensor matrix = Entrywise.with(ComplexScalar::of).apply(re, im);
    assertEquals(Det.of(matrix), ComplexScalar.of(387, 108));
  }

  @Test
  public void testComplex3() {
    Tensor re = Tensors.matrix(new Number[][] { //
        { 5, 0, 3 }, //
        { -2, 0, 0 }, //
        { -3, -4, 2 } //
    });
    Tensor im = Tensors.matrix(new Number[][] { //
        { -9, 0, -2 }, //
        { -1, 9, 0 }, //
        { 8, -2, 1 } //
    });
    Tensor matrix = Entrywise.with(ComplexScalar::of).apply(re, im);
    assertEquals(Det.of(matrix), ComplexScalar.of(421, 120));
  }

  @Test
  public void testSingular() {
    for (Pivot pivot : Pivots.values()) {
      assertEquals(Det.of(Array.zeros(5, 5), pivot), RealScalar.ZERO);
      AssertFail.of(() -> Det.of(Array.zeros(2, 5), pivot));
      AssertFail.of(() -> Det.of(Array.zeros(5, 2), pivot));
    }
  }

  @Test
  public void testSingularFail() {
    AssertFail.of(() -> Det.of(Array.zeros(2, 5)));
    AssertFail.of(() -> Det.of(Array.zeros(5, 2)));
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> Det.of(Array.zeros(5, 2), null));
    AssertFail.of(() -> Det.of(Array.zeros(2, 5), null));
  }

  // https://ch.mathworks.com/help/matlab/ref/det.html
  @Test
  public void testMatlabEx() {
    Tensor matrix = ResourceData.of("/mat/det0-matlab.csv");
    Scalar det = Det.of(matrix);
    assertEquals(det, RealScalar.ZERO);
    // Matlab gives num == 1.0597e+05 !
    // Mathematica gives num == 44934.8 !
    Scalar num1 = Det.of(N.DOUBLE.of(matrix)); // indeed, our algo is no different:
    // num1 == 105968.67122221774
    num1.toString(); // to eliminate warning
    Scalar num2 = Det.of(N.DOUBLE.of(matrix), Pivots.FIRST_NON_ZERO); // indeed, our algo is no different:
    // num2 == 105968.67122221774
    num2.toString(); // to eliminate warning
  }

  @Test
  public void testHilbert() {
    Scalar det = Det.of(HilbertMatrix.of(8));
    assertEquals(det, RationalScalar.of( //
        BigInteger.ONE, new BigInteger("365356847125734485878112256000000")));
  }

  @Test
  public void testHilbert2() {
    Scalar det = Det.of(HilbertMatrix.of(8), Pivots.FIRST_NON_ZERO);
    assertEquals(det, Scalars.fromString("1/365356847125734485878112256000000"));
  }

  @Test
  public void testGaussScalar() {
    int n = 7;
    int prime = 7879;
    Random random = new Random();
    Tensor matrix = Tensors.matrix((i, j) -> GaussScalar.of(random.nextInt(), prime), n, n);
    assertTrue(Det.of(matrix) instanceof GaussScalar);
  }

  @Test
  public void testUnitsSingle() {
    Tensor tensor = Tensors.fromString("{{1[m], 2}, {4, 5[m]}, {3, 5}}");
    AssertFail.of(() -> Det.of(tensor));
  }

  @Test
  public void testUnitsMixed() {
    Tensor tensor = Tensors.fromString("{{1[m], 2}, {4, 5[s]}, {3, 5}}");
    AssertFail.of(() -> Det.of(tensor));
    // assertEquals(Det.of(tensor), Quantity.of(0, ""));
  }

  @Test
  public void testQuantity1() {
    Scalar qs1 = Quantity.of(1, "m");
    Scalar qs2 = Quantity.of(2, "m");
    Scalar qs3 = Quantity.of(3, "rad");
    Scalar qs4 = Quantity.of(4, "rad");
    Tensor ve1 = Tensors.of(qs1.multiply(qs1), qs2.multiply(qs3));
    Tensor ve2 = Tensors.of(qs2.multiply(qs3), qs4.multiply(qs4));
    Tensor mat = Tensors.of(ve1, ve2);
    Scalar det = Det.of(mat);
    ExactScalarQ.require(det);
    assertEquals(det, Scalars.fromString("-20[m^2*rad^2]"));
  }

  @Test
  public void testFailMatrixQ() {
    Tensor tensor = Tensors.fromString("{{1, 2, 3}, {4, 5}}");
    AssertFail.of(() -> Det.of(tensor));
  }

  @Test
  public void testFailNonArray() {
    Tensor matrix = HilbertMatrix.of(4);
    matrix.set(Tensors.vector(1, 2, 3), 1, 2);
    AssertFail.of(() -> Det.of(matrix));
  }

  @Test
  public void testFailRank3() {
    AssertFail.of(() -> Det.of(LeviCivitaTensor.of(3)));
  }

  @Test
  public void testFailScalar() {
    AssertFail.of(() -> Det.of(Pi.HALF));
  }

  @Test
  public void testFailVector() {
    AssertFail.of(() -> Det.of(Tensors.vector(1, 2, 3)));
  }

  @Test
  public void testFailRank3b() {
    AssertFail.of(() -> Det.of(Array.zeros(2, 2, 3)));
  }

  @Test
  public void testFailNull() {
    AssertFail.of(() -> Det.of(null));
    AssertFail.of(() -> Det.of(HilbertMatrix.of(3), null));
  }
}
