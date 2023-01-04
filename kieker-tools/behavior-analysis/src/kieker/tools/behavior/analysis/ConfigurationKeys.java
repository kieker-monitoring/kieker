/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.behavior.analysis;

public final class ConfigurationKeys {

	private static final String PREFIX = "kieker.tools.behavior.";

	public static final String PARAMETER_WEIGHTING = PREFIX + "parameterWeighting";

	/**
	 * Service Behavior Analysis.
	 */
	public static final String MAX_MODEL_AMOUNT = ConfigurationKeys.PREFIX + "maxModelAmount";

	public static final String CLUSTER_OUTPUT_FILE = ConfigurationKeys.PREFIX + "clusterOutputFile";

	public static final String CLUSTER_OUTPUT_URL = ConfigurationKeys.PREFIX + "clusterOutputUrl";

	public static final String MEDOIDS_OUTPUT_FILE = ConfigurationKeys.PREFIX + "medoidsOutputPath";

	/**
	 * Optics Clustering.
	 */

	public static final String EPSILON = ConfigurationKeys.PREFIX + "epsilon";

	public static final String MIN_PTS = ConfigurationKeys.PREFIX + "minPts";

	/**
	 * Graph Edit Distance.
	 */

	public static final String NODE_INSERTION_COST = ConfigurationKeys.PREFIX + "nodeInsertionCost";

	public static final String EDGE_INSERTION_COST = ConfigurationKeys.PREFIX + "edgeInsertionCost";

	public static final String EVENT_GROUP_INSERTION_COST = ConfigurationKeys.PREFIX + "eventGroupInsertionCost";

	/**
	 *
	 */

	public static final String TRACE_SIGNATURE_PROCESSOR = PREFIX + "traceSignatureProcessor";

	public static final String CLASS_SIGNATURE_ACCEPTANCE_MATCHER_FILE = PREFIX + "classSignatureAcceptancePatternFile";

	public static final String OPERATION_SIGNATURE_ACCEPTANCE_MATCHER_FILE = PREFIX + "operationSignatureAcceptancePatternFile";

	public static final String SIGNATURE_ACCEPTANCE_MATCHER_MODE = PREFIX + "signatureAcceptancePatternMode";

	public static final String USER_SESSION_TIMEOUT = PREFIX + "usesSessionTimeout";

	private ConfigurationKeys() {}

}
