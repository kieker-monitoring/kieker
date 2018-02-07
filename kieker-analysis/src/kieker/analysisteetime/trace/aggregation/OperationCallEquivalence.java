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

package kieker.analysisteetime.trace.aggregation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Equivalence;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;

/**
 * This class defines an equivalence for {@link OperationCall}s using the class {@link Equivalence}. Therefore,
 * it provides the ability to wrap {@link OperationCall}s with adequate {@code equals()} and {@code hashCode()}
 * methods using its {@link #wrap(OperationCall)} method.
 *
 * With this class, two {@link OperationCall}s are considered equal iff both belong to the same
 * {@link DeployedOperation}, their children are also considered equal (in the same order) and - optionally -
 * have the same failed cause.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class OperationCallEquivalence extends Equivalence<OperationCall> {

	private final boolean considerFailed;

	public OperationCallEquivalence(final boolean considerFailed) {
		this.considerFailed = considerFailed;
	}

	@Override
	protected boolean doEquivalent(final OperationCall operationCallA, final OperationCall operationCallB) {
		// A and B are not the same object and are not nulls.
		final boolean equalsWithoutFailed = Objects.equals(operationCallA.getOperation(), operationCallB.getOperation())
				&& (operationCallA.getChildren().size() == operationCallB.getChildren().size())
				&& this.equivalent(operationCallA.getChildren(), operationCallB.getChildren());
		if (this.considerFailed) {
			return equalsWithoutFailed
					&& (operationCallA.isFailed() == operationCallB.isFailed())
					&& Objects.equals(operationCallA.getFailedCause(), operationCallB.getFailedCause());
		} else {
			return equalsWithoutFailed;
		}
	}

	@Override
	protected int doHash(final OperationCall operationCall) {
		final List<Integer> childrenHashs = operationCall.getChildren().stream().map(c -> this.hash(c)).collect(Collectors.toList());
		if (this.considerFailed) {
			return Objects.hash(operationCall.getOperation(), operationCall.isFailed(), operationCall.getFailedCause(), childrenHashs);
		} else {
			return Objects.hash(operationCall.getOperation(), childrenHashs);
		}
	}

	private boolean equivalent(final List<OperationCall> listA, final List<OperationCall> listB) {
		if (listA.size() != listB.size()) {
			return false;
		}
		return IntStream.range(0, listA.size()).allMatch(i -> this.equivalent(listA.get(i), listB.get(i)));
	}

}
