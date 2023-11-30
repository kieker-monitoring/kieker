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
package kieker.analysis.generic.graph;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.exception.InternalErrorException;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;

/**
 * Interface for a node and edge selector for graphs to realize diff and subtract.
 *
 * @author Reiner Jung
 *
 */
public interface IGraphElementSelector {

	void setRepository(ModelRepository repository) throws InternalErrorException;

	boolean nodeIsSelected(EObject value);

	boolean edgeIsSelected(Invocation value);

	boolean edgeIsSelected(OperationDataflow value);

	boolean edgeIsSelected(StorageDataflow value);

	String getFilePrefix();

	boolean isColorGroup(EList<String> sources, int group);

}
