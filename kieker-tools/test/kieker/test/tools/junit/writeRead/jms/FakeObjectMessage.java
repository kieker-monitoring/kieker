/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.jms; // NOPMD (Too many public methods)

import java.io.Serializable;
import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * This class is part of a very basic fake JMS message broker. It uses a very simple design to deliver messages synchronously from a singleton producer to a
 * singleton consumer. It has only been designed for test purposes ({@link BasicJMSWriterReaderTest}) and should <b>not</b> be used outside this test.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class FakeObjectMessage implements ObjectMessage {

	private Serializable object;

	/**
	 * Default constructor.
	 */
	public FakeObjectMessage() {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acknowledge() throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearBody() throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearProperties() throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getBooleanProperty(final String arg0) throws JMSException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getByteProperty(final String arg0) throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDoubleProperty(final String arg0) throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getFloatProperty(final String arg0) throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIntProperty(final String arg0) throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJMSCorrelationID() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		return new byte[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getJMSDeliveryMode() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Destination getJMSDestination() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getJMSExpiration() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJMSMessageID() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getJMSPriority() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getJMSRedelivered() throws JMSException { // NOPMD (get -> is)
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Destination getJMSReplyTo() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getJMSTimestamp() throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJMSType() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLongProperty(final String arg0) throws JMSException {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getObjectProperty(final String arg0) throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Enumeration getPropertyNames() throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short getShortProperty(final String arg0) throws JMSException { // NOPMD (no short)
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStringProperty(final String arg0) throws JMSException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean propertyExists(final String arg0) throws JMSException {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBooleanProperty(final String arg0, final boolean arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setByteProperty(final String arg0, final byte arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDoubleProperty(final String arg0, final double arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFloatProperty(final String arg0, final float arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIntProperty(final String arg0, final int arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSCorrelationID(final String arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSCorrelationIDAsBytes(final byte[] arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSDeliveryMode(final int arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSDestination(final Destination arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSExpiration(final long arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSMessageID(final String arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSPriority(final int arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSRedelivered(final boolean arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSReplyTo(final Destination arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSTimestamp(final long arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJMSType(final String arg0) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLongProperty(final String arg0, final long arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setObjectProperty(final String arg0, final Object arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShortProperty(final String arg0, final short arg1) throws JMSException { // NOPMD (no short)
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStringProperty(final String arg0, final String arg1) throws JMSException {
		// No code necessary
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Serializable getObject() throws JMSException {
		return this.object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setObject(final Serializable object) throws JMSException {
		this.object = object;
	}

}
