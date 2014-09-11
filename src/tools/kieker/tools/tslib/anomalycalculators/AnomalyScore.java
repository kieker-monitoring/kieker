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

package kieker.tools.tslib.anomalycalculators;

/**
 * A simple container for an anomaly score.
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 * @since 1.10
 */
public class AnomalyScore {

	private final double score;

	public AnomalyScore(final double score) {
		this.score = score;
	}

	public double getScore() {
		return this.score;
	}

}
