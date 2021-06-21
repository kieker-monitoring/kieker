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
 * The complete origin retention policy represents the intention that every origin should be
 * retained in the graph model and corresponds to the kind {@link kieker.tools.trace.analysis.filter.visualization.graph.OriginRetentionPolicyKind#ALL}.
 * Be aware that this policy may lead to extensive resource consumption when processing vast logs.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public final class CompleteOriginRetentionPolicy extends AbstractOriginRetentionPolicy {

	private static final CompleteOriginRetentionPolicy INSTANCE = new CompleteOriginRetentionPolicy();

	private CompleteOriginRetentionPolicy() {
		super(OriginRetentionPolicyKind.ALL);
	}

	@Override
	public IOriginRetentionPolicy uniteWith(final IOriginRetentionPolicy other) {
		return this;
	}

	@Override
	public <T> void handleOrigin(final AbstractGraphElement<T> element, final T origin) {
		element.addOrigin(origin);
	}

	/**
	 * Factory method for the complete origin retention policy.
	 * 
	 * @return See above
	 */
	public static CompleteOriginRetentionPolicy createInstance() {
		return INSTANCE;
	}
}
