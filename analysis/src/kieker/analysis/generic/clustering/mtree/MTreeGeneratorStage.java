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
package kieker.analysis.generic.clustering.mtree;

import java.util.List;

import teetime.stage.basic.AbstractTransformation;

/**
 * A stage to generate an M-Tree with objects of a generic type, with a given distance function.
 *
 * @param <T>
 *            The type, which the objects should have
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class MTreeGeneratorStage<T> extends AbstractTransformation<List<T>, MTree<T>> {

	private final IDistanceFunction<T> distanceFunction;

	// The minimal and the maximal amount of objects a node can contain. The root may contain fewer
	// objects
	private int minNodeCapacity = 25;
	private int maxNodeCapacity = 49;

	public MTreeGeneratorStage(final IDistanceFunction<T> distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	public MTreeGeneratorStage(final IDistanceFunction<T> distanceFunction, final int minNodeCapacity, final int maxNodeCapacity) {
		this.distanceFunction = distanceFunction;
		this.minNodeCapacity = minNodeCapacity;
		this.maxNodeCapacity = maxNodeCapacity;
	}

	@Override
	protected void execute(final List<T> models) throws Exception {
		final MTree<T> mtree = new MTree<>(this.minNodeCapacity, this.maxNodeCapacity, this.distanceFunction, null);

		if (models != null) {
			this.logger.debug("Received {} new models", models.size());
			for (final T model : models) {
				mtree.add(model);
			}
			this.logger.debug("Created MTree");

			this.outputPort.send(mtree);
		} else {
			this.logger.warn("Received empty model list. Cannot create mtree.");
		}
	}

	public int getMaxNodeCapacity() {
		return this.maxNodeCapacity;
	}

	public void setMaxNodeCapacity(final int maxNodeCapacity) {
		this.maxNodeCapacity = maxNodeCapacity;
	}

	public int getMinNodeCapacity() {
		return this.minNodeCapacity;
	}

	public void setMinNodeCapacity(final int minNodeCapacity) {
		this.minNodeCapacity = minNodeCapacity;
	}
}
