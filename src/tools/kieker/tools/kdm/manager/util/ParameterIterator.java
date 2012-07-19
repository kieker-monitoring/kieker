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
import java.util.NoSuchElementException;

import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterKind;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;

import kieker.tools.kdm.manager.util.descriptions.ParameterDescription;

/**
 * This class provides an iterator for all parameter of the given {@link MethodUnit} or {@link Signature}.
 * 
 * @author Benjamin Harms
 * @version 1.0
 * 
 */
public class ParameterIterator extends AbstractKDMIterator<ParameterDescription> {
	/**
	 * Creates a new instance of this class from a Signature.
	 * 
	 * @param signature
	 *            The signature of the method containing the list of {@link ParameterUnit} to iterate on.
	 * @throws NullPointerException
	 *             If list of ParameterUnits is empty.
	 */
	public ParameterIterator(final Signature signature) throws NullPointerException {
		super(signature.getParameterUnit());
	}

	/**
	 * Creates a new instance of this class from a MethodUnit.
	 * 
	 * @param method
	 *            The method to iterate on.
	 * @throws NullPointerException
	 *             If the method is null.
	 */
	public ParameterIterator(final MethodUnit method) throws NullPointerException {
		super(method.getCodeElement());
	}

	/**
	 * Returns true if the iterator has more elements, otherwise false.
	 * 
	 * @return
	 *         True if the iterator has more elements.
	 */
	@Override
	public boolean hasNext() {
		// If a current element exist there is one
		if (this.currentElement != null) {
			return true;
		}
		// Ensure there are more elements
		if (!this.currentIterator.hasNext()) {
			return false;
		} else {
			boolean cond;
			do {
				this.currentElement = this.currentIterator.next();
				// Instance of Signature? ==> there may be more elements
				if (this.currentElement instanceof Signature) {
					final Signature signature = (Signature) this.currentElement;
					// Ensure the right number of elements on the name stack
					this.nameStak.push(signature.getName());
					// Refresh iterator
					final Iterator<ParameterUnit> iterator = signature.getParameterUnit().iterator();
					this.iteratorStack.push(iterator);
					this.currentIterator = iterator;
					// Check again
					this.stepBack();
					cond = true;
					// Instance of ParameterUnit? ==> there are more elements
				} else if (this.currentElement instanceof ParameterUnit) {
					final ParameterUnit parameterUnit = (ParameterUnit) this.currentElement;
					// Ignore the return parameter.
					if (ParameterKind.RETURN.equals(parameterUnit.getKind())) {
						cond = true;
					} else {
						// Found another element
						cond = false;
						return true;
					}
				} else {
					// Else try again
					cond = true;
					// Reset current element!!
					this.currentElement = null; // NOPMD (ensure this element is not used again)
				}
			} while (cond && this.currentIterator.hasNext());
		}

		return this.currentIterator.hasNext();
	}

	/**
	 * Returns the next class name. Call next() <b>only</b> after a previous call of {@link #hasNext()}!
	 * 
	 * @return
	 *         Returns the next element.
	 * @throws NoSuchElementException
	 *             If no other element exists.
	 * @see #hasNext()
	 */
	@Override
	public ParameterDescription next() throws NoSuchElementException {
		if (this.currentElement == null) {
			throw new NoSuchElementException("No more elements");
		}

		final ParameterUnit parameterUnit = (ParameterUnit) this.currentElement;
		final ParameterDescription description = new ParameterDescription(parameterUnit);

		// Reset current element!!
		this.currentElement = null; // NOPMD (ensure this element is not used again)

		return description;
	}
}
