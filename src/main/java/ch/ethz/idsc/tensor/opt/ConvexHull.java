// code by jph
package ch.ethz.idsc.tensor.opt;

import java.util.stream.Stream;

import ch.ethz.idsc.tensor.Tensor;

/** Computation of convex hull of point cloud in 2D
 * 
 * <p>When {x, y} are taken as pixel coordinates, the ordering appears clockwise.
 * 
 * <p>Clusters of three points in numerical precision with cross product p1-p2 x p3-p2
 * of norm below 1e-15 are treated as a single coordinate.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/ConvexHull.html">ConvexHull</a> */
public enum ConvexHull {
  ;
  /** @param tensor of n x 2 coordinates
   * @return points in counter-clockwise order with no 3 co-linear points
   * @throws Exception for input of invalid format */
  public static Tensor of(Tensor tensor) {
    return of(tensor.stream());
  }

  /** @param stream of 2-vectors
   * @return points in counter-clockwise order with no 3 co-linear points
   * @throws Exception for input of invalid format */
  public static Tensor of(Stream<Tensor> stream) {
    return GrahamScan.of(stream);
  }
}
