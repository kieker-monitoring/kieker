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
package kieker.analysis.architecture.repository;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;

import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.source.SourceFactory;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypePackage;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class ArchitectureModelUtils {

	public static final ModelDescriptor TYPE_MODEL = ArchitectureModelUtils.createDescriptor("type-model.xmi", TypePackage.Literals.TYPE_MODEL,
			TypeFactory.eINSTANCE);

	public static final ModelDescriptor ASSEMBLY_MODEL = ArchitectureModelUtils.createDescriptor("assembly-model.xmi", AssemblyPackage.Literals.ASSEMBLY_MODEL,
			AssemblyFactory.eINSTANCE);

	public static final ModelDescriptor DEPLOYMENT_MODEL = ArchitectureModelUtils.createDescriptor("deployment-model.xmi",
			DeploymentPackage.Literals.DEPLOYMENT_MODEL,
			DeploymentFactory.eINSTANCE);

	public static final ModelDescriptor EXECUTION_MODEL = ArchitectureModelUtils.createDescriptor("execution-model.xmi",
			ExecutionPackage.Literals.EXECUTION_MODEL,
			ExecutionFactory.eINSTANCE);

	public static final ModelDescriptor STATISTICS_MODEL = ArchitectureModelUtils.createDescriptor("statistics-model.xmi",
			StatisticsPackage.Literals.STATISTICS_MODEL,
			StatisticsFactory.eINSTANCE);

	public static final ModelDescriptor SOURCE_MODEL = ArchitectureModelUtils.createDescriptor("sources-model.xmi", SourcePackage.Literals.SOURCE_MODEL,
			SourceFactory.eINSTANCE);

	private ArchitectureModelUtils() {
		// utility class
	}

	private static ModelDescriptor createDescriptor(final String filename, final EClass rootClass, final EFactory factory) {
		return new ModelDescriptor(filename, rootClass, factory);
	}
}
