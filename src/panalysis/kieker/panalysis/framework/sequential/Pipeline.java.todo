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

package kieker.panalysis.framework.sequential;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.framework.core.ISink;
import kieker.panalysis.framework.core.ISource;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <P>
 *            The type of the pipes
 */
public class Pipeline<P extends IPipe<?>> {

	protected final List<IStage> stages = new LinkedList<IStage>();
	private int freeId = 0;
	private AbstractFilter<?>[] startStages;

	private P currentPipe;
	private final Map<Integer, List<P>> pipeGroups;
	private final Map<IOutputPort<?, ?>, IInputPort<?, ?>> connections = new HashMap<IOutputPort<?, ?>, IInputPort<?, ?>>();

	/**
	 * The default constructor.<br>
	 * <i>Public constructors are available via the static method <code>create(..)</code></i>
	 * 
	 * @see for example, {@link #create(Map)}
	 * 
	 * @since 1.10
	 */
	private Pipeline() {
		this(new HashMap<Integer, List<P>>());
	}

	/**
	 * @since 1.10
	 */
	private Pipeline(final Map<Integer, List<P>> pipeGroups) {
		this.pipeGroups = pipeGroups;
	}

	/**
	 * @since 1.10
	 */
	// this constructor prevents the programmer from repeating the type argument
	public static <P extends IPipe<?>> Pipeline<P> create() {
		return new Pipeline<P>();
	}

	/**
	 * 
	 * @param pipeGroups
	 *            a map where its keys represent the group id and its values represent the list of pipes
	 * @return
	 */
	// this constructor prevents the programmer from repeating the type argument
	public static <P extends IPipe<?>> Pipeline<P> create(final Map<Integer, List<P>> pipeGroups) {
		return new Pipeline<P>(pipeGroups);
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
		stage.setOwningPipeline(this);
		this.stages.add(stage);
		return stage;
	}

	/**
	 * @since 1.10
	 */
	public void start() {
		this.fireStartEvent();

		if (this.startStages.length == 0) {
			throw new IllegalStateException("You need to define at least one start stage.");
		} else if (this.startStages.length == 1) {
			// this.startStages.get(0).execute();
		} else {
			// TODO create a single distributor stage as start stage
			throw new IllegalStateException("Multiple start stages are not yet supported.");
		}
	}

	/**
	 * @since 1.10
	 */
	private void fireStartEvent() {
		for (final IStage stage : this.stages) {
			stage.onPipelineStarts();
		}
	}

	/**
	 * @since 1.10
	 */
	public List<IStage> getStages() {
		return this.stages;
	}

	/**
	 * @since 1.10
	 */
	public Pipeline<P> add(final P pipe) {
		this.currentPipe = pipe;
		return this;
	}

	/**
	 * @since 1.10
	 */
	public <S0 extends ISource, S1 extends ISink<S1>, T> void connect(final IOutputPort<S0, T> sourcePort, final IInputPort<S1, T> targetPort) {
		this.connections.put(sourcePort, targetPort);
	}

	/**
	 * @since 1.10
	 */
	public void toGroup(final int pipeGroupIdentifier) {
		List<P> pipes = this.pipeGroups.get(pipeGroupIdentifier);
		if (pipes == null) {
			pipes = new LinkedList<P>();
			this.pipeGroups.put(pipeGroupIdentifier, pipes);
		}
		pipes.add(this.currentPipe);
	}

	/**
	 * @since 1.10
	 */
	// BETTER move this method out of the pipeline
	public void connectPipeGroups() {
		for (final List<P> samePipes : this.pipeGroups.values()) {
			for (final P pipe : samePipes) {
				pipe.copyAllOtherPipes(samePipes);
			}
		}
	}

	/**
	 * @since 1.10
	 */
	public void setStartStages(final AbstractFilter<?>... startStages) {
		this.startStages = startStages;
	}

	/**
	 * @since 1.10
	 */
	public AbstractFilter<?>[] getStartStages() {
		return this.startStages;
	}
}
