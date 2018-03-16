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

package kieker.analysisteetime.trace.reconstruction;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.trace.traversal.IOperationCallVisitor;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DurRatioToRootParentCalculator implements IOperationCallVisitor {

	private final OperationCall rootOperationCall;

	public DurRatioToRootParentCalculator(final OperationCall rootOperationCall) {
		this.rootOperationCall = rootOperationCall;
	}

	@Override
	public void visit(final OperationCall operationCall) {
		if (operationCall.getParent() == null) {
			operationCall.setDurRatioToParent(100.0f);
		} else {
			final long childDuration = operationCall.getDuration().toNanos();
			final long rootDuration = this.rootOperationCall.getDuration().toNanos();
			final float ratio = (100.0f * childDuration) / rootDuration;
			operationCall.setDurRatioToParent(ratio);
		}
	}

}
