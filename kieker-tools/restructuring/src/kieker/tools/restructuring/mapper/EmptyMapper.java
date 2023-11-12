/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring.mapper;

import java.util.Map.Entry;
import java.util.Set;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class EmptyMapper extends BasicComponentMapper {

	/**
	 * Constructor is used to create an empty mapper.
	 *
	 * @param original
	 *            original model
	 * @param goal
	 *            goal model
	 * @param originalModelName
	 *            name of the original model
	 * @param goalModelName
	 *            name of the goal model
	 */
	public EmptyMapper(final AssemblyModel original, final AssemblyModel goal, final String originalModelName,
			final String goalModelName) {
		super(originalModelName, goalModelName);
		this.original = original;
		this.goal = goal;
		// System.out.println(this.orig != null);
		// System.out.println(this.goal != null);
		// init mappings
		this.populateOperationTocomponentG();
		this.populateOperationToComponentO();
		// populateTraceModel();
		// computeOriginalComponentNames();

	}

	private void populateOperationToComponentO() {
		for (final Entry<String, AssemblyComponent> e : this.original.getComponents().entrySet()) {
			final Set<String> ops = e.getValue().getOperations().keySet();
			for (final String s : ops) {
				this.operationToComponentO.put(s, e.getKey());
			}
		}
	}

	private void populateOperationTocomponentG() {
		for (final Entry<String, AssemblyComponent> e : this.goal.getComponents().entrySet()) {
			final Set<String> ops = e.getValue().getOperations().keySet();
			for (final String s : ops) {
				this.operationToComponentG.put(s, e.getKey());
			}
		}
	}
}
