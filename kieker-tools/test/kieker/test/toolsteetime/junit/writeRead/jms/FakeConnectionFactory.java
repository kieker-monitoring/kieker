/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.toolsteetime.junit.writeRead.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * This class is part of a very basic fake JMS message broker. It uses a very simple design to deliver messages synchronously from a singleton producer to a
 * singleton consumer. It has only been designed for test purposes ({@link BasicJMSWriterReaderTest}) and should <b>not</b> be used outside this test.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class FakeConnectionFactory implements ConnectionFactory {

	/**
	 * Default constructor.
	 */
	public FakeConnectionFactory() {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection createConnection() throws JMSException {
		return new FakeConnection();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection createConnection(final String arg0, final String arg1) throws JMSException {
		return null;
	}

}
