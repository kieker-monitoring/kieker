/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.systemModel.util;

import java.io.Serializable;
import java.util.Comparator;

import kieker.tools.traceAnalysis.systemModel.AbstractTrace;

/**
 * Comparator which compares abstract traces according to their end timestamps.
 * 
 * @author Holger Knoche
 * @since 1.10
 * 
 */
public class TraceEndTimestampComparator implements Comparator<AbstractTrace>, Serializable {

	private static final long serialVersionUID = 7583764582950192813L;

	public TraceEndTimestampComparator() {
		// Empty default constructor
	}

	@Override
	public int compare(final AbstractTrace o1, final AbstractTrace o2) {
		final long endTimestamp1 = o1.getEndTimestamp();
		final long endTimestamp2 = o2.getEndTimestamp();

		if (endTimestamp1 == endTimestamp2) {
			return 0;
		} else if (endTimestamp1 < endTimestamp2) {
			return -1;
		} else {
			return 1;
		}
	}

}
