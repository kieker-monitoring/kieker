/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.tt.writeRead.jms;

import javax.jms.CompletionListener;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

/**
 * This class is part of a very basic fake JMS message broker. It uses a very
 * simple design to deliver messages synchronously from a singleton producer to
 * a singleton consumer. It has only been designed for test purposes
 * ({@link BasicJMSWriterReaderTest}) and should <b>not</b> be used outside this
 * test.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.8
 */
public class FakeMessageProducer implements MessageProducer {

	private final FakeMessageConsumer consumer;

	/**
	 * Default constructor.
	 *
	 * @param consumer
	 *            The consumer.
	 */
	public FakeMessageProducer(final FakeMessageConsumer consumer) {
		this.consumer = consumer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDeliveryMode() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Destination getDestination() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getDisableMessageID() throws JMSException { // NOPMD (get -> is)
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getDisableMessageTimestamp() throws JMSException { // NOPMD (get -> is)
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPriority() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getTimeToLive() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void send(final Message message) throws JMSException {
		this.consumer.addMessage(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void send(final Destination arg0, final Message arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void send(final Message arg0, final int arg1, final int arg2, final long arg3) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void send(final Destination arg0, final Message arg1, final int arg2, final int arg3, final long arg4)
			throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeliveryMode(final int arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDisableMessageID(final boolean arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDisableMessageTimestamp(final boolean arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPriority(final int arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTimeToLive(final long arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setDeliveryDelay(final long deliveryDelay) throws JMSException {

	}

	@Override
	public long getDeliveryDelay() throws JMSException {
		return 0;
	}

	@Override
	public void send(final Message message, final CompletionListener completionListener) throws JMSException {

	}

	@Override
	public void send(final Message message, final int deliveryMode, final int priority, final long timeToLive,
			final CompletionListener completionListener) throws JMSException {

	}

	@Override
	public void send(final Destination destination, final Message message, final CompletionListener completionListener)
			throws JMSException {

	}

	@Override
	public void send(final Destination destination, final Message message, final int deliveryMode, final int priority,
			final long timeToLive, final CompletionListener completionListener) throws JMSException {

	}

}
