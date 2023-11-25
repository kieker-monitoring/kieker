/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.aul.stages;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.execution.Invocation;

/**
 * Interface for a node and edge selector for graphs to realize diff and subtract.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public interface IGraphElementSelector {

	void setRepository(ModelRepository repository);

	boolean nodeIsSelected(EObject value);

	boolean edgeIsSelected(Invocation value);

	String getFilePrefix();

	boolean isColorGroup(EList<String> sources, int group);

}
