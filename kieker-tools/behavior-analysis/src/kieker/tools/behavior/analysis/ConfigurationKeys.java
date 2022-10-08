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

import kieker.analysis.behavior.clustering.IModelGenerationFilterFactory;

public class ConfigurationKeys {

	private static final String PREFIX = "kieker.tools.behavior.";

	/** Select clustering filter. */
	public static final String BEHAVIOR_CLUSTERING = ConfigurationKeys.PREFIX + "filter";

	/** Set whether a behavior visualization sink shall be created. STRING ARRAY. */
	public static final String BEHAVIOR_CLUSTERING_SINK = ConfigurationKeys.PREFIX + "sink";

	/** poepke approach. */
	public static final String BEHAVIOR_VISUALIZATION_URL = ConfigurationKeys.PREFIX + "visualizationUrl";

	public static final String BEHAVIOR_CLOSED_WORKLOAD = ConfigurationKeys.PREFIX + "closed.workload";

	public static final String BEHAVIOR_THINK_TIME = ConfigurationKeys.PREFIX + "think.time";

	public static final String BEHAVIOR_VARIANCE_OF_USER_GROUPS = ConfigurationKeys.PREFIX
			+ "variance.of.user.groups";

	public static final String PARAMETER_WEIGHTING = PREFIX + "parameterWeighting";

	/**
	 * Service Behavior Analysis
	 */
	public static final String GED_PREFIX = "org.iobserve.service.behavior.analysis.";

	public static final String MAX_MODEL_AMOUNT = ConfigurationKeys.GED_PREFIX + "maxModelAmount";

	public static final String RESULT_URL = ConfigurationKeys.GED_PREFIX + "outputUrl";

	public static final String RETURN_CLUSTERING = ConfigurationKeys.GED_PREFIX + "returnClustering";

	public static final String RETURN_MEDOIDS = ConfigurationKeys.GED_PREFIX + "returnMedoids";

	/**
	 * Optics Clustering.
	 */

	public static final String EPSILON = ConfigurationKeys.GED_PREFIX + "epsilon";

	public static final String MIN_PTS = ConfigurationKeys.GED_PREFIX + "minPts";

	/**
	 * Graph Edit Distance.
	 */

	public static final String NODE_INSERTION_COST = ConfigurationKeys.GED_PREFIX + "nodeInsertionCost";

	public static final String EDGE_INSERTION_COST = ConfigurationKeys.GED_PREFIX + "edgeInsertionCost";

	public static final String EVENT_GROUP_INSERTION_COST = ConfigurationKeys.GED_PREFIX + "eventGroupInsertionCost";

	// maybe this is not used in behavior clustering code?
	/** Set trace matcher required for EntryCallStage. STRING */
	// public static final String TRACE_MATCHER = ConfigurationKeys.PREFIX + "behavior."
	// + IEntryCallTraceMatcher.class.getSimpleName();
	//
	// /** Set acceptance matcher required for SessionAcceptanceFilter. STRING */
	// public static final String ENTRY_CALL_ACCEPTANCE_MATCHER = ConfigurationKeys.PREFIX + "behavior."
	// + IEntryCallAcceptanceMatcher.class.getSimpleName();
	//
	// /** Set cleanup rewriter required for TraceOperationCleanupFilter. STRING */
	// public static final String CLEANUP_REWRITER = ConfigurationKeys.PREFIX + "behavior."
	// + ITraceSignatureCleanupRewriter.class.getSimpleName();

	/**
	 * Set entry call filter rules factory required for TSessionOperationsFilter. STRING
	 */
	public static final String ENTRY_CALL_FILTER_RULES_FACTORY = ConfigurationKeys.PREFIX + "behavior."
			+ IModelGenerationFilterFactory.class.getSimpleName();

	/** Set time interval required for TimeTriggerFilter. LONG */
	public static final String TRIGGER_INTERVAL = ConfigurationKeys.PREFIX + "behavior." + "triggerInterval";

	/**
	 * Set behaviour model sink base url to configure file writing directory STRING.
	 */
	public static final String SINK_BASE_URL = ConfigurationKeys.PREFIX + "behavior.sink.baseUrl";

	/**
	 * Specific to Similarity Matching.
	 */

	/**
	 * Set parameter similarity radius DOUBLE.
	 */
	public static final String SIM_MATCH_RADIUS_PARAMS = ConfigurationKeys.PREFIX + "behavior.sm.parameters.radius";

	/**
	 * Set structure similarity radius DOUBLE.
	 */
	public static final String SIM_MATCH_RADIUS_STRUCTURE = ConfigurationKeys.PREFIX + "behavior.sm.structure.radius";

	public static final String SINGLE_EVENT_MODE = ConfigurationKeys.PREFIX + "singleEventMode";

	/**
	 * Set similarity radius DOUBLE.
	 */
	public static final String SIM_MATCH_RADIUS = ConfigurationKeys.PREFIX + "behavior.sm.radius";

	public static final String TRACE_OPERATION_CLEANUP_REWRITER = PREFIX + "trace.operation.cleanup.rewriter";

	public static final String ENTRY_CALL_ACCEPTANCE_MATCHER = PREFIX + "call.acceptance.matcher";

}
