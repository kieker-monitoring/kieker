/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * This class is part of the Master's thesis "Development of a Concurrent and Distributed Pipes and Filters Analysis Framework for Kieker".
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
// TODO: Make sure that the connection method in the AC allows connections only between elements within this instance.
@Plugin(outputPorts = {
	@OutputPort(name = "out", eventTypes = Object.class),
	@OutputPort(name = "internalInputPort", eventTypes = Object.class) })
public abstract class AbstractConcurrentCompositeAnalysisFilter extends AbstractFilterPlugin {

	private final List<AbstractAnalysisComponent> components = Collections.synchronizedList(new ArrayList<AbstractAnalysisComponent>());
	private final BlockingQueue<Object> senderQueue = new LinkedBlockingQueue<Object>();

	public AbstractConcurrentCompositeAnalysisFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	public AbstractAnalysisComponent createAndRegister(final Class<? extends AbstractAnalysisComponent> component, final Configuration configuration)
			throws Exception {
		final Constructor<? extends AbstractAnalysisComponent> constructor = component.getConstructor(Configuration.class, IProjectContext.class);
		final AbstractAnalysisComponent concreteComponent = constructor.newInstance(configuration, this.projectContext);

		this.components.add(concreteComponent);

		return concreteComponent;
	}

	public void connectWithOutput(final AbstractAnalysisComponent component, final String outputPortName) {
		// TODO: ((AnalysisController) this.projectContext).connect(component, outputPortName, this, "internalOutputPort");
	}

	public void connectWithInput(final AbstractAnalysisComponent component, final String inputPortName) {
		// TODO: ((AnalysisController) this.projectContext).connect(this, "internalInputPort", component, inputPortName);
	}

	@InputPort(name = "in", eventTypes = Object.class)
	public void in(final Object obj) {
		super.deliver("internalInputPort", obj);
	}

	@InputPort(name = "internalOutputPort", eventTypes = Object.class)
	public void out(final Object obj) {
		this.senderQueue.add(obj);
	}

	public void sendOut(final Object object) {
		this.senderQueue.add(object);
	}

	class SenderThread extends Thread {

		@Override
		public void run() {
			while (true) {
				Object obj;
				try {
					obj = AbstractConcurrentCompositeAnalysisFilter.this.senderQueue.take();
					AbstractConcurrentCompositeAnalysisFilter.this.deliver("out", obj);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
