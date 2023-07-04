// code by jph
package ch.alpine.tensor.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.pi.LeastSquares;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.spa.Nnz;
import ch.alpine.tensor.spa.SparseArray;

class MatrixMarketTest {
  @Test
  void testCoordinate() {
    Tensor matrix = Import.of("/ch/alpine/tensor/io/mtx/well1033.mtx.gz");
    Tensor block = matrix.block(List.of(0, 0), List.of(7, 10));
    assertFalse(Chop._04.allZero(block));
    assertEquals(block.get(0), block.get(1));
    assertEquals(block.get(0), block.get(6));
    assertEquals(Dimensions.of(matrix), List.of(1033, 320));
    assertFalse(ExactTensorQ.of(matrix));
    assertEquals(Nnz.of((SparseArray) matrix), 4732);
    // System.out.println(MatrixRank.of(matrix));
    // LeastSquares.of(matrix, matrix);
  }

  @Test
  void testCoordinateSymmetric() {
    Tensor matrix = Import.of("/ch/alpine/tensor/io/mtx/bcsstk13.mtx.gz");
    SymmetricMatrixQ.require(matrix);
  }

  @Test
  void testArray() {
    Tensor matrix = Import.of("/ch/alpine/tensor/io/mtx/well1033_rhs1.mtx.gz");
    assertEquals(Dimensions.of(matrix), List.of(1033, 1));
    assertFalse(Chop._04.allZero(matrix));
  }

  @Test
  @Disabled
  void testLeastSquares() {
    Tensor matrix = Import.of("/ch/alpine/tensor/io/mtx/well1033.mtx.gz");
    Tensor rhs = Flatten.of(Import.of("/ch/alpine/tensor/io/mtx/well1033_rhs1.mtx.gz"));
    // TODO TENSOR IMPL for some reason slows down at k=192
    LeastSquares.of(matrix, rhs);
  }

  @Test
  void testExportFail() {
    Tensor matrix = Array.zeros(2, 3);
    assertThrows(UnsupportedOperationException.class, () -> Export.of(HomeDirectory.file("matrix.mtx"), matrix));
  }

  @Test
  void testModifierNonPublic() {
    assertFalse(Modifier.isPublic(MatrixMarket.class.getModifiers()));
  }
}
