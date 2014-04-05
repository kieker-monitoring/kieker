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

package kieker.panalysis.base;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class Pipeline {

	protected final List<IStage> stages = new LinkedList<IStage>();
	private int freeId = 0;

	public Pipeline() {
		// No code necessary
	}

	public void addStage(final IStage stage) {
		stage.setId(this.freeId++);
		this.stages.add(stage);
	}

	public void start() {
		this.stages.get(0).execute();
	}

	public List<IStage> getStages() {
		return this.stages;
	}

	// BETTER use this generic method instead of one per new pipe type; However, this method throws an java.lang.InstantiationException
	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" })
	 * public static <O extends Enum<O>, I extends Enum<I>> IPipe<?> connect(final Class<? extends IPipe> pipeClass, final ISource<O> sourceStage,
	 * final O sourcePort,
	 * final ISink<I> targetStage,
	 * final I targetPort) {
	 * try {
	 * final IPipe pipe = pipeClass.newInstance();
	 * sourceStage.setPipeForOutputPort(sourcePort, pipe);
	 * targetStage.setPipeForInputPort(targetPort, pipe);
	 * return pipe;
	 * } catch (final InstantiationException e) {
	 * throw new IllegalStateException(e);
	 * } catch (final IllegalAccessException e) {
	 * throw new IllegalStateException(e);
	 * }
	 * }
	 */

}
