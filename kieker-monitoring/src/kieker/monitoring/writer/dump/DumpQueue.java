/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.dump;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.writer.MonitoringWriterThread;

public class DumpQueue implements BlockingQueue<IMonitoringRecord> { // NOPMD

	private boolean active;
	private final IMonitoringRecord dummyObject = new OperationExecutionRecord("test", "test", 0L, 0L, 0L, "test", 0, 0);

	public DumpQueue() {
		// empty constructor for API purposes
	}

	public DumpQueue(final int initialSize) { // NOPMD used for API purposes
		// empty constructor for API purposes
	}

	@Override
	public IMonitoringRecord remove() {
		return null;
	}

	@Override
	public IMonitoringRecord poll() {
		return null;
	}

	@Override
	public IMonitoringRecord element() {
		return null;
	}

	@Override
	public IMonitoringRecord peek() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Iterator<IMonitoringRecord> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return a;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(final Collection<? extends IMonitoringRecord> c) {
		return false;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
		// Empty on purpose
	}

	@Override
	public boolean add(final IMonitoringRecord e) {
		return false;
	}

	@Override
	public boolean offer(final IMonitoringRecord e) {
		return false;
	}

	@Override
	public void put(final IMonitoringRecord e) throws InterruptedException {
		if (e == MonitoringWriterThread.END_OF_MONITORING_RECORD) {
			this.active = false;
		}
	}

	@Override
	public boolean offer(final IMonitoringRecord e, final long timeout, final TimeUnit unit)
			throws InterruptedException {
		return false;
	}

	@Override
	public IMonitoringRecord take() throws InterruptedException {
		return this.active ? this.dummyObject : MonitoringWriterThread.END_OF_MONITORING_RECORD; // NOCS
	}

	@Override
	public IMonitoringRecord poll(final long timeout, final TimeUnit unit) throws InterruptedException {
		return null;
	}

	@Override
	public int remainingCapacity() {
		return 0;
	}

	@Override
	public boolean remove(final Object o) {
		return false;
	}

	@Override
	public boolean contains(final Object o) {
		return false;
	}

	@Override
	public int drainTo(final Collection<? super IMonitoringRecord> c) {
		return 0;
	}

	@Override
	public int drainTo(final Collection<? super IMonitoringRecord> c, final int maxElements) {
		return 0;
	}
}
