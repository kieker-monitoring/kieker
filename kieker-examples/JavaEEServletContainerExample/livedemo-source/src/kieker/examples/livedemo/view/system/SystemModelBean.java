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

package kieker.examples.livedemo.view.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import kieker.examples.livedemo.view.AnalysisBean;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "systemModelBean")
@ApplicationScoped
public class SystemModelBean {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	public SystemModelBean() {
		// No code necessary
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public List<ComponentType> getComponentTypes() {
		final List<ComponentType> ctList = new ArrayList<ComponentType>();
		final Collection<ComponentType> collection = this.analysisBean.getSystemModelRepository().getTypeRepositoryFactory().getComponentTypes();
		ctList.addAll(collection);
		return ctList;
	}

	public List<Operation> getOperations() {
		final List<Operation> opList = new ArrayList<Operation>();
		final Collection<Operation> collection = this.analysisBean.getSystemModelRepository().getOperationFactory().getOperations();
		opList.addAll(collection);
		return opList;
	}

	public List<AssemblyComponent> getAssemblyComponents() {
		final List<AssemblyComponent> acList = new ArrayList<AssemblyComponent>();
		final Collection<AssemblyComponent> collection = this.analysisBean.getSystemModelRepository().getAssemblyFactory().getAssemblyComponentInstances();
		acList.addAll(collection);
		return acList;
	}

	public List<ExecutionContainer> getExecutionContainers() {
		final List<ExecutionContainer> ecList = new ArrayList<ExecutionContainer>();
		final Collection<ExecutionContainer> collection = this.analysisBean.getSystemModelRepository().getExecutionEnvironmentFactory().getExecutionContainers();
		ecList.addAll(collection);
		return ecList;
	}

	public List<AllocationComponent> getDeploymentComponents() {
		final List<AllocationComponent> acList = new ArrayList<AllocationComponent>();
		final Collection<AllocationComponent> collection = this.analysisBean.getSystemModelRepository().getAllocationFactory().getAllocationComponentInstances();
		acList.addAll(collection);
		return acList;
	}

}
