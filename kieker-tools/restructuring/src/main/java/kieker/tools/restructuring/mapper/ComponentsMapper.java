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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class ComponentsMapper extends BasicComponentMapper {

	/**
	 * Constructor is used to create a component mapper.
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
	public ComponentsMapper(final AssemblyModel original, final AssemblyModel goal, final String originalModelName,
			final String goalModelName) {
		super(originalModelName, goalModelName);

		this.original = original;
		this.goal = goal;

		// init mappings
		this.populateOperationTocomponentG();
		this.populateOperationToComponentO();
		this.populateTraceModel();
		this.computeOriginalComponentNames();

	}

	private void computeOriginalComponentNames() {
		final Set<String> assignedComponentsG = new HashSet<>();
		final Set<String> assignedComponentsO = new HashSet<>();
		for (final Entry<String, Map<String, Integer>> goalComponent : this.traceModell.entrySet()) {
			final List<Entry<String, Integer>> componentTraces = new ArrayList<>(
					goalComponent.getValue().entrySet());
			componentTraces.sort(Comparator.comparing(Entry::getValue));

			for (final Entry<String, Integer> e : componentTraces) {
				if (!assignedComponentsO.contains(e.getKey())) {
					assignedComponentsO.add(e.getKey());
					assignedComponentsG.add(goalComponent.getKey());
					this.goalToOriginal.put(goalComponent.getKey(), e.getKey());
					this.originalToGoal.put(e.getKey(), goalComponent.getKey());
					break;
				}
			}
		}

	}

	private void populateTraceModel() {
		// For each component in the goal model...
		for (final Entry<String, AssemblyComponent> entry : this.goal.getComponents().entrySet()) {

			final AssemblyComponent comp = entry.getValue();
			final String name = entry.getKey();

			// get operations of the current component
			final Set<String> ops = comp.getOperations().keySet();
			// here we store the components where operations actually originate from and
			// count how many operations of each
			// original component are in the "goal component"
			final Map<String, Integer> referencedComponents = new HashMap<>();

			// For each operation of current component..
			for (final String op : ops) {

				// String opName = op.getKey();
				// look up the name of the "original component" for that operation
				final String originalComponent = this.operationToComponentO.get(op);

				// Already found or not?
				if (referencedComponents.containsKey(originalComponent)) {
					referencedComponents.put(originalComponent, referencedComponents.get(originalComponent) + 1);

				} else {
					// first occurence
					referencedComponents.put(originalComponent, 1);
				}

			}
			this.traceModell.put(name, referencedComponents);

		}
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
