# ch.ethz.idsc.tensor

<a href="https://travis-ci.org/idsc-frazzoli/tensor"><img src="https://travis-ci.org/idsc-frazzoli/tensor.svg?branch=master" alt="Build Status"></a>

Library for tensor computations in Java 8.

Version `0.3.6`

Features:
* multi-dimensional arrays: scalars, vectors, matrices, n-linear forms, Lie-algebra ad-tensor, ...
* scalars are real, or complex numbers, or from finite fields, etc.
* values are encoded as exact fractions, in double precision, and as `java.math.BigDecimal`
* other projects can customize the scalars for instance to attach physical units such as `javax.measure.Unit`
* import from and export to `Mathematica`, `CSV`-, and image files

The naming of functions, as well as the string format of the expressions are inspired by Wolfram's `Mathematica`.

## Gallery

<table>
<tr>
<td>

![gammademo](https://user-images.githubusercontent.com/4012178/28755698-bdb96546-7560-11e7-88d5-2d143e155e75.png)

Gamma

<td>

![inversetrigdemo2](https://user-images.githubusercontent.com/4012178/28755697-bdb72d58-7560-11e7-8a70-3ef9d82ff48c.png)

Trigonometry

<td>

![mandelbulbdemo](https://user-images.githubusercontent.com/4012178/28755696-bd98789a-7560-11e7-8ebc-001c37f0a4fd.png)

Nylander Power

</tr>
</table>

## Code Examples

Solving systems of linear equations

    Tensor matrix = Tensors.matrixInt(new int[][] { { 2, -3, 2 }, { 4, 9, -3 }, { -1, 3, 2 } });
    System.out.println(Pretty.of(Inverse.of(matrix)));

gives

    [
     [   9/37    4/37   -3/37 ]
     [ -5/111    2/37  14/111 ]
     [   7/37   -1/37   10/37 ]
    ]

singular value decomposition of given matrix

    System.out.println(Pretty.of(SingularValueDecomposition.of(matrix).getU().map(Round._4)));

gives results in machine precision

    [
     [  0.2532   0.6307  -0.7336 ]
     [ -0.9512   0.3004  -0.0700 ]
     [ -0.1763  -0.7155  -0.6760 ]
    ]

---

The tensor library implements `Quantity`, i.e. numbers with physical units, for the purpose of demonstration and testing.
Several algorithms are verified to work with scalars of type `Quantity`.

    Tensor matrix = Tensors.fromString( //
        "{{60[m^2], 30[m*rad], 20[kg*m]}, {30[m*rad], 20[rad^2], 15[kg*rad]}, {20[kg*m], 15[kg*rad], 12[kg^2]}}", //
        Quantity::fromString);
    CholeskyDecomposition cd = CholeskyDecomposition.of(matrix);
    System.out.println(cd.diagonal());
    System.out.println(Pretty.of(cd.getL()));
    System.out.println(cd.det().divide(Quantity.of(20, "m^2*rad")));

gives

    {60[m^2], 5[rad^2], 1/3[kg^2]}
    [
     [             1              0              0 ]
     [ 1/2[m^-1*rad]              1              0 ]
     [  1/3[kg*m^-1]   1[kg*rad^-1]              1 ]
    ]
    5[kg^2*rad]

---

Linear programming

    Tensor x = LinearProgramming.maxLessEquals( //
        Tensors.vector(1, 1), // cost
        Tensors.fromString("{{4, -1}, {2, 1}, {-5, 2}}"), // matrix
        Tensors.vector(8, 7, 2)); // rhs
    System.out.println(x);

gives

    {4/3, 13/3}

---

Indices for the `set` and `get` functions start from zero like in C/Java:

    Tensor matrix = Array.zeros(3, 4);
    matrix.set(Tensors.vector(9, 8, 4, 5), 2);
    matrix.set(Tensors.vector(6, 7, 8), Tensor.ALL, 1);
    System.out.println(Pretty.of(matrix));

gives

    [
     [ 0  6  0  0 ]
     [ 0  7  0  0 ]
     [ 9  8  4  5 ]
    ]

Extraction of the 4th column

    System.out.println(matrix.get(Tensor.ALL, 3));

gives the vector

    {0, 0, 5}

---

Tensors of rank 3

    Tensor ad = LieAlgebras.so3();
    Tensor x = Tensors.vector(7, 2, -4);
    Tensor y = Tensors.vector(-3, 5, 2);
    System.out.println(ad);
    System.out.println(ad.dot(x).dot(y)); // coincides with cross product of x and y

gives

    {{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, -1, 0}, {1, 0, 0}, {0, 0, 0}}}
    {24, -2, 41}

---

Functions for complex numbers

    System.out.println(Sqrt.of(RationalScalar.of(-9, 16)));

gives

    3/4*I

---

High precision

    System.out.println(Det.of(HilbertMatrix.of(8)));

gives

    1/365356847125734485878112256000000

---

Null-space

    Tensor matrix = Tensors.fromString("{{-1/3, 0, I}}");
    System.out.println(Pretty.of(NullSpace.of(matrix)));

gives

    [
     [    1     0  -I/3 ]
     [    0     1     0 ]
    ]

---

Statistics

    Distribution distribution = HypergeometricDistribution.of(10, 50, 100);
    System.out.println(RandomVariate.of(distribution, 20));

gives

    {6, 5, 1, 4, 3, 4, 7, 5, 7, 4, 6, 3, 5, 4, 5, 4, 6, 2, 6, 7}

and

    PDF pdf = PDF.of(distribution);
    System.out.println("P(X=3)=" + pdf.at(RealScalar.of(3)));

gives

    P(X=3)=84000/742729

---

Image synthesis

    int n = 251;
    Export.of(new File("image.png"), Tensors.matrix((i, j) -> //
    Tensors.of(RealScalar.of(i), RealScalar.of(j), GaussScalar.of(i + 2 * j, n), GaussScalar.of(i * j, n)), n, n));

gives

![gauss_scalar](https://cloud.githubusercontent.com/assets/4012178/26045629/63b756ee-394b-11e7-85f4-d9121905badd.png)

---

Several functions support evaluation to higher than machine precision for type `DecimalScalar`.

    System.out.println(Exp.of(DecimalScalar.of(10)));
    System.out.println(Sqrt.of(DecimalScalar.of(2)));

gives

    220255.6579480671651695790064528423`34
    1.414213562373095048801688724209698`34

The number after the prime indicates the precision of the decimal.
The string representation is compatible with `Mathematica`.

## Include in your project

Modify the `pom` file of your project to specify `repository` and `dependency` of the tensor library:

    <repositories>
      <repository>
        <id>tensor-mvn-repo</id>
        <url>https://raw.github.com/idsc-frazzoli/tensor/mvn-repo/</url>
        <snapshots>
          <enabled>true</enabled>
          <updatePolicy>always</updatePolicy>
        </snapshots>
      </repository>
    </repositories>
    
    <dependencies>
      <dependency>
        <groupId>ch.ethz.idsc</groupId>
        <artifactId>tensor</artifactId>
        <version>0.3.6</version>
      </dependency>
    </dependencies>

The source code is attached to every release.

*Note*: If your IDE or maven compiler fails to download the repository automatically, you can place the binary files from the branch mvn-repo manually in the target location rooted in your user directory

    ~/.m2/repository/ch/ethz/idsc/tensor/0.3.6/*

## Optional

Clone the repository.

The `javadoc` API can be generated with

    .../tensor/mvn javadoc:javadoc

Subsequently, the documentation is accessible through the file

    .../tensor/target/site/apidocs/index.html

## References

The library is used in the projects:
* `matsim`
* `owly`
* `subare`
* `owly3d`
* `SwissTrolley+`
* `retina`
* `queuey`
* `SimBus`
* `lcm-java`

The repository has over `1370` unit tests.