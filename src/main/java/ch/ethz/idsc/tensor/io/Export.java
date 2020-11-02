// code by jph
package ch.ethz.idsc.tensor.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.ObjectFormat;

/** supported file formats are: BMP, CSV, CSV.GZ, JPG, M, PNG
 * 
 * <p>Hint: Do not use Export when exchanging {@link Tensor}s with
 * Mathematica. For that purpose use {@link Put} and {@link Get}.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Export.html">Export</a>
 * 
 * @see Import
 * @see Put */
public enum Export {
  ;
  /** See the documentation of {@link CsvFormat}, {@link ImageFormat}, and {@link MatlabExport}
   * for information on how tensors are encoded in the respective format.
   * 
   * <p>If the extension of the given file is not used in the tensor library, an exception
   * is thrown, and the file will not be created.
   * 
   * @param file destination
   * @param tensor non-null
   * @throws IOException */
  public static void of(File file, Tensor tensor) throws IOException {
    Filename filename = new Filename(file.getName());
    filename.extension(); // known extension
    Objects.requireNonNull(tensor); // tensor non-null
    try (OutputStream outputStream = new FileOutputStream(file)) {
      ExportHelper.of(filename, tensor, outputStream);
    }
  }

  /** export function for Java objects that implement {@link Serializable}.
   * To retrieve the object, use {@link Import#object(File)}.
   * 
   * @param file
   * @param object non-null that implements {@link Serializable}
   * @throws IOException */
  public static void object(File file, Object object) throws IOException {
    Files.write(file.toPath(), ObjectFormat.of(Objects.requireNonNull(object)));
  }
}
