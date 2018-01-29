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
package org.opencypher.caps.api.io.conversion

object RelationshipMapping {
  val empty = MissingSourceIdKey

  object MissingSourceIdKey {
    /**
      * @param sourceIdKey represents a key to the relationship identifier within the source data. The retrieved value
      *                    from the source data is expected to be a [[Long]] value that is unique among relationships.
      * @return incomplete relationship mapping
      */
    def withSourceIdKey(sourceIdKey: String) =
      new MissingSourceStartNodeKey(sourceIdKey)
  }

  sealed class MissingSourceStartNodeKey(sourceIdKey: String) {
    /**
      * @param sourceStartNodeKey represents a key to the start node identifier within the source data. The retrieved
      *                           value from the source data is expected to be a [[Long]] value.
      * @return incomplete relationship mapping
      */
    def withSourceStartNodeKey(sourceStartNodeKey: String) =
      new MissingSourceEndNodeKey(sourceIdKey, sourceStartNodeKey)
  }

  sealed class MissingSourceEndNodeKey(sourceIdKey: String, sourceStartNodeKey: String) {
    /**
      * @param sourceEndNodeKey represents a key to the end node identifier within the source data. The retrieved
      *                         value from the source data is expected to be a [[Long]] value.
      * @return incomplete relationship mapping
      */
    def withSourceEndNodeKey(sourceEndNodeKey: String) =
      new MissingRelTypeMapping(sourceIdKey, sourceStartNodeKey, sourceEndNodeKey)
  }

  sealed class MissingRelTypeMapping(sourceIdKey: String, sourceStartNodeKey: String, sourceEndNodeKey: String) {
    /**
      * @param relType represents the relationship type for all relationships in the source data
      * @return relationship mapping
      */
    def withRelType(relType: String) =
      RelationshipMapping(sourceIdKey, sourceStartNodeKey, sourceEndNodeKey, Left(relType))

    /**
      * @param sourceRelTypeKey represents a key to the relationship type within the source data. The retrieved
      *                         value from the source data is expected to be a [[String]] value.
      * @return relationship mapping
      */
    def withSourceRelTypeKey(sourceRelTypeKey: String) =
      RelationshipMapping(sourceIdKey, sourceStartNodeKey, sourceEndNodeKey, Right(sourceRelTypeKey))
  }

}

/**
  * Represents a mapping from a source with key-based access to relationship components (e.g. a table definition) to a
  * Cypher relationship. The purpose of this class is to define a mapping from an external data source to a property
  * graph.
  *
  * Construct a [[RelationshipMapping]] starting with {{RelationshipMapping.empty}}.
  */
final case class RelationshipMapping(
  sourceIdKey: String,
  sourceStartNodeKey: String,
  sourceEndNodeKey: String,
  relTypeOrSourceRelTypeKey: Either[String, String],
  propertyMapping: Map[String, String] = Map.empty) {

  def withPropertyKey(sourcePropertyKey: String, propertyKey: String): RelationshipMapping =
    copy(propertyMapping = propertyMapping.updated(propertyKey, sourcePropertyKey))

  def withPropertyKey(tuple: (String, String)): RelationshipMapping =
    withPropertyKey(tuple._1, tuple._2)

  def withPropertyKey(property: String): RelationshipMapping =
    withPropertyKey(property, property)

  def withPropertyKeys(properties: String*): RelationshipMapping =
    properties.foldLeft(this)((mapping, propertyKey) => mapping.withPropertyKey(propertyKey, propertyKey))
}
