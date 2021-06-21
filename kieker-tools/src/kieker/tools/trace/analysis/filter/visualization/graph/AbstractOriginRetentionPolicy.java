/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

/**
 * Abstract superclass for all origin retention policies.
 *
 * @author Holger Knoche
 *
 * @since 1.6
 */
public abstract class AbstractOriginRetentionPolicy implements IOriginRetentionPolicy {

	private final OriginRetentionPolicyKind kind;

	/**
	 * This constructor uses the given parameter to initialize the class.
	 *
	 * @param kind
	 *            The origin retention policy kind.
	 */
	protected AbstractOriginRetentionPolicy(final OriginRetentionPolicyKind kind) {
		this.kind = kind;
	}

	@Override
	public OriginRetentionPolicyKind getKind() {
		return this.kind;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCompatibleWith(final IOriginRetentionPolicy policy) { // NOPMD, for some reason, PMD regards this function as empty
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean dependsOn(final IOriginRetentionPolicy policy) {
		return this == policy;
	}
}
