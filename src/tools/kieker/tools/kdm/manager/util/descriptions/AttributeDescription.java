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

import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.gmt.modisco.omg.kdm.action.ActionElement;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeRelationship;
import org.eclipse.gmt.modisco.omg.kdm.code.ArrayType;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.DataElement;
import org.eclipse.gmt.modisco.omg.kdm.code.Datatype;
import org.eclipse.gmt.modisco.omg.kdm.code.ExportKind;
import org.eclipse.gmt.modisco.omg.kdm.code.HasValue;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MemberUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.PrimitiveType;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableKind;
import org.eclipse.gmt.modisco.omg.kdm.code.StorableUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Value;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.controller.ProbeController;
import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.util.ElementType;

/**
 * This class provides a description an attribute of a class or an interface.
 * 
 * @author Benjamin Harms
 * 
 */
public class AttributeDescription {
	private static final Log LOG = LogFactory.getLog(ProbeController.class);

	/**
	 * The name of the attribute.
	 */
	private String attributeName;
	/**
	 * The full name of the type of the attribute.
	 */
	private String fullTypeName;
	/**
	 * The defaultValue of the Attribute.
	 */
	private String defaultValue;
	/**
	 * If true, the attribute has a default defaultValue.
	 */
	private boolean hasValue;
	/**
	 * If true, the type of the attribute is a primitive type (int, char ect.).
	 */
	private boolean isPrimitive;
	/**
	 * If true, the attribute is an array.
	 */
	private boolean isArray;
	/**
	 * If true, the attribute is declared as static.
	 */
	private boolean isAStatic;
	/**
	 * Describes the type of the attribute, if it is not a primitive type.
	 */
	private ElementType elementType;
	/**
	 * The visibility modifier of the attribute.
	 */
	private ExportKind visibilityModifier;

	/**
	 * Creates a new instance of this class from the given {@code StorableUnit}.
	 * 
	 * @param attribute
	 *            The {@code StorableUnit} to describe.
	 */
	public AttributeDescription(final StorableUnit attribute) {
		// Initialize
		this.initAttributeDescription(attribute);
	}

	/**
	 * Creates a new instance of this class from the given {@link MemberUnit}.
	 * 
	 * @param attribute
	 *            The {@link MemberUnit} to describe.
	 */
	public AttributeDescription(final MemberUnit attribute) {
		// Initialize
		this.initAttributeDescription(attribute);
	}

	/**
	 * This method initializes this instance from the given {@link DataElement}.
	 * 
	 * @param element
	 *            The {@link DataElement} to get all information.
	 */
	private void initAttributeDescription(final DataElement element) {
		// Initialize
		this.attributeName = element.getName();
		this.initTypeDescription(element);
		this.initVisibilityModifier(element);
		// Initialize default values only for primitive types. Only call it after initTypeDescription to ensure that isPrimitive has the right value.
		if (this.isPrimitive) {
			this.initDefaultValue(element);
		}
		// Static: Only StorableUnits can be static
		if (element instanceof StorableUnit) {
			final StorableUnit storableUnit = (StorableUnit) element;
			this.isAStatic = StorableKind.STATIC.equals(storableUnit.getKind());
		} else {
			this.isAStatic = false;
		}
	}

