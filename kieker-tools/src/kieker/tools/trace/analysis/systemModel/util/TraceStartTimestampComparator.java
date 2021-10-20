/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.systemModel.util;

import java.io.Serializable;
import java.util.Comparator;

import kieker.tools.trace.analysis.systemModel.AbstractTrace;

/**
 * Comparator which compares abstract traces according to their start timestamps.
 * 
 * @author Holger Knoche
 * @since 1.10
 * 
 */
public class TraceStartTimestampComparator implements Comparator<AbstractTrace>, Serializable {

	private static final long serialVersionUID = -8372489878918932083L;

	public TraceStartTimestampComparator() {
		// Empty default constructor
	}

	@Override
	public int compare(final AbstractTrace o1, final AbstractTrace o2) {
		final long startTimestamp1 = o1.getStartTimestamp();
		final long startTimestamp2 = o2.getStartTimestamp();

		if (startTimestamp1 == startTimestamp2) {
			return 0;
		} else if (startTimestamp1 < startTimestamp2) {
			return -1;
		} else {
			return 1;
		}
	}

}
