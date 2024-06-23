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
package kieker.analysis.metrics.graph.entropy;

import java.util.Objects;

import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.execution.OperationDataflow;

/**
 * Central node class for graphs.
 *
 * @param <T>
 *            type representing modules
 * @param <E>
 *            type representing nodes
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class KiekerNode<T, E> implements Node<T> {

	private final E member;

	/**
	 * @param member
	 */
	public KiekerNode(final E member) {
		this.member = member;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */

	@Override
	public int hashCode() {
		return Objects.hash(this.member);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof KiekerNode)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		final KiekerNode<T, E> other = (KiekerNode<T, E>) obj;
		return Objects.equals(this.member, other.member);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getModule() {
		if (this.member != null) {
			if (this.member instanceof DeployedOperation) {
				final DeployedOperation memberLocal = (DeployedOperation) this.member;
				return (T) memberLocal.eContainer().eContainer();

			} else if (this.member instanceof DeployedStorage) {
				final DeployedStorage memberLocal = (DeployedStorage) this.member;
				return (T) memberLocal.eContainer().eContainer();
			} else {
				System.err.println( // NOCS, NOPMD we cannot trigger a exception, thus we have to handle
									// this currently as println
						"[Kieker Node Object] Invalid Kieker Node detected! Member is either an instance of DeployedOperation nor DeployedStorage.");
				return null;
			}
		} else {
			return null;
		}
	}

	public E getMember() {
		return this.member;
	}

	public boolean isOperation() {
		return this.member instanceof OperationDataflow;
	}
}
