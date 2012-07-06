package kieker.common.util;

import java.io.Serializable;
import java.util.Map;

/**
 * <b>Note that this class is a 1:1 copy of Java 1.6's class <code>AbstractMap.SimpleImmutableEntry</code>!</b>
 * 
 * </br></br>
 * 
 * An immutable key-value mapping.
 * 
 * @param <K>
 *            the type of key
 * @param <V>
 *            the type of value
 * 
 */
public class SimpleImmutableEntry<K, V> implements Map.Entry<K, V>,
		Serializable {

	private static final long serialVersionUID = 7138329143949025153L;

	private final K key;

	private final V value;

	/**
	 * Constructs a new instance by key and value.
	 * 
	 * @param theKey
	 *            the key
	 * @param theValue
	 *            the value
	 */
	public SimpleImmutableEntry(final K theKey, final V theValue) {
		this.key = theKey;
		this.value = theValue;
	}

	/**
	 * Constructs a new instance by an entry
	 * 
	 * @param entry
	 *            the entry
	 */
	public SimpleImmutableEntry(final Map.Entry<? extends K, ? extends V> entry) {
		this.key = entry.getKey();
		this.value = entry.getValue();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map.Entry#getKey()
	 */
	public K getKey() {
		return this.key;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Map.Entry#getValue()
	 */
	public V getValue() {
		return this.value;
	}

	/**
	 * Throws an UnsupportedOperationException.
	 * 
	 * @param object
	 *            new value
	 * @return (Does not)
	 * @throws UnsupportedOperationException
	 *             always
	 * 
	 * @see java.util.Map.Entry#setValue(java.lang.Object)
	 */
	public V setValue(final V object) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Answers whether the object is equal to this entry. This works across
	 * all kinds of the Map.Entry interface.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof Map.Entry) {
			final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
			return (this.key == null ? entry.getKey() == null : this.key.equals(entry
					.getKey()))
					&& (this.value == null ? entry.getValue() == null : this.value
							.equals(entry.getValue()));
		}
		return false;
	}

	/**
	 * Answers the hash code of this entry.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (this.key == null ? 0 : this.key.hashCode())
				^ (this.value == null ? 0 : this.value.hashCode());
	}

	/**
	 * Answers a String representation of this entry.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.key + "=" + this.value; //$NON-NLS-1$
	}
}
