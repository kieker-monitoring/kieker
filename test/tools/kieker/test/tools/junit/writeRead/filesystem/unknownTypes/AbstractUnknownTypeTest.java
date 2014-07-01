/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.filesystem.unknownTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.BranchingRecord;
import kieker.common.record.misc.EmptyRecord;

import kieker.test.tools.junit.writeRead.filesystem.AbstractTestFSWriterReader;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.5
 */
public abstract class AbstractUnknownTypeTest extends AbstractTestFSWriterReader { // NOPMD (TestClassWithoutTestCases) // NOCS (MissingCtorCheck)
	protected static final EmptyRecord EVENT0_KNOWN_TYPE = new EmptyRecord();
	protected static final BranchingRecord EVENT1_UNKNOWN_TYPE = new BranchingRecord(-1, -1, -1);
	protected static final EmptyRecord EVENT2_KNOWN_TYPE = new EmptyRecord();
	protected static final BranchingRecord EVENT3_UNKNOWN_TYPE = new BranchingRecord(-1, -1, -1);

	private static final Class<? extends IMonitoringRecord> EVENT_CLASS_TO_MANIPULATE = EVENT1_UNKNOWN_TYPE.getClass();

	@Override
	protected List<IMonitoringRecord> provideEvents() {
		final List<IMonitoringRecord> events = new ArrayList<IMonitoringRecord>();
		events.add(EVENT0_KNOWN_TYPE);
		events.add(EVENT1_UNKNOWN_TYPE);
		events.add(EVENT2_KNOWN_TYPE);
		events.add(EVENT3_UNKNOWN_TYPE);
		return events;
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return Boolean.TRUE; // required for the CSV writers; simply do it always
	}

	@Override
	protected void doSomethingBeforeReading(final String[] monitoringLogs) throws IOException {
		final String classnameToManipulate = EVENT_CLASS_TO_MANIPULATE.getName();
		this.replaceStringInMapFiles(monitoringLogs, classnameToManipulate, classnameToManipulate + "XYZ");
	}
}
