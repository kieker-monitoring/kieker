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
package kieker.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class CutTransformation extends AbstractAtomicTransformation {

	private String componentName;
	private String operationName;
	private AssemblyOperation operation;

	public CutTransformation(final AssemblyModel model) {
		super(model);
	}

	public void setComponentName(final String componentName) {
		this.componentName = componentName;
	}

	public void setOperationName(final String operationName) {
		this.operationName = operationName;
	}

	public AssemblyOperation getOperation() {
		return this.operation;
	}

	public String getComponentName() {
		return this.componentName;
	}

	public String getOperationName() {
		return this.operationName;
	}

	@Override
	public void applyTransformation(final AssemblyModel model) {
		// System.out.println("cutting" +this.componentName);
		model.getComponents().get(this.componentName).getOperations().removeKey(this.operationName);
		this.model = model;

	}

}
