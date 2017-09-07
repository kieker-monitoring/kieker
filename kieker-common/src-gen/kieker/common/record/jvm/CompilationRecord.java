/***************************************************************************
 * Copyright 2017 iObserve Project
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
package kieker.common.record.jvm;

import java.nio.BufferOverflowException;

import kieker.common.record.jvm.AbstractJVMRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Nils Christian Ehmke
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.10
 */
public class CompilationRecord extends AbstractJVMRecord  {
	private static final long serialVersionUID = 3634137431488075031L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			 + TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			 + TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			 + TYPE_SIZE_STRING // CompilationRecord.jitCompilerName
			 + TYPE_SIZE_LONG // CompilationRecord.totalCompilationTimeMS
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // AbstractJVMRecord.timestamp
		String.class, // AbstractJVMRecord.hostname
		String.class, // AbstractJVMRecord.vmName
		String.class, // CompilationRecord.jitCompilerName
		long.class, // CompilationRecord.totalCompilationTimeMS
	};
	
	
	/** default constants. */
	public static final String JIT_COMPILER_NAME = "";
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"hostname",
		"vmName",
		"jitCompilerName",
		"totalCompilationTimeMS",
	};
	
	/** property declarations. */
	private final String jitCompilerName;
	private final long totalCompilationTimeMS;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param vmName
	 *            vmName
	 * @param jitCompilerName
	 *            jitCompilerName
	 * @param totalCompilationTimeMS
	 *            totalCompilationTimeMS
	 */
	public CompilationRecord(final long timestamp, final String hostname, final String vmName, final String jitCompilerName, final long totalCompilationTimeMS) {
		super(timestamp, hostname, vmName);
		this.jitCompilerName = jitCompilerName == null?"":jitCompilerName;
		this.totalCompilationTimeMS = totalCompilationTimeMS;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #CompilationRecord(IValueDeserializer)} instead.
	 */
	@Deprecated
	public CompilationRecord(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.jitCompilerName = (String) values[3];
		this.totalCompilationTimeMS = (Long) values[4];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #CompilationRecord(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected CompilationRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.jitCompilerName = (String) values[3];
		this.totalCompilationTimeMS = (Long) values[4];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public CompilationRecord(final IValueDeserializer deserializer) {
		super(deserializer);
		this.jitCompilerName = deserializer.getString();
		this.totalCompilationTimeMS = deserializer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 1.13. Use {@link #serialize(IValueSerializer)} with an array serializer instead.
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getHostname(),
			this.getVmName(),
			this.getJitCompilerName(),
			this.getTotalCompilationTimeMS()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getVmName());
		stringRegistry.get(this.getJitCompilerName());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getVmName());
		serializer.putString(this.getJitCompilerName());
		serializer.putLong(this.getTotalCompilationTimeMS());
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
		
		final CompilationRecord castedRecord = (CompilationRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (!this.getVmName().equals(castedRecord.getVmName())) return false;
		if (!this.getJitCompilerName().equals(castedRecord.getJitCompilerName())) return false;
		if (this.getTotalCompilationTimeMS() != castedRecord.getTotalCompilationTimeMS()) return false;
		return true;
	}
	
	public final String getJitCompilerName() {
		return this.jitCompilerName;
	}
	
	
	public final long getTotalCompilationTimeMS() {
		return this.totalCompilationTimeMS;
	}
	
}
