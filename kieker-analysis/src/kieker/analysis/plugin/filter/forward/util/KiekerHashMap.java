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
package kieker.analysis.plugin.filter.forward.util;

import java.lang.ref.SoftReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 * @deprecated since 1.15.1 old plugin api
 */
@Deprecated
public class KiekerHashMap {

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
	private final Segment[] segments;

	/**
	 * @since 1.10
	 */
	public KiekerHashMap() {
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
			this.segments[i] = new Segment(cap, LOAD_FACTOR);
		}
	}

	/**
	 * Applies a supplemental hash function to a given hashCode, which defends against poor quality hash functions. This is critical because ConcurrentHashMap uses
	 * power-of-two length hash tables, that otherwise encounter collisions for hashCodes that do not differ in lower or upper bits.
	 */
	private static final int hash(final String value) {
		// Spread bits to regularize both segment and index locations, using variant of single-word Wang/Jenkins hash.
		int h = value.hashCode();
		h += (h << 15) ^ 0xffffcd7d;
		h ^= h >>> 10;
		h += h << 3;
		h ^= h >>> 6;
		h += (h << 2) + (h << 14);
		return h ^ (h >>> 16);
	}

	public final String get(final String value) {
		final int hash = KiekerHashMap.hash(value);
		return this.segments[(hash >>> this.segmentShift) & this.segmentMask].get(value, hash);
	}

	// ---------------- Inner Classes --------------

	/**
	 * StringBuffer entry.
	 */
	private static final class HashEntry extends SoftReference<String> {
		final int hash; // NOPMD NOCS (package visible for inner class)
		final HashEntry next; // NOPMD NOCS (package visible for inner class)

		protected HashEntry(final String value, final int hash, final HashEntry next) {
			super(value);
			this.hash = hash;
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
	private static final class Segment extends ReentrantLock {

		private static final long serialVersionUID = 1L;

		/**
		 * The number of elements in this segment's region.
		 */
		private volatile int count;

		/**
		 * The per-segment table.
		 */
		private HashEntry[] table;

		/**
		 * The table is rehashed when its size exceeds this threshold. (The value of this field is always <tt>(int)(capacity * loadFactor)</tt>.)
		 */
		private int threshold;

		protected Segment(final int initialCapacity, final double lf) {
			this.table = new HashEntry[initialCapacity];
			this.threshold = (int) (initialCapacity * lf);
			this.count = 0;
		}

		protected final String get(final String value, final int hash) {
			HashEntry e = null;
			String cachedString;
			if (this.count != 0) { // volatile read! search for entry without locking
				final HashEntry[] tab = this.table;
				final int index = hash & (tab.length - 1);
				final HashEntry first = tab[index];
				e = first;
				while (e != null) {
					if (e.hash == hash) {
						cachedString = e.get();
						if (value.equals(cachedString)) { // NOPMD (deeply nested if)
							return cachedString;
						}
					}
					e = e.next;
				}
			}
			this.lock();
			try {
				final int c = this.count + 1;
				if (c >= this.threshold) {
					this.cleanup();
					if (c >= this.threshold) { // if still full
						this.rehash();
					}
					this.count = c; // write volatile
				}
				final HashEntry[] tab = this.table;
				final int index = hash & (tab.length - 1);
				final HashEntry first = tab[index]; // the bin the value may be inside
				e = first;
				while (e != null) {
					if (e.hash == hash) {
						cachedString = e.get();
						if (value.equals(cachedString)) {
							return cachedString;
						}
					}
					e = e.next;
				}
				tab[index] = new HashEntry(value, hash, first);
				this.count = c; // write-volatile
				return value; // return now cached string
			} finally {
				this.unlock();
			}
		}

		private final void cleanup() {
			int c = this.count;
			final HashEntry[] tab = this.table;
			for (int index = 0; index < tab.length; index++) {
				// find first remaining entry
				HashEntry e = tab[index];
				while ((e != null) && (e.get() == null)) {
					e = e.next;
					c--;
				}
				if (e == null) {
					tab[index] = null;
				} else {
					// find more existing entries and enqueue before this one
					HashEntry first = new HashEntry(e.get(), e.hash, null);
					e = e.next;
					while (e != null) {
						final String s = e.get();
						if (s != null) {
							first = new HashEntry(s, e.hash, first);
						} else {
							c--;
						}
						e = e.next;
					}
					tab[index] = first;
				}
			}
			c--;
			this.count = c; // write-volatile
		}

		/**
		 * Reclassify nodes in each list to new Map. Because we are using power-of-two expansion, the elements from each bin must either stay at same index, or
		 * move with a power of two offset. We eliminate unnecessary node creation by catching cases where old nodes can be reused because their next fields
		 * won't change. Statistically, at the default threshold, only about one-sixth of them need cloning when a table doubles. The nodes they replace will be
		 * garbage collectable as soon as they are no longer referenced by any reader thread that may be in the midst of traversing table right now.
		 */
		private final void rehash() {
			final HashEntry[] oldTable = this.table;
			final int oldCapacity = oldTable.length;
			if (oldCapacity >= MAXIMUM_CAPACITY) {
				return;
			}
			final HashEntry[] newTable = new HashEntry[oldCapacity << 1];
			this.threshold = (int) (newTable.length * LOAD_FACTOR);
			final int sizeMask = newTable.length - 1;
			for (int i = 0; i < oldCapacity; i++) {
				// We need to guarantee that any existing reads of old Map can proceed. So we cannot yet null out each bin.
				final HashEntry e = oldTable[i];

				if (e != null) {
					final HashEntry next = e.next;
					final int idx = e.hash & sizeMask;

					// Single node on list
					if (next == null) {
						newTable[idx] = e;
					} else {
						// Reuse trailing consecutive sequence at same slot
						HashEntry lastRun = e;
						int lastIdx = idx;
						for (HashEntry last = next; last != null; last = last.next) { // find end of bin
							final int k = last.hash & sizeMask;
							if (k != lastIdx) { // NOCS (nested if)
								lastIdx = k;
								lastRun = last;
							}
						}
						newTable[lastIdx] = lastRun;

						// Clone all remaining nodes
						for (HashEntry p = e; p != lastRun; p = p.next) { // NOPMD (no equals meant here)
							final int k = p.hash & sizeMask;
							final HashEntry n = newTable[k];
							newTable[k] = new HashEntry(p.get(), p.hash, n);
						}
					}
				}
			}
			this.table = newTable;
		}
	}
}
