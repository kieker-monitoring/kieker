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

package kieker.tools.kdm.manager.util.descriptions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.Extends;
import org.eclipse.gmt.modisco.omg.kdm.code.Implements;
import org.eclipse.gmt.modisco.omg.kdm.code.Imports;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Namespace;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;

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
		String pName;
		try {
			pName = this.getValueFromAttribute(item, "FullyQualifiedName");
			if (pName.endsWith(this.name)) {
				final int index = pName.length() - this.name.length();
				pName = pName.substring(0, index);
			}
		} catch (final NoSuchElementException ex) {
			pName = KDMModelManager.reassembleFullParentName(item);
		}
		final StringBuilder parentName = new StringBuilder();
		parentName.append(pName);
		// Ensure it doesn't end with a dot
		if (parentName.toString().endsWith(".")) {
			parentName.deleteCharAt(parentName.length() - 1);
		}
		this.fullParentName = this.removeGlobalPrefix(parentName.toString());
		// Set the name of the parent package or namespace
		if (item instanceof Namespace) {
			this.parentPackageName = this.fullParentName;
		} else {
			String ppName = KDMModelManager.reassembleParentPackageName(item);
			if ("".equals(ppName) && !"".equals(this.fullParentName)) {
				ppName = this.tryAssembleParentNamespace(item);
				ppName = this.removeGlobalPrefix(ppName);
			}
			this.parentPackageName = ppName;
		}
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
	 * This method tries to remove the 'global.'-prefix from the given value which used in C#-models.
	 * 
	 * @param value
	 *            The value to remove the prefix.
	 * @return
	 *         The value without the prefix or just the value.
	 */
	private String removeGlobalPrefix(final String value) {
		String result = "";
		final String prefix = "global.";
		if (!"".equals(value)) {
			if (value.startsWith(prefix)) {
				result = value.substring(prefix.length());
			} else {
				result = value;
			}
		}
		return result;
	}

	/**
	 * This method tries to reassemble the name of the parent {@link Namespace} of the given item.
	 * 
	 * @param item
	 *            The {@link CodeItem} to reassemble the parent namespace.
	 * @return
	 *         The parent {@link Namespace} name of the given {@link CodeItem}.
	 */
	private String tryAssembleParentNamespace(final CodeItem item) {
		final StringBuilder nonNamespacePart = new StringBuilder();
		final List<String> parts = new LinkedList<String>();
		parts.add(item.getName());
		// Get first container
		EObject parentElement = item.eContainer();
		String fullyQualifiedName;

		// Try to get the full name from the attribute
		try {
			fullyQualifiedName = this.getValueFromAttribute(item, "FullyQualifiedName");
		} catch (final NoSuchElementException e) {
			// Without the fully qualified name we cannot assemble the parent namespace name.
			return "";
		}

		// As long as there is an other package, class or interface container we must go on
		while (parentElement != null) {
			if (parentElement instanceof ClassUnit) {
				final ClassUnit classUnit = (ClassUnit) parentElement;
				parts.add(classUnit.getName());
				parentElement = parentElement.eContainer();
			} else if (parentElement instanceof InterfaceUnit) {
				final InterfaceUnit interfaceUnit = (InterfaceUnit) parentElement;
				parts.add(interfaceUnit.getName());
				parentElement = parentElement.eContainer();
			} else {
				break;
			}
		}

		// Keep the wrong order in mind
		Collections.reverse(parts);
		// Assemble name
		for (final String part : parts) {
			nonNamespacePart.append(part).append('.');
		}
		// Remove the last dot
		nonNamespacePart.deleteCharAt(nonNamespacePart.length() - 1);

		final int nnPartLength = nonNamespacePart.length();
		String namespaceName;
		if (fullyQualifiedName.length() > nnPartLength) {
			namespaceName = fullyQualifiedName.substring(0, fullyQualifiedName.length() - nnPartLength - 1);
		} else {
			namespaceName = "";
		}
		return namespaceName;
	}

	/**
	 * This method searches for an attribute where the tag equals the 'tag'-parameter and return the value.
	 * 
	 * @param item
	 *            The item which should contains the attribute.
	 * @param tag
	 *            The tag of the attribute.
	 * @return
	 *         The value of the attribute.
	 * @throws NoSuchElementException
	 *             If the attribute does not exist.
	 */
	private String getValueFromAttribute(final CodeItem item, final String tag) throws NoSuchElementException {
		for (final Attribute attribute : item.getAttribute()) {
			if (tag.equals(attribute.getTag())) {
				return attribute.getValue();
			}
		}

		throw new NoSuchElementException();
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
		} else if (item instanceof Namespace) {
			this.elementType = ElementType.NAMESPACE;
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
