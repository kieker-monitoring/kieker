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

package kieker.test.toolsteetime.junit.writeRead.jms; // NOPMD (Too many public methods)

import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class FakeTextMessage implements TextMessage {

	private String text;

	/**
	 * Empty constructor.
	 */
	public FakeTextMessage() {
		// No code necessary
	}

	@Override
	public void acknowledge() throws JMSException {
		// No code necessary
	}

	@Override
	public void clearBody() throws JMSException {
		// No code necessary
	}

	@Override
	public void clearProperties() throws JMSException {
		// No code necessary
	}

	@Override
	public boolean getBooleanProperty(final String arg0) throws JMSException {
		return false;
	}

	@Override
	public byte getByteProperty(final String arg0) throws JMSException {
		return 0;
	}

	@Override
	public double getDoubleProperty(final String arg0) throws JMSException {
		return 0;
	}

	@Override
	public float getFloatProperty(final String arg0) throws JMSException {
		return 0;
	}

	@Override
	public int getIntProperty(final String arg0) throws JMSException {
		return 0;
	}

	@Override
	public String getJMSCorrelationID() throws JMSException {
		return null;
	}

	@Override
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		return new byte[0];
	}

	@Override
	public int getJMSDeliveryMode() throws JMSException {
		return 0;
	}

	@Override
	public Destination getJMSDestination() throws JMSException {
		return null;
	}

	@Override
	public long getJMSExpiration() throws JMSException {
		return 0;
	}

	@Override
	public String getJMSMessageID() throws JMSException {
		return null;
	}

	@Override
	public int getJMSPriority() throws JMSException {
		return 0;
	}

	@Override
	public boolean getJMSRedelivered() throws JMSException { // NOPMD (get -> is)
		return false;
	}

	@Override
	public Destination getJMSReplyTo() throws JMSException {
		return null;
	}

	@Override
	public long getJMSTimestamp() throws JMSException {
		return 0;
	}

	@Override
	public String getJMSType() throws JMSException {
		return null;
	}

	@Override
	public long getLongProperty(final String arg0) throws JMSException {
		return 0;
	}

	@Override
	public Object getObjectProperty(final String arg0) throws JMSException {
		return null;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Enumeration getPropertyNames() throws JMSException {
		return null;
	}

	@Override
	public short getShortProperty(final String arg0) throws JMSException { // NOPMD (no short)
		return 0;
	}

	@Override
	public String getStringProperty(final String arg0) throws JMSException {
		return null;
	}

	@Override
	public boolean propertyExists(final String arg0) throws JMSException {
		return false;
	}

	@Override
	public void setBooleanProperty(final String arg0, final boolean arg1) throws JMSException {
		// No code necessary
	}

	@Override
	public void setByteProperty(final String arg0, final byte arg1) throws JMSException {
		// No code necessary
	}

	@Override
	public void setDoubleProperty(final String arg0, final double arg1) throws JMSException {
		// No code necessary
	}

	@Override
	public void setFloatProperty(final String arg0, final float arg1) throws JMSException {
		// No code necessary
	}

	@Override
	public void setIntProperty(final String arg0, final int arg1) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSCorrelationID(final String arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSCorrelationIDAsBytes(final byte[] arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSDeliveryMode(final int arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSDestination(final Destination arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSExpiration(final long arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSMessageID(final String arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSPriority(final int arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSRedelivered(final boolean arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSReplyTo(final Destination arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSTimestamp(final long arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setJMSType(final String arg0) throws JMSException {
		// No code necessary
	}

	@Override
	public void setLongProperty(final String arg0, final long arg1) throws JMSException {
		// No code necessary
	}

	@Override
	public void setObjectProperty(final String arg0, final Object arg1) throws JMSException {
		// No code necessary
	}

	@Override
	public void setShortProperty(final String arg0, final short arg1) throws JMSException { // NOPMD (no short)
		// No code necessary
	}

	@Override
	public void setStringProperty(final String arg0, final String arg1) throws JMSException {
		// No code necessary
	}

	@Override
	public String getText() throws JMSException {
		return this.text;
	}

	@Override
	public void setText(final String text) throws JMSException {
		this.text = text;
	}

}
