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
package kieker.analysis.generic.source;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.OutputPort;

/**
 * Marker interface for composite stages used as source.
 *
 * @author Reiner Jung
 *
 * @since 2.0.0
 *
 */
public interface ISourceCompositeStage {

	/**
	 * get the output port of a source composite stage.
	 *
	 * @return the proper output port
	 * @since 1.15
	 */
	OutputPort<IMonitoringRecord> getOutputPort();
}
