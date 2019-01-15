/*
 * Copyright (c) 2016-2018 "Neo4j Sweden, AB" [https://neo4j.com]
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
package org.opencypher.okapi.ir.impl.parse.functions

import org.opencypher.okapi.api.types.CTVoid
import org.opencypher.v9_0.expressions._
import org.opencypher.v9_0.expressions.functions.Function
import org.opencypher.v9_0.util.symbols._

case object FunctionLookup {

  def apply(name: String): Vector[TypeSignature] = name match {
    case Timestamp.name => Timestamp.signatures
    case DateTime.name => DateTime.signatures
    case Date.name => Date.signatures
    case Duration.name => Duration.signatures
    case _ => Vector.empty
  }

}

case object Timestamp extends Function with TypeSignatures {
  override val name = "timestamp"

  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(), outputType = CTInteger)
  )
}

case object DateTime extends Function with TypeSignatures {
  override val name = "datetime"

  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(CTString), outputType = CTDateTime),
    TypeSignature(argumentTypes = Vector(CTMap), outputType = CTDateTime),
    TypeSignature(argumentTypes = Vector(), outputType = CTDateTime)
  )
}

case object Date extends Function with TypeSignatures {
  override val name = "date"

  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(CTString), outputType = CTDate),
    TypeSignature(argumentTypes = Vector(CTMap), outputType = CTDate),
    TypeSignature(argumentTypes = Vector(), outputType = CTDate)

  )
}

case object Duration extends Function with TypeSignatures {
  override val name = "duration"

  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(CTString), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTMap), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(), outputType = CTDuration) //todo: something like CTNULL sounds better? (as empty call is illegal)

  )
}