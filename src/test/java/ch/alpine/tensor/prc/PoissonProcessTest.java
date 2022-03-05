// code by jph
package ch.alpine.tensor.prc;

import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.qty.Quantity;
import junit.framework.TestCase;

public class PoissonProcessTest extends TestCase {
  public void testSimple() {
    Random random = new Random(1);
    PoissonProcess poissonProcess = new PoissonProcess(Quantity.of(0.2, "s^-1"));
    Tensor e0 = poissonProcess.next(random);
    // System.out.println(e0);
    Tensor e1 = poissonProcess.next(random);
    // System.out.println(e1);
    Tensor e2 = poissonProcess.next(random);
    // System.out.println(e2);
    e0.add(e1).add(e2);
  }
}