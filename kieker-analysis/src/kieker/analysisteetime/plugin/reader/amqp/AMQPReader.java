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

package kieker.analysisteetime.plugin.reader.amqp;

import kieker.analysisteetime.plugin.reader.AbstractReader;
import kieker.common.logging.LogFactory;

/**
 * Reader stage that reads monitoring records from an AMQP queue.
 *
 * @author Holger Knoche, Lars Bluemke
 *
 * @since 1.12
 */
public class AMQPReader extends AbstractReader {

	/**
	 * Creates a new AMQP reader.
	 *
	 * @param uri
	 *            The name of the configuration property for the server URI.
	 * @param queueName
	 *            The name of the configuration property for the AMQP queue name.
	 * @param heartbeat
	 *            The name of the configuration property for the heartbeat timeout.
	 */
	public AMQPReader(final String uri, final String queueName, final int heartbeat) {
		this.readerLogic = new AMQPReaderLogic(uri, queueName, heartbeat, LogFactory.getLog(AMQPReader.class), this);
	}

}
