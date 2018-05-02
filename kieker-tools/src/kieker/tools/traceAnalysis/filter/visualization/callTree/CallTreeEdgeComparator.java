/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.callTree;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Christian Wulf (chw)
 * 
 * @since 1.14
 */
class CallTreeEdgeComparator implements Comparator<WeightedDirectedCallTreeEdge<?>>, Serializable {

	/**
	 * 8026580351004780853L for 1.14
	 */
	private static final long serialVersionUID = 8026580351004780853L;

	public CallTreeEdgeComparator() {
		super();
	}

	@Override
	public int compare(final WeightedDirectedCallTreeEdge<?> o1, final WeightedDirectedCallTreeEdge<?> o2) {
		return Integer.compare(o1.getTarget().getId(), o2.getTarget().getId());
	}

}
