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

import java.util.NoSuchElementException;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;

import kieker.tools.kdm.manager.util.descriptions.AttributeDescription;

/**
 * This class provides an iterator for all attributes of the given {@link ClassUnit} or {@link InterfaceUnit}.
 * 
 * @author Benjamin Harms
 * 
 */
public class AttributeIterator extends AbstractKDMIterator<AttributeDescription> {
	/**
	 * Creates a new instance of this class from the given {@link ClassUnit}.
	 * 
	 * @param classUnit
	 *            The {@link ClassUnit} to which contains the attributes.
	 */
	public AttributeIterator(final ClassUnit classUnit) {
		super(classUnit.getCodeElement());
	}

	/**
	 * Creates a new instance of this class from the given {@link InterfaceUnit}.
	 * 
	 * @param interfaceUnit
	 *            The {@link InterfaceUnit} which contains the attributes.
	 */
	public AttributeIterator(final InterfaceUnit interfaceUnit) {
		super(interfaceUnit.getCodeElement());
	}

	/**
	 * Returns true if the iterator has more elements.
	 * 
	 * @return
	 *         True if the iterator has more elements.
	 */
	// @Override
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
			boolean cond = false;
			do {
				this.currentElement = this.currentIterator.next();
				// Instance of StoreableUnit? ==> there are more elements
				if (this.currentElement instanceof StorableUnit) {
					// Found another element
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

	/**
	 * Returns the description of the next attribute. Call next() <b>only</b> after a previous call of {@link #hasNext()}!
	 * 
	 * @return
	 *         Returns the description of the next attribute.
	 * 
	 * @throws NoSuchElementException
	 *             If no other element exists.
	 * @see #hasNext()
	 */
	// @Override
	public AttributeDescription next() throws NoSuchElementException {
		if (this.currentElement == null) {
			throw new NoSuchElementException("No more elements.");
		}

		final StorableUnit attribute = (StorableUnit) this.currentElement;

		this.currentElement = null; // NOPMD (ensure this element is not used again)

		return new AttributeDescription(attribute);
	}
}
