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
package kieker.tools.cmi.stages;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyPackage;

/**
 * Check assembly model.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CheckAssemblyModelStage extends AbstractCollector<ModelRepository> {

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final Report report = new Report("assembly model");

        final AssemblyModel assemblyModel = repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL);

        GenericCheckUtils.missingSignature(assemblyModel.eAllContents(), report);
        GenericCheckUtils.missingName(assemblyModel.eAllContents(), report);
        GenericCheckUtils.missingPackage(assemblyModel.eAllContents(), report);

        GenericCheckUtils.checkReferences(AssemblyPackage.Literals.ASSEMBLY_MODEL, assemblyModel.eAllContents(),
                report);
        this.outputPort.send(repository);
        this.reportOutputPort.send(report);
    }

}
