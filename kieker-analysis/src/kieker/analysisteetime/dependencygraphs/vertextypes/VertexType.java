/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.dependencygraphs.vertextypes;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public enum VertexType {

	ENTRY("entry"), COMPONENT_TYPE("component type"), OPERATION_TYPE("operation type"), ASSEMBLY_COMPONENT("assembly component"), ASSEMBLY_OPERATION(
			"assembly operation"), DEPLOYMENT_CONTEXT("deployment context"), DEPLOYED_COMPONENT("deployed component"), DEPLOYED_OPERATION("deployed operation");

	private final String name;

	private VertexType(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
