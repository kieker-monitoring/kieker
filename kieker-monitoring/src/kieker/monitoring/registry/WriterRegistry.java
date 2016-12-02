package kieker.monitoring.registry;

import java.util.HashMap;
import java.util.Map;

public class WriterRegistry implements IWriterRegistry<String> {

	// TODO introduce faster, non-boxing ObjectIntMap
	private final Map<String, Integer> storage = new HashMap<String, Integer>();
	private int nextId = 0;

	private final IRegistryListener<String> registryListener;
	/**
	 * ID of this registry.
	 */
	private final long id;

	public WriterRegistry(final IRegistryListener<String> registryListener) {
		this.registryListener = registryListener;
		this.id = WriterRegistryUtil.generateId();
	}

	@Override
	public int getId(final String value) {
		final Integer id = this.storage.get(value);
		if (id == null) {
			throw new IllegalArgumentException(
					"The given value '" + value + "' is not registered. Thus, there is no identifier for this value.");
		}
		return id;
	}

	@Override
	public void register(final String value) {
		if (!this.storage.containsKey(value)) {
			final int id = this.nextId++;
			this.storage.put(value, id);
			this.registryListener.onNewRegistryEntry(value, id);
		}
	}

	@Override
	public long getId() {
		return this.id;
	}

}
