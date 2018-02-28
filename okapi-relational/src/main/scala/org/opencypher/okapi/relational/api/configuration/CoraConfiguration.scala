/*
 * Copyright (c) 2016-2018 "Neo4j, Inc." [https://neo4j.com]
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
 */
package org.opencypher.okapi.relational.api.configuration

import org.opencypher.okapi.impl.configuration.ConfigFlag

object CoraConfiguration {

  object PrintFlatPlan extends ConfigFlag("cora.explainFlat")

  object PrintPhysicalPlan extends ConfigFlag("cora.explainPhysical", false)

  object DebugPhysicalResult extends ConfigFlag("cora.debugPhysical", false)

  object PrintQueryExecutionStages extends ConfigFlag("cora.stages", false)

}