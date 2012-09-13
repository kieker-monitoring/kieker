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
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeRelationship;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.CompilationUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Datatype;
import org.eclipse.gmt.modisco.omg.kdm.code.Extends;
import org.eclipse.gmt.modisco.omg.kdm.code.Implements;
import org.eclipse.gmt.modisco.omg.kdm.code.Imports;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Namespace;
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
	private final boolean isPackageOrNamespace;
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

		this.isPackageOrNamespace = true;
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
		// Keep C#-specials in mind
		this.tryGetParentCompilationUnit(classUnit);
		this.isPackageOrNamespace = false;
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
		// Keep C#-specials in mind
		this.tryGetParentCompilationUnit(interfaceUnit);
		this.isPackageOrNamespace = false;
	}

	/**
	 * Creates a new instance of this class from the given {@link Namespace}.
	 * 
	 * @param namespaze
	 *            The {@link Namespace} to get the dependencies from.
	 * @throws NullPointerException
	 *             If the grouped code list of the {@link Namespace} is null.
	 */
	public DependencyIterator(final Namespace namespaze) throws NullPointerException {
		super(namespaze.getGroupedCode());

		this.isPackageOrNamespace = true;
	}

	/**
	 * This method checks whether there is another element or not.
	 * 
	 * @return
	 *         Returns true if the iterator has more elements, otherwise false. You have to call it, otherwise next() will fail!
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
					// If we are in a C#-model, we must keep the CompilationUnit in mind
					this.tryGetParentCompilationUnit(classUnit);
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
					// If we are in a C#-model, we must keep the CompilationUnit in mind
					this.tryGetParentCompilationUnit(interfaceUnit);
					// Check the new iterator
					this.stepBack();
					cond = true;
				} else if ((this.currentElement instanceof Imports) || (this.currentElement instanceof Implements) || (this.currentElement instanceof Extends)) {
					// If it not a package or namespace just return true
					if (!this.isPackageOrNamespace) {
						return true;
					} else { // If it is a package or namespace we must use every target only once.
						final DependencyDescription description;
						final CodeItem destination;
						if (this.currentElement instanceof Imports) {
							final Imports imports = (Imports) this.currentElement;
							description = new DependencyDescription(imports);
							destination = imports.getTo();
						} else if (this.currentElement instanceof Implements) {
							final Implements implement = (Implements) this.currentElement;
							description = new DependencyDescription(implement);
							destination = implement.getTo();
						} else {
							final Extends extend = (Extends) this.currentElement;
							description = new DependencyDescription(extend);
							destination = extend.getTo();
						}
						String identifier;
						// If the target is a package we must user the full name
						if ((destination instanceof Package) || (destination instanceof Namespace)) {
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
	 * This method tries to find a parent element of type {@link CompilationUnit}. In C# all the {@link Imports}-dependencies are stored in it.
	 * 
	 * @param type
	 *            The element to check the ancestors.
	 */
	private void tryGetParentCompilationUnit(final Datatype type) {
		EObject parent = type.eContainer();
		while (parent != null) {
			if (parent instanceof CodeModel) {
				break; // Stop at the CodeModel
			} else if (parent instanceof CompilationUnit) { // We have found the target
				final CompilationUnit cUnit = (CompilationUnit) parent;
				final Iterator<AbstractCodeRelationship> relationIterator = cUnit.getCodeRelation().iterator();
				this.iteratorStack.push(relationIterator);
				this.currentIterator = relationIterator;
				break;
			} else if (parent instanceof Package) {
				break; // We can stop here, because we are in a Java model
			} else {
				parent = parent.eContainer(); // Use the next parent
			}
		}
	}

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
		this.currentElement = null; // NOPMD (null)
		return description;
	}
}
