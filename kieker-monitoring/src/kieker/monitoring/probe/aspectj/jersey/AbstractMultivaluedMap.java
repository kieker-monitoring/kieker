/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package kieker.monitoring.probe.aspectj.jersey;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Abstract skeleton implementation of a {@link MultivaluedMap} that is backed
 * by a [key, multi-value] store represented as a {@link Map Map&lt;K, List&lt;V&gt;&gt;}.
 *
 * @param <K>
 *            the type of keys maintained by this map.
 * @param <V>
 *            the type of mapped values.
 * @author Marek Potociar
 */
public abstract class AbstractMultivaluedMap<K, V> implements MultivaluedMap<K, V> {

	/**
	 * Backing store for the [key, multi-value] pairs.
	 */
	protected final Map<K, List<V>> store;

	/**
	 * Initialize the backing store in the abstract parent multivalued map
	 * implementation.
	 *
	 * @param store
	 *            the backing {@link Map} to be used as a [key, multi-value]
	 *            store. Must not be {@code null}.
	 * @throws NullPointerException
	 *             in case the underlying {@code store} parameter
	 *             is {@code null}.
	 */
	public AbstractMultivaluedMap(final Map<K, List<V>> store) {
		if (store == null) {
			throw new NullPointerException("Underlying store must not be 'null'.");
		}
		this.store = store;
	}

	/**
	 * Set the value for the key to be a one item list consisting of the supplied
	 * value. Any existing values will be replaced.
	 * <p />
	 * NOTE: This implementation ignores {@code null} values; A supplied value of {@code null} is ignored and not added to the purged value list. As a result of such
	 * operation, empty value list would be registered for the supplied key. Overriding implementations may modify this behavior by redefining the
	 * {@link #addNull(java.util.List)} method.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the single value of the key. If the value is {@code null} it
	 *            will be ignored.
	 */
	@Override
	public final void putSingle(final K key, final V value) {
		final List<V> values = this.getValues(key);

		values.clear();
		if (value != null) {
			values.add(value);
		} else {
			this.addNull(values);
		}
	}

	/**
	 * Define the behavior for adding a {@code null} values to the value list.
	 * <p />
	 * Default implementation is a no-op, i.e. the {@code null} values are ignored. Overriding implementations may modify this behavior by providing their own
	 * definitions of this method.
	 *
	 * @param values
	 *            value list where the {@code null} value addition is being
	 *            requested.
	 */
	@SuppressWarnings("UnusedParameters")
	protected void addNull(final List<V> values) {
		// do nothing in the default implementation; ignore the null value
	}

	/**
	 * Define the behavior for adding a {@code null} values to the first position
	 * in the value list.
	 * <p />
	 * Default implementation is a no-op, i.e. the {@code null} values are ignored. Overriding implementations may modify this behavior by providing their own
	 * definitions of this method.
	 *
	 * @param values
	 *            value list where the {@code null} value addition is being
	 *            requested.
	 */
	@SuppressWarnings("UnusedParameters")
	protected void addFirstNull(final List<V> values) {
		// do nothing in the default implementation; ignore the null value
	}

	/**
	 * Add a value to the current list of values for the supplied key.
	 * <p />
	 * NOTE: This implementation ignores {@code null} values; A supplied value of {@code null} is ignored and not added to the value list. Overriding implementations
	 * may modify this behavior by redefining the {@link #addNull(java.util.List)} method.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value to be added.
	 */
	@Override
	public final void add(final K key, final V value) {
		final List<V> values = this.getValues(key);

		if (value != null) {
			values.add(value);
		} else {
			this.addNull(values);
		}
	}

	/**
	 * Add multiple values to the current list of values for the supplied key. If
	 * the supplied array of new values is empty, method returns immediately.
	 * Method throws a {@code NullPointerException} if the supplied array of values
	 * is {@code null}.
	 * <p />
	 * NOTE: This implementation ignores {@code null} values; Any of the supplied values of {@code null} is ignored and not added to the value list. Overriding
	 * implementations may modify this behavior by redefining the {@link #addNull(java.util.List)} method.
	 *
	 * @param key
	 *            the key.
	 * @param newValues
	 *            the values to be added.
	 * @throws NullPointerException
	 *             if the supplied array of new values is {@code null}.
	 */
	public final void addAll(final K key, final V... newValues) {
		if (newValues == null) {
			throw new NullPointerException("Supplied array of values must not be null.");
		}
		if (newValues.length == 0) {
			return;
		}

		final List<V> values = this.getValues(key);

		for (final V value : newValues) {
			if (value != null) {
				values.add(value);
			} else {
				this.addNull(values);
			}
		}
	}

