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

import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.util.descriptions.MethodDescription;

/**
 * This class provides an iterator for all methods within the given {@link ClassUnit}, {@link InterfaceUnit} or list of {@link AbstractCodeElement}.
 * 
 * @author Benjamin Harms
 */
public final class MethodNameIterator extends AbstractKDMIterator<MethodDescription> {
	/**
	 * Creates a new instance of this class from a List of AbstractCodeElement.
	 * 
	 * @param elementList
	 *            The list to iterate on.
	 * @throws NullPointerException
	 *             If the list is null.
	 */
	public MethodNameIterator(final List<? extends AbstractCodeElement> elementList) throws NullPointerException {
		super(elementList);
	}

	/**
	 * Creates a new instance of this class from ClassUnit and the full name of the class.
	 * 
	 * @param clazz
	 *            The Package to iterate on.
	 * @param fullClassName
	 *            The full name of the package to iterate on.
	 * @throws NullPointerException
	 *             If the element list of the class is null.
	 */
	public MethodNameIterator(final ClassUnit clazz, final String fullClassName) throws NullPointerException {
		super(clazz.getCodeElement());

		this.nameStak.push(fullClassName);
	}

	/**
	 * Creates a new instance of this class from InterfaceUnit and the full name of the Interface.
	 * 
	 * @param interfaze
	 *            The interface to iterate on.
	 * @param fullInterfaceName
	 *            The name of the Interface to iterate on.
	 * @throws NullPointerException
	 *             If the element list of the interface is null.
	 */
	public MethodNameIterator(final InterfaceUnit interfaze, final String fullInterfaceName) throws NullPointerException {
		super(interfaze.getCodeElement());

		this.nameStak.push(fullInterfaceName);
	}

	/**
	 * Returns true if the iterator has more elements, otherwise false.
	 */
	public boolean hasNext() {
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
				if (this.currentElement instanceof MethodUnit) {
					cond = false;
					return true;
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
	 * Returns the next method description. Call next() <b>only</b> after a previous call of {@link #hasNext()}!
	 * 
	 * @returns
	 *          The next method description.
	 * @throws NoSuchElemetException
	 *             if no other element exists.
	 * @see #hasNaxt()
	 */
	public MethodDescription next() throws NoSuchElementException {
		if (this.currentElement == null) {
			throw new NoSuchElementException("No more elements");
		}

		final MethodUnit methodUnit = (MethodUnit) this.currentElement;
		// Assemble name
		final String qualifier = KDMModelManager.reassembleMethodQualifier(methodUnit);
		final MethodDescription description = new MethodDescription(methodUnit, qualifier);

		// Reset current element!!
		this.currentElement = null; // NOPMD (null)
		return description;
	}
}
