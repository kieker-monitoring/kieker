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

package kieker.analysis.plugin;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.util.SerializationUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = AnalysisNode.OUTPUT_PORT_NAME_EVENTS, eventTypes = Object.class),
			@OutputPort(name = AnalysisNode.INTERNAL_OUTPUT_PORT_NAME_EVENTS, eventTypes = Object.class, internalUseOnly = true) },
		// repositoryOutputPorts = {
		// @RepositoryOutputPort(name = AnalysisNode.REPOSITORY_OUTPUT_PORT_NAME_EVENTS),
		// @RepositoryOutputPort(name = AnalysisNode.INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS, internalUseOnly = true),
		// },
		configuration = {
			@Property(name = AnalysisNode.CONFIG_PROPERTY_NAME_MOM_SERVER, defaultValue = "localhost"),
			@Property(name = AnalysisNode.CONFIG_PROPERTY_NAME_DISTRIBUTED, defaultValue = "false"),
			@Property(name = AnalysisNode.CONFIG_PROPERTY_NAME_NODE_NAME, defaultValue = "kieker-node"),
		})
public class AnalysisNode extends AbstractFilterPlugin {

	public static final String CONFIG_PROPERTY_NAME_MOM_SERVER = "server";
	public static final String CONFIG_PROPERTY_NAME_DISTRIBUTED = "distributed";
	public static final String CONFIG_PROPERTY_NAME_NODE_NAME = "nodeName";

	// public static final String REPOSITORY_INPUT_PORT_NAME_EVENTS = "receivedRepositoryEvents";
	// public static final String REPOSITORY_OUTPUT_PORT_NAME_EVENTS = "sentRepositoryEvents";
	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";
	public static final String OUTPUT_PORT_NAME_EVENTS = "sentEvents";

	// protected static final String INTERNAL_REPOSITORY_INPUT_PORT_NAME_EVENTS = "internalRepositoryInputPort";
	// protected static final String INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS = "internalRepositoryOutputPort";
	protected static final String INTERNAL_INPUT_PORT_NAME_EVENTS = "internalInputPort";
	protected static final String INTERNAL_OUTPUT_PORT_NAME_EVENTS = "internalOutputPort";

	protected static final String DATA_EXCHANGE_NAME = "net.kieker-monitoring.analysis.data";

	private static final Log LOG = LogFactory.getLog(AnalysisNode.class);

	private final ReceiverThread receiverThread;
	private final SenderThread senderThread;
	private final BlockingQueue<Object> sendQueue;
	private final String name;
	private final String host;
	private final boolean distributed;
	private final Channel receiveChannel;
	private final Channel sendChannel;
	private final String receiveQueueName;

	public AnalysisNode(final Configuration configuration, final IProjectContext projectContext) throws IOException {
		super(configuration, projectContext);

		// Get the properties
		this.name = configuration.getStringProperty(CONFIG_PROPERTY_NAME_NODE_NAME);
		this.distributed = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_DISTRIBUTED);
		this.host = configuration.getStringProperty(CONFIG_PROPERTY_NAME_MOM_SERVER);

