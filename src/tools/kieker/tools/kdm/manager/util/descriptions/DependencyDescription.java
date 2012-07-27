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

package kieker.tools.kdm.manager.util.descriptions;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.Extends;
import org.eclipse.gmt.modisco.omg.kdm.code.Implements;
import org.eclipse.gmt.modisco.omg.kdm.code.Imports;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.util.DependencyType;
import kieker.tools.kdm.manager.util.ElementType;

/**
 * This class provides a description of a dependency element like an {@link Imports} element, an {@link Implements} element or an {@link Extends} element.
 * 
 * @author Benjamin Harms
 * 
 */
public class DependencyDescription {
	/**
	 * Describes the type of the element.
	 */
	private ElementType elementType;
	/**
	 * Describes the type of the dependency.
	 */
	private DependencyType dependencyType;
	/**
	 * The name of the element.
	 */
	private final String name;
	/**
	 * The full name of the parent element.
	 */
	private final String fullParentName;
	/**
	 * The full name of the parent package. May be different from the fullParentName.
	 */
	private final String parentPackageName;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param item
	 *            The item to be described.
	 */
	private DependencyDescription(final CodeItem item) {
		// Set the type
		this.setElementType(item);
		// Set the name
		this.name = item.getName();
		// Assemble the full parent name
		final String pName = KDMModelManager.reassembleFullParentName(item);
		final StringBuilder parentName = new StringBuilder();
		parentName.append(pName);
		// Ensure it doesn't end with a dot
		if (parentName.toString().endsWith(".")) {
			parentName.deleteCharAt(parentName.length() - 1);
		}
		this.fullParentName = parentName.toString();
		// Set the name of the parent package
		this.parentPackageName = KDMModelManager.reassembleParentPackageName(item);
	}

	/**
	 * Creates a new instance of this class from the given {@link Imports} element.
	 * 
	 * @param importElement
	 *            The {@link Imports} element to describe.
	 */
	public DependencyDescription(final Imports importElement) {
		this(importElement.getTo());

		// Set the dependency type
		this.dependencyType = DependencyType.IMPORTS;
	}

	/**
	 * Creates a new instance of this class from the given {@link Implements} element.
	 * 
	 * @param implementsElement
	 *            The {@link Implements} element to describe.
	 */
	public DependencyDescription(final Implements implementsElement) {
		this(implementsElement.getTo());

		// Set the dependency type
		this.dependencyType = DependencyType.IMPLEMENTS;
	}

	/**
	 * Creates a new instance of this class from the given {@link Extends} element.
	 * 
	 * @param extendsElement
	 *            The {@link Extends} element to describe.
	 */
	public DependencyDescription(final Extends extendsElement) {
		this(extendsElement.getTo());

		// Set the dependency type
		this.dependencyType = DependencyType.EXTENDS;
	}

	/**
	 * This method tries to determine the type of the {@link CodeItem}.
	 * 
	 * @param item
	 *            The {@link CodeItem} to determine the type.
	 */
	private void setElementType(final CodeItem item) {
		// Check the type of the destination element
		if (item instanceof Package) {
			this.elementType = ElementType.PACKAGE;
		} else if (item instanceof ClassUnit) {
			this.elementType = ElementType.CLASS;
		} else if (item instanceof InterfaceUnit) {
			this.elementType = ElementType.INTERFACE;
		} else {
			this.elementType = ElementType.UNKNOWN;
		}
	}

	/**
	 * This method returns the name of the dependency element.
	 * 
	 * @return
	 *         The name of the dependency element.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method returns the full name of the dependency element.
	 * 
	 * @return
	 *         The full name of the dependency element.
	 */
	public String getFullName() {
		final StringBuilder fullName = new StringBuilder();
		// Assemble full name
		if (this.fullParentName.length() > 0) {
			fullName.append(this.fullParentName);
			if (!this.fullParentName.endsWith(".")) {
				fullName.append('.');
			}
		}
		fullName.append(this.name);

		return fullName.toString();
	}

	/**
	 * This method returns the full name of the parent element.
	 * 
	 * @return
	 *         The full name of the parent element.
	 * @see #getParentPackageName()
	 */
	public String getParentName() {
		return this.fullParentName;
	}

	/**
	 * This method returns the full name of the parent element.
	 * 
	 * @return
	 *         The full name of the parent element.
	 * @deprecated
	 *             Use {@link #getParentName()} instead.
	 */
	@Deprecated
	public String getFullParentName() {
		return this.fullParentName;
	}

	/**
	 * This method returns the full name of the parent package. It may be different from the the parent name.
	 * 
	 * @return
	 *         Returns the full name of the parent package.
	 * @see #getParentName()
	 */
	public String getParentPackageName() {
		return this.parentPackageName;
	}

	/**
	 * This method returns the {@link ElementType} of the dependency element.
	 * 
	 * @return
	 *         The {@link ElementType} of the dependency element.
	 */
	public ElementType getElementType() {
		return this.elementType;
	}

	/**
	 * This method returns the {@link DependencyType} for this dependency.
	 * 
	 * @return
	 *         The {@link DependencyType} for this dependency.
	 */
	public DependencyType getDependencyType() {
		return this.dependencyType;
	}

	/**
	 * This method creates a string representation of this instance.
	 * 
	 * @return
	 *         Returns a string representation of this instance.
	 */
	@Override
	public String toString() {
		final StringBuilder line = new StringBuilder();
		line.append(this.dependencyType).append(' ');
		line.append(this.elementType).append(' ');
		line.append(this.getFullName());

		return line.toString();
	}
}
