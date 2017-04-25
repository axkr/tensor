// code by jph
// https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html#valueOf(java.lang.String)
package ch.ethz.idsc.tensor;

enum StaticHelper {
  ;
  /** code from java.lang.Double */
  private static final String Digits = "(\\p{Digit}+)";
  private static final String HexDigits = "(\\p{XDigit}+)";
  // an exponent is 'e' or 'E' followed by an optionally
  // signed decimal integer.
  private static final String Exp = "[eE][+-]?" + Digits;
  // optional leading and trailing whitespace and sign is obsolete
  static final String fpRegex = ("(" + //
      "NaN|" + // "NaN" string
      "Infinity|" + // "Infinity" string
      // A decimal floating-point string representing a finite positive
      // number without a leading sign has at most five basic pieces:
      // Digits . Digits ExponentPart FloatTypeSuffix
      //
      // Since this method allows integer-only strings as input
      // in addition to strings of floating-point literals, the
      // two sub-patterns below are simplifications of the grammar
      // productions from section 3.10.2 of
      // The Java Language Specification.
      // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
      "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +
      // . Digits ExponentPart_opt FloatTypeSuffix_opt
      "(\\.(" + Digits + ")(" + Exp + ")?)|" +
      // Hexadecimal strings
      "((" +
      // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
      "(0[xX]" + HexDigits + "(\\.)?)|" +
      // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
      "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" + ")[pP][+-]?" + Digits + "))" + "[fFdD]?))" //
  );
}
