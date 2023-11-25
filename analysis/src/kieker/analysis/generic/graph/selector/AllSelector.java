/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.generic.graph.selector;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.generic.graph.IGraphElementSelector;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public class AllSelector implements IGraphElementSelector {

	@Override
	public void setRepository(final ModelRepository repository) {
		// nothing to do here
	}

	@Override
	public boolean nodeIsSelected(final EObject value) {
		return true;
	}

	@Override
	public boolean edgeIsSelected(final Invocation value) {
		return true;
	}

	@Override
	public boolean edgeIsSelected(final OperationDataflow value) {
		return true;
	}

	@Override
	public boolean edgeIsSelected(final StorageDataflow value) {
		return true;
	}

	@Override
	public String getFilePrefix() {
		return "all";
	}

	@Override
	public boolean isColorGroup(final EList<String> sources, final int group) {
		return group == 0;
	}

}
