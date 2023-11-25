/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.recovery.assembler;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.source.SourceModel;

/**
 * The {@link AbstractModelAssembler} provides standard functionality for all assemblers regarding
 * tagging elements of a model.
 *
 * @param <T>
 *            Imput event type
 *
 * @author Reiner Jung
 * @since 1.15
 */
public abstract class AbstractModelAssembler<T> {

	private final SourceModel sourceModel;
	private final String sourceLabel;

	public AbstractModelAssembler(final SourceModel sourceModel, final String sourceLabel) {
		this.sourceModel = sourceModel;
		this.sourceLabel = sourceLabel;
	}

	/**
	 * Process an event of type T and add corresponding model elements to the model.
	 *
	 * @param event
	 *            input event
	 */
	public abstract void assemble(T event);

	public void updateSourceModel(final EObject object) {
		final EMap<EObject, EList<String>> sources = this.sourceModel.getSources();
		EList<String> sourceIds = sources.get(object);
		if (sourceIds == null) {
			sourceIds = new UniqueEList<>();
			sources.put(object, sourceIds);
			sourceIds = sources.get(object);
		}
		sourceIds.add(this.sourceLabel);
	}
}
