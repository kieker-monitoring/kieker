/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.test.util.signaturePattern;

import java.util.ArrayList;
import java.util.List;

/**
 * This helper class allows to easily create multiple signatures for test purposes. The class provides methods to add, for example, modificators and parameters,
 * before assembling all possible signature variants from those values.
 *
 * @author Bjoern Weissenfels, Jan Waller
 *
 * @since 1.6
 */
public final class SignatureConstructor {

	private static final String DEFAULT_VISIBILITY = "";
	private static final String DEFAULT_ABSTRACT_NON_ABSTRACT = "";
	private static final String DEFAULT_DEFAULT_NON_DEFAULT = "";
	private static final String DEFAULT_STATIC_NON_STATIC = "";
	private static final String DEFAULT_FINAL_NON_FINAL = "";
	private static final String DEFAULT_SYNCHRONIZED_NON_SYNCHRONIZED = "";
	private static final String DEFAULT_NATIVE_NON_NATIVE = "";
	private static final String DEFAULT_RETRUN_TYPE = "void";
	private static final String DEFAULT_FQ_CLASSNAME = "Clazz";
	private static final String DEFAULT_OPERATIONNAME = "get";
	private static final String DEFAULT_PARAMETERLIST = "";
	private static final String DEFAULT_THROWS_LIST = "";

	private final List<String> visibilityList = new ArrayList<>();
	private final List<String> abstractNonAbstractList = new ArrayList<>();
	private final List<String> defaultNonDefaultList = new ArrayList<>();
	private final List<String> staticNonStaticList = new ArrayList<>();
	private final List<String> finalNonFinalList = new ArrayList<>();
	private final List<String> synchronizedNonSynchronizedList = new ArrayList<>();
	private final List<String> nativeNonNativeList = new ArrayList<>();
	private final List<String> returnTypeList = new ArrayList<>();
	private final List<String> fqClassNameList = new ArrayList<>();
	private final List<String> operationNameList = new ArrayList<>();
	private final List<String> parameterListList = new ArrayList<>();
	private final List<String> throwsListList = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	public SignatureConstructor() {
		// empty default constructor
	}

