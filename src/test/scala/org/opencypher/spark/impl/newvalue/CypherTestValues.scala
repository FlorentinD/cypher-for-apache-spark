package org.opencypher.spark.impl.newvalue

object CypherTestValues {

  // The inner seqs are supposed to be duplicate free
  //
  // Note: We can't use sets here as that would mean we'd use the equality in the test data that we're about to test
  //
  type ValueGroups[V] = Seq[Values[V]]
  type Values[V] = Seq[V]

  implicit val STRING_valueGroups: ValueGroups[CypherString] = Seq(
    Seq(CypherString("")),
    Seq(CypherString("  ")),
    Seq(CypherString("1234567890")),
    Seq(CypherString("a")),
    Seq(CypherString("A")),
    Seq(CypherString("AB")),
    Seq(CypherString("ABC")),
    Seq(CypherString("Is it a query, if no one sees it running?")),
    Seq(cypherNull[CypherString])
  )

  implicit val BOOLEAN_valueGroups: ValueGroups[CypherBoolean] = Seq(
    Seq(CypherBoolean(false)),
    Seq(CypherBoolean(true)),
    Seq(cypherNull[CypherBoolean])
  )

  implicit val INTEGER_valueGroups: ValueGroups[CypherInteger] = Seq(
    Seq(CypherInteger(Long.MinValue)),
    Seq(CypherInteger(-23L)),
    Seq(CypherInteger(-10)),
    Seq(CypherInteger(-10)),
    Seq(CypherInteger(-1)),
    Seq(CypherInteger(0)),
    Seq(CypherInteger(1)),
    Seq(CypherInteger(2)),
    Seq(CypherInteger(5)),
    Seq(CypherInteger(5)),
    Seq(CypherInteger(42L)),
    Seq(CypherInteger(Long.MaxValue)),
    Seq(cypherNull[CypherInteger]),
    Seq(cypherNull[CypherInteger])
  )

  implicit val FLOAT_valueGroups: ValueGroups[CypherFloat] = Seq(
    Seq(CypherFloat(Double.NegativeInfinity)),
    Seq(CypherFloat(Double.MinValue)),
    Seq(CypherFloat(-23.0d)),
    Seq(CypherFloat(-10.0d), CypherFloat(-10.0d)),
    Seq(CypherFloat(0.0d)),
    Seq(CypherFloat(2.3d)),
    Seq(CypherFloat(5.0d)),
    Seq(CypherFloat(5.1d), CypherFloat(5.1d)),
    Seq(CypherFloat(42.0d)),
    Seq(CypherFloat(Double.MaxValue)),
    Seq(CypherFloat(Double.PositiveInfinity)),
    Seq(CypherFloat(Double.NaN)),
    Seq(cypherNull[CypherFloat])
  )

  implicit val NUMBER_valueGroups: ValueGroups[CypherNumber] = Seq(
    Seq(CypherFloat(Double.NegativeInfinity)),
    Seq(CypherFloat(Double.MinValue)),
    Seq(CypherInteger(Long.MinValue)),
    Seq(CypherInteger(-23L)),
    Seq(CypherFloat(-23.0d)),
    Seq(CypherFloat(-10.0d), CypherInteger(-10), CypherFloat(-10.0d), CypherInteger(-10)),
    Seq(CypherInteger(-1), CypherFloat(-1.0d)),
    Seq(CypherInteger(0), CypherFloat(0.0d)),
    Seq(CypherInteger(1)),
    Seq(CypherFloat(2.3d)),
    Seq(CypherInteger(2)),
    Seq(CypherInteger(5), CypherInteger(5), CypherFloat(5.0d)),
    Seq(CypherFloat(5.1d), CypherFloat(5.1d)),
    Seq(CypherFloat(42.0d), CypherInteger(42L)),
    Seq(CypherInteger(Long.MaxValue)),
    Seq(CypherFloat(Double.MaxValue)),
    Seq(CypherFloat(Double.PositiveInfinity)),
    Seq(CypherFloat(Double.NaN)),
    Seq(cypherNull[CypherFloat], cypherNull[CypherInteger], cypherNull[CypherNumber])
  )

  implicit val ALL_valueGroups: ValueGroups[CypherValue] = Seq(
    Seq(CypherString("")),
    Seq(CypherString("  ")),
    Seq(CypherString("1234567890")),
    Seq(CypherString("a")),
    Seq(CypherString("A")),
    Seq(CypherString("AB")),
    Seq(CypherString("ABC")),
    Seq(CypherString("Is it a query, if no one sees it running?")),
    Seq(CypherBoolean(false)),
    Seq(CypherBoolean(true)),
    Seq(CypherFloat(Double.NegativeInfinity)),
    Seq(CypherFloat(Double.MinValue)),
    Seq(CypherInteger(Long.MinValue)),
    Seq(CypherInteger(-23L)),
    Seq(CypherFloat(-23.0d)),
    Seq(CypherFloat(-10.0d), CypherInteger(-10), CypherFloat(-10.0d), CypherInteger(-10)),
    Seq(CypherInteger(-1), CypherFloat(-1.0d)),
    Seq(CypherInteger(0), CypherFloat(0.0d)),
    Seq(CypherInteger(1)),
    Seq(CypherFloat(2.3d)),
    Seq(CypherInteger(2)),
    Seq(CypherInteger(5), CypherInteger(5), CypherFloat(5.0d)),
    Seq(CypherFloat(5.1d), CypherFloat(5.1d)),
    Seq(CypherFloat(42.0d), CypherInteger(42L)),
    Seq(CypherInteger(Long.MaxValue)),
    Seq(CypherFloat(Double.MaxValue)),
    Seq(CypherFloat(Double.PositiveInfinity)),
    Seq(CypherFloat(Double.NaN)),
    Seq(
      cypherNull[CypherValue],
      cypherNull[CypherBoolean],
      cypherNull[CypherString],
      cypherNull[CypherFloat],
      cypherNull[CypherInteger],
      cypherNull[CypherNumber]
    )
  )

  implicit final class CypherValueGroups[V <: CypherValue](elts: ValueGroups[V]) {
    def materialValueGroups: ValueGroups[V] = elts.map(_.filter(_ != cypherNull)).filter(_.isEmpty)
    def scalaValueGroups(implicit companion: CypherValueCompanion[V]): Seq[Seq[Option[Any]]] = elts.map(_.scalaValues)
    def indexed = elts.zipWithIndex.flatMap { case ((group), index) => group.map { v => index -> v } }
  }

  implicit final class CypherValues[V <: CypherValue](elts: Values[V]) {
    def scalaValues(implicit companion: CypherValueCompanion[V]): Seq[Option[Any]] = elts.map(companion.scalaValue)
  }
}