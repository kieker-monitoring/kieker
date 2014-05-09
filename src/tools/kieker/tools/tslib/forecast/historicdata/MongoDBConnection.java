/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
 ***************************************************************************/

package kieker.tools.tslib.forecast.historicdata;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * The database connection used by the pattern checking forecaster. This must be
 * the database, were the results of OPAD are stored. This enables the pattern
 * checking forecaster to search for the patterns in the past.
 * 
 * @author Tom Frotscher
 * 
 */
public class MongoDBConnection {

	private static final String DB_NAME = "opadxDB";
	private static final String COLL_NAME = "analysisResults";
	private static final Log LOG = LogFactory.getLog(MongoDBConnection.class);

	private final DBCollection coll;

	/**
	 * Creates a new MongoDB connection.
	 */
	public MongoDBConnection() {
		final MongoClient mongoClient;
		final DB db;
		DBCollection dbCol = null;
		try {
			mongoClient = new MongoClient("localhost");
			db = mongoClient.getDB(DB_NAME);
			dbCol = db.getCollection(COLL_NAME);
		} catch (final UnknownHostException e) {
			LOG.error("Database connection can not be established for the  Pattern Checking Forecaster!", e);
		}
		this.coll = dbCol;
	}

	/**
	 * Getter for the collection.
	 * 
	 * @return
	 *         The corresponding collection of the MongoDB
	 */
	public DBCollection getColl() {
		return this.coll;
	}

}
