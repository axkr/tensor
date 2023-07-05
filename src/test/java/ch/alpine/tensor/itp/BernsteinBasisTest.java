// code by jph
package ch.alpine.tensor.itp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.Quaternion;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.d.BinomialDistribution;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Clips;

class BernsteinBasisTest {
  @Test
  void testSimple() {
    Tensor actual = BernsteinBasis.of(5, RationalScalar.of(2, 3));
    Tensor expect = Tensors.fromString("{1/243, 10/243, 40/243, 80/243, 80/243, 32/243}");
    assertEquals(actual, expect);
    assertEquals(Total.of(actual), RealScalar.ONE);
  }

  @Test
  void testSimpleReverse() {
    Tensor actual = BernsteinBasis.of(5, RationalScalar.of(1, 3));
    Tensor expect = Reverse.of(Tensors.fromString("{1/243, 10/243, 40/243, 80/243, 80/243, 32/243}"));
    assertEquals(actual, expect);
    assertEquals(Total.of(actual), RealScalar.ONE);
  }

  /** The weight mask is generated by the following formula
   * <pre>
   * With[{p = n / (n - 1)}, Table[Binomial[n - 1, k] (1 - p)^(n - k - 1) p^k, {k, 0, n - 1}]]
   * </pre>
   * 
   * The leading coefficient converges to
   * <pre>
   * Limit[(n/(n - 1))^(n - 1), n -> Infinity] == Exp[1]
   * </pre>
   * 
   * @param n
   * @return weight mask of length n with entries that sum up to 1 */
  public static Tensor extrapolate(int n) {
    int nm1 = Integers.requirePositive(n) - 1;
    Scalar p = RationalScalar.of(n, nm1);
    return BernsteinBasis.of(nm1, p);
  }

  /** @param n positive
   * @return vector of length n */
  public static ScalarTensorFunction of(int n) {
    int nm1 = Integers.requirePositive(n) - 1;
    return p -> BernsteinBasis.of(nm1, p);
  }

  @Test
  void testSimple1() {
    assertEquals(of(1).apply(RealScalar.of(0.2)), Tensors.vector(1));
  }

  @Test
  void testSimple2() throws ClassNotFoundException, IOException {
    ScalarTensorFunction scalarTensorFunction = Serialization.copy(of(2));
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.fromString("{1/2, 1/2}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(0)), Tensors.fromString("{1, 0}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), Tensors.fromString("{0, 1}"));
  }

  @Test
  void testSimple3() {
    ScalarTensorFunction scalarTensorFunction = of(3);
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.fromString("{1/4, 1/2, 1/4}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(0)), Tensors.fromString("{1, 0, 0}"));
    assertEquals(scalarTensorFunction.apply(RealScalar.of(1)), Tensors.fromString("{0, 0, 1}"));
  }

  @Test
  void testDistribution() {
    for (int n = 5; n < 10; ++n)
      for (Scalar p : new Scalar[] { RationalScalar.of(1, 3), RationalScalar.of(6, 7) }) {
        Tensor vector = of(n).apply(p);
        Distribution distribution = BinomialDistribution.of(n - 1, p);
        PDF pdf = PDF.of(distribution);
        Tensor cmp = Range.of(0, n).map(pdf::at);
        assertEquals(vector, cmp);
        ExactTensorQ.require(vector);
        ExactTensorQ.require(cmp);
      }
  }

  @Test
  void testFunctionMatch() {
    int n = 5;
    ScalarTensorFunction scalarTensorFunction = new BezierFunction(LinearBinaryAverage.INSTANCE, IdentityMatrix.of(n));
    Scalar p = RationalScalar.of(2, 7);
    Tensor vector = scalarTensorFunction.apply(p);
    Tensor weight = of(n).apply(p);
    ExactTensorQ.require(weight);
    ExactTensorQ.require(vector);
    assertEquals(weight, vector);
  }

  @Test
  void testExtrapolate() {
    assertEquals(extrapolate(2), Tensors.fromString("{-1, 2}"));
    assertEquals(extrapolate(3), Tensors.fromString("{1/4, -3/2, 9/4}"));
    assertEquals(extrapolate(4), Tensors.fromString("{-1/27, 4/9, -16/9, 64/27}"));
    for (int n = 2; n < 10; ++n) {
      Tensor mask = extrapolate(n);
      assertEquals(Total.of(mask), RealScalar.ONE);
      ExactTensorQ.require(mask);
    }
  }

  @Test
  void testNegFail2() {
    assertThrows(Exception.class, () -> of(0));
    assertThrows(Exception.class, () -> of(-1));
  }

  @Test
  void testQuaternion() {
    Quaternion quaternion = Quaternion.of(2, 3, 4, 5);
    assertThrows(ClassCastException.class, () -> BernsteinBasis.of(5, quaternion));
  }

  @Test
  void testTheory() {
    int n = 1207;
    Scalar p = RationalScalar.of(2, 3);
    Tensor table = BernsteinBasis.of(n, Clips.unit().requireInside(p));
    assertEquals(Total.of(table), RealScalar.ONE);
  }

  @Test
  void testNegFail() {
    assertEquals(BernsteinBasis.of(0, RationalScalar.of(2, 3)), Tensors.vector(1));
    assertThrows(IllegalArgumentException.class, () -> BernsteinBasis.of(-1, RationalScalar.of(2, 3)));
  }
}
