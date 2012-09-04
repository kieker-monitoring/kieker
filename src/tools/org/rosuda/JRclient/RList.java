package org.rosuda.JRclient;

// JRclient library - client interface to Rserve, see http://www.rosuda.org/Rserve/
// Copyright (C) 2004,2007 Simon Urbanek
// --- for licensing information see LICENSE file in the original JRclient distribution ---

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * implementation of R-lists<br>
 * All lists (dotted-pair lists, language lists, expressions and vectors) are regarded as named generic vectors.
 * Note: This implementation has changed radically in Rserve 0.5!
 * 
 * This class inofficially implements the Map interface. Unfortunately a conflict in the Java iterface classes Map and List doesn't allow us to implement both
 * officially. Most prominently the Map 'remove' method had to be renamed to removeByKey.
 * 
 * @version $Id: RList.java 2735 2007-04-27 03:45:20Z urbanek $
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class RList extends Vector implements List {
	public Vector names;

	/** constructs an empty list */
	public RList() {
		super();
		this.names = null;
	}

	/**
	 * constructs an initialized list
	 * 
	 * @param h
	 *            head xpression
	 * @param b
	 *            body xpression
	 */
	public RList(final REXP[] contents) {
		super(contents.length);
		int i = 0;
		while (i < contents.length) {
			super.add(contents[i++]);
		}
		this.names = null;
	}

	public RList(final int initSize, final boolean hasNames) {
		super(initSize);
		this.names = null;
		if (hasNames) {
			this.names = new Vector(initSize);
		}
	}

	public RList(final Collection contents) {
		super(contents);
		this.names = null;
	}

	public RList(final REXP[] contents, final String[] names) {
		this(contents);
		if ((names != null) && (names.length > 0)) {
			this.names = new Vector(names.length);
			int i = 0;
			while (i < names.length) {
				this.names.add(names[i++]);
			}
			while (this.names.size() < this.size()) {
				this.names.add(null);
			}
		}
	}

	public RList(final Collection contents, final String[] names) {
		this(contents);
		if ((names != null) && (names.length > 0)) {
			this.names = new Vector(names.length);
			int i = 0;
			while (i < names.length) {
				this.names.add(names[i++]);
			}
			while (this.names.size() < this.size()) {
				this.names.add(null);
			}
		}
	}

	public RList(final Collection contents, final Collection names) {
		this(contents);
		if ((names != null) && (names.size() > 0)) {
			this.names = new Vector(names);
			while (this.names.size() < this.size()) {
				this.names.add(null);
			}
		}
	}

	public boolean isNamed() {
		return this.names != null;
	}

	/**
	 * get xpression given a key
	 * 
	 * @param v
	 *            key
	 * @return xpression which corresponds to the given key or <code>null</code> if list is not standartized or key not found
	 */
	public REXP at(final String v) {
		if (this.names == null) {
			return null;
		}
		final int i = this.names.indexOf(v);
		if (i < 0) {
			return null;
		}
		return (REXP) this.elementAt(i);
	}

	/**
	 * get element at the specified position
	 * 
	 * @param i
	 *            index
	 * @return xpression at the index or <code>null</code> if list is not standartized or
	 *         if index out of bounds
	 */
	public REXP at(final int i) {
		return ((i >= 0) && (i < this.size())) ? (REXP) this.elementAt(i) : null;
	}

	public String keyAt(final int i) {
		return ((this.names == null) || (i < 0) || (i >= this.names.size())) ? null : (String) this.names.get(i);
	}

	/**
	 * returns all keys of the list
	 * 
	 * @return array containing all keys or <code>null</code> if list is not standartized
	 */
	public String[] keys() {
		if (this.names == null) {
			return null;
		}
		int i = 0;
		final String k[] = new String[this.names.size()];
		while (i < k.length) {
			k[i] = this.keyAt(i);
			i++;
		}
		;
		return k;
	}

	// --- overrides that sync names

	@Override
	public void add(final int index, final Object element) {
		super.add(index, element);
		if (this.names == null) {
			return;
		}
		this.names.add(index, null);
	}

	@Override
	public boolean addAll(final Collection c) {
		final boolean ch = super.addAll(c);
		if (this.names == null) {
			return ch;
		}
		final int l = this.size();
		while (this.names.size() < l) {
			this.names.add(null);
		}
		return ch;
	}

	@Override
	public boolean addAll(final int index, final Collection c) {
		final boolean ch = super.addAll(index, c);
		if (this.names == null) {
			return ch;
		}
		final int l = c.size();
		while (l > 0) {
			this.names.add(index, null);
		}
		return ch;
	}

	@Override
	public void clear() {
		super.clear();
		this.names = null;
	}

	@Override
	public Object clone() {
		return new RList(this, this.names);
	}

	@Override
	public Object remove(final int index) {
		final Object o = super.remove(index);
		if (this.names != null) {
			this.names.remove(index);
			if (this.size() == 0) {
				this.names = null;
			}
		}
		return o;
	}

	@Override
	public boolean remove(final Object elem) {
		final int i = this.indexOf(elem);
		if (i < 0) {
			return false;
		}
		this.remove(i);
		if (this.size() == 0) {
			this.names = null;
		}
		return true;
	}

	@Override
	public boolean removeAll(final Collection c) {
		if (this.names == null) {
			return super.removeAll(c);
		}
		boolean changed = false;
		final Iterator it = c.iterator();
		while (it.hasNext()) {
			changed |= this.remove(it.next());
		}
		return changed;
	}

	@Override
	public boolean retainAll(final Collection c) {
		if (this.names == null) {
			return super.retainAll(c);
		}
		final boolean rm[] = new boolean[this.size()];
		boolean changed = false;
		int i = 0;
		while (i < rm.length) {
			changed |= rm[i] = !c.contains(this.get(i));
			i++;
		}
		while (i > 0) {
			i--;
			if (rm[i]) {
				this.remove(i);
			}
		}
		return changed;
	}

	// --- old API mapping
	@Override
	public void removeAllElements() {
		this.clear();
	}

	@Override
	public void insertElementAt(final Object obj, final int index) {
		this.add(index, obj);
	}

	@Override
	public void addElement(final Object obj) {
		this.add(obj);
	}

	@Override
	public void removeElementAt(final int index) {
		this.remove(index);
	}

	@Override
	public boolean removeElement(final Object obj) {
		return this.remove(obj);
	}

	// --- Map interface

	public boolean containsKey(final Object key) {
		return (this.names == null) ? false : this.names.contains(key);
	}

	public boolean containsValue(final Object value) {
		return this.contains(value);
	}

	/** NOTE: THIS IS UNIMPLEMENTED and always returns <code>null</code>! Due to the fact that R lists are not proper maps we canot maintain a set-view of the list */
	public Set entrySet() {
		return null;
	}

	public Object get(final Object key) {
		return this.at((String) key);
	}

	/** Note: sinde RList is not really a Map, the returned set is only an approximation as it cannot reference duplicate or null names that may exist in the list */
	public Set keySet() {
		if (this.names == null) {
			return null;
		}
		return new HashSet(this.names);
	}

	public Object put(final Object key, final Object value) {
		if (key == null) {
			this.add(value);
			return null;
		}
		if (this.names != null) {
			final int p = this.names.indexOf(key);
			if (p >= 0) {
				return super.set(p, value);
			}
		}
		final int i = this.size();
		this.add(value);
		if (this.names == null) {
			this.names = new Vector(i + 1);
		}
		while (this.names.size() < i) {
			this.names.add(null);
		}
		this.names.add(key);
		return null;
	}

	public void putAll(final Map t) {
		if (t == null) {
			return;
		}
		if (t instanceof RList) { // we need some more sophistication for RLists as they may have null-names which we append
			final RList l = (RList) t;
			if (this.names == null) {
				this.addAll(l);
				return;
			}
			final int n = l.size();
			int i = 0;
			while (i < n) {
				final String key = l.keyAt(i);
				if (key == null) {
					this.add(l.at(i));
				} else {
					this.put(key, l.at(i));
				}
				i++;
			}
		} else {
			final Set ks = t.keySet();
			final Iterator i = ks.iterator();
			while (i.hasNext()) {
				final Object key = i.next();
				this.put(key, t.get(key));
			}
		}
	}

	public Object removeByKey(final Object key) {
		if (this.names == null) {
			return null;
		}
		final int i = this.names.indexOf(key);
		if (i < 0) {
			return null;
		}
		final Object o = this.elementAt(i);
		this.removeElementAt(i);
		this.names.removeElementAt(i);
		return o;
	}

	public Collection values() {
		return this;
	}
}
