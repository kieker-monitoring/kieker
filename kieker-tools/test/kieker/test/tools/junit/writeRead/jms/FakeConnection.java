/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.jms;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * This class is part of a very basic fake JMS message broker. It uses a very simple design to deliver messages synchronously from a singleton producer to a
 * singleton consumer. It has only been designed for test purposes ({@link BasicJMSWriterReaderTest}) and should <b>not</b> be used outside this test.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class FakeConnection implements Connection {

	/**
	 * Default constructor.
	 */
	public FakeConnection() {
		// No code necessary
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
	public ConnectionConsumer createConnectionConsumer(final Destination arg0, final String arg1, final ServerSessionPool arg2, final int arg3) throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnectionConsumer createDurableConnectionConsumer(final Topic arg0, final String arg1, final String arg2, final ServerSessionPool arg3, final int arg4)
			throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Session createSession(final boolean arg0, final int arg1) throws JMSException {
		return new FakeSession();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClientID() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClientID(final String arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExceptionListener(final ExceptionListener arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() throws JMSException {
		// No code necessary
	}

}
