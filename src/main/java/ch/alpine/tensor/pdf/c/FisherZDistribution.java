// code by jph
package ch.alpine.tensor.pdf.c;

import java.io.Serializable;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.MeanInterface;
import ch.alpine.tensor.pdf.PDF;
import ch.alpine.tensor.pdf.VarianceInterface;

class FisherZDistribution implements Distribution, //
    PDF, MeanInterface, VarianceInterface, Serializable {
  @Override
  public Scalar at(Scalar x) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Scalar mean() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Scalar variance() {
    // TODO Auto-generated method stub
    return null;
  }
}