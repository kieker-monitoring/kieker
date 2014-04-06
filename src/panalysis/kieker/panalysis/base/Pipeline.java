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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <T>
 *            The type of the pipeline
 */
public class Pipeline<T extends IPipe> {

	protected final List<IStage> stages = new LinkedList<IStage>();
	private int freeId = 0;
	private List<AbstractFilter<?, ?>> startStages;

	private T currentPipe;
	private final Map<Integer, List<T>> pipeGroups;

	public Pipeline() {
		this(null);
	}

	public Pipeline(final Map<Integer, List<T>> pipeGroups) {
		this.pipeGroups = pipeGroups;
	}

	/**
	 * <p>
	 * Adds the given <code>stage</code> to this pipeline and set a unique identifier.
	 * </p>
	 * <p>
	 * Use this method as indicated in the following example: <blockquote> <code>Distributor distributor = pipeline.addStage(new Distributor())</code> </blockquote>
	 * </p>
	 * 
	 * @param stage
	 * @return
	 */
	public <S extends IStage> S addStage(final S stage) {
		stage.setId(this.freeId++);
		this.stages.add(stage);
		return stage;
	}

	public void start() {
		if (this.startStages.size() == 0) {
			throw new IllegalStateException("You need to define at least one start stage.");
		} else if (this.startStages.size() == 1) {
			this.stages.get(0).execute();
		} else {
			// TODO create a single distributor stage as start stage
			throw new IllegalStateException("Multiple start stages are not yet supported.");
		}
	}

	public List<IStage> getStages() {
		return this.stages;
	}

	public void setStartStages(final AbstractFilter<?, ?>... startStages) {
		this.startStages = Arrays.asList(startStages);
	}

	public Pipeline<T> add(final T pipe) {
		this.currentPipe = pipe;
		return this;
	}

	public void toGroup(final int pipeGroupIdentifier) {
		List<T> pipes = this.pipeGroups.get(pipeGroupIdentifier);
		if (pipes == null) {
			pipes = new LinkedList<T>();
			this.pipeGroups.put(pipeGroupIdentifier, pipes);
		}
		pipes.add(this.currentPipe);
	}

	public List<AbstractFilter<?, ?>> getStartStages() {
		return this.startStages;
	}
}
