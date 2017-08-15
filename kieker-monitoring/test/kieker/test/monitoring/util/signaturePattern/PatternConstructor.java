/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.util.signaturePattern;

import java.util.ArrayList;
import java.util.List;

/**
 * This class realizes an pattern generator for the adaptive monitoring.
 * 
 * @author Bjoern Weissenfels, Jan Waller
 * 
 * @since 1.6
 */
public final class PatternConstructor {

	private static final String DEFAULT_VISIBILITY = "";
	private static final String DEFAULT_ABSTRACT_NON_ABSTRACT = "";
	private static final String DEFAULT_STATIC_NON_STATIC = "";
	private static final String DEFAULT_FINAL_NON_FINAL = "";
	private static final String DEFAULT_SYNCHRONIZED_NON_SYNCHRONIZED = "";
	private static final String DEFAULT_NATIVE_NON_NATIVE = "";
	private static final String DEFAULT_RETRUN_TYPE = "..*";
	private static final String DEFAULT_FQ_CLASSNAME = "..*";
	private static final String DEFAULT_OPERATIONNAME = "*";
	private static final String DEFAULT_PARAMETERLIST = "..";
	private static final String DEFAULT_THROWS_LIST = "";
	private static final String DEFAULT_WHITESPACE = " ";

	private final List<String> visibilityList = new ArrayList<String>();
	private final List<String> abstractNonAbstractList = new ArrayList<String>();
	private final List<String> staticNonStaticList = new ArrayList<String>();
	private final List<String> finalNonFinalList = new ArrayList<String>();
	private final List<String> synchronizedNonSynchronizedList = new ArrayList<String>();
	private final List<String> nativeNonNativeList = new ArrayList<String>();
	private final List<String> returnTypeList = new ArrayList<String>();
	private final List<String> fqClassNameList = new ArrayList<String>();
	private final List<String> operationNameList = new ArrayList<String>();
	private final List<String> parameterListList = new ArrayList<String>();
	private final List<String> whiteSpaceList = new ArrayList<String>();
	private final List<String> optionalWhitespaceList = new ArrayList<String>();
	private final List<String> throwsListList = new ArrayList<String>();

	/**
	 * Default constructor.
	 */
	public PatternConstructor() {
		// empty default constructor
	}

