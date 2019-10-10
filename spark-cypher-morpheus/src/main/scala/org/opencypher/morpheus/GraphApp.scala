package org.opencypher.morpheus

import org.apache.spark.graph.api.{NodeFrame, RelationshipFrame}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.opencypher.morpheus.api.GraphSources
import org.opencypher.okapi.api.graph.Namespace

object GraphApp extends App {
  implicit val spark = SparkSession.builder().master("local[*]").getOrCreate()
  spark.sparkContext.setLogLevel("error")

//  implicit val cypherSession = SparkCypherSession.create
  implicit val cypherSession = MorpheusExternSession.create

  // SPIP API
  val nodeData: DataFrame = spark.createDataFrame(Seq(0 -> "Alice", 1 -> "Bob")).toDF("id", "name")
  val nodeFrame: NodeFrame = NodeFrame.create(df = nodeData, idColumn = "id", labelSet = Set("Person"))

  val graph = cypherSession.createGraph(Array(nodeFrame), Array.empty[RelationshipFrame])
  val result = graph.cypher("MATCH (n) RETURN n")
  result.df.show()

  // Okapi API

  // CAPSSession needs to be in implicit scope for PGDSs etc.
  implicit val caps = cypherSession.morpheus

  cypherSession.registerSource(Namespace("fs"), GraphSources.fs(getClass.getResource("/csv").getFile).csv)
  cypherSession.cypher(s"FROM GRAPH fs.products MATCH (n) RETURN n").show
  cypherSession.cypher(
    s"""
       |FROM GRAPH fs.products
       |MATCH (n)
       |CONSTRUCT
       | CREATE (n)
       |RETURN GRAPH""".stripMargin).graph.nodes("n").show
}
