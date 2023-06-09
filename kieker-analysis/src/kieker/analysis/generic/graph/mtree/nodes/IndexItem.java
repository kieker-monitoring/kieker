/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.graph.mtree.nodes;

/**
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class IndexItem<T> { // NOCS cannot be declared final

	protected double radius;
	private final T data;
	private double distanceToParent;

	public IndexItem(final T data) {
		this.data = data;
		this.radius = 0;
		this.distanceToParent = -1;
	}

	public T getData() {
		return this.data;
	}

	public double getDistanceToParent() {
		return this.distanceToParent;
	}

	public void setDistanceToParent(final double distance) {
		this.distanceToParent = distance;
	}

	public int check() {
		this.checkRadius();
		this.checkDistanceToParent();

		return 1;
	}

	private void checkRadius() {
		assert this.radius >= 0;
	}

	protected void checkDistanceToParent() {
		assert !(this instanceof RootLeafNode);
		assert !(this instanceof RootNode);
		assert this.distanceToParent >= 0;
	}

	public double getRadius() {
		return this.radius;
	}

}
