/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.filter;

import java.net.UnknownHostException;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.opad.record.ExtendedStorableDetectionResult;
import kieker.tools.opad.record.StorableDetectionResult;

/**
 * This class is able to receive the calculated anomaly detection results. Including anomaly or not,
 * anomaly score and name of the corresponding application. The results are then communicated to
 * the Logging-queue via ZeroMQ.
 * 
 * @author Tom Frotscher, Thomas Düllmann, Andreas Eberlein, Tobias Rudolph
 * @since 1.10
 * 
 */
@Plugin(name = "Send and Store Detection Result Filter",
		outputPorts = { @OutputPort(eventTypes = { StorableDetectionResult.class }, name = SendAndStoreDetectionResultFilter.OUTPUT_PORT_SENT_DATA)
		})
public class SendAndStoreDetectionResultFilter extends AbstractFilterPlugin {

	/**
	 * Name of the input port receiving the detection result data.
	 */
	public static final String INPUT_PORT_DETECTION_RESULTS = "detectionResults";

	/**
	 * Name of the output port delivering the sent data.
	 */
	public static final String OUTPUT_PORT_SENT_DATA = "sent_data";

	private static final Log LOG = LogFactory.getLog(SendAndStoreDetectionResultFilter.class);

	private DBCollection coll;
	private boolean connected = true; // NOPMD cannot be final

	/**
	 * Creates a new instance of this class. Initialize the connection via ZeroMQ to write back
	 * to the Logging-queue. Stores the result in a MongoDB database.
	 * 
	 * @param configuration
	 *            Configuration of this component
	 * @param projectContext
	 *            ProjectContext of this component
	 */
	public SendAndStoreDetectionResultFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		MongoClient mongoClient;
		// Initialize MongoDB Connection Variables
		DB db;
		try {
			mongoClient = new MongoClient("localhost");
			db = mongoClient.getDB("opadxDB"); // Database name
			this.coll = db.getCollection("analysisResults"); // Collection name
		} catch (final UnknownHostException e) {
			this.connected = false;
			LOG.error("Database connection can not be established. The data is processed, but not stored!", e);
		}
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * This method represents the input port for the incoming detection data.
	 * 
	 * @param detectionData
	 *            Incoming detection data
	 */
	@InputPort(eventTypes = { ExtendedStorableDetectionResult.class }, name = SendAndStoreDetectionResultFilter.INPUT_PORT_DETECTION_RESULTS)
	public void inputAnomaly(final ExtendedStorableDetectionResult detectionData) {
		final boolean anomaly = detectionData.getScore() > detectionData.getAnomalyThreshold();
		final JSONObject obj = new JSONObject();

		// Workaround because JSON doesn't accept Double.NaN
		if (!Double.isNaN(detectionData.getScore())) {
			obj.put("score", detectionData.getScore());
		} else {
			obj.put("score", "NaN");
		}
		obj.put("anomaly", anomaly);

		// Write the result to the database
		if (this.connected) {
			final BasicDBObject result = new BasicDBObject("application", detectionData.getApplication()).
					append("value", detectionData.getValue()).
					append("timestamp", detectionData.getTimestamp()).
					append("forecast", detectionData.getForecast()).
					append("score", detectionData.getScore()).
					append("anomaly", anomaly);
			this.coll.insert(result);
		}
		super.deliver(OUTPUT_PORT_SENT_DATA, detectionData);
	}
}
