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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeRelationship;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;

/**
 * This class provides an iterator for all method calls from the given {@link MethodUnit}.
 * 
 * @author Benjamin Harms
 */
public class MethodCallIterator extends AbstractKDMIterator<String> {
	/**
	 * Creates a new instance of this class from a MethodUnit.
	 * 
	 * @param method
	 *            The method containing some call relations.
	 * @throws NullPointerException
	 *             If the list of relations is null;
	 */
	public MethodCallIterator(final MethodUnit method) throws NullPointerException {
		super(method.getCodeRelation());
	}

	/**
	 * Returns true if the iterator has more elements.
	 * 
	 * @return
	 *         True if the iterator has more elements.
	 */
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
				// Instance of CodeRelationship? ==> there are more elements
				if (this.currentElement instanceof CodeRelationship) {
					final CodeRelationship codeRelationship = (CodeRelationship) this.currentElement;
					// Ensure it is a relation between to MethodUnits
					final CodeItem fromItem = codeRelationship.getFrom();
					final KDMEntity toItem = codeRelationship.getTo();
					if ((fromItem instanceof MethodUnit) && (toItem instanceof MethodUnit)) {
						// Found another element
						cond = false;
						return true;
					} else {
						cond = true;
					}
				} else {
					// Else try again
					cond = true;
					// Reset current element!!
					this.currentElement = null;
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
	 * 
	 * @throws NoSuchElementException
	 *             If no other element exists.
	 * @see #hasNext()
	 */
	public String next() throws NoSuchElementException {
		if (this.currentElement == null) {
			throw new NoSuchElementException("No more elements");
		}

		final CodeRelationship codeRelationship = (CodeRelationship) this.currentElement;

		// Get the methods
		final MethodUnit fromMethod = (MethodUnit) codeRelationship.getFrom();
		final MethodUnit toMethod = (MethodUnit) codeRelationship.getTo();
		final String fromParentName = this.reassembleFullParentName(fromMethod);
		final String toParentName = this.reassembleFullParentName(toMethod);

		// Assemble relationship description
		final StringBuilder relationship = new StringBuilder();
		relationship.append(fromParentName);
		relationship.append(fromMethod.getName());
		relationship.append(" ");
		relationship.append(toParentName);
		relationship.append(toMethod.getName());

		// Reset current element!!
		this.currentElement = null;

		return relationship.toString();
	}

	/**
	 * This method reassembles the full parent name of a method by running through the parent elements and collecting the names.
	 * 
	 * @param methodUnit
	 *            The method to reassemble the the of.
	 * @return
	 *         The full parent name.
	 */
	private String reassembleFullParentName(final MethodUnit methodUnit) {
		final StringBuilder fullName = new StringBuilder();
		final List<String> parts = new LinkedList<String>();
		// Get first container
		EObject currentElement = methodUnit.eContainer();

		// As long as there is an other package, class or interface container we must go on
		while (currentElement != null) {
			if (currentElement instanceof Package) {
				final Package pack = (Package) currentElement;
				// Add the name
				parts.add(pack.getName());
				// Get the next container
				currentElement = pack.eContainer();
			} else if (currentElement instanceof ClassUnit) {
				final ClassUnit clazz = (ClassUnit) currentElement;
				// Add the name
				parts.add(clazz.getName());
				// Get the next container
				currentElement = clazz.eContainer();
			} else if (currentElement instanceof InterfaceUnit) {
				final InterfaceUnit interfaze = (InterfaceUnit) currentElement;
				// Add the name
				parts.add(interfaze.getName());
				// Get the next container
				currentElement = interfaze.eContainer();
			} else {
				currentElement = null;
			}
		}
		// Keep the wrong order in mind
		Collections.reverse(parts);
		// Assemble name
		for (final String part : parts) {
			fullName.append(part).append('.');
		}

		return fullName.toString();
	}
}
