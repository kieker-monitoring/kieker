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

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.opad.record.ExtendedStorableDetectionResult;
import kieker.tools.opad.record.OpadOutputData;

/**
 * Converts the incoming ExtendedStorableDetectionResult objects to OpadOutputData objects.
 * This includes the extraction of information in separate fields.
 * 
 * @author Thomas Düllmann
 */
@Plugin(name = "Combines information for RanCorr usage",
		outputPorts =
		@OutputPort(name = OpadOutputCompositionFilter.OUTPUT_PORT_OPAD_DATA, eventTypes = { OpadOutputData.class }))
public class OpadOutputCompositionFilter extends AbstractFilterPlugin {

	/**
	 * Name of output port which returns the OPAD anomaly scores.
	 */
	public static final String OUTPUT_PORT_OPAD_DATA = "opadOutputData";

	/**
	 * Name of input port which gets the input data for OPAD filter.
	 */
	public static final String INPUT_PORT_NAME_DETECTION_RESULTS = "detectionResults";

	private static final Log LOG = LogFactory.getLog(OpadOutputCompositionFilter.class);

	/**
	 * Constructor which sets all necessary attributes.
	 * 
	 * @param configuration
	 *            - Configuration object
	 * @param projectContext
	 *            - IProjectContext object
	 */
	public OpadOutputCompositionFilter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * Reads the incoming ExtendedStorableDetectionResult and extracts the containing information in a new
	 * OpadOutputData object.
	 * 
	 * @param sdr
	 *            incoming opad detection results
	 */
	@InputPort(name = OpadOutputCompositionFilter.INPUT_PORT_NAME_DETECTION_RESULTS, eventTypes = { ExtendedStorableDetectionResult.class })
	public void readData(final ExtendedStorableDetectionResult sdr) {
		final String[] splitData = sdr.getApplication().split(":", 2);
		if (splitData.length == 2) {
			final String hostdata = splitData[0];
			final String operation = splitData[1];
			final String[] hostSplitData = hostdata.split("\\+", 2);
			if (hostSplitData.length == 2) {
				final long timestamp = sdr.getTimestamp();
				final OpadOutputData ood = new OpadOutputData(timestamp, hostSplitData[0], hostSplitData[1], operation, (long) sdr.getValue(),
						sdr.getScore(), sdr.getAnomalyThreshold());
				super.deliver(OUTPUT_PORT_OPAD_DATA, ood);
			} else {
				LOG.error("The kieker.monitoring.hostname does not match the pattern {hostname}+{appname}");
			}
		} else {
			LOG.error("The application string of the input data does not contain host information.");
		}
	}
}
