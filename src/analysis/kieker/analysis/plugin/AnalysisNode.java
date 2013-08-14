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

import java.lang.reflect.Constructor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.util.SerializationUtils;

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryInputPort;
import kieker.analysis.plugin.annotation.RepositoryOutputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.metasignal.MetaSignal;
import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.IRepository;
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
		repositoryOutputPorts = {
			@RepositoryOutputPort(name = AnalysisNode.REPOSITORY_OUTPUT_PORT_NAME_EVENTS),
			@RepositoryOutputPort(name = AnalysisNode.INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS, internalUseOnly = true),
		},
		configuration = {
			@Property(name = AnalysisNode.CONFIG_PROPERTY_NAME_MOM_SERVER, defaultValue = "tcp://localhost:61616"),
			@Property(name = AnalysisNode.CONFIG_PROPERTY_NAME_DISTRIBUTED, defaultValue = "false"),
			@Property(name = AnalysisNode.CONFIG_PROPERTY_NAME_NODE_NAME, defaultValue = "kieker-node"),
		})
public class AnalysisNode extends AbstractFilterPlugin {

	public static final String CONFIG_PROPERTY_NAME_MOM_SERVER = "server";
	public static final String CONFIG_PROPERTY_NAME_DISTRIBUTED = "distributed";
	public static final String CONFIG_PROPERTY_NAME_NODE_NAME = "nodeName";

	public static final String REPOSITORY_INPUT_PORT_NAME_EVENTS = "receivedRepositoryEvents";
	public static final String REPOSITORY_OUTPUT_PORT_NAME_EVENTS = "sentRepositoryEvents";
	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";
	public static final String OUTPUT_PORT_NAME_EVENTS = "sentEvents";

	protected static final String INTERNAL_REPOSITORY_INPUT_PORT_NAME_EVENTS = "internalRepositoryInputPort";
	protected static final String INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS = "internalRepositoryOutputPort";
	protected static final String INTERNAL_INPUT_PORT_NAME_EVENTS = "internalInputPort";
	protected static final String INTERNAL_OUTPUT_PORT_NAME_EVENTS = "internalOutputPort";

	protected static final String REPOSITORY_DATA_TOPIC_TEMPLATE = "net.kieker-monitoring.analysis.data.repository.%s";
	protected static final String DATA_TOPIC_TEMPLATE = "net.kieker-monitoring.analysis.data.%s";

	private static final Log LOG = LogFactory.getLog(AnalysisNode.class);

	private final AtomicInteger activeConnectionThreads = new AtomicInteger(4);

	private final RepositoryReceiverThread repositoryReceiverThread;
	private final RepositorySenderThread repositorySenderThread;
	private final ReceiverThread receiverThread;
	private final SenderThread senderThread;
	protected final BlockingQueue<Object> sendQueue;
	private final BlockingQueue<Object> repositorySendQueue;
	private final String name;
	private final String host;
	private final boolean distributed;
	private final Connection distributedConnection;
	private final Session distributedSession;
	private final BlockingQueue<Object> receiverQueue;
	private final BlockingQueue<Object> repositoryReceiverQueue;

	public AnalysisNode(final Configuration configuration, final IProjectContext projectContext) throws Exception {
		super(configuration, projectContext);

		// Get the properties
		this.name = configuration.getStringProperty(CONFIG_PROPERTY_NAME_NODE_NAME);
		this.distributed = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_DISTRIBUTED);
		this.host = configuration.getStringProperty(CONFIG_PROPERTY_NAME_MOM_SERVER);

