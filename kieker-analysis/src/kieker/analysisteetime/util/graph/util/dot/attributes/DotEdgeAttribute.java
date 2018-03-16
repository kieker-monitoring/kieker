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

package kieker.analysisteetime.util.graph.util.dot.attributes;

/**
 * A enumeration of possible attributes for edges in dot graphs.
 *
 * <strong>Currently only the attributes we are using are implemented, but the enumeration
 * can be extended at will.</strong>
 *
 * The whole set of attributes including further documentation can be found here:
 * {@link http://www.graphviz.org/doc/info/attrs.html}
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public enum DotEdgeAttribute {

	LABEL("label"), STYLE("style"), ARROWHEAD("arrowhead"), ARROWSIZE("arrowsize"), ARROWTAIL("arrowtail"), COLOR("color");

	private final String attribute;

	private DotEdgeAttribute(final String attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return this.attribute;
	}

}
