package kieker.monitoring.registry;

import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.IRegistryRecordReceiver;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class GetIdAdapter<E> implements IRegistry<E> {

	private final IWriterRegistry<E> writerRegistry;

	public GetIdAdapter(final IWriterRegistry<E> writerRegistry) {
		this.writerRegistry = writerRegistry;
	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get(final E value) {
		return this.writerRegistry.getId(value);
	}

	@Override
	public E get(final int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E[] getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRecordReceiver(final IRegistryRecordReceiver registryRecordReceiver) {
		throw new UnsupportedOperationException();
	}

}
