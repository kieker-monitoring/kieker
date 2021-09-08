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
package kieker.tools.trace.analysis.filter.visualization.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * The specific origin retention policy represents the intention that only specific origins should
 * be retained and corresponds to the kind {@link kieker.tools.trace.analysis.filter.visualization.graph.OriginRetentionPolicyKind#SPECIFIC}. Two specific
 * retention policies are united by uniting the underlying sets. Instead of calculating the union set,
 * however, the two policies are chained together using an implicit union/or operator to provide a
 * higher flexibility.
 *
 * @author Holger Knoche
 *
 * @since 1.6
 */
public class SpecificOriginRetentionPolicy extends AbstractOriginRetentionPolicy {

	private final Set<Object> selectedOrigins = new HashSet<>();

	private IOriginRetentionPolicy successor;

	protected SpecificOriginRetentionPolicy(final Set<?> selectedOrigins) {
		super(OriginRetentionPolicyKind.SPECIFIC);
		this.selectedOrigins.addAll(selectedOrigins);
	}

	@Override
	public boolean dependsOn(final IOriginRetentionPolicy policy) {
		if (this == policy) {
			return true;
		} else if (this.successor == null) {
			return false;
		} else {
			return this.successor.equals(policy) || this.successor.dependsOn(policy);
		}
	}

	@Override
	public IOriginRetentionPolicy uniteWith(final IOriginRetentionPolicy other) {
		if (other == null) {
			return this;
		}

		// Do not add a default case to the following switch to avoid the suppression of warnings
		// about missing enum values.
		switch (other.getKind()) { // NOPMD NOCS
		case NONE:
			return this;
		case SPECIFIC:
			if (other.dependsOn(this)) {
				throw new IllegalArgumentException(other.toString());
			}

			this.successor = other;
			return this;
		case ALL:
			return other;
		}

		throw new IllegalArgumentException(other.toString());
	}

	@Override
	public <T> void handleOrigin(final AbstractGraphElement<T> element, final T origin) {
		if (this.selectedOrigins.contains(origin)) {
			element.addOrigin(origin);
		}

		if (this.successor != null) {
			this.successor.handleOrigin(element, origin);
		}
	}

	/**
	 * Factory method for the specific origin retention policy.
	 *
	 * @param selectedOrigins
	 *            The origins to retain
	 * @return See above
	 */
	public static SpecificOriginRetentionPolicy createInstance(final Set<?> selectedOrigins) {
		return new SpecificOriginRetentionPolicy(selectedOrigins);
	}
}
