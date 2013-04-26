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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.util.SerializationUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
@Plugin(outputPorts = {
	@OutputPort(name = "out", eventTypes = Object.class),
	@OutputPort(name = "internalOutputPort", eventTypes = Object.class) })
public class AnalysisNode extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";
	public static final String OUTPUT_PORT_NAME_EVENTS = "sentEvents";

	protected static final String INTERNAL_INPUT_PORT_NAME_EVENTS = "internalInputPort";
	protected static final String INTERNAL_OUTPUT_PORT_NAME_EVENTS = "internalOutputPort";

	protected static final String DATA_EXCHANGE_NAME = "net.kieker-monitoring.analysis.data";

	private final BlockingQueue<Object> sendQueue = new LinkedBlockingQueue<Object>();
	private final String name;
	private final boolean distributed;
	private final Channel receiveChannel;
	private final Channel sendChannel;
	private final String receiveQueueName;

	public AnalysisNode(final Configuration configuration, final IProjectContext projectContext, final String name, final boolean distributed) throws IOException {
		super(configuration, projectContext);

		// TODO: As configuration
		this.name = name;
		this.distributed = distributed;

		if (distributed) {
			final ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");

			this.receiveChannel = factory.newConnection().createChannel();
			this.receiveChannel.exchangeDeclare(DATA_EXCHANGE_NAME, "topic");
			this.receiveQueueName = this.receiveChannel.queueDeclare().getQueue();

			this.sendChannel = factory.newConnection().createChannel();
			this.sendChannel.exchangeDeclare(DATA_EXCHANGE_NAME, "topic");
		} else {
			this.receiveChannel = null;
			this.sendChannel = null;
			this.receiveQueueName = null;
		}
	}

	@Override
	public boolean init() {
		if (this.distributed) {
			final Thread senderThread = new SenderThread();
			final Thread receiverThread = new ReceiverThread();

			senderThread.setDaemon(true);
			receiverThread.setDaemon(true);

			senderThread.start();
			receiverThread.start();
		}
		return true;
	}

	@Override
	public void terminate(final boolean error) {
		super.terminate(error);
	}

	public final void connect(final String predecessorNode) throws IOException {
		this.receiveChannel.queueBind(this.receiveQueueName, DATA_EXCHANGE_NAME, predecessorNode);
	}

	/**
	 * This method creates a new instance of this given component, registers it with this composite filter and returns it for further actions.
	 * 
	 * @param componentClass
	 *            The class of the component to be created.
	 * @param configuration
	 *            The configuration for the component to create.
	 * 
	 * @return The newly created component.
	 * 
	 * @throws Exception
	 *             If something went wrong.
	 */
	@SuppressWarnings("unchecked")
	public final <T extends AbstractAnalysisComponent> T createAndRegister(final Class<T> componentClass, final Configuration configuration) throws Exception {
		final Constructor<? extends AbstractAnalysisComponent> constructor = componentClass.getConstructor(Configuration.class, IProjectContext.class);
		final AbstractAnalysisComponent concreteComponent = constructor.newInstance(configuration, this.projectContext);

		concreteComponent.registerWithinComponent(this);

		return (T) concreteComponent;
	}

	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = Object.class)
	public final void inputPort(final Object data) {
		super.deliver(INTERNAL_OUTPUT_PORT_NAME_EVENTS, data);
	}

	@InputPort(name = INTERNAL_INPUT_PORT_NAME_EVENTS, eventTypes = Object.class)
	public final void internalInputPort(final Object data) {
		this.sendQueue.add(data);
		super.deliver(OUTPUT_PORT_NAME_EVENTS, data);
	}

	public final void connectWithOutput(final AbstractPlugin component, final String outputPortName) throws IllegalStateException, AnalysisConfigurationException {
		((AnalysisController) this.projectContext).connect(component, outputPortName, this, INTERNAL_INPUT_PORT_NAME_EVENTS);
	}

	public final void connectWithInput(final AbstractPlugin component, final String inputPortName) throws IllegalStateException, AnalysisConfigurationException {
		((AnalysisController) this.projectContext).connect(this, INTERNAL_OUTPUT_PORT_NAME_EVENTS, component, inputPortName);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	class SenderThread extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					final Object data = AnalysisNode.this.sendQueue.take();
					// TODO Replace SerializationUtils
					AnalysisNode.this.sendChannel.basicPublish(DATA_EXCHANGE_NAME, AnalysisNode.this.name, null, SerializationUtils.serialize(data));
				} catch (final Exception ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	class ReceiverThread extends Thread {

		@Override
		public void run() {
			try {
				final QueueingConsumer consumer = new QueueingConsumer(AnalysisNode.this.receiveChannel);
				AnalysisNode.this.receiveChannel.basicConsume(AnalysisNode.this.receiveQueueName, true, consumer);

				while (true) {
					final QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					final Object data = SerializationUtils.deserialize(delivery.getBody());
					AnalysisNode.this.deliver(INTERNAL_OUTPUT_PORT_NAME_EVENTS, data);
				}
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}

	}
}
