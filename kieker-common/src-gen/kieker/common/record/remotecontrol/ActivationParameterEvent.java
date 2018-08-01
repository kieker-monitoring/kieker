/***************************************************************************
* Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.remotecontrol.IRemoteParameterControlEvent;

/**
 * @author Marc Adolf
 * API compatibility: Kieker 1.14.0
 * 
 * @since 1.14
 */
public class ActivationParameterEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IRemoteParameterControlEvent {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // IRemoteControlEvent.pattern
			 + TYPE_SIZE_STRING // IRemoteParameterControlEvent.parameterNames
			 + TYPE_SIZE_STRING; // IRemoteParameterControlEvent.parameters
	
	public static final Class<?>[] TYPES = {
		String.class, // IRemoteControlEvent.pattern
		String[].class, // IRemoteParameterControlEvent.parameterNames
		String[][].class, // IRemoteParameterControlEvent.parameters
	};
	
	/** default constants. */
	public static final String PATTERN = "";
	private static final long serialVersionUID = -2801456957417446121L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"pattern",
		"parameterNames",
		"parameters",
	};
	
	/** property declarations. */
	private final String pattern;
	private final String[] parameterNames;
	private final String[][] parameters;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param pattern
	 *            pattern
	 * @param parameterNames
	 *            parameterNames
	 * @param parameters
	 *            parameters
	 */
	public ActivationParameterEvent(final String pattern, final String[] parameterNames, final String[][] parameters) {
		this.pattern = pattern == null?"":pattern;
		this.parameterNames = parameterNames;
		this.parameters = parameters;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	public ActivationParameterEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.pattern = (String) values[0];
		this.parameterNames = (String[]) values[1];
		this.parameters = (String[][]) values[2];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	protected ActivationParameterEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.pattern = (String) values[0];
		this.parameterNames = (String[]) values[1];
		this.parameters = (String[][]) values[2];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ActivationParameterEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.pattern = deserializer.getString();
		// load array sizes
		int _parameterNames_size0 = deserializer.getInt();
		this.parameterNames = new String[_parameterNames_size0];
		for (int i0=0;i0<_parameterNames_size0;i0++)
			this.parameterNames[i0] = deserializer.getString();
		
		// load array sizes
		int _parameters_size0 = deserializer.getInt();
		int _parameters_size1 = deserializer.getInt();
		this.parameters = new String[_parameters_size0][_parameters_size1];
		for (int i0=0;i0<_parameters_size0;i0++)
			for (int i1=0;i1<_parameters_size1;i1++)
				this.parameters[i0][i1] = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated to be removed in 1.15
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getPattern(),
			this.getParameterNames(),
			this.getParameters(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putString(this.getPattern());
		// store array sizes
		int _parameterNames_size0 = this.getParameterNames().length;
		serializer.putInt(_parameterNames_size0);
		for (int i0=0;i0<_parameterNames_size0;i0++)
			serializer.putString(this.getParameterNames()[i0]);
		
		// store array sizes
		int _parameters_size0 = this.getParameters().length;
		serializer.putInt(_parameters_size0);
		int _parameters_size1 = this.getParameters()[0].length;
		serializer.putInt(_parameters_size1);
		for (int i0=0;i0<_parameters_size0;i0++)
			for (int i1=0;i1<_parameters_size1;i1++)
				serializer.putString(this.getParameters()[i0][i1]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getValueNames() {
		return PROPERTY_NAMES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return SIZE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated to be rmeoved in 1.15
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
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		final ActivationParameterEvent castedRecord = (ActivationParameterEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getPattern().equals(castedRecord.getPattern())) {
			return false;
		}
		// get array length
		int _parameterNames_size0 = this.getParameterNames().length;
		if (_parameterNames_size0 != castedRecord.getParameterNames().length) {
			return false;
		}
		for (int i0=0;i0<_parameterNames_size0;i0++)
			if (!this.getParameterNames()[i0].equals(castedRecord.getParameterNames()[i0])) {
				return false;
			}
		
		// get array length
		int _parameters_size0 = this.getParameters().length;
		if (_parameters_size0 != castedRecord.getParameters().length) {
			return false;
		}
		int _parameters_size1 = this.getParameters()[0].length;
		if (_parameters_size1 != castedRecord.getParameters()[0].length) {
			return false;
		}
		for (int i0=0;i0<_parameters_size0;i0++)
			for (int i1=0;i1<_parameters_size1;i1++)
				if (!this.getParameters()[i0][i1].equals(castedRecord.getParameters()[i0][i1])) {
					return false;
				}
		
		return true;
	}
	
	public final String getPattern() {
		return this.pattern;
	}
	
	
	public final String[] getParameterNames() {
		return this.parameterNames;
	}
	
	
	public final String[][] getParameters() {
		return this.parameters;
	}
	
}
