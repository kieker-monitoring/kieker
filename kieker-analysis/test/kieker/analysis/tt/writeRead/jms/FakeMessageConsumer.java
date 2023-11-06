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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * This class is part of a very basic fake JMS message broker. It uses a very simple design to deliver messages synchronously from a singleton producer to a
 * singleton consumer. It has only been designed for test purposes ( {@link BasicJMSWriterReaderTest}) and should <b>not</b> be used outside this test.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.8
 */
public class FakeMessageConsumer implements MessageConsumer {

	private MessageListener messageListener;
	private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

	/**
	 * Default constructor.
	 */
	public FakeMessageConsumer() {
		// Just necessary for Findbugs
		this.messageListener = null; // NOPMD (null)
	}

	/**
	 * Add a message from the producer to the consumer queue.
	 *
	 * @param message
	 *            the message to add to the internal queue
	 */
	public void addMessage(final Message message) {
		this.messages.add(message);
		// Some tests already use this method without a connected consumer or
		// do not use the listener API.
		if (this.messageListener != null) {
			while (!this.messages.isEmpty()) {
				this.messageListener.onMessage(this.messages.poll());
			}
		}
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
	public MessageListener getMessageListener() throws JMSException {
		return this.messageListener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessageSelector() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message receive() throws JMSException {
		try {
			return this.messages.take();
		} catch (final InterruptedException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message receive(final long arg0) throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message receiveNoWait() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMessageListener(final MessageListener listener) throws JMSException {
		this.messageListener = listener;
	}

}