	/**
	 * Construct a list of pattern.
	 * 
	 * @return List of strings representing pattern
	 */
	public List<String> createPatterns() {
		if (this.visibilityList.isEmpty()) {
			this.visibilityList.add(PatternConstructor.DEFAULT_VISIBILITY);
		}
		if (this.abstractNonAbstractList.isEmpty()) {
			this.abstractNonAbstractList.add(PatternConstructor.DEFAULT_ABSTRACT_NON_ABSTRACT);
		}
		if (this.staticNonStaticList.isEmpty()) {
			this.staticNonStaticList.add(PatternConstructor.DEFAULT_STATIC_NON_STATIC);
		}
		if (this.finalNonFinalList.isEmpty()) {
			this.finalNonFinalList.add(PatternConstructor.DEFAULT_FINAL_NON_FINAL);
		}
		if (this.synchronizedNonSynchronizedList.isEmpty()) {
			this.synchronizedNonSynchronizedList.add(PatternConstructor.DEFAULT_SYNCHRONIZED_NON_SYNCHRONIZED);
		}
		if (this.nativeNonNativeList.isEmpty()) {
			this.nativeNonNativeList.add(PatternConstructor.DEFAULT_NATIVE_NON_NATIVE);
		}
		if (this.returnTypeList.isEmpty()) {
			this.returnTypeList.add(PatternConstructor.DEFAULT_RETRUN_TYPE);
		}
		if (this.fqClassNameList.isEmpty()) {
			this.fqClassNameList.add(PatternConstructor.DEFAULT_FQ_CLASSNAME);
		}
		if (this.operationNameList.isEmpty()) {
			this.operationNameList.add(PatternConstructor.DEFAULT_OPERATIONNAME);
		}
		if (this.parameterListList.isEmpty()) {
			this.parameterListList.add(PatternConstructor.DEFAULT_PARAMETERLIST);
		}
		if (this.throwsListList.isEmpty()) {
			this.throwsListList.add(PatternConstructor.DEFAULT_THROWS_LIST);
		}
		if (this.whiteSpaceList.isEmpty()) {
			this.whiteSpaceList.add(PatternConstructor.DEFAULT_WHITESPACE);
		}
		this.optionalWhitespaceList.add("");
		this.optionalWhitespaceList.addAll(this.whiteSpaceList);
		final List<String> result = new ArrayList<String>();

		for (final String visibility : this.visibilityList) {
			for (final String abstractNonAbstract : this.abstractNonAbstractList) {
				for (final String staticNonStatic : this.staticNonStaticList) { // NOCS
					for (final String finalNonFinal : this.finalNonFinalList) { // NOCS
						for (final String synchronizedNonSynchronized : this.synchronizedNonSynchronizedList) { // NOCS
							for (final String nativeNonNative : this.nativeNonNativeList) { // NOCS
								for (final String returnType : this.returnTypeList) { // NOCS
									for (final String fqClassName : this.fqClassNameList) { // NOCS
										for (final String operationName : this.operationNameList) { // NOCS
											for (final String paramList : this.parameterListList) { // NOCS
												for (final String throwsList : this.throwsListList) { // NOCS
													for (final String white1 : this.whiteSpaceList) { // NOCS
														for (final String white2 : this.whiteSpaceList) { // NOCS
															for (final String white3 : this.whiteSpaceList) { // NOCS
																for (final String white4 : this.whiteSpaceList) { // NOCS
																	for (final String white5 : this.whiteSpaceList) { // NOCS
																		for (final String white6 : this.whiteSpaceList) { // NOCS
																			for (final String white7 : this.whiteSpaceList) { // NOCS
																				for (final String white8 : this.whiteSpaceList) { // NOCS
																					for (final String white9 : this.whiteSpaceList) { // NOCS
																						for (final String opWhite1 : this.optionalWhitespaceList) { // NOCS
																							for (final String opWhite2 : this.optionalWhitespaceList) { // NOCS
																								for (final String opWhite3 : this.optionalWhitespaceList) { // NOCS
																									for (final String opWhite4 : this.optionalWhitespaceList) { // NOCS
																										final StringBuilder sb = new StringBuilder();
																										sb.append(opWhite1);
																										if (visibility.length() > 0) {
																											sb.append(visibility)
																													.append(white1);
																										}
																										if (abstractNonAbstract.length() > 0) {
																											sb.append(abstractNonAbstract)
																													.append(white2);
																										}
																										if (staticNonStatic.length() > 0) {
																											sb.append(staticNonStatic)
																													.append(white3);
																										}
																										if (finalNonFinal.length() > 0) {
																											sb.append(finalNonFinal)
																													.append(white4);
																										}
																										if (synchronizedNonSynchronized.length() > 0) {
																											sb.append(synchronizedNonSynchronized)
																													.append(white5);
																										}
																										if (nativeNonNative.length() > 0) {
																											sb.append(nativeNonNative)
																													.append(white6);
																										}
																										sb.append(returnType)
																												.append(white7)
																												.append(fqClassName)
																												.append('.')
																												.append(operationName)
																												.append(opWhite2)
																												.append('(')
																												.append(opWhite3)
																												.append(paramList)
																												.append(opWhite4)
																												.append(')');
																										if (throwsList.length() > 0) {
																											sb.append(white8)
																													.append("throws")
																													.append(white9)
																													.append(throwsList);
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
	 * Add a visibility modifier, like public, private, protected.
	 */
	public PatternConstructor addVisibilityVariant(final String visibility) {
		this.visibilityList.add(visibility);
		return this;
	}

	/**
	 * Add abstract and non_abstract to the pattern list.
	 */
	public PatternConstructor addAbstractNonAbstractVariant(final String abstractNonAbstract) {
		this.abstractNonAbstractList.add(abstractNonAbstract);
		return this;
	}

	/**
	 * Add static and non static to the pattern list.
	 */
	public PatternConstructor addStaticNonStaticVariant(final String staticNonStatic) {
		this.staticNonStaticList.add(staticNonStatic);
		return this;
	}

	/**
	 * Add final/non-final to the pattern list.
	 */
	public PatternConstructor addFinalNonFinalVariant(final String finalNonFinal) {
		this.finalNonFinalList.add(finalNonFinal);
		return this;
	}

	/**
	 * Add synchronized/non-synchronized to the pattern list.
	 */
	public PatternConstructor addSynchronizedNonSynchronizedVariant(final String synchronizedNonSynchronized) {
		this.synchronizedNonSynchronizedList.add(synchronizedNonSynchronized);
		return this;
	}

	/**
	 * Add native/non-native to the pattern list.
	 */
	public PatternConstructor addNativeNonNativeVariant(final String nativeNonNative) {
		this.nativeNonNativeList.add(nativeNonNative);
		return this;
	}

	/**
	 * Add return type to the return type list for the patterns.
	 */
	public PatternConstructor addreturnTypeVariant(final String returnType) {
		this.returnTypeList.add(returnType);
		return this;
	}

	/**
	 * Add class names to the pattern list.
	 */
	public PatternConstructor addfqClassNameVariant(final String fqClassName) {
		this.fqClassNameList.add(fqClassName);
		return this;
	}

	/**
	 * Add operation names to the pattern list.
	 */
	public PatternConstructor addoperationNameVariant(final String operationName) {
		this.operationNameList.add(operationName);
		return this;
	}

	/**
	 * Add parameter names to the pattern list.
	 */
	public PatternConstructor addparameterListVariant(final String parameterList) {
		this.parameterListList.add(parameterList);
		return this;
	}

	/**
	 * Add exception names to the pattern list.
	 */
	public PatternConstructor addthrowsListVariant(final String throwsList) {
		this.throwsListList.add(throwsList);
		return this;
	}

	/**
	 * Add different whitespace variants to the pattern list.
	 */
	public PatternConstructor addwhitespaceVariant(final String whitespace) {
		this.whiteSpaceList.add(whitespace);
		return this;
	}
}
