/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.framework.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
abstract class AbstractMultiPort<S extends IStage, T> {

	private final Set<IPipe<T>> associatedPipes = new HashSet<IPipe<T>>();
	private final Set<IPipe<T>> readOnlyAssociatedPipes = Collections.unmodifiableSet(this.associatedPipes);

	private S owningStage;

	public void addAssociatedPipe(final IPipe<T> associatedPipe) {
		this.associatedPipes.add(associatedPipe);
	}

	public Set<IPipe<T>> getAssociatedPipes() {
		return this.readOnlyAssociatedPipes;
	}

	public S getOwningStage() {
		return this.owningStage;
	}

	public void setOwningStage(final S owningStage) {
		this.owningStage = owningStage;
	}
}
