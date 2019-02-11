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
package org.opencypher.okapi.ir.impl.parse.functions

import org.opencypher.v9_0.expressions._
import org.opencypher.v9_0.expressions.functions.Function
import org.opencypher.v9_0.util.symbols._

case object FunctionLookup {

  def apply(name: String): Vector[TypeSignature] = name match {
    case Timestamp.name => Timestamp.signatures
    case LocalDateTime.name => LocalDateTime.signatures
    case Date.name => Date.signatures
    case Duration.name => Duration.signatures
    case DurationInSeconds.name | DurationInDays.name | DurationInMonth.name | DurationBetween.name => DurationSubfunctions.signatures
    case _ => Vector.empty
  }

}

case object Timestamp extends Function with TypeSignatures {
  override val name = "timestamp"

  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(), outputType = CTInteger)
  )
}

case object LocalDateTime extends Function with TypeSignatures {
  override val name = "localdatetime"

  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(CTString), outputType = CTLocalDateTime),
    TypeSignature(argumentTypes = Vector(CTMap), outputType = CTLocalDateTime),
    TypeSignature(argumentTypes = Vector(), outputType = CTLocalDateTime)
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

case object DurationBetween extends Function {
  override val name = "duration.between"
}
case object Duration extends Function with TypeSignatures {
  override val name = "duration"

  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(CTString), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTMap), outputType = CTDuration)
  )
}
case object DurationInSeconds extends Function {
  override val name = "duration.inSeconds"
}

case object DurationInDays extends Function {
  override val name = "duration.inDays"
}

case object DurationInMonth extends Function {
  override val name = "duration.inMonths"
}


case object DurationSubfunctions extends TypeSignatures {
  override val signatures = Vector(
    TypeSignature(argumentTypes = Vector(CTDate, CTDate), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTDate, CTLocalTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTDate, CTLocalDateTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTDateTime, CTDate), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTDateTime, CTDateTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTDateTime, CTLocalTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTDateTime, CTLocalDateTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTLocalTime, CTDate), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTLocalTime, CTDateTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTLocalTime, CTLocalTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTLocalTime, CTLocalDateTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTLocalDateTime, CTDate), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTLocalDateTime, CTDateTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTLocalDateTime, CTLocalTime), outputType = CTDuration),
    TypeSignature(argumentTypes = Vector(CTLocalDateTime, CTLocalDateTime), outputType = CTDuration)
  )
}
