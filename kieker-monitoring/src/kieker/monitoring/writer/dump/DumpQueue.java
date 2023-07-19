package kieker.monitoring.writer.dump;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.writer.MonitoringWriterThread;

public class DumpQueue implements BlockingQueue<IMonitoringRecord> {

	private boolean active = false;
	private final IMonitoringRecord dummyObject = new OperationExecutionRecord("test", "test", 0l, 0l, 0l, "test", 0, 0);
	
	public DumpQueue() {
		
	}
	
	public DumpQueue(int initialSize) {
		
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
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends IMonitoringRecord> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
		
	}

	@Override
	public boolean add(IMonitoringRecord e) {
		return false;
	}

	@Override
	public boolean offer(IMonitoringRecord e) {
		return false;
	}

	@Override
	public void put(IMonitoringRecord e) throws InterruptedException {
		if (e == MonitoringWriterThread.END_OF_MONITORING_RECORD) {
			active = false;
		}
	}

	@Override
	public boolean offer(IMonitoringRecord e, long timeout, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public IMonitoringRecord take() throws InterruptedException {
		return active ? dummyObject : MonitoringWriterThread.END_OF_MONITORING_RECORD;
	}

	@Override
	public IMonitoringRecord poll(long timeout, TimeUnit unit) throws InterruptedException {
		return null;
	}

	@Override
	public int remainingCapacity() {
		return 0;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public int drainTo(Collection<? super IMonitoringRecord> c) {
		return 0;
	}

	@Override
	public int drainTo(Collection<? super IMonitoringRecord> c, int maxElements) {
		return 0;
	}
}
