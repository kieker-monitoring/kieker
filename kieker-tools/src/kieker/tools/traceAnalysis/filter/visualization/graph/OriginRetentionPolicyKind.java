/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
 * This enumeration contains origin retention policy kinds, i.e. types of origin retention policies.
 * These policies are implemented by subtypes of {@link AbstractOriginRetentionPolicy}, this enum just
 * serves as the basis for the order relation.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public enum OriginRetentionPolicyKind {
	/**
	 * Value to denote that no origins should be retained.
	 */
	NONE,
	/**
	 * Value to denote that only specific origins should be retained.
	 */
	SPECIFIC,
	/**
	 * Value to denote that all origins should be retained.
	 */
	ALL;
}
