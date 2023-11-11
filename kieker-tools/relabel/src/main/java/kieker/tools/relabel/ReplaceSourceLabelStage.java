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
package kieker.tools.relabel;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.source.SourcePackage;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public class ReplaceSourceLabelStage extends AbstractFilter<ModelRepository> {

    private final List<Replacement> replacements;
    private final String repositoryName;

    public ReplaceSourceLabelStage(final String repositoryName, final List<Replacement> replacements) {
        this.replacements = replacements;
        this.repositoryName = repositoryName;
    }

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final ModelRepository newRepository;
        if (this.repositoryName != null) {
            newRepository = new ModelRepository(this.repositoryName);
            repository.getModels().entrySet()
                    .forEach(entry -> newRepository.getModels().put(entry.getKey(), entry.getValue()));
        } else {
            newRepository = repository;
        }
        final SourceModel sourceModel = newRepository.getModel(SourcePackage.Literals.SOURCE_MODEL);
        for (final Replacement replacement : this.replacements) {
            if ("".equals(replacement.getSources().get(0))) {
                this.annotateAllModels(newRepository, sourceModel, replacement);
            } else {
                this.processModel(sourceModel, replacement);
            }
        }
        this.outputPort.send(repository);
    }

    private void annotateAllModels(final ModelRepository repository, final SourceModel sourceModel,
            final Replacement replacement) {
        for (final EObject model : repository.getModels().values()) {
            if (!(model instanceof SourceModel)) {
                this.annotateModel(model, sourceModel, replacement);
            }
        }
    }

    private void annotateModel(final EObject model, final SourceModel sourceModel, final Replacement replacement) {
        final EList<String> targets = this.makeElist(replacement.getTargets());
        model.eAllContents().forEachRemaining(object -> sourceModel.getSources().put(object, targets));
    }

    private EList<String> makeElist(final List<String> targets) {
        final EList<String> list = new BasicEList<>();
        list.addAll(targets);
        return list;
    }

    private void processModel(final SourceModel sourceModel, final Replacement replacement) {
        for (final Entry<EObject, EList<String>> entry : sourceModel.getSources()) {
            this.processReplacement(replacement, entry.getValue());
        }
    }

    private void processReplacement(final Replacement replacement, final EList<String> labels) {
        int matches = 0;
        for (final String find : replacement.getSources()) {
            if (labels.contains(find)) {
                matches++;
            }
        }
        if (matches == replacement.getSources().size()) {
            for (final String find : replacement.getSources()) {
                labels.remove(find);
            }
            for (final String target : replacement.getTargets()) {
                if (!labels.contains(target)) {
                    labels.add(target);
                }
            }
        }
    }
}
