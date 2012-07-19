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

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeRelationship;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.Extends;
import org.eclipse.gmt.modisco.omg.kdm.code.Implements;
import org.eclipse.gmt.modisco.omg.kdm.code.Imports;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;

import kieker.tools.kdm.manager.util.descriptions.DependencyDescription;

/**
 * This class provides an iterator for all dependencies for the given {@link ClassUnit} or {@link InterfaceUnit}. This can be {@link Imports}-, {@link Implements}-
 * or {@link Extends}-elements. It also provides dependencies for a given {@link Package}. In this case only {@link Imports}-elements are considered.
 * 
 * @author Benjamin Harms
 * 
 */
public class DependencyIterator extends AbstractKDMIterator<DependencyDescription> {
	/**
	 * Contains the information whether the instance was created with a given package, or not. This is necessary to get only interesting dependencies.
	 */
	private final boolean isPackage;
	/**
	 * If this instance was created from a {@link Package} the set is used to avoid duplicate dependencies.
	 */
	private final Set<String> descriptionSet = new HashSet<String>();

	/**
	 * Creates a new instance of this class from the given {@link Package}.
	 * 
	 * @param pack
	 *            The {@link Package} to get the dependencies.
	 * @throws NullPointerException
	 *             If the package is null.
	 */
	public DependencyIterator(final Package pack) throws NullPointerException {
		super(pack.getCodeElement());

		this.isPackage = true;
	}

	/**
	 * Creates a new instance of this class from the given {@link ClassUnit}.
	 * 
	 * @param classUnit
	 *            The {@link ClassUnit} to get the dependencies from.
	 * @throws NullPointerException
	 *             If the element list of the {@link ClassUnit} is null.
	 */
	public DependencyIterator(final ClassUnit classUnit) throws NullPointerException {
		super(classUnit.getCodeRelation());

		this.isPackage = false;
	}

	/**
	 * Creates a new instance of this class from the given {@link InterfaceUnit}.
	 * 
	 * @param interfaceUnit
	 *            The {@link InterfaceUnit} to get the dependencies from.
	 * @throws NullPointerException
	 *             If the element list of the {@link InterfaceUnit} is null.
	 */
	public DependencyIterator(final InterfaceUnit interfaceUnit) throws NullPointerException {
		super(interfaceUnit.getCodeRelation());

		this.isPackage = false;
	}

	/**
	 * This method checks whether there is another element or not.
	 * 
	 * @return
	 *         Returns true if the iterator has more elements, otherwise false. You have to call it, otherwise next() will fail!
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
				// If we work on the CodeElement-list from a package, we must keep classes and interfaces in mind.
				if (this.currentElement instanceof ClassUnit) {
					final ClassUnit classUnit = (ClassUnit) this.currentElement;
					// Get the dependencies
					final Iterator<AbstractCodeRelationship> depIterator = classUnit.getCodeRelation().iterator();
					this.iteratorStack.push(depIterator);
					// Keep nested classes and interfaces in mind. In KDM there may be more relations theoretically.
					final Iterator<CodeItem> childIterator = classUnit.getCodeElement().iterator();
					this.iteratorStack.push(childIterator);
					this.currentIterator = childIterator;
					// Check the new iterator
					this.stepBack();
					cond = true;
				} else if (this.currentElement instanceof InterfaceUnit) {
					final InterfaceUnit interfaceUnit = (InterfaceUnit) this.currentElement;
					// Get the dependencies
					final Iterator<AbstractCodeRelationship> depIterator = interfaceUnit.getCodeRelation().iterator();
					this.iteratorStack.push(depIterator);
					// Keep nested classes and interfaces in mind
					final Iterator<CodeItem> childIterator = interfaceUnit.getCodeElement().iterator();
					this.iteratorStack.push(childIterator);
					this.currentIterator = childIterator;
					// Check the new iterator
					this.stepBack();
					cond = true;
				} else if ((this.currentElement instanceof Imports) && this.isPackage) {
					final Imports imports = (Imports) this.currentElement;
					final DependencyDescription description = new DependencyDescription(imports);
					final CodeItem destination = imports.getTo();
					String identifier;
					// If the target is a package we must user the full name
					if (destination instanceof Package) {
						identifier = description.getFullName();
					} else { // Else we must use the name of the parent package
						identifier = description.getParentPackageName();
					}
					// Check whether the destination is already known
					if (this.descriptionSet.add(identifier)) {
						return true;
					} else {
						cond = true;
					}
				} else if (((this.currentElement instanceof Imports) || (this.currentElement instanceof Implements) || (this.currentElement instanceof Extends))
						&& !this.isPackage) {
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
	 * Returns the next dependency. Call next() <b>only</b> after a previous call of {@link #hasNext()}!
	 * 
	 * @return
	 *         Returns the next dependency.
	 * @throws NoSuchElemetException
	 *             if no other element exists.
	 * @see #hasNaxt()
	 */
	public DependencyDescription next() throws NoSuchElementException {
		if (this.currentElement == null) {
			throw new NoSuchElementException("No more elements");
		}

		// Create new description object...
		DependencyDescription description = null;
		// On the right way
		if (this.currentElement instanceof Imports) {
			final Imports imports = (Imports) this.currentElement;
			description = new DependencyDescription(imports);
		} else if (this.currentElement instanceof Implements) {
			final Implements impl = (Implements) this.currentElement;
			description = new DependencyDescription(impl);
		} else if (this.currentElement instanceof Extends) {
			final Extends ext = (Extends) this.currentElement;
			description = new DependencyDescription(ext);
		}
		// Reset current element!!
		this.currentElement = null; // NOPMD (ensure this element is not used again)

		return description;
	}
}
