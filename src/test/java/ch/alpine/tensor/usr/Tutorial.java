// code by jph
package ch.alpine.tensor.usr;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/* package */ class Tutorial {
  public static void demoA() {
    Scalar a = RealScalar.of(2); // exact number 2 == 2/1
    Scalar b = RationalScalar.of(5, 3); // exact fraction 5/3
    Scalar c = RealScalar.of(4.56); // decimal 4.56 in numerical precision
    System.out.println(a.add(b));
    System.out.println(b.add(c));
    System.out.println(a.multiply(b));
  }

  public static void demoB() {
    Tensor v1 = Tensors.vector(5, -1, 7.3);
    Scalar a = RealScalar.of(2); // exact number 2 == 2/1
    Scalar b = RationalScalar.of(5, 3); // exact fraction 5/3
    Scalar c = RealScalar.of(4.56); // decimal 4.56 in numerical precision
    Tensor v2 = Tensors.of(a, b, c);
    Tensor matrix = Tensors.of(v1, v2);
    System.out.println(matrix);
  }

  public static void main(String[] args) {
    demoA();
    demoB();
  }
}
