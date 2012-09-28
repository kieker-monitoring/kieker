package kieker.test.monitoring.util;

import java.util.ArrayList;
import java.util.List;

public class SignatureConstructor {

	List<String> visibilityList = new ArrayList<String>();
	List<String> staticNonStaticList = new ArrayList<String>();
	List<String> nativeNonNativeList = new ArrayList<String>();
	List<String> returnTypeList = new ArrayList<String>();
	List<String> fqClassNameList = new ArrayList<String>();
	List<String> operationNameList = new ArrayList<String>();
	List<String> parameterListList = new ArrayList<String>();

	final String visibilityDefault = "";
	final String staticNonStaticDefault = "";
	final String nativeNonNativeDefault = "";
	final String returnTypeDefault = "void";
	final String fqClassNameDefault = "Clazz";
	final String operationNameDefault = "get";
	final String parameterListDefault = "";

	public List<String> getSignatures() {
		if (this.visibilityList.isEmpty()) {
			this.visibilityList.add(this.visibilityDefault);
		}
		if (this.staticNonStaticList.isEmpty()) {
			this.staticNonStaticList.add(this.staticNonStaticDefault);
		}
		if (this.nativeNonNativeList.isEmpty()) {
			this.nativeNonNativeList.add(this.nativeNonNativeDefault);
		}
		if (this.returnTypeList.isEmpty()) {
			this.returnTypeList.add(this.returnTypeDefault);
		}
		if (this.fqClassNameList.isEmpty()) {
			this.fqClassNameList.add(this.fqClassNameDefault);
		}
		if (this.operationNameList.isEmpty()) {
			this.operationNameList.add(this.operationNameDefault);
		}
		if (this.parameterListList.isEmpty()) {
			this.parameterListList.add(this.parameterListDefault);
		}
		final List<String> result = new ArrayList<String>();
		for (final String visibility : this.visibilityList) {
			for (final String staticNonStatic : this.staticNonStaticList) {
				for (final String nativeNonNative : this.nativeNonNativeList) {
					for (final String returnType : this.returnTypeList) {
						for (final String fqClassName : this.fqClassNameList) {
							for (final String operationName : this.operationNameList) {
								for (final String paramList : this.parameterListList) {
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
