/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.sources.SourceModel;

/**
 * Helper functions for SourceModel.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public abstract class AbstractSourceModelAssembler { // NOPMD should be abstract to avoid misuse as standalone assembler

	private final SourceModel sourceModel;
	private final String sourceLabel;

	public AbstractSourceModelAssembler(final SourceModel sourceModel, final String sourceLabel) {
		this.sourceModel = sourceModel;
		this.sourceLabel = sourceLabel;
	}

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
