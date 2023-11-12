/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.sar.stages.dataflow;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.source.SourceModel;

import teetime.stage.basic.AbstractTransformation;

/**
 * Assembler stage based on dataflow events.
 *
 * @param <I>
 *            Input data type
 * @param <O>
 *            Output data type
 *
 * @author Reiner Jung
 * @since 1.1
 */
public abstract class AbstractDataflowAssemblerStage<I, O> extends AbstractTransformation<I, O> {

    protected final SourceModel sourceModel;
    protected final String sourceLabel;

    public AbstractDataflowAssemblerStage(final SourceModel sourceModel, final String sourceLabel) {
        this.sourceModel = sourceModel;
        this.sourceLabel = sourceLabel;
    }

    protected void addObjectToSource(final EObject object) {
        EList<String> sources = this.sourceModel.getSources().get(object);
        boolean exists = false;
        if (sources != null) {
            for (final String source : sources) {
                if (this.sourceLabel.equals(source)) {
                    exists = true;
                }
            }
        } else {
            sources = new BasicEList<>();
        }
        if (!exists) {
            sources.add(this.sourceLabel);
            this.sourceModel.getSources().put(object, sources);
        }
    }
}
