/***************************************************************************
* Copyright 2018 Kieker Project (http://www.iobserve-devops.net)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
***************************************************************************/
package kieker.common.record.remotecontrol;


import kieker.common.record.remotecontrol.RemoteControlEvent;
import kieker.common.record.io.IValueDeserializer;


/**
 * @author Marc Adolf
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.14
 */
public abstract class DeactivationEvent extends RemoteControlEvent  {
	private static final long serialVersionUID = -5333262085397327129L;

	
	
	
		
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param pattern
	 *            pattern
	 */
	public DeactivationEvent(final String pattern) {
		super(pattern);
	}


	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #DeactivationEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected DeactivationEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public DeactivationEvent(final IValueDeserializer deserializer) {
		super(deserializer);
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final DeactivationEvent castedRecord = (DeactivationEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (!this.getPattern().equals(castedRecord.getPattern())) return false;
		return true;
	}
	
}
