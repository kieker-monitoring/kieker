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

package kieker.common.util.registry;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import kieker.common.record.misc.RegistryRecord;

/**
 * A simple registry to assign unique ids to objects.
 * The basic strategy is to subdivide the table among Segments, each of which itself is a concurrently readable hash table.
 *
 * Based upon ConcurrentHashMap.
 * Written by Doug Lea with assistance from members of JCP JSR-166 Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 *
 * @param <E>
 *            the type of registered objects
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class Registry<E> implements IRegistry<E> {
	private static final int INITIAL_CAPACITY = 16;
	private static final double LOAD_FACTOR = 0.75d;
	private static final int CONCURRENCY_LEVEL = 16;
	private static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * Mask value for indexing into segments. The upper bits of a key's hash code are used to choose the segment.
	 */
	private final int segmentMask;

	/**
	 * Shift value for indexing within segments.
	 */
	private final int segmentShift;

	/**
	 * The segments, each of which is a specialized hash table.
	 */
	private final Segment<E>[] segments;

	/**
	 * The next id used.
	 */
	private final AtomicInteger nextId = new AtomicInteger(0);

	/**
	 * A cached copy of the current assignment of IDs.
	 */
	private volatile E[] eArrayCached;

	/**
	 * Creates a new, empty registry with a default initial capacity (32), load factor (0.75) and concurrencyLevel (8).
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Registry() {
		// Find power-of-two sizes best matching arguments
		int sshift = 0;
		int ssize = 1;
		while (ssize < CONCURRENCY_LEVEL) {
			++sshift;
			ssize <<= 1;
		}
		this.segmentShift = 32 - sshift;
		this.segmentMask = ssize - 1;
		this.segments = new Segment[ssize];
		int c = INITIAL_CAPACITY / ssize;
		if ((c * ssize) < INITIAL_CAPACITY) {
			++c;
		}
		int cap = 1;
		while (cap < c) {
			cap <<= 1;
		}
		for (int i = 0; i < this.segments.length; ++i) {
			this.segments[i] = new Segment<E>(cap, LOAD_FACTOR);
		}
		this.eArrayCached = (E[]) new Object[0];
	}

	/**
	 * Applies a supplemental hash function to a given hashCode, which defends against poor quality hash functions. This is critical because ConcurrentHashMap uses
	 * power-of-two length hash tables, that otherwise encounter collisions for hashCodes that do not differ in lower or upper bits.
	 *
	 * @param value
	 *            The value to hash.
	 *
	 * @return The hashed value.
	 */
	private static final int hash(final Object value) {
		// Spread bits to regularize both segment and index locations, using variant of single-word Wang/Jenkins hash.
		int h = value.hashCode();
		h += (h << 15) ^ 0xffffcd7d;
		h ^= h >>> 10;
		h += h << 3;
		h ^= h >>> 6;
		h += (h << 2) + (h << 14);
		return h ^ (h >>> 16);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setRecordReceiver(final IRegistryRecordReceiver recordReceiver) {
		for (final Segment<E> segment : this.segments) {
			segment.setRecordReceiver(recordReceiver);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int get(final E value) {
		final int hash = Registry.hash(value);
		final int index = (hash >>> this.segmentShift) & this.segmentMask;
		final Segment<E> segment = this.segments[index];
		return segment.get(value, hash, this.nextId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final E get(final int id) {
		final int capacity = this.nextId.get();
		if (id > capacity) {
			return null;
		}
		if (this.eArrayCached.length != capacity) { // volatile read
			@SuppressWarnings("unchecked")
			final E[] eArray = (E[]) new Object[capacity];
			for (final Segment<E> segment : this.segments) {
				segment.insertIntoArray(eArray);
			}
			this.eArrayCached = eArray; // volatile write
		}
		return this.eArrayCached[id];
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated As of 1.13. Do not iterate through the registry.
	 */
	@Deprecated
	@Override
	public final E[] getAll() {
		final int capacity = this.nextId.get();
		if (this.eArrayCached.length != capacity) { // volatile read
			@SuppressWarnings("unchecked")
			final E[] eArray = (E[]) new Object[capacity];
			for (final Segment<E> segment : this.segments) {
				segment.insertIntoArray(eArray);
			}
			this.eArrayCached = eArray; // volatile write
		}
		// HINT for (String s:registry.getAll()) leads to a ClassCastException (chw 15.06.2016)
		// since iterable is of type Object, not String
		return Arrays.copyOf(this.eArrayCached, capacity);
	}

	@Override
	public final int getSize() {
		return this.nextId.get();
	}

	/**
	 * Removes the given element from the registry.
	 *
	 * @param value
	 *            The element to remove.
	 */
	@SuppressWarnings("unchecked")
	public final void remove(final E value) {
		final int hash = Registry.hash(value);
		this.segments[(hash >>> this.segmentShift) & this.segmentMask].remove(value, hash);
		this.eArrayCached = (E[]) new Object[0]; // invalidate cache
	}

	/**
	 * Clears the whole registry.
	 */
	@SuppressWarnings("unchecked")
	public final void clear() {
		for (final Segment<E> segment : this.segments) {
			segment.clear();
		}
		this.eArrayCached = (E[]) new Object[0]; // invalidate cache
	}

	@Override
	public String toString() {
		return Arrays.toString(this.getAll());
	}

	// ---------------- Inner Classes --------------

	/**
	 * Registry entry.
	 */
	private static final class HashEntry<E> implements Serializable {
		private static final long serialVersionUID = 1L;
		final E value; // NOPMD NOCS (package visible for inner class)
		final int hash; // NOPMD NOCS (package visible for inner class)
		final int id; // NOPMD NOCS (package visible for inner class)
		final HashEntry<E> next; // NOPMD NOCS (package visible for inner class)

		protected HashEntry(final E value, final int hash, final int id, final HashEntry<E> next) {
			this.value = value;
			this.hash = hash;
			this.id = id;
			this.next = next;
		}
	}

	/**
	 * Segments are specialized versions of hash tables. This subclasses from ReentrantLock opportunistically, just to simplify some locking and avoid separate
	 * construction.
	 *
	 * Segments maintain a table of entry lists that are ALWAYS kept in a consistent state, so can be read without locking. Next fields of nodes are immutable
	 * (final). All list additions are performed at the front of each bin. This makes it easy to check changes, and also fast to traverse. When nodes would
	 * otherwise be changed, new nodes are created to replace them. This works well for hash tables since the bin lists tend to be short. (The average length is
	 * less than two for the default load factor threshold.)
	 *
	 * Read operations can thus proceed without locking, but rely on selected uses of volatiles to ensure that completed write operations performed by other
	 * threads are noticed. For most purposes, the "count" field, tracking the number of elements, serves as that volatile variable ensuring visibility. This is
	 * convenient because this field needs to be read in many read operations anyway:
	 *
	 * - All (unsynchronized) read operations must first read the "count" field, and should not look at table entries if it is 0.
	 *
	 * - All (synchronized) write operations should write to the "count" field after structurally changing any bin. The operations must not take any action that
	 * could even momentarily cause a concurrent read operation to see inconsistent data. This is made easier by the nature of the read operations in Map. For
	 * example, no operation can reveal that the table has grown but the threshold has not yet been updated, so there are no atomicity requirements for this with
	 * respect to reads.
	 *
	 * As a guide, all critical volatile reads and writes to the count field are marked in code comments.
	 */
	private static final class Segment<E> extends ReentrantLock {

		private static final long serialVersionUID = 1L;

		/**
		 * The number of elements in this segment's region.
		 */
		private volatile int count;

		/**
		 * The per-segment table.
		 */
		private HashEntry<E>[] table;

		/**
		 * The table is rehashed when its size exceeds this threshold. (The value of this field is always <tt>(int)(capacity * loadFactor)</tt>.)
		 */
		private int threshold;

		/**
		 * Send messages on new entries to this.
		 */
		private transient IRegistryRecordReceiver recordReceiver;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		protected Segment(final int initialCapacity, final double lf) {
			this.table = new HashEntry[initialCapacity];
			this.threshold = (int) (initialCapacity * lf);
			this.count = 0;
		}

		protected final void setRecordReceiver(final IRegistryRecordReceiver recordReceiver) {
			this.lock();
			try {
				this.recordReceiver = recordReceiver;
			} finally {
				this.unlock();
			}
		}

		protected final void insertIntoArray(final E[] eArray) {
			if (this.count != 0) { // volatile read!
				this.lock(); // could be smaller area! it is only important to acquire the lock, not to hold it.
				try {
					final int capacity = eArray.length;
					final HashEntry<E>[] tab = this.table;
					for (final HashEntry<E> hashEntry : tab) {
						HashEntry<E> e = hashEntry;
						while (e != null) {
							if (e.id < capacity) {
								eArray[e.id] = e.value;
							}
							e = e.next;
						}
					}
				} finally {
					this.unlock();
				}
			}
		}

		protected final int get(final E value, final int hash, final AtomicInteger nextId) {
			HashEntry<E> e = null;
			if (this.count != 0) { // volatile read! search for entry without locking
				final HashEntry<E>[] tab = this.table;
				final int index = hash & (tab.length - 1);
				final HashEntry<E> first = tab[index];
				e = first;
				while ((e != null) && ((e.hash != hash) || !value.equals(e.value))) {
					e = e.next;
				}
			}
			if (e == null) { // not yet present, but have to repeat search
				this.lock();
				try {
					final int c = this.count + 1;
					if (c >= this.threshold) {
						this.rehash();
						this.count = c; // write volatile
					}
					final HashEntry<E>[] tab = this.table;
					final int index = hash & (tab.length - 1);
					final HashEntry<E> first = tab[index]; // the bin the value may be inside
					e = first;
					while ((e != null) && ((e.hash != hash) || !value.equals(e.value))) {
						e = e.next;
					}
					if (e == null) { // still not found
						final int id = nextId.getAndIncrement();
						tab[index] = new HashEntry<E>(value, hash, id, first);
						this.count = c; // write-volatile
						if (this.recordReceiver != null) { // NOPMD NOCS (nested if)
							this.recordReceiver.newRegistryRecord(new RegistryRecord(id, (String) value));
						}
						return id; // return new id
					}
				} finally {
					this.unlock();
				}
			}
			return e.id; // return id if found
		}

		protected final void remove(final E value, final int hash) {
			this.lock();
			try {
				final int c = this.count - 1;
				final HashEntry<E>[] tab = this.table;
				final int index = hash & (tab.length - 1);
				final HashEntry<E> first = tab[index];
				HashEntry<E> e = first;
				while ((e != null) && ((e.hash != hash) || !value.equals(e.value))) {
					e = e.next;
				}
				if (e != null) {
					// All entries following removed node can stay in list, but all preceding ones need to be cloned.
					HashEntry<E> newFirst = e.next;
					for (HashEntry<E> p = first; p != e; p = p.next) { // NOPMD (=== instead of equals)
						newFirst = new HashEntry<E>(p.value, p.hash, p.id, newFirst);
					}
					tab[index] = newFirst;
					this.count = c; // write-volatile
				}
			} finally {
				this.unlock();
			}
		}

		protected final void clear() {
			if (this.count != 0) {
				this.lock();
				try {
					final HashEntry<E>[] tab = this.table;
					for (int i = 0; i < tab.length; i++) {
						tab[i] = null;
					}
					this.count = 0; // write-volatile
				} finally {
					this.unlock();
				}
			}
		}

		/**
		 * Reclassify nodes in each list to new Map. Because we are using power-of-two expansion, the elements from each bin must either stay at same index, or
		 * move with a power of two offset. We eliminate unnecessary node creation by catching cases where old nodes can be reused because their next fields
		 * won't change. Statistically, at the default threshold, only about one-sixth of them need cloning when a table doubles. The nodes they replace will be
		 * garbage collectable as soon as they are no longer referenced by any reader thread that may be in the midst of traversing table right now.
		 */
		private final void rehash() {
			final HashEntry<E>[] oldTable = this.table;
			final int oldCapacity = oldTable.length;
			if (oldCapacity >= MAXIMUM_CAPACITY) {
				return;
			}
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final HashEntry<E>[] newTable = new HashEntry[oldCapacity << 1];
			this.threshold = (int) (newTable.length * LOAD_FACTOR);
			final int sizeMask = newTable.length - 1;
			for (int i = 0; i < oldCapacity; i++) {
				// We need to guarantee that any existing reads of old Map can proceed. So we cannot yet null out each bin.
				final HashEntry<E> e = oldTable[i];

				if (e != null) {
					final HashEntry<E> next = e.next;
					final int idx = e.hash & sizeMask;

					// Single node on list
					if (next == null) {
						newTable[idx] = e;
					} else {
						// Reuse trailing consecutive sequence at same slot
						HashEntry<E> lastRun = e;
						int lastIdx = idx;
						for (HashEntry<E> last = next; last != null; last = last.next) { // find end of bin
							final int k = last.hash & sizeMask;
							if (k != lastIdx) { // NOCS (nested if)
								lastIdx = k;
								lastRun = last;
							}
						}
						newTable[lastIdx] = lastRun;

						// Clone all remaining nodes
						for (HashEntry<E> p = e; p != lastRun; p = p.next) { // NOPMD (no equals meant here)
							final int k = p.hash & sizeMask;
							final HashEntry<E> n = newTable[k];
							newTable[k] = new HashEntry<E>(p.value, p.hash, p.id, n);
						}
					}
				}
			}
			this.table = newTable;
		}
	}
}
