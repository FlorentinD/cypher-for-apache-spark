package org.opencypher.spark.api.spark

import org.apache.spark.sql.Row
import org.opencypher.spark.SparkCypherTestSuite
import org.opencypher.spark.api.ir.global.TokenRegistry
import org.opencypher.spark.api.record._

class SparkCypherGraphTest extends SparkCypherTestSuite {

  implicit val space = SparkGraphSpace.empty(session, TokenRegistry.empty)

  val `:Person` =
    NodeScan.on("p" -> "ID") {
      _.build
       .withImpliedLabel("Person")
       .withOptionalLabel("Swedish" -> "IS_SWEDE")
       .withPropertyKey("name" -> "NAME")
       .withPropertyKey("lucky_number" -> "NUM")
    }
    .from(SparkCypherRecords.create(
      Seq("ID", "IS_SWEDE", "NAME", "NUM"),
      Seq(
        (1, true, "Mats", 23),
        (2, false, "Martin", 42),
        (3, false, "Max", 1337),
        (4, false, "Stefan", 9))
    ))

  val `:Book` =
    NodeScan.on("b" -> "ID") {
      _.build
        .withImpliedLabel("Book")
        .withPropertyKey("title" -> "NAME")
        .withPropertyKey("year" -> "YEAR")
    }
      .from(SparkCypherRecords.create(
        Seq("ID", "NAME", "YEAR"),
        Seq(
          (10, "1984", 1949),
          (20, "Cryptonomicon", 1999),
          (30, "The Eye of the World", 1990),
          (40, "The Circle", 2013))
      ))

  val `:KNOWS` =
    RelationshipScan.on("r" -> "ID") {
      _.from("SRC").to("DST").relType("KNOWS")
       .build
       .withPropertyKey("since" -> "SINCE")
    }
    .from(SparkCypherRecords.create(
      Seq("SRC", "ID", "DST", "SINCE"),
      Seq(
        (1, 1, 2, 2017),
        (1, 2, 3, 2016),
        (1, 3, 4, 2015),
        (2, 4, 3, 2016),
        (2, 5, 4, 2013),
        (3, 6, 4, 2016))
    ))

  test("Construct graph from single node scan") {
    val graph = SparkCypherGraph.create(`:Person`)
    val nodes = graph.nodes("n")

    nodes.details.toDF().columns should equal(Array(
      "n", "____n:Person", "____n:Swedish", "____n_dot_nameSTRING", "____n_dot_lucky_bar_numberINTEGER"
    ))

    nodes.details.toDF().collect().toSet should equal (Set(
      Row(1, true, true,    "Mats",   23),
      Row(2, true, false, "Martin",   42),
      Row(3, true, false,    "Max", 1337),
      Row(4, true, false, "Stefan",    9)
    ))
  }

  test("Construct graph from multiple node scans") {
    val graph = SparkCypherGraph.create(`:Person`, `:Book`)
    val nodes = graph.nodes("n")

    nodes.details.toDF().columns should equal(Array(
      "n", "____n:Person", "____n:Swedish", "____n:Book", "____n_dot_nameSTRING", "____n_dot_lucky_bar_numberINTEGER", "____n_dot_titleSTRING", "____n_dot_yearINTEGER"
    ))

    nodes.details.toDF().collect().toSet should equal (Set(
      Row( 1,  true,  true,  false,   "Mats",   23,                   null, null),
      Row( 2,  true,  false, false, "Martin",   42,                   null, null),
      Row( 3,  true,  false, false,    "Max", 1337,                   null, null),
      Row( 4,  true,  false, false, "Stefan",    9,                   null, null),
      Row(10, false,  false,  true,     null, null,                 "1984", 1949),
      Row(20, false,  false,  true,     null, null,        "Cryptonomicon", 1999),
      Row(30, false,  false,  true,     null, null, "The Eye of the World", 1990),
      Row(40, false,  false,  true,     null, null,           "The Circle", 2013)
    ))
  }

  ignore("Construct graph from scans") {
     val graph = SparkCypherGraph.create(`:Person`, `:KNOWS`)

     val nodes = graph.nodes("n")

     nodes shouldMatch `:Person`.records
  }
}