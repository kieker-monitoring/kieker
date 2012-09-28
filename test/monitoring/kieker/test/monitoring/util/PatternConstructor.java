package kieker.test.monitoring.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatternConstructor {

	List<String> visibilityList = new ArrayList<String>();
	List<String> staticNonStaticList = new ArrayList<String>();
	List<String> nativeNonNativeList = new ArrayList<String>();
	List<String> returnTypeList = new ArrayList<String>();
	List<String> fqClassNameList = new ArrayList<String>();
	List<String> operationNameList = new ArrayList<String>();
	List<String> parameterListList = new ArrayList<String>();
	List<String> whiteSpaceList = new ArrayList<String>();
	List<String> optionalWhitespaceList = new ArrayList<String>();

	final String visibilityDefault = "";
	final String staticNonStaticDefault = "";
	final String nativeNonNativeDefault = "";
	final String returnTypeDefault = "..*";
	final String fqClassNameDefault = "..*";
	final String operationNameDefault = "*";
	final String parameterListDefault = "..";
	final String whiteSpaceDefault = " ";

	final Random random = new Random();

	public List<String> getPattern() {
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
		if (this.whiteSpaceList.isEmpty()) {
			this.whiteSpaceList.add(this.whiteSpaceDefault);
		}
		this.optionalWhitespaceList.add("");
		this.optionalWhitespaceList.addAll(this.whiteSpaceList);
		final List<String> result = new ArrayList<String>();

		for (final String visibility : this.visibilityList) {
			for (final String staticNonStatic : this.staticNonStaticList) {
				for (final String nativeNonNative : this.nativeNonNativeList) {
					for (final String returnType : this.returnTypeList) {
						for (final String fqClassName : this.fqClassNameList) {
							for (final String operationName : this.operationNameList) {
								for (final String paramList : this.parameterListList) {
									for (final String white1 : this.whiteSpaceList) {
										for (final String white2 : this.whiteSpaceList) {
											for (final String white3 : this.whiteSpaceList) {
												for (final String white4 : this.whiteSpaceList) {
													for (final String opWhite1 : this.optionalWhitespaceList) {
														for (final String opWhite2 : this.optionalWhitespaceList) {
															for (final String opWhite3 : this.optionalWhitespaceList) {
																for (final String opWhite4 : this.optionalWhitespaceList) {
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
