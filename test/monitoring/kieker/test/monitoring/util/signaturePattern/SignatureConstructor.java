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
public final class SignatureConstructor {

	private static final String DEFAULT_VISIBILITY = "";
	private static final String DEFAULT_STATIC_NON_STATIC = "";
	private static final String DEFAULT_NATIVE_NON_NATIVE = "";
	private static final String DEFAULT_RETRUN_TYPE = "void";
	private static final String DEFAULT_FQ_CLASSNAME = "Clazz";
	private static final String DEFAULT_OPERATIONNAME = "get";
	private static final String DEFAULT_PARAMETERLIST = "";

	private final List<String> visibilityList = new ArrayList<String>();
	private final List<String> staticNonStaticList = new ArrayList<String>();
	private final List<String> nativeNonNativeList = new ArrayList<String>();
	private final List<String> returnTypeList = new ArrayList<String>();
	private final List<String> fqClassNameList = new ArrayList<String>();
	private final List<String> operationNameList = new ArrayList<String>();
	private final List<String> parameterListList = new ArrayList<String>();

	public SignatureConstructor() {
		// empty default constructor
	}

	public List<String> getSignatures() {
		if (this.visibilityList.isEmpty()) {
			this.visibilityList.add(SignatureConstructor.DEFAULT_VISIBILITY);
		}
		if (this.staticNonStaticList.isEmpty()) {
			this.staticNonStaticList.add(SignatureConstructor.DEFAULT_STATIC_NON_STATIC);
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
		final List<String> result = new ArrayList<String>();
		for (final String visibility : this.visibilityList) { // NOCS
			for (final String staticNonStatic : this.staticNonStaticList) { // NOCS
				for (final String nativeNonNative : this.nativeNonNativeList) { // NOCS
					for (final String returnType : this.returnTypeList) { // NOCS
						for (final String fqClassName : this.fqClassNameList) { // NOCS
							for (final String operationName : this.operationNameList) { // NOCS
								for (final String paramList : this.parameterListList) { // NOCS
									final StringBuilder sb = new StringBuilder();
									if (visibility.length() > 0) {
										sb.append(visibility).append(' ');
									}
									if (staticNonStatic.length() > 0) {
										sb.append(staticNonStatic).append(' ');
									}
									if (nativeNonNative.length() > 0) {
										sb.append(nativeNonNative).append(' ');
									}
									if (returnType.length() > 0) {
										sb.append(returnType).append(' ');
									}
									sb.append(fqClassName).append('.').append(operationName).append('(').append(paramList).append(')');
									result.add(sb.toString());
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	public SignatureConstructor addVisibilityVariant(final String visibility) {
		this.visibilityList.add(visibility);
		return this;
	}

	public SignatureConstructor addStaticNonStaticVariant(final String staticNonStatic) {
		this.staticNonStaticList.add(staticNonStatic);
		return this;
	}

	public SignatureConstructor addNativeNonNativeVariant(final String nativeNonNative) {
		this.nativeNonNativeList.add(nativeNonNative);
		return this;
	}

	public SignatureConstructor addreturnTypeVariant(final String returnType) {
		this.returnTypeList.add(returnType);
		return this;
	}

	public SignatureConstructor addfqClassNameVariant(final String fqClassName) {
		this.fqClassNameList.add(fqClassName);
		return this;
	}

	public SignatureConstructor addoperationNameVariant(final String operationName) {
		this.operationNameList.add(operationName);
		return this;
	}

	public SignatureConstructor addparameterListVariant(final String parameterList) {
		this.parameterListList.add(parameterList);
		return this;
	}

}
