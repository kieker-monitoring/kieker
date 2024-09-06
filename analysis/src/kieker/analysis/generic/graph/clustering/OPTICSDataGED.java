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
package kieker.analysis.generic.graph.clustering;

import kieker.analysis.generic.clustering.mtree.IDistanceFunction;
import kieker.analysis.generic.clustering.optics.OpticsData;

public class OPTICSDataGED<T> implements IDistanceFunction<OpticsData<T>> {

	private final IDistanceFunction<T> distanceFunction;

	public OPTICSDataGED(final IDistanceFunction<T> distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	@Override
	public double calculate(final OpticsData<T> model1, final OpticsData<T> model2) {
		return this.distanceFunction.calculate(model1.getData(), model2.getData());
	}

}