	/**
	 * Add all the values from the supplied value list to the current list of
	 * values for the supplied key. If the supplied value list is empty, method
	 * returns immediately. Method throws a {@code NullPointerException} if the
	 * supplied array of values is {@code null}.
	 * <p />
	 * NOTE: This implementation ignores {@code null} values; Any {@code null} value in the supplied value list is ignored and not added to the value list.
	 * Overriding implementations may modify this behavior by redefining the {@link #addNull(java.util.List)} method.
	 *
	 * @param key
	 *            the key.
	 * @param valueList
	 *            the list of values to be added.
	 * @throws NullPointerException
	 *             if the supplied value list is {@code null}.
	 */
	public final void addAll(final K key, final List<V> valueList) {
		if (valueList == null) {
			throw new NullPointerException("Supplied list of values must not be null.");
		}
		if (valueList.isEmpty()) {
			return;
		}

		final List<V> values = this.getValues(key);

		for (final V value : valueList) {
			if (value != null) {
				values.add(value);
			} else {
				this.addNull(values);
			}
		}
	}

	@Override
	public final V getFirst(final K key) {
		final List<V> values = this.store.get(key);
		if ((values != null) && (values.size() > 0)) {
			return values.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Add a value to the first position in the current list of values for the
	 * supplied key.
	 * <p />
	 * NOTE: This implementation ignores {@code null} values; A supplied value of {@code null} is ignored and not added to the purged value list. Overriding
	 * implementations may modify this behavior by redefining the {@link #addFirstNull(java.util.List)} method.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value to be added.
	 */
	public final void addFirst(final K key, final V value) {
		final List<V> values = this.getValues(key);

		if (value != null) {
			values.add(0, value);
		} else {
			this.addFirstNull(values);
		}
	}

	/**
	 * Return a non-null list of values for a given key. The returned list may be
	 * empty.
	 * <p />
	 * If there is no entry for the key in the map, a new empty {@link List} instance is created, registered within the map to hold the values of the key and
	 * returned from the method.
	 *
	 * @param key
	 *            the key.
	 * @return value list registered with the key. The method is guaranteed to never
	 *         return {@code null}.
	 */
	protected final List<V> getValues(final K key) {
		List<V> l = this.store.get(key);
		if (l == null) {
			l = new LinkedList<V>();
			this.store.put(key, l);
		}
		return l;
	}

	@Override
	public String toString() {
		return this.store.toString();
	}

	/**
	 * {@inheritDoc }
	 * <p />
	 * This implementation delegates the method call to to the the underlying [key, multi-value] store.
	 *
	 * @return a hash code value for the underlying [key, multi-value] store.
	 */
	@Override
	public int hashCode() {
		return this.store.hashCode();
	}

	/**
	 * {@inheritDoc }
	 * <p />
	 * This implementation delegates the method call to to the the underlying [key, multi-value] store.
	 *
	 * @return {@code true} if the specified object is equal to the underlying
	 *         [key, multi-value] store, {@code false} otherwise.
	 */
	@Override
	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	public boolean equals(final Object o) {
		return this.store.equals(o);
	}

	@Override
	public Collection<List<V>> values() {
		return this.store.values();
	}

	@Override
	public int size() {
		return this.store.size();
	}

	@Override
	public List<V> remove(final Object key) {
		return this.store.remove(key);
	}

	@Override
	public void putAll(final Map<? extends K, ? extends List<V>> m) {
		this.store.putAll(m);
	}

	@Override
	public List<V> put(final K key, final List<V> value) {
		return this.store.put(key, value);
	}

	@Override
	public Set<K> keySet() {
		return this.store.keySet();
	}

	@Override
	public boolean isEmpty() {
		return this.store.isEmpty();
	}

	@Override
	public List<V> get(final Object key) {
		return this.store.get(key);
	}

	@Override
	public Set<Entry<K, List<V>>> entrySet() {
		return this.store.entrySet();
	}

	@Override
	public boolean containsValue(final Object value) {
		return this.store.containsValue(value);
	}

	@Override
	public boolean containsKey(final Object key) {
		return this.store.containsKey(key);
	}

	@Override
	public void clear() {
		this.store.clear();
	}

	public boolean equalsIgnoreValueOrder(final MultivaluedMap<K, V> omap) {
		if (this == omap) {
			return true;
		}
		if (!this.keySet().equals(omap.keySet())) {
			return false;
		}
		for (final Entry<K, List<V>> e : this.entrySet()) {
			final List<V> olist = omap.get(e.getKey());
			if (e.getValue().size() != olist.size()) {
				return false;
			}
			for (final V v : e.getValue()) {
				if (!olist.contains(v)) {
					return false;
				}
			}
		}
		return true;
	}
}