	/**
	 * This method initializes the type description. It checks whether it is a primitive type or an array and sets up the type name.
	 * 
	 * @param element
	 *            The {@link DataElement} to get the type information.
	 */
	private void initTypeDescription(final DataElement element) {
		try {
			// Get information about the type
			final Datatype datatype = element.getType();
			String tName = datatype.getName();
			final StringBuilder fullTName = new StringBuilder();
			// First test primitive type
			if (datatype instanceof PrimitiveType) {
				this.isArray = false;
				this.isPrimitive = true;
				this.elementType = ElementType.UNKNOWN;
			} else if (datatype instanceof ArrayType) { // Test array type
				this.isArray = true;
				final ArrayType arrayType = (ArrayType) datatype;
				// Check the type of the array elements
				final Datatype itemType = arrayType.getItemUnit().getType();
				tName = itemType.getName();
				if (itemType instanceof PrimitiveType) {
					this.isPrimitive = true;
					this.elementType = ElementType.UNKNOWN;
				} else {
					this.isPrimitive = false;
					// Let the model manager reassemble the name
					fullTName.append(KDMModelManager.reassembleFullParentName(itemType));
					// Set the element type
					this.setElementType(itemType);
				}
			} else { // Anything else like ClassUnit etc
				this.isArray = false;
				this.isPrimitive = false;
				// Set the element type
				this.setElementType(datatype);
				// If it is a C#-model we must use the 'fullyQuallifiedName'-attribute to get the full name
				try {
					final Attribute fullyQualifiedName = this.getAttribute(datatype, "FullyQualifiedName");
					String value = fullyQualifiedName.getValue();
					// Keep 'global'-prefix in mind
					final String globalPrefix = "global.";
					if (value.startsWith(globalPrefix)) {
						value = value.substring(globalPrefix.length());
					}
					fullTName.append(value);
				} catch (final NoSuchElementException e) {
					// Let the model manager reassemble the name
					fullTName.append(KDMModelManager.reassembleFullParentName(datatype));
				}
			}
			// In C#-models the 'fullyQualifiedName' contains the name
			if (!fullTName.toString().endsWith(tName)) {
				if ((fullTName.length() > 0) && !fullTName.toString().endsWith(".")) {
					fullTName.append('.');
				}
				fullTName.append(tName);
			}
			this.fullTypeName = fullTName.toString();
		} catch (final NullPointerException e) { // Some MemberUnits in Java-Models does not have a type...
			this.fullTypeName = "";
		}
	}

	/**
	 * This method tries to get information about the visibility modifier from the export attribute used by MoDisco.
	 * 
	 * @param element
	 *            The {@link DataElement} to get the visibility modifier.
	 */
	private void initVisibilityModifier(final DataElement element) {
		if (element instanceof MemberUnit) {
			final MemberUnit memberUnit = (MemberUnit) element;
			this.visibilityModifier = memberUnit.getExport();
		} else {
			try {
				// Try to get the export attribute
				final Attribute attribute = this.getAttribute(element, "export");
				final String value = attribute.getValue();
				// Split the defaultValue
				final String[] tokens = value.split("\\s");

				this.visibilityModifier = ExportKind.UNKNOWN;
				// Try to find the visibility modifier
				for (final String token : tokens) {
					if ("public".equals(token)) {
						this.visibilityModifier = ExportKind.PUBLIC;
					} else if ("private".equals(token)) {
						this.visibilityModifier = ExportKind.PRIVATE;
					} else if ("protected".equals(token)) {
						this.visibilityModifier = ExportKind.PROTECTED;
					}
				}
			} catch (final NoSuchElementException ex) {
				// Set the visibility modifier to 'UNKNOWN' if the attribute does not exist.
				this.visibilityModifier = ExportKind.UNKNOWN;
			}
		}
	}

	/**
	 * This method tries to get information about the default value.
	 * 
	 * @param element
	 *            The {@link DataElement} to get the default value from.
	 */
	private void initDefaultValue(final DataElement element) {
		this.defaultValue = "";
		try {
			final HasValue hasValueelement = this.getHasValueRelation(element.getCodeRelation());
			final AbstractCodeElement target = hasValueelement.getTo();

			if (target instanceof Value) {
				final Value value = (Value) target;
				this.defaultValue = value.getExt();
				this.hasValue = true;
			} else if (target instanceof ActionElement) {
				final ActionElement action = (ActionElement) target;
				final Value value = this.getValue(action.getCodeElement());
				this.defaultValue = value.getExt();
				this.hasValue = true;
			} else {
				this.hasValue = false;
				LOG.error("Failed to get default value for " + target.getClass());
			}
		} catch (final NoSuchElementException ex) {
			this.hasValue = false;
		}
	}

