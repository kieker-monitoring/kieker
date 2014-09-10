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

package kieker.tools.traceAnalysis.filter.visualization.graph;

/**
 * The no-origin-retention policy represents the intention that no origins should be retained at all
 * and corresponds to the kind {@link kieker.tools.traceAnalysis.filter.visualization.graph.OriginRetentionPolicyKind#NONE}.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public final class NoOriginRetentionPolicy extends AbstractOriginRetentionPolicy {

	private static final NoOriginRetentionPolicy INSTANCE = new NoOriginRetentionPolicy();

	private NoOriginRetentionPolicy() {
		super(OriginRetentionPolicyKind.NONE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IOriginRetentionPolicy uniteWith(final IOriginRetentionPolicy other) {
		return other;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> void handleOrigin(final AbstractGraphElement<T> element, final T origin) {
		// Do nothing
	}

	/**
	 * Factory method for the no-origin-retention policy.
	 * 
	 * @return See above
	 */
	public static NoOriginRetentionPolicy createInstance() {
		return INSTANCE;
	}
}
