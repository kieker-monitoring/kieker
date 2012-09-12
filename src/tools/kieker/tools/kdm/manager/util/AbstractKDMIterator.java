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

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.gmt.modisco.omg.kdm.core.ModelElement;

/**
 * An abstract class representing the base of all other KDM element iterators within this project.
 * 
 * @param <T>
 *            The type of the iterator.
 * 
 * @author Benjamin Harms
 */
public abstract class AbstractKDMIterator<T> implements Iterator<T> {
	/**
	 * The list of elements to iterate on, passed by constructor.
	 */
	protected final List<? extends ModelElement> codeElementList;
	/**
	 * The current iterator is always used to check whether there exists another element or not and to get that element.
	 */
	protected Iterator<? extends ModelElement> currentIterator;
	/**
	 * This stack manages the iterators you need to run trough the tree-structure.
	 */
	protected final Stack<Iterator<? extends ModelElement>> iteratorStack = new Stack<Iterator<? extends ModelElement>>();
	/**
	 * This stack manages the full name of the current element. Important to get the right name within the tree-structure.
	 */
	protected final Stack<String> nameStak = new Stack<String>();
	/**
	 * The current element is the next element to work on with {@link #next()}.
	 */
	protected ModelElement currentElement;
	/**
	 * If true, the implementation of the iterator should not <b>only</b> search within the actual element, but also within nested elements.
	 */
	protected boolean performDepthFirstSearch = false;

	/**
	 * Creates a new instance of this class from EList of AbstractCodeElements.
	 * 
	 * @param elementList
	 *            The list to iterate on.
	 * @throws NullPointerException
	 *             If the list is null.
	 */
	protected AbstractKDMIterator(final List<? extends ModelElement> elementList) throws NullPointerException {
		if (elementList == null) {
			throw new NullPointerException("The element list must not be null!");
		}

		this.codeElementList = elementList;
		this.currentIterator = this.codeElementList.iterator();
		this.iteratorStack.push(this.currentIterator);
	}

	/**
	 * Steps back to the last iterator if possible AND necessary.
	 */
	protected final void stepBack() {
		// necessary && possible?
		while (!this.currentIterator.hasNext() && !this.iteratorStack.isEmpty()) {
			this.iteratorStack.pop();
			if (!this.nameStak.isEmpty()) {
				this.nameStak.pop();
			}
			if (!this.iteratorStack.isEmpty()) {
				this.currentIterator = this.iteratorStack.peek();
			}
		}
	}

	/**
	 * This method returns the last element of the nameStack concatenated with a dot
	 * or an empty String, if the nameStack is empty.
	 * 
	 * @return The last name.
	 */
	protected final String getLastName() {
		String name = "";

		if (!this.nameStak.isEmpty()) {
			name = this.nameStak.peek() + ".";
		}

		return name;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
