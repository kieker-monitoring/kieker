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

package kieker.analysis.behavior.clustering;

import java.util.HashMap;
import java.util.Map;

/**
 * A weighting function, which defines events from the JPetStore a insert and
 * duplication cost.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public final class JPetStoreParameterWeighting implements IParameterWeighting {

	private static final double DEFAULT_INSERTION_COST = 1;
	private static final double DEFAULT_DUPLICATION_COST = 0.1;

	private final Map<String, Double> insertionCost;
	private final Map<String, Double> duplicationCost;

	public JPetStoreParameterWeighting() {
		this.insertionCost = new HashMap<>();
		this.duplicationCost = new HashMap<>();

		this.insertionCost.put("order.cardType", 0.0);

		// the login events have no costs, as the parameter values don't matter
		this.insertionCost.put("username", 0.0);
		this.insertionCost.put("password", 0.0);

		// the category id is an important piece of information
		this.insertionCost.put("categoryId", 3.0);
		this.insertionCost.put("workingItemId", 1.0);
		this.insertionCost.put("productId", 1.0);

		// the costs to duplicate an equal event
		this.duplicationCost.put("order.cardType", 0.0);
		this.duplicationCost.put("username", 0.0);
		this.duplicationCost.put("password", 0.0);
		this.duplicationCost.put("categoryId", 0.1);
		this.duplicationCost.put("workingItemId", 1.0);
		this.duplicationCost.put("productId", 0.1);

	}

	@Override
	public double getInsertCost(final String[] parameterNames) {
		Double match;
		for (final String parameter : parameterNames) {
			match = this.insertionCost.get(parameter);
			if (match != null) {
				return match;
			}
		}
		return this.DEFAULT_INSERTION_COST;

	}

	@Override
	public double getDuplicateCost(final String[] parameterNames) {
		Double match;
		for (final String parameter : parameterNames) {
			match = this.duplicationCost.get(parameter);
			if (match != null) {
				return match;
			}
		}
		return this.DEFAULT_DUPLICATION_COST;

	}

}
