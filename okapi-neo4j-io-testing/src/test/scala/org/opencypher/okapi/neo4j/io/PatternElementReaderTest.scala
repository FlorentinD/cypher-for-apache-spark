/*
 * Copyright (c) 2016-2019 "Neo4j Sweden, AB" [https://neo4j.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Attribution Notice under the terms of the Apache License 2.0
 *
 * This work was created by the collective efforts of the openCypher community.
 * Without limiting the terms of Section 6, any Derivative Work that is not
 * approved by the public consensus process of the openCypher Implementers Group
 * should not be described as “Cypher” (and Cypher® is a registered trademark of
 * Neo4j Inc.) or as "openCypher". Extensions by implementers or prototypes or
 * proposals for change that have been documented or implemented should only be
 * described as "implementation extensions to Cypher" or as "proposed changes to
 * Cypher that are not yet approved by the openCypher community".
 */
package org.opencypher.okapi.neo4j.io

import org.opencypher.okapi.api.schema.PropertyGraphSchema
import org.opencypher.okapi.api.types.{CTBoolean, CTFloat, CTInteger, CTString}
import org.opencypher.okapi.neo4j.io.Neo4jHelpers.Neo4jDefaults._
import org.opencypher.okapi.testing.BaseTestSuite

class PatternElementReaderTest extends BaseTestSuite {

  private val schema = PropertyGraphSchema.empty
    .withNodePropertyKeys("A")("foo" -> CTInteger, "bar" -> CTString.nullable)
    .withNodePropertyKeys("B")()
    .withNodePropertyKeys(s"${metaPrefix}C")()
    .withRelationshipPropertyKeys("TYPE")("foo" -> CTFloat.nullable, "f" -> CTBoolean)
    .withRelationshipPropertyKeys("TYPE2")()

  it("constructs flat node queries from schema") {
    ElementReader.flatExactLabelQuery(Set("A"), schema) should equal(
      s"""|MATCH ($elementVarName:`A`)
          |WHERE length(labels($elementVarName)) = 1
          |RETURN id($elementVarName) AS $idPropertyKey, $elementVarName.bar, $elementVarName.foo""".stripMargin
    )
  }

  it("constructs flat node queries from schema without properties") {
    ElementReader.flatExactLabelQuery(Set("B"), schema) should equal(
      s"""|MATCH ($elementVarName:`B`)
          |WHERE length(labels($elementVarName)) = 1
          |RETURN id($elementVarName) AS $idPropertyKey""".stripMargin
    )
  }

  it("constructs flat relationship queries from schema") {
    ElementReader.flatRelTypeQuery("TYPE", schema) should equal(
      s"""|MATCH (s)-[$elementVarName:TYPE]->(t)
          |RETURN id($elementVarName) AS $idPropertyKey, id(s) AS $startIdPropertyKey, id(t) AS $endIdPropertyKey, $elementVarName.f, $elementVarName.foo""".stripMargin
    )
  }

  it("constructs flat relationship queries from schema with no properties") {
    ElementReader.flatRelTypeQuery("TYPE2", schema) should equal(
      s"""|MATCH (s)-[$elementVarName:TYPE2]->(t)
          |RETURN id($elementVarName) AS $idPropertyKey, id(s) AS $startIdPropertyKey, id(t) AS $endIdPropertyKey""".stripMargin
    )
  }

}
