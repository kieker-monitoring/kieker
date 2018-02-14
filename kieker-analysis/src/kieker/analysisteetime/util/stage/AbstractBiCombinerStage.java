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

package kieker.analysisteetime.util.stage;

import java.util.LinkedList;
import java.util.Queue;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;

/**
 * Abstract stage that combines the elements from its two input ports.
 *
 * @param <I>
 *            Type of elements at first input port
 * @param <J>
 *            Type of elements at second input port
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public abstract class AbstractBiCombinerStage<I, J> extends AbstractStage {

	protected final InputPort<I> inputPort1 = this.createInputPort();
	protected final InputPort<J> inputPort2 = this.createInputPort();

	private final Queue<I> elements1 = new LinkedList<>();
	private final Queue<J> elements2 = new LinkedList<>();

	public AbstractBiCombinerStage() {
		super();
	}

	public final InputPort<I> getInputPort1() {
		return this.inputPort1;
	}

	public final InputPort<J> getInputPort2() {
		return this.inputPort2;
	}

	@Override
	protected void execute() {

		final I element1 = this.getInputPort1().receive();
		if (element1 != null) {
			this.elements1.add(element1);
		}
		final J element2 = this.getInputPort2().receive();
		if (element2 != null) {
			this.elements2.add(element2);
		}

		if ((this.elements1.size() > 0) && (this.elements2.size() > 0)) {
			this.combine(this.elements1.poll(), this.elements2.poll());
		}
	}

	protected abstract void combine(final I element1, final J element2);

}