	/**
	 * This method assembles the signatures based on the content of the fields.
	 *
	 * @return A list containing the signatures.
	 */
	public List<String> getSignatures() {
		if (this.visibilityList.isEmpty()) {
			this.visibilityList.add(SignatureConstructor.DEFAULT_VISIBILITY);
		}
		if (this.abstractNonAbstractList.isEmpty()) {
			this.abstractNonAbstractList.add(SignatureConstructor.DEFAULT_ABSTRACT_NON_ABSTRACT);
		}
		if (this.defaultNonDefaultList.isEmpty()) {
			this.defaultNonDefaultList.add(SignatureConstructor.DEFAULT_DEFAULT_NON_DEFAULT);
		}
		if (this.staticNonStaticList.isEmpty()) {
			this.staticNonStaticList.add(SignatureConstructor.DEFAULT_STATIC_NON_STATIC);
		}
		if (this.finalNonFinalList.isEmpty()) {
			this.finalNonFinalList.add(SignatureConstructor.DEFAULT_FINAL_NON_FINAL);
		}
		if (this.synchronizedNonSynchronizedList.isEmpty()) {
			this.synchronizedNonSynchronizedList.add(SignatureConstructor.DEFAULT_SYNCHRONIZED_NON_SYNCHRONIZED);
		}
		if (this.nativeNonNativeList.isEmpty()) {
			this.nativeNonNativeList.add(SignatureConstructor.DEFAULT_NATIVE_NON_NATIVE);
		}
		if (this.returnTypeList.isEmpty()) {
			this.returnTypeList.add(SignatureConstructor.DEFAULT_RETRUN_TYPE);
		}
		if (this.fqClassNameList.isEmpty()) {
			this.fqClassNameList.add(SignatureConstructor.DEFAULT_FQ_CLASSNAME);
		}
		if (this.operationNameList.isEmpty()) {
			this.operationNameList.add(SignatureConstructor.DEFAULT_OPERATIONNAME);
		}
		if (this.parameterListList.isEmpty()) {
			this.parameterListList.add(SignatureConstructor.DEFAULT_PARAMETERLIST);
		}
		if (this.throwsListList.isEmpty()) {
			this.throwsListList.add(SignatureConstructor.DEFAULT_THROWS_LIST);
		}
		final List<String> result = new ArrayList<>();
		for (final String visibility : this.visibilityList) { // NOCS
			for (final String abstractNonAbstract : this.abstractNonAbstractList) {
				for (final String defaultNonDefault : this.defaultNonDefaultList) {
					for (final String staticNonStatic : this.staticNonStaticList) { // NOCS
						for (final String finalNonFinal : this.finalNonFinalList) { // NOCS
							for (final String synchronizedNonSynchronized : this.synchronizedNonSynchronizedList) { // NOCS
								for (final String nativeNonNative : this.nativeNonNativeList) { // NOCS
									for (final String returnType : this.returnTypeList) { // NOCS
										for (final String fqClassName : this.fqClassNameList) { // NOCS
											for (final String operationName : this.operationNameList) { // NOCS
												for (final String paramList : this.parameterListList) { // NOCS
													for (final String throwsList : this.throwsListList) { // NOCS
														final StringBuilder sb = new StringBuilder();
														if (visibility.length() > 0) {
															sb.append(visibility).append(' ');
														}
														if (abstractNonAbstract.length() > 0) {
															sb.append(abstractNonAbstract).append(' ');
														}
														if (defaultNonDefault.length() > 0) {
															sb.append(defaultNonDefault).append(' ');
														}
														if (staticNonStatic.length() > 0) {
															sb.append(staticNonStatic).append(' ');
														}
														if (finalNonFinal.length() > 0) {
															sb.append(finalNonFinal).append(' ');
														}
														if (synchronizedNonSynchronized.length() > 0) {
															sb.append(synchronizedNonSynchronized).append(' ');
														}
														if (nativeNonNative.length() > 0) {
															sb.append(nativeNonNative).append(' ');
														}
														if (returnType.length() > 0) {
															sb.append(returnType).append(' ');
														}
														sb.append(fqClassName).append('.').append(operationName).append('(').append(paramList).append(')');
														if (throwsList.length() > 0) {
															sb.append(" throws ").append(throwsList);
														}
														result.add(sb.toString());
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Adds the given string as a visibility modificator.
	 *
	 * @param visibility
	 *            The visibility modificator.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addVisibilityVariant(final String visibility) {
		this.visibilityList.add(visibility);
		return this;
	}

	/**
	 * Adds the given string as an abstract or non abstract variant.
	 *
	 * @param abstractNonAbstract
	 *            The abstract or non abstract variant.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addAbstractNonAbstractVariant(final String abstractNonAbstract) {
		this.abstractNonAbstractList.add(abstractNonAbstract);
		return this;
	}

	/**
	 * Adds the given string as a default or non default variant.
	 *
	 * @param defaultNonDefault
	 *            The default or non default variant.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addDefaultNonDefaultVariant(final String defaultNonDefault) {
		this.defaultNonDefaultList.add(defaultNonDefault);
		return this;
	}

	/**
	 * Adds the given string as a static or non static variant.
	 *
	 * @param staticNonStatic
	 *            The static or non static variant.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addStaticNonStaticVariant(final String staticNonStatic) {
		this.staticNonStaticList.add(staticNonStatic);
		return this;
	}

	/**
	 * Adds the given string as a final or non final variant.
	 *
	 * @param finalNonFinal
	 *            The final or non final variant.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addFinalNonFinalVariant(final String finalNonFinal) {
		this.finalNonFinalList.add(finalNonFinal);
		return this;
	}

	/**
	 * Adds the given string as a synchronized or non synchronized variant.
	 *
	 * @param synchronizedNonSynchronized
	 *            The synchronized or non synchronized variant.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addSynchronizedNonSynchronizedVariant(final String synchronizedNonSynchronized) {
		this.synchronizedNonSynchronizedList.add(synchronizedNonSynchronized);
		return this;
	}

	/**
	 * Adds the given string as a native or non native variant.
	 *
	 * @param nativeNonNative
	 *            The native or non native variant.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addNativeNonNativeVariant(final String nativeNonNative) {
		this.nativeNonNativeList.add(nativeNonNative);
		return this;
	}

	/**
	 * Adds the given string as a return type.
	 *
	 * @param returnType
	 *            The return type.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addreturnTypeVariant(final String returnType) {
		this.returnTypeList.add(returnType);
		return this;
	}

	/**
	 * Adds the given string as a full qualified class name variant.
	 *
	 * @param fqClassName
	 *            The full qualified class name.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addfqClassNameVariant(final String fqClassName) {
		this.fqClassNameList.add(fqClassName);
		return this;
	}

	/**
	 * Adds the given string as an operation name variant.
	 *
	 * @param operationName
	 *            The operation name.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addoperationNameVariant(final String operationName) {
		this.operationNameList.add(operationName);
		return this;
	}

	/**
	 * Adds the given string as a parameter list variant.
	 *
	 * @param parameterList
	 *            The parameter list.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addparameterListVariant(final String parameterList) {
		this.parameterListList.add(parameterList);
		return this;
	}

	/**
	 * Adds the given string as a throws list variant.
	 *
	 * @param throwsList
	 *            The throws list.
	 *
	 * @return A reference to {@code this} in order to allow the chaining of the methods.
	 */
	public SignatureConstructor addthrowsListVariant(final String throwsList) {
		this.throwsListList.add(throwsList);
		return this;
	}

}
