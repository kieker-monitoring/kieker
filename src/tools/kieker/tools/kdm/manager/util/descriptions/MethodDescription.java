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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ArrayType;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.Datatype;
import org.eclipse.gmt.modisco.omg.kdm.code.ExportKind;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodKind;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterKind;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.PrimitiveType;
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.kdm.manager.KDMModelManager;

/**
 * This class provides a description of a {@link MethodUnit}. It also contains the unique method qualifier necessary to use other iterators form the KDMModelManager.
 * 
 * @author Benjamin Harms
 * 
 */
public class MethodDescription {
	private static final Log LOG = LogFactory.getLog(MethodDescription.class);

	/**
	 * The original method unit from kdm model.
	 */
	private final MethodUnit methodUnit;
	/**
	 * The special qualifier used within the KDMModelManager.
	 */
	private final String methodQualifier;
	/**
	 * If true, the return type is a primitive like int, boolean or char. Otherwise it is something like class or interface type.
	 */
	private boolean arrayReturnType;
	/**
	 * The full name of the return type.
	 */
	private String fullReturnTypeName;

	/**
	 * Creates a new instance of this class from the given {@link MethodUnit} and the method qualifier used within the KDMModelManager.
	 * 
	 * @param methodUnit
	 *            The method unit form the kdm model.
	 * @param methodQualifier
	 *            The method qualifier used within the KDMModelManager.
	 */
	public MethodDescription(final MethodUnit methodUnit, final String methodQualifier) {
		this.methodUnit = methodUnit;
		this.methodQualifier = methodQualifier;

		if (!MethodKind.CONSTRUCTOR.equals(methodUnit.getKind())) {
			// Find the signature
			final Iterator<AbstractCodeElement> elementIterator = methodUnit.getCodeElement().iterator();
			while (elementIterator.hasNext()) {
				final AbstractCodeElement codeElement = elementIterator.next();
				if (codeElement instanceof Signature) {
					final Signature signature = (Signature) codeElement;
					// Try to extract the type
					if (this.extraktReturnType(signature)) {
						break;
					}
				}
			}

			if ("".equals(this.fullReturnTypeName)) {
				MethodDescription.LOG.error("The return type of the method couldn't be found.");
			}
		} else {
			this.arrayReturnType = false;
			this.fullReturnTypeName = "";
		}
	}

	/**
	 * This method tries to extract the full name of the return type of a signature. It returns true if it was successful, otherwise false.
	 * 
	 * @param signature
	 *            The signature to extract the return type.
	 * @return
	 *         True if it was successful, otherwise false.
	 */
	private boolean extraktReturnType(final Signature signature) {
		// Find the return type
		final Iterator<ParameterUnit> parameterIterator = signature.getParameterUnit().iterator();
		while (parameterIterator.hasNext()) {
			final ParameterUnit parameterUnit = parameterIterator.next();
			if (ParameterKind.RETURN.equals(parameterUnit.getKind())) {
				final Datatype returnType = parameterUnit.getType();
				String name = returnType.getName();
				final StringBuilder fullName = new StringBuilder();
				// Check primitive type
				if (returnType instanceof PrimitiveType) {
					this.arrayReturnType = false;
				} else if (returnType instanceof ArrayType) { // Check array type
					this.arrayReturnType = true;
					final ArrayType arrayType = (ArrayType) returnType;
					final Datatype itemType = arrayType.getItemUnit().getType();
					// Use the item type name
					name = itemType.getName();
					if (!(itemType instanceof PrimitiveType)) { // No primitive type
						final String parentName = this.reassembleParentName(itemType);
						fullName.append(parentName);
						// fullName.append(KDMModelManager.reassembleFullParentName(itemType));
					}
				} else { // anything else
					final String parentName = this.reassembleParentName(returnType);
					fullName.append(parentName);
					// fullName.append(KDMModelManager.reassembleFullParentName(returnType));
				}
				// Assemble full name
				if ((fullName.length() > 0) && !fullName.toString().endsWith(".")) {
					fullName.append('.');
				}
				fullName.append(name);
				this.fullReturnTypeName = fullName.toString();
				// The type could be extracted
				return true;
			}
		}

		// The type could not be extracted
		return false;
	}

	/**
	 * This method searches for the attribute with the tag-value given in the parameter 'tag' and returs the value.
	 * 
	 * @param item
	 *            The {@link CodeItem} to get the attribute from.
	 * @param tag
	 *            The tag to identify the attribute.
	 * @return
	 *         Returns the value of the attribute specified by the 'tag'-value.
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
	 * Reassembles the parent name from the given {@link Datatype} in a C# or Java model.
	 * 
	 * @param type
	 *            The {@link Datatype} to reassemble the parent name from.
	 * @return
	 *         The parent name if it could be reassembled or an empty string.
	 */
	private String reassembleParentName(final Datatype type) {
		String result = "";
		final String prefix = "global.";

		if (type != null) {
			// Try to get the full name from the attribute used in C#-models
			try {
				final String name = type.getName();
				String value = this.getValueFromAttribute(type, "FullyQualifiedName");
				// Keep the global-prefix in mind
				if (value.startsWith(prefix)) {
					value = value.substring(prefix.length());
				}
				// Remove the name of the type if necessary
				if (value.endsWith(name)) {
					final int index = value.length() - name.length() - 2;
					if (index >= 0) {
						value = value.substring(0, index);
					}
				}
				result = value;
			} catch (final NoSuchElementException e) {
				// Reassemble the name if the attribute does not exist.
				result = KDMModelManager.reassembleFullParentName(type);
			}
		}

		return result;
	}

	/**
	 * This method returns the visibility modifier of the class.
	 * 
	 * @return
	 *         The visibility modifier.
	 */
	public ExportKind getVisibilityModifier() {
		return this.methodUnit.getExport();
	}

	/**
	 * This method returns the kind of the method.
	 * 
	 * @return
	 *         The kind of the method.
	 */
	public MethodKind getMethodKind() {
		return this.methodUnit.getKind();
	}

	/**
	 * This method returns the name of the method.
	 * 
	 * @return
	 *         The name of the method.
	 */
	public String getName() {
		return this.methodUnit.getName();
	}

	/**
	 * This method returns the method qualifier used within the KDMModelManager.
	 * 
	 * @return
	 *         The method qualifier used within the KDMModelManager.
	 */
	public String getMethodQualifier() {
		return this.methodQualifier;
	}

	/**
	 * This method returns the return type of the method.
	 * 
	 * @return
	 *         The return type of the method.
	 */
	public String getReturnType() {
		return this.fullReturnTypeName;
	}

	/**
	 * This method returns true if the return type of the method is an array type, otherwise false.
	 * 
	 * @return
	 *         True if the return type is an array type, otherwise false.
	 */
	public boolean isArrayReturnType() {
		return this.arrayReturnType;
	}

	/**
	 * This method returns true if the method is a constructor, otherwise false.
	 * 
	 * @return
	 *         True if the method is a constructor, otherwise false.
	 */
	public boolean isConstructor() {
		return MethodKind.CONSTRUCTOR.equals(this.getMethodKind());
	}
}
