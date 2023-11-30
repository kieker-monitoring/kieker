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
package kieker.tools.mop.merge;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.type.TypePackage;

/**
 * Merge two different model repositories.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class ModelRepositoryMergerUtils {

	private ModelRepositoryMergerUtils() {
		// Utility class
	}

	public static void perform(final ModelRepository lastModelRepository, final ModelRepository mergeModelRepository) {
		TypeModelMerger.mergeTypeModel(lastModelRepository.getModel(TypePackage.Literals.TYPE_MODEL),
				mergeModelRepository.getModel(TypePackage.Literals.TYPE_MODEL));
		AssemblyModelMerger.mergeAssemblyModel(lastModelRepository.getModel(TypePackage.Literals.TYPE_MODEL),
				lastModelRepository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
				mergeModelRepository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL));
		DeploymentModelMerger.mergeDeploymentModel(
				lastModelRepository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
				lastModelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
				mergeModelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL));
		ExecutionModelMerger.mergeExecutionModel(
				lastModelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
				lastModelRepository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL),
				mergeModelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
				mergeModelRepository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL));
		StatisticsModelMerger.mergeStatisticsModel(
				lastModelRepository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL),
				lastModelRepository.getModel(StatisticsPackage.Literals.STATISTICS_MODEL),
				mergeModelRepository.getModel(StatisticsPackage.Literals.STATISTICS_MODEL));
		SourceModelMerger.mergeSourceModel(lastModelRepository.getModel(TypePackage.Literals.TYPE_MODEL),
				lastModelRepository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
				lastModelRepository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
				lastModelRepository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL),
				lastModelRepository.getModel(SourcePackage.Literals.SOURCE_MODEL),
				mergeModelRepository.getModel(SourcePackage.Literals.SOURCE_MODEL));
	}

}
