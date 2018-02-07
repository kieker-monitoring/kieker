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

package kieker.analysisteetime.util.graph;

/**
 * Direction is used to denote the direction of an edge or location of a vertex on an edge.
 *
 * @author Sören Henning
 *
 */
public enum Direction {

	OUT, IN, BOTH;

	public Direction opposite() {
		if (this.equals(OUT)) {
			return IN;
		} else if (this.equals(IN)) {
			return OUT;
		} else {
			return BOTH;
		}
	}
}
