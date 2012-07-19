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

import org.eclipse.gmt.modisco.omg.kdm.code.ArrayType;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Datatype;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.PrimitiveType;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.util.ElementType;

/**
 * This class provides a description for a parameter of a method.
 * 
 * @author Benjamin Harms
 * 
 */
public class ParameterDescription {
	/**
	 * The parameter unit from the KDM instance.
	 */
	private final ParameterUnit parameterUnit;
	/**
	 * The full name of the type.
	 */
	private String fullTypeName;
	/**
	 * If true, the type of the parameter is a primitive like int, boolean or char. Otherwise it is something like class or interface type.
	 */
	private boolean primitiveType;
	/**
	 * If true, the type of the parameter is an array type, otherwise it is a simple type.
	 */
	private boolean arrayType;
	/**
	 * Describes the type of the parameter if it is not a primitive type.
	 */
	private ElementType elementType;

	/**
	 * Creates a new instance of this class from a {@link ParameterUnit}.
	 * 
	 * @param parameterUnit
	 *            The {@link ParameterUnit} to describe.
	 */
	public ParameterDescription(final ParameterUnit parameterUnit) {
		this.parameterUnit = parameterUnit;

		// Get information about the type
		this.initTypeDescription();
	}

	/**
	 * This method tries to get some information about the type of the parameter and initializes the attributes.
	 */
	private void initTypeDescription() {
		final Datatype datatype = this.parameterUnit.getType();
		String typeName = datatype.getName();
		final StringBuilder tempFullTypeName = new StringBuilder();
		// First test primitive type
		if (datatype instanceof PrimitiveType) {
			this.arrayType = false;
			this.primitiveType = true;
			this.elementType = ElementType.UNKNOWN;
		} else if (datatype instanceof ArrayType) { // Test array type
			this.arrayType = true;
			final ArrayType tempArrayType = (ArrayType) datatype;
			// Check the type of the array elements
			final Datatype itemType = tempArrayType.getItemUnit().getType();
			typeName = itemType.getName();
			if (itemType instanceof PrimitiveType) {
				this.primitiveType = true;
				this.elementType = ElementType.UNKNOWN;
			} else {
				this.primitiveType = false;
				// Let the model manager reassemble the name
				tempFullTypeName.append(KDMModelManager.reassembleFullParentName(itemType));
				// Set the element type
				this.setElementType(itemType);
			}
		} else { // Anything else like ClassUnit etc
			this.arrayType = false;
			this.primitiveType = false;
			// Set the element type
			this.setElementType(datatype);
			// Let the model manager reassemble the name
			tempFullTypeName.append(KDMModelManager.reassembleFullParentName(datatype));
		}
		// Assemble full type name
		if ((tempFullTypeName.length() > 0) && !tempFullTypeName.toString().endsWith(".")) {
			tempFullTypeName.append('.');
		}
		tempFullTypeName.append(typeName);
		this.fullTypeName = tempFullTypeName.toString();
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
	 *         The name of the parameter.
	 */
	public String getName() {
		return this.parameterUnit.getName();
	}

	/**
	 * This method returns the full type name of this parameter.
	 * 
	 * @return
	 *         The full type name of this parameter.
	 */
	public String getTypeName() {
		return this.fullTypeName;
	}

	/**
	 * This method returns the short name of the type, if it is a class or something like that.
	 * 
	 * @return
	 *         The short name of the type.
	 */
	public String getShortName() {
		return this.parameterUnit.getType().getName();
	}

	/**
	 * This method returns the position of the parameter within the signature of the method.
	 * 
	 * @return
	 *         The position of the parameter within the signature of the method.
	 */
	public int getPosition() {
		return this.parameterUnit.getPos();
	}

	/**
	 * This method returns true if the type of the parameter is a primitive like int, boolean or, otherwise false.
	 * 
	 * @return
	 *         True if the type of the parameter is primitive, otherwise false.
	 */
	public boolean isPrimitiveType() {
		return this.primitiveType;
	}

	/**
	 * This method returns true if the type of the parameter is an array type, otherwise false.
	 * 
	 * @return
	 *         True if the type of the parameter is an array type, otherwise false.
	 */
	public boolean isArrayType() {
		return this.arrayType;
	}

	/**
	 * This method returns the type of the parameter if it is <b>not</b> a primitive type.
	 * 
	 * @return
	 *         The element type of the parameter.
	 * @see #isPrimitiveType()
	 */
	public ElementType getElementType() {
		return this.elementType;
	}
}
