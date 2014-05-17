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
package kieker.panalysis.framework.concurrent;

import java.util.LinkedList;
import java.util.List;

import kieker.panalysis.framework.concurrent.steal.IStealStrategy;
import kieker.panalysis.framework.concurrent.steal.StealIfEmptyStrategy;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentWorkStealingPipeFactory<T> {

	private final List<ConcurrentWorkStealingPipe<T>> pipes = new LinkedList<ConcurrentWorkStealingPipe<T>>();
	private final IStealStrategy<T> stealStrategy = new StealIfEmptyStrategy<T>();

	public ConcurrentWorkStealingPipe<T> create() {
		final ConcurrentWorkStealingPipe<T> pipe = new ConcurrentWorkStealingPipe<T>(this.stealStrategy);
		pipe.setPipesToStealFrom(this.pipes);

		this.pipes.add(pipe);

		return pipe;
	}

	public List<ConcurrentWorkStealingPipe<T>> getPipes() {
		return this.pipes;
	}
}
