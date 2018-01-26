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
package org.opencypher.caps.impl.spark.io.hdfs

import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.http.client.utils.URIBuilder
import org.opencypher.caps.api.CAPSSession
import org.opencypher.caps.impl.spark.io.{CAPSPropertyGraphDataSourceFactoryImpl, _}

case object HdfsCsvPropertyGraphDataSourceFactory extends CAPSGraphSourceFactoryCompanion("hdfs+csv")

case class HdfsCsvPropertyGraphDataSourceFactory()
  extends CAPSPropertyGraphDataSourceFactoryImpl(HdfsCsvPropertyGraphDataSourceFactory) {

  override protected def sourceForURIWithSupportedScheme(uri: URI)(implicit capsSession: CAPSSession): HdfsCsvPropertyGraphDataSource = {
    val internalURI: URI = new URIBuilder(uri)
      .setScheme("hdfs")
      .build()

    val hadoopConf = new Configuration(capsSession.sparkSession.sparkContext.hadoopConfiguration)
    hadoopConf.set("fs.default.name", internalURI.toString)
    HdfsCsvPropertyGraphDataSource(uri, hadoopConf, uri.getPath)
  }
}