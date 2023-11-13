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
package kieker.tools.fxca.stages.dataflow.data;

import kieker.model.analysismodel.execution.EDirection;

/**
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class CallerCalleeDataflow implements IDataflowEntry {

	private final String sourceFileName;

	private final String sourceModuleName;

	private final String sourceOperationName;

	private final String targetFileName;

	private final String targetModuleName;

	private final String targetOperatioName;

	private EDirection direction; // NOPMD pmd does not understand lombok

	public CallerCalleeDataflow(final String sourceFileName, final String sourceModuleName,
			final String sourceOperationName, final String targetFileName, final String targetModuleName,
			final String targetOperatioName, final EDirection direction) {
		this.sourceFileName = sourceFileName;
		this.sourceModuleName = sourceModuleName;
		this.sourceOperationName = sourceOperationName;
		this.targetFileName = targetFileName;
		this.targetModuleName = targetModuleName;
		this.targetOperatioName = targetOperatioName;
		this.direction = direction;
	}

	public String getSourceFileName() {
		return this.sourceFileName;
	}

	public String getSourceModuleName() {
		return this.sourceModuleName;
	}

	public String getSourceOperationName() {
		return this.sourceOperationName;
	}

	public String getTargetFileName() {
		return this.targetFileName;
	}

	public String getTargetModuleName() {
		return this.targetModuleName;
	}

	public String getTargetOperatioName() {
		return this.targetOperatioName;
	}

	public EDirection getDirection() {
		return this.direction;
	}

	public void setDirection(final EDirection direction) {
		this.direction = direction;
	}

}
