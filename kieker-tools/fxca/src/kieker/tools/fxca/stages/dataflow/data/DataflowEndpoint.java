/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.tools.fxca.stages.dataflow.data;

import java.util.ArrayList;
import java.util.List;

import kieker.model.analysismodel.execution.EDirection;
import kieker.tools.fxca.model.FortranModule;
import kieker.tools.fxca.model.FortranOperation;
import kieker.tools.fxca.model.FortranParameter;
import kieker.tools.fxca.model.FortranVariable;
import kieker.tools.fxca.model.IDataflowEndpoint;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class DataflowEndpoint implements IDataflowEndpoint {

	private final String name;

	private final FortranModule module;

	private final FortranOperation operation;

	private EDirection direction; // NOPMD lombok

	private final List<IDataflowEndpoint> sources = new ArrayList<>();

	private final IDataflowEndpoint endpoint;

	public DataflowEndpoint(final FortranModule module, final FortranOperation operation,
			final IDataflowEndpoint endpoint, final EDirection direction) {
		this.module = module;
		this.operation = operation;
		this.name = this.operation.getName();
		this.endpoint = endpoint;
		this.direction = direction;
	}

	@Override
	public String toString() {
		final String moduleName = this.module != null ? this.module.getFileName() : "<>";
		final String operationName = this.operation != null ? this.operation.getName() : "<>";
		final String endpointName = this.endpoint != null ? this.endpointName() : "<>";
		return moduleName + "::" + operationName + " ~ " + endpointName + ":" + this.direction.name();
	}

	@Override
	public String getName() {
		return this.name;
	}

	public FortranModule getModule() {
		return this.module;
	}

	public FortranOperation getOperation() {
		return this.operation;
	}

	public EDirection getDirection() {
		return this.direction;
	}

	public void setDirection(final EDirection direction) {
		this.direction = direction;
	}

	public List<IDataflowEndpoint> getSources() {
		return this.sources;
	}

	public IDataflowEndpoint getEndpoint() {
		return this.endpoint;
	}

	private String endpointName() {
		if (this.endpoint instanceof FortranOperation) {
			return "<return-value>";
		} else if (this.endpoint instanceof FortranParameter) {
			return ((FortranParameter) this.endpoint).getName();
		} else if (this.endpoint instanceof FortranVariable) {
			return ((FortranVariable) this.endpoint).getName();
		} else {
			return "<" + this.endpoint.getClass().getSimpleName() + ">";
		}
	}

}
