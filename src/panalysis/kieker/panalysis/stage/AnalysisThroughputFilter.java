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
package kieker.panalysis.stage;

import kieker.analysis.ThroughputAnalysisResult;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class AnalysisThroughputFilter<T> extends AbstractFilter<AnalysisThroughputFilter<T>> {

	public final IInputPort<AnalysisThroughputFilter<T>, T> objectInputPort = this.createInputPort();
	public final IInputPort<AnalysisThroughputFilter<T>, Long> timestampInputPort = this.createInputPort();

	public final IOutputPort<AnalysisThroughputFilter<T>, T> objectOutputPort = this.createOutputPort();
	public final IOutputPort<AnalysisThroughputFilter<T>, ThroughputAnalysisResult> analysisOutputPort = this.createOutputPort();

	private long lastTimestampInNs;
	private int numObjectsPassed;

	@Override
	protected boolean execute(final Context<AnalysisThroughputFilter<T>> context) {
		final Long timestampInNs = context.tryTake(this.timestampInputPort);
		if (timestampInNs == null) {
			final T object = context.tryTake(this.objectInputPort);
			context.put(this.objectOutputPort, object);
			this.numObjectsPassed++;
		} else {
			final long durationInNs = timestampInNs - this.lastTimestampInNs;
			context.put(this.analysisOutputPort, new ThroughputAnalysisResult(durationInNs, this.numObjectsPassed));
			this.reset(timestampInNs);
		}

		return true;
	}

	private void reset(final Long timestampInNs) {
		this.numObjectsPassed = 0;
		this.lastTimestampInNs = timestampInNs;
	}

}
