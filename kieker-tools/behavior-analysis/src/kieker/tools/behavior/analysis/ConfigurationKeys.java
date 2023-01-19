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

	public static final String PREFIX = "kieker.tools.behavior";

	private static final String FULL_PREFIX = PREFIX + ".";

	public static final String PARAMETER_WEIGHTING = FULL_PREFIX + "parameterWeighting";

	/**
	 * Service Behavior Analysis.
	 */
	public static final String MAX_MODEL_AMOUNT = ConfigurationKeys.FULL_PREFIX + "maxModelAmount";

	public static final String CLUSTER_OUTPUT_FILE = ConfigurationKeys.FULL_PREFIX + "clusterOutputFile";

	public static final String CLUSTER_OUTPUT_URL = ConfigurationKeys.FULL_PREFIX + "clusterOutputUrl";

	public static final String MEDOIDS_OUTPUT_FILE = ConfigurationKeys.FULL_PREFIX + "medoidsOutputPath";

	/**
	 * Optics Clustering.
	 */

	public static final String EPSILON = ConfigurationKeys.FULL_PREFIX + "epsilon";

	public static final String MIN_PTS = ConfigurationKeys.FULL_PREFIX + "minPts";

	/**
	 * Graph Edit Distance.
	 */

	public static final String NODE_INSERTION_COST = ConfigurationKeys.FULL_PREFIX + "nodeInsertionCost";

	public static final String EDGE_INSERTION_COST = ConfigurationKeys.FULL_PREFIX + "edgeInsertionCost";

	public static final String EVENT_GROUP_INSERTION_COST = ConfigurationKeys.FULL_PREFIX + "eventGroupInsertionCost";

	/**
	 *
	 */

	public static final String TRACE_SIGNATURE_PROCESSOR = FULL_PREFIX + "traceSignatureProcessor";

	public static final String CLASS_SIGNATURE_ACCEPTANCE_MATCHER_FILE = FULL_PREFIX + "classSignatureAcceptancePatternFile";

	public static final String OPERATION_SIGNATURE_ACCEPTANCE_MATCHER_FILE = FULL_PREFIX + "operationSignatureAcceptancePatternFile";

	public static final String SIGNATURE_ACCEPTANCE_MATCHER_MODE = FULL_PREFIX + "signatureAcceptancePatternMode";

	public static final String USER_SESSION_TIMEOUT = FULL_PREFIX + "usesSessionTimeout";

	public static final String VERBOSE = FULL_PREFIX + "verbose";

	public static final String DATA_BUFFER_SIZE = FULL_PREFIX + "buffer";

	public static final String LOG_DIRECTORIES = FULL_PREFIX + "directories";

	private ConfigurationKeys() {}

}
