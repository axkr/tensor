// code by jph
package ch.ethz.idsc.tensor.pdf;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class CDFTest extends TestCase {
  public void testCDFFail() {
    Distribution distribution = ErlangDistribution.of(3, RealScalar.of(0.3));
    AssertFail.of(() -> CDF.of(distribution));
  }

  public void testNullFail() {
    AssertFail.of(() -> CDF.of(null));
  }
}
