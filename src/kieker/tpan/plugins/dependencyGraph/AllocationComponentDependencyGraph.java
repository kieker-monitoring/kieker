package kieker.tpan.plugins.dependencyGraph;

import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.factories.SystemEntityFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */
/**
 *
 * @author Andre van Hoorn
 */
public class AllocationComponentDependencyGraph extends DependencyGraphPlugin<AllocationComponentInstance> {

    public AllocationComponentDependencyGraph(final String name,
            final SystemEntityFactory systemEntityFactory) {
        super(name, systemEntityFactory);
    }

    protected String nodeLabel(final DependencyNode node,
            final boolean shortLabels) {
        AllocationComponentInstance component = (AllocationComponentInstance) node.getAllocationComponent();
        if (component == super.getSystemEntityFactory().getAllocationFactory().rootAllocationComponent) {
            return "$";
        }

        String resourceContainerName = component.getExecutionContainer().getName();
        String assemblyComponentName = component.getAssemblyComponent().getName();
        String componentTypePackagePrefx = component.getAssemblyComponent().getType().getPackageName();
        String componentTypeIdentifier = component.getAssemblyComponent().getType().getTypeName();

        StringBuilder strBuild = new StringBuilder(resourceContainerName).append("::").append(assemblyComponentName).append(":");
        if (!shortLabels) {
            strBuild.append(componentTypePackagePrefx);
        } else {
            strBuild.append("..");
        }
        strBuild.append(componentTypeIdentifier);
        return strBuild.toString();
    }
}
