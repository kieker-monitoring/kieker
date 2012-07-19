/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
import java.util.NoSuchElementException;

import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;

/**
 * This class provides an iterator for all class names in a given package. This class is <b>not</b>
 * thread safe, so while iterating changes of the model can lead to unexpected behavior.
 * 
 * @author Benjamin Harms
 * 
 * @version 1.0
 * 
 */
public final class ClassNameIterator extends AbstractKDMIterator<String> {
	/**
	 * Creates a new instance of this class from a List of AbstractCodeElements.
	 * 
	 * @param elementList
	 *            The list to iterate on.
	 * @throws NullPointerException
	 *             If the list is null.
	 */
	public ClassNameIterator(final List<? extends AbstractCodeElement> elementList) throws NullPointerException {
		super(elementList);
	}

	/**
	 * Creates a new instance of this class from a Package and the full name of the package.
	 * 
	 * @param pack
	 *            The package to iterate on.
	 * @param fullPackageName
	 *            The full name of the package to iterate on.
	 * @throws NullPointerException
	 *             If the package is empty.
	 */
	public ClassNameIterator(final Package pack, final String fullPackageName) throws NullPointerException {
		super(pack.getCodeElement());

		this.nameStak.push(fullPackageName);
	}

	/**
	 * Creates a new instance of this class from a Package and the full name of the package.
	 * 
	 * @param pack
	 *            The package to iterate on.
	 * @param fullPackageName
	 *            The full name of the package to iterate on.
	 * @param depthFirstSearch
	 *            If true, the iterator performs depth-first search and tries to get more information by iterating nested packages, classes and interfaces. This may
	 *            result in duplicates in the output.
	 * @throws NullPointerException
	 *             If the package is empty.
	 */
	public ClassNameIterator(final Package pack, final String fullPackageName, final boolean depthFirstSearch) throws NullPointerException {
		this(pack, fullPackageName);

		this.performDepthFirstSearch = depthFirstSearch;
	}

	/**
	 * This method checks whether there is another element or not.
	 * 
	 * @return
	 *         Returns true if the iterator has more elements, otherwise false.
	 */
	@Override
	public boolean hasNext() { // NOCS (JavaNCSSCheck, CyclomaticComplexityCheck)
		// If a current element exist there is one
		if (this.currentElement != null) {
			return true;
		}
		// Go back if necessary
		this.stepBack();
		// Ensure there are more elements
		if (!this.currentIterator.hasNext()) {
			return false;
		} else {
			boolean cond;
			do {
				this.currentElement = this.currentIterator.next();
				// Instance of Package? ==> there may be more elements
				if ((this.currentElement instanceof Package) && this.performDepthFirstSearch) {
					final Package pack = (Package) this.currentElement;
					final StringBuilder parentName = new StringBuilder(this.getLastName());
					// Assemble name
					parentName.append(pack.getName());
					// Refresh
					this.nameStak.push(parentName.toString());
					final Iterator<AbstractCodeElement> iterator = pack.getCodeElement().iterator();
					this.iteratorStack.push(iterator);
					this.currentIterator = iterator;
					// Check new iterator
					this.stepBack();
					cond = true;
					// Instance of InterfaceUnit? ==> there may be more elements
				} else if ((this.currentElement instanceof InterfaceUnit) && this.performDepthFirstSearch) {
					final InterfaceUnit interfaze = (InterfaceUnit) this.currentElement;
					// Assemble name
					final StringBuilder parentName = new StringBuilder(this.getLastName());
					parentName.append(interfaze.getName());
					// Refresh
					this.nameStak.push(parentName.toString());
					final Iterator<CodeItem> iterator = interfaze.getCodeElement().iterator();
					this.iteratorStack.push(iterator);
					this.currentIterator = iterator;
					// Check new iterator
					this.stepBack();
					cond = true;
					// Instance of ClassUnit? ==> there are more elements
				} else if (this.currentElement instanceof ClassUnit) {
					// Found another element
					cond = false;
					return true;
				} else {
					// Else try again
					cond = true;
					// Reset current element!!
					this.currentElement = null; // NOPMD (ensure this element is not used again)
					this.stepBack();
				}
			} while (cond && this.currentIterator.hasNext() && !this.iteratorStack.isEmpty());
		}

		return this.currentIterator.hasNext();
	}

	@Override
	/**
	 * Returns the next class name. Call next() <b>only</b> after a previous call of {@link #hasNext()}!
	 * 
	 * @return
	 *         Returns the next class name.
	 * @throws NoSuchElemetException
	 *             if no other element exists.
	 * @see #hasNext()
	 */
	public String next() throws NoSuchElementException {
		if (this.currentElement == null) {
			throw new NoSuchElementException("No more elements");
		}

		final ClassUnit clazz = (ClassUnit) this.currentElement;

		// Assemble name
		final StringBuilder parentName = new StringBuilder(this.getLastName());
		parentName.append(clazz.getName());

		// perform depth-first search?
		if (this.performDepthFirstSearch) {
			this.nameStak.push(parentName.toString());
			// refresh iterator
			final Iterator<CodeItem> iterator = clazz.getCodeElement().iterator();
			this.iteratorStack.push(iterator);
			this.currentIterator = iterator;
		}
		// Reset current element!!
		this.currentElement = null; // NOPMD (ensure this element is not used again)

		return parentName.toString();
	}
}
