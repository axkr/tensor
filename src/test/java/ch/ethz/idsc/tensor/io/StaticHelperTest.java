// code by jph
package ch.ethz.idsc.tensor.io;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.qty.Quantity;
import junit.framework.TestCase;

public class StaticHelperTest extends TestCase {
  public void testParseString() {
    Object object = StaticHelper.parse(String.class, "ethz idsc ");
    assertEquals(object, "ethz idsc ");
  }

  public void testParseScalar() {
    Object object = StaticHelper.parse(Scalar.class, " 3/4+8*I[m*s^-2]");
    Scalar scalar = Quantity.of(ComplexScalar.of(RationalScalar.of(3, 4), RealScalar.of(8)), "m*s^-2");
    assertEquals(object, scalar);
  }

  public void testParseFile() {
    Object object = StaticHelper.parse(File.class, "/home/datahaki/file.txt");
    assertEquals(object, new File("/home/datahaki/file.txt"));
  }

  public void testParseBoolean() {
    Object object = StaticHelper.parse(Boolean.class, "true");
    assertEquals(object, Boolean.TRUE);
  }

  public void testIsTracked() {
    Field[] fields = ParamContainer.class.getFields();
    int count = 0;
    for (Field field : fields)
      count += StaticHelper.isTracked(field) ? 1 : 0;
    ParamContainer paramContainer = new ParamContainer();
    TensorProperties tensorProperties = TensorProperties.wrap(paramContainer);
    int count2 = (int) tensorProperties.fields().count();
    assertEquals(count, count2);
    assertEquals(count, 5);
  }

  public void testParseFail() {
    try {
      StaticHelper.parse(Integer.class, "123");
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testPackageVisibility() {
    assertFalse(Modifier.isPublic(StaticHelper.class.getModifiers()));
  }
}
