// code by jph
package ch.alpine.tensor.fft;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Inverse;

class FourierDCTMatrixTest {
  @RepeatedTest(8)
  void test23(RepetitionInfo repetitionInfo) {
    int n = repetitionInfo.getCurrentRepetition();
    Tensor matrix2 = FourierDCTMatrix._2.of(n);
    Tensor matrix3 = FourierDCTMatrix._3.of(n);
    Tensor result = matrix2.dot(matrix3);
    Tolerance.CHOP.requireClose(result, IdentityMatrix.of(n));
  }

  @RepeatedTest(8)
  void test4(RepetitionInfo repetitionInfo) {
    int n = repetitionInfo.getCurrentRepetition();
    Tensor matrix = FourierDCTMatrix._4.of(n);
    Tolerance.CHOP.requireClose(matrix, Inverse.of(matrix));
  }

  @Test
  void testSpecific() {
    Scalar scalar = FourierDCTMatrix._2.of(7).Get(5, 6);
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(-0.2356569943861637));
  }
}