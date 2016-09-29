package kieker.common.util.registry.reader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
// TODO move to kieker.common.registry.read
public class ReaderRegistry<E> {

	// TODO replace by a high performance map with primitive key type
	private final Map<Long, E> registryEntries = new HashMap<Long, E>();

	public E get(final long key) {
		return this.registryEntries.get(key);
	}

	public void register(final long key, final E value) {
		this.registryEntries.put(key, value);
	}
}
