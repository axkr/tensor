// code by jph
package ch.ethz.idsc.tensor;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.usr.AssertFail;
import junit.framework.TestCase;

public class UnprotectTest extends TestCase {
  public void testUsingEmpty() {
    assertEquals(Unprotect.using(new LinkedList<>()), Tensors.empty());
    assertEquals(Unprotect.using(new LinkedList<>()), Tensors.unmodifiableEmpty());
    assertEquals(Unprotect.using(Arrays.asList()), Tensors.empty());
    assertEquals(Unprotect.using(Arrays.asList()), Tensors.unmodifiableEmpty());
  }

  public void testUsingListScalar() {
    List<Tensor> list = Arrays.asList(RealScalar.of(4), RealScalar.of(5));
    Tensor tensor = Unprotect.using(list);
    assertEquals(tensor, Tensors.vector(4, 5));
  }

  public void testUsingCopyOnWrite() {
    List<Tensor> list = new CopyOnWriteArrayList<>();
    Tensor tensor = Unprotect.using(list);
    tensor.append(RealScalar.of(2));
    tensor.append(RealScalar.of(6));
    assertEquals(tensor, Tensors.vector(2, 6));
  }

  public void testUsingNCopies() {
    Tensor tensor = Unprotect.using(Collections.nCopies(5, RealScalar.of(2)));
    assertEquals(tensor, Tensors.vector(2, 2, 2, 2, 2));
    AssertFail.of(() -> tensor.append(RealScalar.ONE));
  }

  public void testEmptyLinkedListUnmodifiable() {
    AssertFail.of(() -> Unprotect.using(new LinkedList<>()).unmodifiable().append(RealScalar.ZERO));
  }

  public void testByref() {
    Tensor beg = Tensors.vector(1, 2, 3);
    Tensor byref = Unprotect.byRef(beg, beg, beg);
    byref.set(RealScalar.ZERO, 0, 0);
    assertEquals(beg, Tensors.vector(0, 2, 3));
    assertEquals(byref, Tensors.fromString("{{0, 2, 3}, {0, 2, 3}, {0, 2, 3}}"));
  }

  public void testByrefFail() {
    Tensor beg = Tensors.vector(1, 2, 3);
    Tensor byref = Unprotect.byRef(beg, null, beg);
    byref.get(0);
    byref.get(2);
    AssertFail.of(() -> byref.get(1)); // invokes copy() on the entry
    AssertFail.of(() -> byref.extract(0, 3)); // invokes copy() on the entries
  }

  public void testDimension1() {
    assertTrue(Unprotect.dimension1(Tensors.vector(1, 2, 3)) == Scalar.LENGTH);
    assertTrue(Unprotect.dimension1(HilbertMatrix.of(2, 4)) == 4);
    assertTrue(Unprotect.dimension1(Array.zeros(2, 3, 4)) == 3);
  }

  public void testFailEmpty() {
    AssertFail.of(() -> Unprotect.dimension1(Tensors.empty()));
  }

  public void testFail1() {
    Tensor unstruct = Tensors.fromString("{{-1, 0, 1, 2}, {3, 4, 5}}");
    assertEquals(unstruct.length(), 2);
    AssertFail.of(() -> Unprotect.dimension1(unstruct));
  }

  public void testFail2() {
    AssertFail.of(() -> Unprotect.dimension1(RealScalar.ONE));
  }

  public void testReferencesScalar() {
    AssertFail.of(() -> Unprotect.references(RealScalar.ONE));
  }

  public void testReferencesNull() {
    AssertFail.of(() -> Unprotect.references(null));
  }
}
