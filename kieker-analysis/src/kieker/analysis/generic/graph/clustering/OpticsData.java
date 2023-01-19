/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.generic.graph.clustering;

/**
 * A wrapper class for behavior models, which assigns the models additional information. If the
 * Model was visited before, how big the core distance is and how big the reachability distance is
 *
 * @param <T>
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class OpticsData<T> {

	public static final int UNDEFINED = -1;

	private final OPTICSDataGED<T> ged;

	private double reachabilityDistance = OpticsData.UNDEFINED;

	private double coreDistance = OpticsData.UNDEFINED;
	private boolean visited = false;

	private final T data;

	public OpticsData(final T data, final OPTICSDataGED<T> ged) {
		this.data = data;
		this.ged = ged;
	}

	public double getCoreDistance() {
		return this.coreDistance;

	}

	public double distanceTo(final OpticsData<T> model) {
		return this.ged.calculate(this, model);
	}

	public void setCoreDistance(final double coreDistance) {
		this.coreDistance = coreDistance;
	}

	public double getReachabilityDistance() {
		return this.reachabilityDistance;
	}

	public boolean isVisited() {
		return this.visited;
	}

	public T getData() {
		return this.data;
	}

	public void setVisited(final boolean visited) {
		this.visited = visited;
	}

	public void setReachabilityDistance(final double reachabilityDistance) {
		this.reachabilityDistance = reachabilityDistance;
	}

	public void reset() {
		this.reachabilityDistance = -1;

		this.coreDistance = -1;
		this.visited = false;
	}
}