	/**
	 * This method tries to get a {@code Value} element from the given list.
	 * 
	 * @param elements
	 *            The list which should contain the {@code Value} element.
	 * @return
	 *         The {@code Value} element.
	 * @throws NoSuchElementException
	 *             If no {@code Value} element exist.
	 */
	private Value getValue(final List<AbstractCodeElement> elements) throws NoSuchElementException {
		for (final AbstractCodeElement element : elements) {
			if (element instanceof Value) {
				return (Value) element;
			}
		}

		throw new NoSuchElementException();
	}

	/**
	 * This method tries to get a {@code HasValue} element from the code relations.
	 * 
	 * @param relations
	 *            The list which should contain the {@link HasValue} element.
	 * @return
	 *         The {@code HasValue} element.
	 * @throws NoSuchElementException
	 *             If no {@code HasValue} element exist.
	 */
	private HasValue getHasValueRelation(final List<AbstractCodeRelationship> relations) throws NoSuchElementException {
		for (final AbstractCodeRelationship relation : relations) {
			if (relation instanceof HasValue) {
				return (HasValue) relation;
			}
		}

		throw new NoSuchElementException();
	}

	/**
	 * This method tries to get the attribute with the tag 'tag' from the given {@code CodeItem}.
	 * 
	 * @param element
	 *            The {@link CodeItem} to get the 'export' attribute.
	 * @param tag
	 *            The value of the tag.
	 * @return
	 *         The attribute with the name 'export'.
	 * @throws NoSuchElementException
	 *             If an attribute with the name 'export' does not exist.
	 */
	private Attribute getAttribute(final CodeItem element, final String tag) throws NoSuchElementException {
		for (final Attribute attribute : element.getAttribute()) {
			if (tag.equals(attribute.getTag())) {
				return attribute;
			}
		}

		throw new NoSuchElementException();
	}

	/**
	 * This method sets the element type.
	 * 
	 * @param type
	 *            The datatype to get the element type.
	 */
	private void setElementType(final Datatype type) {
		if (type instanceof ClassUnit) {
			this.elementType = ElementType.CLASS;
		} else if (type instanceof InterfaceUnit) {
			this.elementType = ElementType.INTERFACE;
		} else {
			this.elementType = ElementType.UNKNOWN;
		}
	}

	/**
	 * This method returns the name of the parameter.
	 * 
	 * @return
	 *         The anem of the parameter.
	 */
	public String getName() {
		return this.attributeName;
	}

	/**
	 * This method returns the name of the type of the parameter.
	 * 
	 * @return
	 *         The name of the type.
	 * @see #isArrayType()
	 * @see #isPrimitiveType()
	 */
	public String getTypeName() {
		return this.fullTypeName;
	}

	/**
	 * This method returns true if the attribute is an array, otherwise false.
	 * 
	 * @return
	 *         True, if the attribute is an array, otherwise false.
	 */
	public boolean isArrayType() {
		return this.isArray;
	}

	/**
	 * This method returns true if the type of the attribute is a primitive, otherwise false.
	 * 
	 * @return
	 *         True if the type of the attribute is a primitive, otherwise false.
	 */
	public boolean isPrimitiveType() {
		return this.isPrimitive;
	}

	/**
	 * This method returns true is the attribute is static, otherwise false.
	 * 
	 * @return
	 *         True is the attribute is static, otherwise false.
	 */
	public boolean isStatic() {
		return this.isAStatic;
	}

	/**
	 * This method returns true if the attribute has a default value. <b>Only for primitive types!</b>
	 * 
	 * @return
	 *         True if the attribute has a default value.
	 */
	public boolean hasDefaultValue() {
		return this.hasValue;
	}

	/**
	 * This method returns the default value of the attribute.
	 * 
	 * @return
	 *         The default value of the attribute.
	 * @see #hasDefaultValue()
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * This method returns the {@link ElementType} of the attribute if it is not a primitive type.
	 * 
	 * @return
	 *         The {@code ElementType} of the attribute if it is not a primitive type.
	 */
	public ElementType getElementType() {
		return this.elementType;
	}

	/**
	 * This method returns the visibility modifier of the attribute.
	 * 
	 * @return
	 *         The visibility modifier of the attribute.
	 */
	public ExportKind getVisibilityModifier() {
		return this.visibilityModifier;
	}
}
