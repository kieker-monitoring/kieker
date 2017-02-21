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

package kieker.test.common.util.record.factory;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * This record is used in tests where a record is needed that has no associated record factory.
 *
 * @author Christian Wulf
 *
 * @since 1.11
 */
public class TestRecord extends AbstractMonitoringRecord {

	private static final long serialVersionUID = 9088190056147961692L;
	
	public static final String[] PROPERTY_NAMES = {
			
	};
	
	public TestRecord() {
		// Nothing to do
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {
		// not used in test
	}

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		// not used in test
	}

	@Override
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		// not used in test
	}

	@Override
	public void initFromArray(final Object[] values) {
		// not used in test
	}

	@Override
	public Class<?>[] getValueTypes() {
		return new Class<?>[0];
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public String[] getValueNames() {
        return PROPERTY_NAMES; // NOPMD
    }

	@Override
	public int getSize() {
		return 0;
	}

}
