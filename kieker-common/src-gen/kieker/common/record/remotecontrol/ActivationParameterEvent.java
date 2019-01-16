/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.remotecontrol;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
//import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.remotecontrol.IRemoteParameterControlEvent;

/**
 * @author Marc Adolf
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.14
 */
public class ActivationParameterEvent extends AbstractMonitoringRecord implements IRemoteParameterControlEvent {			
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
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ActivationParameterEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.pattern = deserializer.getString();
		// load array sizes
		final int _parameterNames_size0 = deserializer.getInt();
		this.parameterNames = new String[_parameterNames_size0];
		for (int i0=0;i0<_parameterNames_size0;i0++)
			this.parameterNames[i0] = deserializer.getString();
		
		// load array sizes
		final int _parameters_size0 = deserializer.getInt();
		final int _parameters_size1 = deserializer.getInt();
		this.parameters = new String[_parameters_size0][_parameters_size1];
		for (int i0=0;i0<_parameters_size0;i0++)
			for (int i1=0;i1<_parameters_size1;i1++)
				this.parameters[i0][i1] = deserializer.getString();
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
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int code = 0;
		code += this.getPattern().hashCode();
		// get array length
		for (int i0=0;i0 < this.parameterNames.length;i0++) {
			for (int i1=0;i1 < this.parameterNames.length;i1++) {
				code += this.getParameterNames()[i0].hashCode();
			}
		}
		
		// get array length
		for (int i0=0;i0 < this.parameters.length;i0++) {
			for (int i1=0;i1 < this.parameters.length;i1++) {
				for (int i2=0;i2 < this.parameters.length;i2++) {
					code += this.getParameters()[i0][i1].hashCode();
				}
			}
		}
		
		return code;
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
