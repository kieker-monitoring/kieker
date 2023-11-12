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
package org.oceandsl.tools.mop.merge;

import java.util.Map.Entry;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Reiner Jung
 * @since
 */
public final class SourceModelMerger {

    private static final Logger LOGGER = LoggerFactory.getLogger(SourceModelMerger.class);

    private SourceModelMerger() {
        // Utility class
    }

    /* default */ static void mergeSourceModel(final TypeModel typeModel, final AssemblyModel assemblyModel, // NOPMD
            final DeploymentModel deploymentModel, final ExecutionModel executionModel,
            final SourceModel lastSourceModel, final SourceModel mergeSourceModel) {
        for (final Entry<EObject, EList<String>> mergeSource : mergeSourceModel.getSources()) {
            final EObject modelKey = SourceModelMerger.findCorrespondingKey(lastSourceModel.getSources(),
                    mergeSource.getKey());
            if (modelKey != null) {
                final EList<String> modelSource = lastSourceModel.getSources().get(modelKey);
                if (modelSource == null) {
                    SourceModelMerger.LOGGER.error("Model error no sources for existing key {}", modelKey);
                    lastSourceModel.getSources().put(modelKey, mergeSource.getValue());
                } else {
                    SourceModelMerger.mergeSources(modelSource, mergeSource.getValue());
                }
            } else {
                final EList<String> modelSources = new BasicEList<>();
                SourceModelMerger.mergeSources(modelSources, mergeSource.getValue());
                lastSourceModel.getSources().put(SourceModelMerger.findCorrespondingObject(typeModel, assemblyModel,
                        deploymentModel, executionModel, mergeSource.getKey()), modelSources);
            }
        }
    }

    private static EObject findCorrespondingObject(final TypeModel typeModel, final AssemblyModel assemblyModel,
            final DeploymentModel deploymentModel, final ExecutionModel executionModel, final EObject key) {
        EObject result = SourceModelMerger.findObjectInModel(typeModel.eAllContents(), key);
        if (result != null) {
            return result;
        }

        result = SourceModelMerger.findObjectInModel(assemblyModel.eAllContents(), key);
        if (result != null) {
            return result;
        }

        result = SourceModelMerger.findObjectInModel(deploymentModel.eAllContents(), key);
        if (result != null) {
            return result;
        }

        result = SourceModelMerger.findObjectInModel(executionModel.eAllContents(), key);
        if (result != null) {
            return result;
        }

        return null;
    }

    private static EObject findObjectInModel(final TreeIterator<EObject> iterator, final EObject key) {
        while (iterator.hasNext()) {
            final EObject item = iterator.next();
            if (ModelUtils.areObjectsEqual(key, item)) {
                return item;
            }
        }

        return null;
    }

    private static void mergeSources(final EList<String> modelSource, final EList<String> mergeSource) {
        for (final String value : mergeSource) {
            if (!modelSource.contains(value)) {
                modelSource.add(value);
            }
        }
    }

    private static EObject findCorrespondingKey(final EMap<EObject, EList<String>> sources, final EObject mergeKey) {
        for (final EObject key : sources.keySet()) {
            if (ModelUtils.areObjectsEqual(key, mergeKey)) {
                return key;
            }
        }
        return null;
    }
}
