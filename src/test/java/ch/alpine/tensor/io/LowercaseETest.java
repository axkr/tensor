// code by jph
package ch.alpine.tensor.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.ext.ReadLine;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.SquareMatrixQ;
import junit.framework.TestCase;

/** reported by mgini:
 * 
 * When exporting numeric values with MATLAB
 * there is a risk of using lowercase 'e' for the exponent.
 * For example: "1.23e-45"
 * The tensor library parses the expression as a {@link StringScalar},
 * which results in errors further down the pipeline.
 * 
 * For now, the solution is
 * 1) to fix this in MATLAB. For instance to ensure that uppercase 'E' is used. Can fprintf do that?
 * 2) use the function importMatlabCsv(...) below on csv files that encode numeric expressions exclusively
 * 
 * After importing the csv file using {@link Import}
 * the check StringScalarQ.any(tensor) should return false. */
public class LowercaseETest extends TestCase {
  private static final String RESOURCE = "/io/lowercase_e.csv";

  public void testConventional() {
    Tensor tensor = ResourceData.of(RESOURCE);
    assertTrue(StringScalarQ.any(tensor));
    assertEquals(tensor.length(), 6);
    assertEquals(tensor.get(0).length(), 3);
    MatrixQ.require(tensor.extract(0, 3));
    assertTrue(SquareMatrixQ.of(tensor.extract(0, 3)));
    assertEquals(Dimensions.of(tensor.extract(3, 6)), Arrays.asList(3, 2));
  }

  public static Tensor importMatlabCsv(String string) throws IOException {
    try (InputStream inputStream = ResourceData.class.getResourceAsStream(string)) { // auto closeable
      return XsvFormat.CSV.parse(ReadLine.of(inputStream).map(String::toUpperCase));
    }
  }

  public void testUppercase() throws IOException {
    Tensor tensor = importMatlabCsv(RESOURCE);
    assertFalse(StringScalarQ.any(tensor));
    assertEquals(tensor.length(), 6);
    assertEquals(tensor.get(0).length(), 3);
    MatrixQ.require(tensor.extract(0, 3));
    assertTrue(SquareMatrixQ.of(tensor.extract(0, 3)));
    assertEquals(Dimensions.of(tensor.extract(3, 6)), Arrays.asList(3, 2));
  }
}
