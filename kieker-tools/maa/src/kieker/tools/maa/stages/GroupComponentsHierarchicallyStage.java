/***************************************************************************
 * Copyright (C) 2022 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.maa.stages;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.generic.MapFileReader;
import kieker.analysis.generic.StringValueConverter;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class GroupComponentsHierarchicallyStage extends AbstractTransformation<ModelRepository, ModelRepository> {

	// File to module map
	private final Map<String, String> componentMap = new HashMap<>();

	public GroupComponentsHierarchicallyStage(final List<Path> componentMapFiles, final String separator,
			final boolean caseInsensitive) throws IOException {
		for (final Path componentMapFile : componentMapFiles) {
			this.logger.info("Reading map file {}", componentMapFile.toString());
			final MapFileReader<String, String> mapFileReader = new MapFileReader<>(componentMapFile, separator,
					this.componentMap, new StringValueConverter(caseInsensitive, 1),
					new StringValueConverter(caseInsensitive, 0));
			mapFileReader.read();
		}
	}

	@Override
	protected void execute(final ModelRepository repository) throws Exception {
		final TypeModel typeModel = repository.getModel(TypePackage.Literals.TYPE_MODEL);
		final AssemblyModel assemblyModel = repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL);
		this.componentMap.values().forEach(componentName -> {
			if (!typeModel.getComponentTypes().containsKey(componentName)) {
				final ComponentType componentType = this.createComponentType(componentName);
				final AssemblyComponent component = this.createAssemblyComponent(componentName, componentType);
				this.addSubComponents(componentType, component, assemblyModel.getComponents());
				if (component.getContainedComponents().size() > 0) { // ignore empty main component
					typeModel.getComponentTypes().put(componentName, componentType);
					assemblyModel.getComponents().put(componentName, component);
				} // TODO should also update source model and deployment model

			}
		});
		this.outputPort.send(repository);
	}

	private void addSubComponents(final ComponentType componentType, final AssemblyComponent component,
			final EMap<String, AssemblyComponent> components) {
		this.componentMap.entrySet().forEach(entry -> {
			if (entry.getValue().equals(component.getSignature())) {
				final AssemblyComponent subComponent = components.get(entry.getKey());
				if (subComponent != null) {
					componentType.getContainedComponents().add(subComponent.getComponentType());
					component.getContainedComponents().add(subComponent);
				}
			}
		});
	}

	private ComponentType createComponentType(final String componentName) {
		final ComponentType componentType = TypeFactory.eINSTANCE.createComponentType();
		componentType.setSignature(componentName);
		final int divider = componentName.lastIndexOf('.');
		componentType.setName(componentName.substring(divider + 1));
		if (divider <= 0) {
			componentType.setPackage("");
		} else {
			componentType.setPackage(componentName.substring(0, divider - 1));
		}

		return componentType;
	}

	private AssemblyComponent createAssemblyComponent(final String componentName, final ComponentType componentType) {
		final AssemblyComponent component = AssemblyFactory.eINSTANCE.createAssemblyComponent();
		component.setSignature(componentName);
		component.setComponentType(componentType);

		return component;
	}
}