		// Configure the plugin depending on whether this is a distributed or a local node.
		if (this.distributed) {
			final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(this.host);
			this.distributedConnection = factory.createConnection();
			this.distributedSession = this.distributedConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			this.sendQueue = new LinkedBlockingQueue<Object>();
			this.repositorySendQueue = new LinkedBlockingQueue<Object>();
			this.receiverQueue = new LinkedBlockingQueue<Object>();
			this.repositoryReceiverQueue = new LinkedBlockingQueue<Object>();

			this.senderThread = new SenderThread();
			this.receiverThread = new ReceiverThread();

			this.repositorySenderThread = new RepositorySenderThread();
			this.repositoryReceiverThread = new RepositoryReceiverThread();
		} else {
			this.distributedConnection = null;
			this.distributedSession = null;

			this.sendQueue = null;
			this.receiverThread = null;

			this.receiverQueue = null;
			this.repositoryReceiverQueue = null;

			this.senderThread = null;
			this.repositorySendQueue = null;

			this.repositorySenderThread = null;
			this.repositoryReceiverThread = null;
		}
	}

	@Override
	public boolean init() {
		// Start all threads if necessary
		if (this.distributed) {
			try {
				this.distributedConnection.start();
			} catch (final JMSException e) {
				return false;
			}

			this.senderThread.start();
			this.receiverThread.start();
			this.repositorySenderThread.start();
			this.repositoryReceiverThread.start();
		}

		return true;
	}

	protected void shutdownDistributedPorts() throws InterruptedException {
		// Terminate all threads if necessary
		if (this.distributed) {
			this.senderThread.terminate();
			this.receiverThread.terminate();
			this.repositorySenderThread.terminate();
			this.repositoryReceiverThread.terminate();

			// Avoid the joins as this can block the node
			// this.senderThread.join();
			// this.receiverThread.join();
			// this.repositorySenderThread.join();
			// this.repositoryReceiverThread.join();
		}
	}

	public final void connect(final String predecessorNode) throws Exception, AnalysisConfigurationException {
		if (this.distributed) {
			final Topic dataTopic = this.distributedSession.createTopic(String.format(DATA_TOPIC_TEMPLATE, predecessorNode));
			final Topic repositoryDataTopic = this.distributedSession.createTopic(String.format(REPOSITORY_DATA_TOPIC_TEMPLATE, predecessorNode));

			final MessageConsumer dataConsumer = this.distributedSession.createConsumer(dataTopic);
			final MessageConsumer repositoryDataConsumer = this.distributedSession.createConsumer(repositoryDataTopic);

			dataConsumer.setMessageListener(new MessageListener() {

				public void onMessage(final Message msg) {
					AnalysisNode.this.receiverQueue.add(msg);
				}
			});
			repositoryDataConsumer.setMessageListener(new MessageListener() {

				public void onMessage(final Message msg) {
					AnalysisNode.this.repositoryReceiverQueue.add(msg);
				}
			});
		} else {
			throw new AnalysisConfigurationException("Node is not configured for distributed usage.");
		}
	}

	public boolean isDistributed() {
		return this.distributed;
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

	@RepositoryInputPort(name = REPOSITORY_INPUT_PORT_NAME_EVENTS)
	public final void repositoryInputPort(final Object data) {
		super.deliverWithoutReturnTypeToRepository(INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS, data);
	}

	@RepositoryInputPort(name = INTERNAL_REPOSITORY_INPUT_PORT_NAME_EVENTS, internalUseOnly = true)
	public final void internalRepositoryInputPort(final Object data) {
		// If this node is configured for distributed access, we have to send the message to the MOM as well.
		if (this.distributed) {
			this.repositorySendQueue.add(data);
		}
		super.deliver(OUTPUT_PORT_NAME_EVENTS, data);
	}

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

	public final void connectWithRepositoryOutput(final AbstractPlugin component, final String outputPortName) throws IllegalStateException,
			AnalysisConfigurationException {
		((AnalysisController) this.projectContext).connect(component, outputPortName, (IRepository) this, INTERNAL_REPOSITORY_INPUT_PORT_NAME_EVENTS);
	}

	public final void connectWithRepositoryInput(final AbstractRepository component, final String inputPortName) throws IllegalStateException,
			AnalysisConfigurationException {
		((AnalysisController) this.projectContext).connect(this, INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS, component, inputPortName);
	}

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

	private void shutdownDistributedConnection() {
		// Check whether this thread is the last one
		if (AnalysisNode.this.activeConnectionThreads.decrementAndGet() == 0) {
			try {
				AnalysisNode.this.distributedConnection.stop();
				AnalysisNode.this.distributedConnection.close();
			} catch (final JMSException ex) {
				ex.printStackTrace();
			}
		}
	}

	class RepositorySenderThread extends Thread {

		private final Object TERMINATION_TOKEN = new Object();

		private final Topic topic;
		private final MessageProducer publisher;

		public RepositorySenderThread() throws JMSException {
			this.topic = AnalysisNode.this.distributedSession.createTopic(String.format(REPOSITORY_DATA_TOPIC_TEMPLATE, AnalysisNode.this.name));
			this.publisher = AnalysisNode.this.distributedSession.createProducer(this.topic);
			this.publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public void run() {
			try {
				while (true) {
					final Object data = AnalysisNode.this.repositorySendQueue.take();

					if (data == this.TERMINATION_TOKEN) {
						AnalysisNode.this.shutdownDistributedConnection();
						return;
					} else {
						try {
							final BytesMessage bytesMessage = AnalysisNode.this.distributedSession.createBytesMessage();
							bytesMessage.writeBytes(SerializationUtils.serialize(data));
							this.publisher.send(bytesMessage);
						} catch (final JMSException ex) {
							LOG.warn("Sending failed", ex);
						}
					}
				}
			} catch (final InterruptedException ex) {
				// We expect this to happen as it is possible that another method wants to terminate this thread.
				LOG.info("RepositorySenderThread interrupted", ex);
			}
		}

		public void terminate() {
			AnalysisNode.this.repositorySendQueue.add(this.TERMINATION_TOKEN);
		}

	}

	class SenderThread extends Thread {

		private final Object TERMINATION_TOKEN = new Object();

		private final Topic topic;
		private final MessageProducer publisher;

		public SenderThread() throws JMSException {
			this.topic = AnalysisNode.this.distributedSession.createTopic(String.format(DATA_TOPIC_TEMPLATE, AnalysisNode.this.name));
			this.publisher = AnalysisNode.this.distributedSession.createProducer(this.topic);
			this.publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}

		@SuppressWarnings("synthetic-access")
		@Override
		public void run() {
			try {
				while (true) {
					final Object data = AnalysisNode.this.sendQueue.take();

					if (data == this.TERMINATION_TOKEN) {
						AnalysisNode.this.shutdownDistributedConnection();
						return;
					} else {
						try {
							final BytesMessage bytesMessage = AnalysisNode.this.distributedSession.createBytesMessage();
							bytesMessage.writeBytes(SerializationUtils.serialize(data));
							this.publisher.send(bytesMessage);
						} catch (final JMSException ex) {
							LOG.warn("Sending failed", ex);
						}
					}
				}
			} catch (final InterruptedException ex) {
				// We expect this to happen as it is possible that another method wants to terminate this thread.
				LOG.info("SenderThread interrupted", ex);
			}
		}

		public void terminate() {
			AnalysisNode.this.sendQueue.add(this.TERMINATION_TOKEN);
		}

	}

	class RepositoryReceiverThread extends Thread {

		private final Object TERMINATION_TOKEN = new Object();

		@SuppressWarnings("synthetic-access")
		@Override
		public void run() {
			try {
				while (true) {
					final Object data = AnalysisNode.this.repositoryReceiverQueue.take();
					if (data == this.TERMINATION_TOKEN) {
						AnalysisNode.this.shutdownDistributedConnection();
						return;
					} else {
						try {
							final BytesMessage byteMessage = (BytesMessage) data;
							final byte[] buff = new byte[(int) byteMessage.getBodyLength()];
							byteMessage.readBytes(buff);

							final Object content = SerializationUtils.deserialize(buff);
							AnalysisNode.this.deliverWithoutReturnTypeToRepository(INTERNAL_REPOSITORY_OUTPUT_PORT_NAME_EVENTS, content);
						} catch (final JMSException ex) {
							LOG.warn("Receiving failed", ex);
						}
					}
				}
			} catch (final InterruptedException ex) {
				// We expect this to happen as it is possible that another method wants to terminate this thread.
				LOG.info("RepositoryReceiverThread interrupted", ex);
			}
		}

		public void terminate() {
			AnalysisNode.this.repositoryReceiverQueue.add(this.TERMINATION_TOKEN);
		}

	}

	class ReceiverThread extends Thread {

		private final Object TERMINATION_TOKEN = new Object();

		@SuppressWarnings("synthetic-access")
		@Override
		public void run() {
			try {
				while (true) {
					final Object data = AnalysisNode.this.receiverQueue.take();

					if (data == this.TERMINATION_TOKEN) {
						AnalysisNode.this.shutdownDistributedConnection();
						return;
					} else {
						try {
							final BytesMessage byteMessage = (BytesMessage) data;
							final byte[] buff = new byte[(int) byteMessage.getBodyLength()];
							byteMessage.readBytes(buff);

							final Object content = SerializationUtils.deserialize(buff);
							if (content instanceof MetaSignal) {
								AnalysisNode.this.processAndDelayMetaSignal((MetaSignal) content);
							} else {
								AnalysisNode.this.deliver(INTERNAL_OUTPUT_PORT_NAME_EVENTS, content);
							}
						} catch (final JMSException ex) {
							LOG.warn("Receiving failed", ex);
						}
					}
				}
			} catch (final InterruptedException ex) {
				// We expect this to happen as it is possible that another method wants to terminate this thread.
				LOG.info("ReceiverThread interrupted", ex);
			}
		}

		public void terminate() {
			AnalysisNode.this.receiverQueue.add(this.TERMINATION_TOKEN);
		}

	}

}
