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

package kieker.test.monitoring.util.signaturePattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bjoern Weissenfels, Jan Waller
 */
public final class PatternConstructor {

	private static final String DEFAULT_VISIBILITY = "";
	private static final String DEFAULT_STATIC_NON_STATIC = "";
	private static final String DEFAULT_NATIVE_NON_NATIVE = "";
	private static final String DEFAULT_RETRUN_TYPE = "..*";
	private static final String DEFAULT_FQ_CLASSNAME = "..*";
	private static final String DEFAULT_OPERATIONNAME = "*";
	private static final String DEFAULT_PARAMETERLIST = "..";
	private static final String DEFAULT_WHITESPACE = " ";

	private final List<String> visibilityList = new ArrayList<String>();
	private final List<String> staticNonStaticList = new ArrayList<String>();
	private final List<String> nativeNonNativeList = new ArrayList<String>();
	private final List<String> returnTypeList = new ArrayList<String>();
	private final List<String> fqClassNameList = new ArrayList<String>();
	private final List<String> operationNameList = new ArrayList<String>();
	private final List<String> parameterListList = new ArrayList<String>();
	private final List<String> whiteSpaceList = new ArrayList<String>();
	private final List<String> optionalWhitespaceList = new ArrayList<String>();

	public PatternConstructor() {
		// empty default constructor
	}

	public List<String> getPattern() {
		if (this.visibilityList.isEmpty()) {
			this.visibilityList.add(PatternConstructor.DEFAULT_VISIBILITY);
		}
		if (this.staticNonStaticList.isEmpty()) {
			this.staticNonStaticList.add(PatternConstructor.DEFAULT_STATIC_NON_STATIC);
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
		if (this.whiteSpaceList.isEmpty()) {
			this.whiteSpaceList.add(PatternConstructor.DEFAULT_WHITESPACE);
		}
		this.optionalWhitespaceList.add("");
		this.optionalWhitespaceList.addAll(this.whiteSpaceList);
		final List<String> result = new ArrayList<String>();

		for (final String visibility : this.visibilityList) {
			for (final String staticNonStatic : this.staticNonStaticList) { // NOCS
				for (final String nativeNonNative : this.nativeNonNativeList) { // NOCS
					for (final String returnType : this.returnTypeList) { // NOCS
						for (final String fqClassName : this.fqClassNameList) { // NOCS
							for (final String operationName : this.operationNameList) { // NOCS
								for (final String paramList : this.parameterListList) { // NOCS
									for (final String white1 : this.whiteSpaceList) { // NOCS
										for (final String white2 : this.whiteSpaceList) { // NOCS
											for (final String white3 : this.whiteSpaceList) { // NOCS
												for (final String white4 : this.whiteSpaceList) { // NOCS
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
																	if (staticNonStatic.length() > 0) {
																		sb.append(staticNonStatic)
																				.append(white2);
																	}
																	if (nativeNonNative.length() > 0) {
																		sb.append(nativeNonNative)
																				.append(white3);
																	}
																	sb.append(returnType)
																			.append(white4)
																			.append(fqClassName)
																			.append('.')
																			.append(operationName)
																			.append(opWhite2)
																			.append('(')
																			.append(opWhite3)
																			.append(paramList)
																			.append(opWhite4)
																			.append(')');
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
		return result;
	}

	public PatternConstructor addVisibilityVariant(final String visibility) {
		this.visibilityList.add(visibility);
		return this;
	}

	public PatternConstructor addStaticNonStaticVariant(final String staticNonStatic) {
		this.staticNonStaticList.add(staticNonStatic);
		return this;
	}

	public PatternConstructor addNativeNonNativeVariant(final String nativeNonNative) {
		this.nativeNonNativeList.add(nativeNonNative);
		return this;
	}

	public PatternConstructor addreturnTypeVariant(final String returnType) {
		this.returnTypeList.add(returnType);
		return this;
	}

	public PatternConstructor addfqClassNameVariant(final String fqClassName) {
		this.fqClassNameList.add(fqClassName);
		return this;
	}

	public PatternConstructor addoperationNameVariant(final String operationName) {
		this.operationNameList.add(operationName);
		return this;
	}

	public PatternConstructor addparameterListVariant(final String parameterList) {
		this.parameterListList.add(parameterList);
		return this;
	}

	public PatternConstructor addwhitespaceVariant(final String whitespace) {
		this.whiteSpaceList.add(whitespace);
		return this;
	}
}
