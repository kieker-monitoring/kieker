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

import java.util.HashMap;
import java.util.Map;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 2.0.0
 */
public class BasicComponentMapper {

	protected AssemblyModel original;
	protected AssemblyModel goal;

	protected Map<String, String> operationToComponentO = new HashMap<>();
	protected Map<String, String> operationToComponentG = new HashMap<>();

	protected Map<String, Map<String, Integer>> traceModell = new HashMap<>();
	protected Map<String, String> goalToOriginal = new HashMap<>();
	protected Map<String, String> originalToGoal = new HashMap<>();

	private final String originalModelName;
	private final String goalModelName;

	public BasicComponentMapper(final String originalModelName, final String goalModelName) {
		this.originalModelName = originalModelName;
		this.goalModelName = goalModelName;
	}

	public String getGoalModelName() {
		return this.goalModelName;
	}

	public String getOriginalModelName() {
		return this.originalModelName;
	}

	public Map<String, String> getOperationToComponentO() {
		return this.operationToComponentO;
	}

	public void setOperationToComponentO(final Map<String, String> operationToComponentO) {
		this.operationToComponentO = operationToComponentO;
	}

	public Map<String, String> getOperationToComponentG() {
		return this.operationToComponentG;
	}

	public void setOperationToComponentG(final Map<String, String> operationToComponentG) {
		this.operationToComponentG = operationToComponentG;
	}

	public Map<String, Map<String, Integer>> getTraceModell() {
		return this.traceModell;
	}

	public void setTraceModell(final Map<String, Map<String, Integer>> traceModell) {
		this.traceModell = traceModell;
	}

	public Map<String, String> getGoalToOriginal() {
		return this.goalToOriginal;
	}

	public void setGoalToOriginal(final Map<String, String> goalToOriginal) {
		this.goalToOriginal = goalToOriginal;
	}

	public Map<String, String> getOriginalToGoal() {
		return this.originalToGoal;
	}

	public void setOriginalToGoal(final Map<String, String> originalToGoal) {
		this.originalToGoal = originalToGoal;
	}

	public AssemblyModel getOriginal() {
		return this.original;

	}

	public AssemblyModel getGoal() {
		return this.goal;

	}
}
