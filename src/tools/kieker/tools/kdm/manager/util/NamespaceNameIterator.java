/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.kdm.manager.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.Namespace;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;

/**
 * This class provides an iterator for namespaces within a given {@link Namespace} or list of {@link AbstractCodeElement}. Namespaces are used in C#-models.
 * 
 * @author Benjamin Harms
 * 
 */
public class NamespaceNameIterator extends AbstractKDMIterator<String> {
	/**
	 * If true the iterator should only search form global namespaces.
	 */
	private final boolean isGlobal;
	/**
	 * This hash set contains all all keys of the namespaces.
	 */
	private final Set<String> keys = new HashSet<String>();

	/**
	 * Creates a new instance of this class from the given list of {@link AbstractCodeElement}. Depth first is disabled.
	 * 
	 * @param elementList
	 *            The list to iterate on.
	 * @throws NullPointerException
	 *             If the list is null.
	 */
	public NamespaceNameIterator(final List<? extends AbstractCodeElement> elementList) throws NullPointerException {
		super(elementList);

		this.performDepthFirstSearch = false;
		this.isGlobal = true;
	}

	/**
	 * Creates a new instance of this class from the given list of {@link AbstractCodeElement}.
	 * 
	 * @param elementList
	 *            The list to iterate on.
	 * @param depthFirstSearch
	 *            If true, the iterator tries to get more information by iterating nested namespaces.
	 * @throws NullPointerException
	 *             If the list is null.
	 */
	public NamespaceNameIterator(final List<? extends AbstractCodeElement> elementList, final boolean depthFirstSearch) throws NullPointerException {
		super(elementList);

		this.performDepthFirstSearch = depthFirstSearch;
		this.isGlobal = false;
	}

	/**
	 * Creates a new instance of this class from a given {@link Namespace}.
	 * 
	 * @param namespaze
	 *            The namespace to iterate on.
	 * @param fullNamespaceName
	 *            The full name of the namespace.
	 * @param depthFirstSearch
	 *            If true, the iterator performs depth-first search and tries to get more information by iterating nested namespaces.
	 * @throws NullPointerException
	 *             If the element list is null.
	 */
	public NamespaceNameIterator(final Namespace namespaze, final String fullNamespaceName, final boolean depthFirstSearch) throws NullPointerException {
		this(namespaze.getGroupedCode(), depthFirstSearch);

		this.nameStak.push(fullNamespaceName);
	}

	/**
	 * This method tries to find another element.
	 * 
	 * @return
	 *         Returns true if the iterator has more elements, otherwise false.
	 */
	public boolean hasNext() {
		// If a current element exist there is one
		if (this.currentElement != null) {
			return true;
		}
		// Go back if necessary an possible
		this.stepBack();
		// Ensure there are more elements
		if (!this.currentIterator.hasNext()) {
			return false;
		} else {
			boolean cond;
			do {
				this.currentElement = this.currentIterator.next();
				// Instance of Namespace? ==> there are more elements
				if (this.currentElement instanceof Namespace) {
					final Namespace namespaze = (Namespace) this.currentElement;
					try {
						// If we search only for 'global', check the name
						if (this.isGlobal) {
							final String value = NamespaceNameIterator.getValueFromAttribute(namespaze, "FullyQualifiedName");
							String key;
							// Get the first part only
							if (value.contains(".")) {
								final int index = value.indexOf('.');
								key = value.substring(0, index);
							} else {
								key = value;
							}

							// Avoid the empty string and the global value
							if ("".equals(key) || "global".equals(key)) {
								cond = true;
							} else {
								if (this.keys.add(key)) {
									cond = false;
									return true;
								} else {
									cond = true;
								}
							}
						} else { // The global name does not matter
							return true;
						}

					} catch (final NoSuchElementException e) {
						cond = false;
						return true;
					}
				} else {
					// Else try again
					cond = true;
					// Reset current element!!
					this.currentElement = null; // NOPMD (null)
					this.stepBack();
				}
			} while (cond && this.currentIterator.hasNext() && !this.iteratorStack.isEmpty());
		}

		return this.currentIterator.hasNext();
	}

	/**
	 * Returns the next namespace name. Call next() <b>only</b> after a previous call of {@link #hasNext()}!
	 * 
	 * @throws NoSuchElemetException
	 *             if no other element exists.
	 * @see #hasNext()
	 */
	public String next() {
		if (this.currentElement == null) {
			throw new NoSuchElementException("No more elements");
		}

		final Namespace namespaze = (Namespace) this.currentElement;

		// Assemble name
		String name;
		try {
			name = NamespaceNameIterator.getValueFromAttribute(namespaze, "FullyQualifiedName");
		} catch (final NoSuchElementException e) {
			name = namespaze.getName();
		}

		// Perform depth-first search?
		if (this.performDepthFirstSearch) {
			this.nameStak.push(name);
			// refresh iterator
			final Iterator<CodeItem> iterator = namespaze.getGroupedCode().iterator();
			this.iteratorStack.push(iterator);
			this.currentIterator = iterator;
		}
		// Reset current element!!
		this.currentElement = null; // NOPMD (null)
		return name;
	}

	/**
	 * This method tries to get an {@link Attribute} with the given tag and returns the value.
	 * 
	 * @param item
	 *            The {@link CodeItem} to get the attribute from.
	 * @param tag
	 *            The value for the tag-attribute of the {@link Attribute}.
	 * @return
	 *         The {@link Attribute} with the tag value 'FullyQualifiedName'.
	 * @throws NoSuchElementException
	 *             If no such attribute exist.
	 */
	private static String getValueFromAttribute(final CodeItem item, final String tag) throws NoSuchElementException {
		if ((tag != null) && (tag.length() > 0)) {
			for (final Attribute attribute : item.getAttribute()) {
				if (attribute.getTag().equals(tag)) {
					return attribute.getValue();
				}
			}
		}
		throw new NoSuchElementException();
	}
}