		// Configure the plugin depending on whether this is a distributed or a local node.
		if (this.distributed) {
			final ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(configuration.getStringProperty(CONFIG_PROPERTY_NAME_MOM_SERVER));

			this.receiveChannel = factory.newConnection().createChannel();
			this.receiveChannel.exchangeDeclare(DATA_EXCHANGE_NAME, "topic");
			this.receiveQueueName = this.receiveChannel.queueDeclare().getQueue();

			this.sendChannel = factory.newConnection().createChannel();
			this.sendChannel.exchangeDeclare(DATA_EXCHANGE_NAME, "topic");

			this.sendQueue = new LinkedBlockingQueue<Object>();

			this.senderThread = new SenderThread();
			this.receiverThread = new ReceiverThread();
		} else {
			this.receiveChannel = null;
			this.sendChannel = null;
			this.receiveQueueName = null;
			this.sendQueue = null;
			this.receiverThread = null;
			this.senderThread = null;
		}
	}

	@Override
	public boolean init() {
		// Start both threads if necessary
		if (this.distributed) {
			this.senderThread.start();
			this.receiverThread.start();
		}

		return true;
	}

	@Override
	public void terminate(final boolean error) {
		// Terminate both threads if necessary
		if (this.distributed) {
			this.senderThread.terminate();
			this.receiverThread.terminate();
		}
	}

	public final void connect(final String predecessorNode) throws IOException, AnalysisConfigurationException {
		if (this.distributed) {
			this.receiveChannel.queueBind(this.receiveQueueName, DATA_EXCHANGE_NAME, predecessorNode);
		} else {
			throw new AnalysisConfigurationException("Node is not configured for distributed usage.");
		}
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

	// @RepositoryInputPort(name = REPOSITORY_INPUT_PORT_NAME_EVENTS)
	// public final void repositoryInputPort(final Object data) {
	// super.deliverWithoutReturnTypeToRepository(INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS, data);
	// }

	// @RepositoryInputPort(name = INTERNAL_REPOSITORY_INPUT_PORT_NAME_EVENTS, internalUseOnly = true)
	// public final void internalRepositoryInputPort(final Object data) {
	// If this node is configured for distributed access, we have to send the message to the MOM as well.
	// if (this.distributed) {
	// this.repositorySendQueue.add(data);
	// }
	// super.deliver(OUTPUT_PORT_NAME_EVENTS, data);
	// }

	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = Object.class)
	public final void inputPort(final Object data) {
		super.deliver(INTERNAL_OUTPUT_PORT_NAME_EVENTS, data);
	}

	@InputPort(name = INTERNAL_INPUT_PORT_NAME_EVENTS, eventTypes = Object.class, internalUseOnly = true)
	public final void internalInputPort(final Object data) {
		// If this node is configured for distributed access, we have to send the message to the MOM as well.
		if (this.distributed) {
			this.sendQueue.add(data);
		}
		super.deliver(OUTPUT_PORT_NAME_EVENTS, data);
	}

	// public final void connectWithRepositoryOutput(final AbstractPlugin component, final String outputPortName) throws IllegalStateException,
	// AnalysisConfigurationException {
	// ((AnalysisController) this.projectContext).connect(component, outputPortName, this, INTERNAL_REPOSITORY_INPUT_PORT_NAME_EVENTS);
	// }

	// public final void connectWithRepositoryInput(final AbstractRepository component, final String inputPortName) throws IllegalStateException,
	// AnalysisConfigurationException {
	// ((AnalysisController) this.projectContext).connect(this, INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS, component, inputPortName);
	// }

	public final void connectWithOutput(final AbstractPlugin component, final String outputPortName) throws IllegalStateException, AnalysisConfigurationException {
		((AnalysisController) this.projectContext).connect(component, outputPortName, this, INTERNAL_INPUT_PORT_NAME_EVENTS);
	}

	public final void connectWithInput(final AbstractPlugin component, final String inputPortName) throws IllegalStateException, AnalysisConfigurationException {
		((AnalysisController) this.projectContext).connect(this, INTERNAL_OUTPUT_PORT_NAME_EVENTS, component, inputPortName);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_MOM_SERVER, this.host);
		configuration.setProperty(CONFIG_PROPERTY_NAME_NODE_NAME, this.name);
		configuration.setProperty(CONFIG_PROPERTY_NAME_DISTRIBUTED, Boolean.toString(this.distributed));

		return configuration;
	}

	class SenderThread extends Thread {

		private volatile boolean terminated;

		@SuppressWarnings("synthetic-access")
		@Override
		public void run() {
			while (!this.terminated) {
				try {
					final Object data = AnalysisNode.this.sendQueue.take();
					// TODO Replace SerializationUtils
					AnalysisNode.this.sendChannel.basicPublish(DATA_EXCHANGE_NAME, AnalysisNode.this.name, null, SerializationUtils.serialize(data));
				} catch (final InterruptedException ex) {
					// We expect this to happen as it is possible that another method wants to terminate this thread.
					LOG.info("SenderThread interrupted", ex);
				} catch (final IOException ex) {
					LOG.warn("Sending failed", ex);
				}
			}
		}

		public void terminate() {
			this.terminated = true;
			this.interrupt();
		}

	}

	class ReceiverThread extends Thread {

		private volatile boolean terminated;

		@SuppressWarnings("synthetic-access")
		@Override
		public void run() {
			try {
				final QueueingConsumer consumer = new QueueingConsumer(AnalysisNode.this.receiveChannel);
				AnalysisNode.this.receiveChannel.basicConsume(AnalysisNode.this.receiveQueueName, true, consumer);

				while (!this.terminated) {
					final QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					final Object data = SerializationUtils.deserialize(delivery.getBody());
					AnalysisNode.this.deliver(INTERNAL_OUTPUT_PORT_NAME_EVENTS, data);
				}
			} catch (final InterruptedException ex) {
				// We expect this to happen as it is possible that another method wants to terminate this thread.
				LOG.info("ReceiverThread interrupted", ex);
			} catch (final IOException ex) {
				LOG.warn("Receiving failed", ex);
			}
		}

		public void terminate() {
			this.terminated = true;
			this.interrupt();
		}

	}
}
