package kieker.common.util.registry.reader;

import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.IRegistryRecordReceiver;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class GetValueAdapter<E> implements IRegistry<E> {

	private final ReaderRegistry<E> readerRegistry;

	public GetValueAdapter(final ReaderRegistry<E> readerRegistry) {
		this.readerRegistry = readerRegistry;
	}

	@Override
	public int get(final E value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E get(final int key) {
		return this.readerRegistry.get(key);
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
