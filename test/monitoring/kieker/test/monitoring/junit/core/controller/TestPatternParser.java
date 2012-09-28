package kieker.test.monitoring.junit.core.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.core.helper.InvalidPatternException;
import kieker.monitoring.core.helper.PatternParser;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.PatternConstructor;
import kieker.test.monitoring.util.SignatureConstructor;

public class TestPatternParser extends AbstractKiekerTest {

	@Test
	// works : 810.000 tests
	public void whitespaceTest() throws InvalidPatternException {
		final PatternParser parser = new PatternParser();
		final String signature = "public static void default.package.Clazz.getVal(int, java.lang.String)";

		final PatternConstructor positivePattern = new PatternConstructor();
		positivePattern.addVisibilityVariant("public")
				.addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("void")
				.addfqClassNameVariant("default.package.Clazz")
				.addoperationNameVariant("getVal")
				.addparameterListVariant("int, java.lang.String")
				.addwhitespaceVariant(" ").addwhitespaceVariant("\t").addwhitespaceVariant("\f").addwhitespaceVariant("\r").addwhitespaceVariant("\n");

		final List<String> positivePatternList = positivePattern.getPattern();
		for (final String pattern : positivePatternList) {
			Assert.assertTrue("pattern: " + pattern + "; signature: " + signature, parser.parseToPattern(pattern).matcher(signature).matches());
		}
	}

	@Test
	// 55.296 tests
	public void parameterListTest() throws InvalidPatternException {
		final PatternParser parser = new PatternParser();
		final String signature = "public static void default.package.Clazz.getVal(int, java.lang.String)";

		final PatternConstructor positivePattern = new PatternConstructor();
		positivePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("*,..*")
				.addparameterListVariant(".., int, java.lang.String").addparameterListVariant("int, java.lang.String, ..").addparameterListVariant(".., ..")
				.addparameterListVariant("int,.., java.lang.String").addparameterListVariant("int,..,.., java.lang.String, ..")
				.addwhitespaceVariant(" ");

		final List<String> positivePatternList = positivePattern.getPattern();
		for (final String pattern : positivePatternList) {
			Assert.assertTrue(parser.parseToPattern(pattern).matcher(signature).matches());
		}
	}

	@Test
	// 104.448 tests
	public void signatureTest() throws InvalidPatternException {
		final PatternParser parser = new PatternParser();
		final String signature = "public static void default.package.Clazz.getVal(int, java.lang.String)";

		final PatternConstructor positivePattern = new PatternConstructor();
		positivePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("*,..*")
				.addparameterListVariant(".., int, java.lang.String").addparameterListVariant("int, java.lang.String, ..")
				.addparameterListVariant("int,.., java.lang.String")
				.addwhitespaceVariant(" ");

		final List<String> positivePatternList = positivePattern.getPattern();
		for (final String pattern : positivePatternList) {
			Assert.assertTrue(parser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongVisibilityPattern = new PatternConstructor();
		wrongVisibilityPattern.addVisibilityVariant("private").addVisibilityVariant("protected").addVisibilityVariant("package")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addwhitespaceVariant(" ");
		final List<String> wrongVisibilityPatternList = wrongVisibilityPattern.getPattern();
		for (final String pattern : wrongVisibilityPatternList) {
			Assert.assertFalse(parser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongStaticPattern = new PatternConstructor();
		wrongStaticPattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addStaticNonStaticVariant("non_static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addwhitespaceVariant(" ");
		final List<String> wrongStaticPatternList = wrongStaticPattern.getPattern();
		for (final String pattern : wrongStaticPatternList) {
			Assert.assertFalse(parser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongNativePattern = new PatternConstructor();
		wrongNativePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addwhitespaceVariant(" ");
		final List<String> wrongNativePatternList = wrongNativePattern.getPattern();
		for (final String pattern : wrongNativePatternList) {
			Assert.assertFalse(parser.parseToPattern(pattern).matcher(signature).matches());
		}
	}

	@Test
	// 160 tests
	public void patternTest() throws InvalidPatternException {
		final PatternParser parser = new PatternParser();
		final String pattern = "public void ..Clazz.get*(int, ..)";
		final Matcher matcher = parser.parseToPattern(pattern).matcher("");

		final SignatureConstructor positiveSignature = new SignatureConstructor();
		positiveSignature.addVisibilityVariant("public")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("void")
				.addfqClassNameVariant("default.package.Clazz")
				.addoperationNameVariant("getVal").addoperationNameVariant("get")
				.addparameterListVariant("int, java.lang.String").addparameterListVariant("int");
		final List<String> positiveSignatureList = positiveSignature.getSignatures();
		for (final String signature : positiveSignatureList) {
			Assert.assertTrue(matcher.reset(signature).matches());
		}

		final SignatureConstructor wrongVisibilitySignature = new SignatureConstructor();
		wrongVisibilitySignature.addVisibilityVariant("private").addVisibilityVariant("protected").addVisibilityVariant("")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("void")
				.addfqClassNameVariant("default.package.Clazz")
				.addoperationNameVariant("getVal").addoperationNameVariant("get")
				.addparameterListVariant("int,int").addparameterListVariant("int, java.lang.String");
		final List<String> wrongVisibilitySignatureList = wrongVisibilitySignature.getSignatures();
		for (final String signature : wrongVisibilitySignatureList) {
			Assert.assertFalse(matcher.reset(signature).matches());
		}

		final SignatureConstructor wrongRetTypeSignature = new SignatureConstructor();
		wrongRetTypeSignature.addVisibilityVariant("public")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("").addreturnTypeVariant("int").addreturnTypeVariant("java.lang.String")
				.addfqClassNameVariant("default.package.Clazz")
				.addoperationNameVariant("getVal").addoperationNameVariant("get")
				.addparameterListVariant("int,int").addparameterListVariant("int, java.lang.String");
		final List<String> wrongRetTypeSignatureList = wrongRetTypeSignature.getSignatures();
		for (final String signature : wrongRetTypeSignatureList) {
			Assert.assertFalse(matcher.reset(signature).matches());
		}

		final SignatureConstructor wrongFQClassNameSignature = new SignatureConstructor();
		wrongFQClassNameSignature.addVisibilityVariant("public")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("void")
				.addfqClassNameVariant("default.package.Class")
				.addoperationNameVariant("getVal").addoperationNameVariant("get")
				.addparameterListVariant("int,int").addparameterListVariant("int, java.lang.String");
		final List<String> wrongFQClassNameSignatureList = wrongFQClassNameSignature.getSignatures();
		for (final String signature : wrongFQClassNameSignatureList) {
			Assert.assertFalse(matcher.reset(signature).matches());
		}

		final SignatureConstructor wrongOperationNameSignature = new SignatureConstructor();
		wrongOperationNameSignature.addVisibilityVariant("public")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("void")
				.addfqClassNameVariant("default.package.Clazz")
				.addoperationNameVariant("set").addoperationNameVariant("yougetit")
				.addparameterListVariant("int,int").addparameterListVariant("int, java.lang.String");
		final List<String> wrongOperationNameSignatureList = wrongOperationNameSignature.getSignatures();
		for (final String signature : wrongOperationNameSignatureList) {
			Assert.assertFalse(matcher.reset(signature).matches());
		}

		final SignatureConstructor wrongParamListSignature = new SignatureConstructor();
		wrongParamListSignature.addVisibilityVariant("public")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("void")
				.addfqClassNameVariant("default.package.Clazz")
				.addoperationNameVariant("getVal").addoperationNameVariant("get")
				.addparameterListVariant("java.lang.String, int").addparameterListVariant("");
		final List<String> wrongParamListSignatureList = wrongParamListSignature.getSignatures();
		for (final String signature : wrongParamListSignatureList) {
			Assert.assertFalse(matcher.reset(signature).matches());
		}
	}

	private int i = 0;

	private void checkCombination(final String patternStr, final String visibility, final String staticNonStatic, final String nativeNonNative,
			final String returnTypeOrNew, final String fqClassName, final String operationName, final String paramList) throws InvalidPatternException {

		final StringBuilder signatureBuilder = new StringBuilder();

		/* Visibility */
		if ("public".equals(visibility)) {
			signatureBuilder.append("public").append(' ');
		} else if ("private".equals(visibility)) {
			signatureBuilder.append("private").append(' ');
		} else if ("package".equals(visibility)) {
		} else if ("protected".equals(visibility)) {
			signatureBuilder.append("protected").append(' ');
		} else if ("".equals(visibility)) {
		} else {
			Assert.fail("Invalid visibility: " + visibility);
		}

		/* Static/Non-static */
		if ("static".equals(staticNonStatic)) {
			signatureBuilder.append("static").append(' ');
		} else if ("non_static".equals(staticNonStatic)) {
		} else if ("".equals(staticNonStatic)) {
		} else {
			Assert.fail("Invalid staticNonStatic: " + staticNonStatic);
		}

		/* Static/Non-static */
		if ("native".equals(nativeNonNative)) {
			signatureBuilder.append("native").append(' ');
		} else if ("non_native".equals(nativeNonNative)) {
		} else if ("".equals(nativeNonNative)) {
		} else {
			Assert.fail("Invalid nativeNonNative: " + nativeNonNative);
		}

		/* Return type/Constructor */
		if ("new".equals(returnTypeOrNew)) {
		} else if ("*".equals(returnTypeOrNew)) {
			signatureBuilder.append("void").append(' ');
		} else if ("..*".equals(returnTypeOrNew)) {
			signatureBuilder.append("java.util.List").append(' ');
		} else if ("java.lang.String".equals(returnTypeOrNew)) {
			signatureBuilder.append("java.lang.String").append(' ');
		} else {
			Assert.fail("Invalid returnTypeOrNew: " + returnTypeOrNew);
		}

		/* classname */
		if ("a.b.C".equals(fqClassName)) {
			signatureBuilder.append("a.b.C");
		} else if ("a.b.*".equals(fqClassName)) {
			signatureBuilder.append("a.b.C");
		} else if ("*".equals(fqClassName)) {
			signatureBuilder.append("C");
		} else if ("..*".equals(fqClassName)) {
			signatureBuilder.append("a.b.C");
		} else {
			Assert.fail("Invalid fqClassName: " + fqClassName);
		}
		signatureBuilder.append('.');

		/* Operation name */
		if ("doIt".equals(operationName)) {
			signatureBuilder.append("doIt");
		} else if ("get*".equals(operationName)) {
			signatureBuilder.append("getBlup");
		} else if ("*".equals(operationName)) {
			signatureBuilder.append("foo");
		} else {
			Assert.fail("Invalid operationName: " + operationName);
		}
		signatureBuilder.append('(');

		/* Parameter list */
		if ("".equals(paramList)) {
		} else if ("*".equals(paramList)) {
			signatureBuilder.append("boolean");
		} else if ("A, B".equals(paramList)) {
			signatureBuilder.append("A, B");
		} else if ("..".equals(paramList)) {
			signatureBuilder.append("long, java.lang.und.short");
		} else {
			Assert.fail("Invalid paramList: " + paramList);
		}
		signatureBuilder.append(')');

		final String signature = signatureBuilder.toString();

		final PatternParser myParser = new PatternParser();
		final Pattern pattern = myParser.parseToPattern(patternStr);
		final Matcher m = pattern.matcher(signature);

		Assert.assertTrue((this.i++) + "Unexpected match result.\nregExp: " + pattern.toString() + "\npattern: " + pattern
				+ "\nsignature: " + signature, m.matches());
	}

	@Test
	// 103.680 tests
	public void testBasic() throws InvalidPatternException {
		final PatternParser myParser = new PatternParser();

		final String[] visibilities = { "public", "private", "package", "protected", "" };
		final String[] staticNonStatics = { "static", "non_static", "" };
		final String[] nativeNonNatives = { "native", "non_native", "" };
		final String[] returnTypesOrNews = { "java.lang.String", "new", "*", "..*" };
		final String[] fqClassNames = { "a.b.C", "a.b.*", "*", "..*" };
		final String[] operationNames = { "doIt", "get*", "*" };
		final String[] paramLists = { "", "*", "A, B", ".." };
		final String[] whites = { " ", "  ", "\t" };
		final String[] whiteAndNoWhite = { " ", "  ", "\t", /* in addition to whites: */"" };

		// works
		// final String signature01 = "public static void package.Class.method(A, B)";
		// final boolean[] visibilityMatches = { true, false, false, false, true };
		// final boolean[] staticNonStaticMatches = { true, false, true };
		// final boolean[] nativeNonNativeMatches = { false, true, true };
		// final boolean[] returnTypeOrNewMatches = { false, false, true, true };
		// final boolean[] fqClassNameMatches = { false, false, false, true };
		// final boolean[] operationNameMatches = { false, false, true };
		// final boolean[] paramListMatches = { false, false, true, true };

		// works
		final String signature01 = "public static native void package.Class.method()";
		final boolean[] visibilityMatches = { true, false, false, false, true };
		final boolean[] staticNonStaticMatches = { true, false, true };
		final boolean[] nativeNonNativeMatches = { true, false, true };
		final boolean[] returnTypeOrNewMatches = { false, false, true, true };
		final boolean[] fqClassNameMatches = { false, false, false, true };
		final boolean[] operationNameMatches = { false, false, true };
		final boolean[] paramListMatches = { true, false, false, true };

		// works
		// final String signature01 = "public static native void package.Class.method(A, B)";
		// final boolean[] visibilityMatches = { true, false, false, false, true };
		// final boolean[] staticNonStaticMatches = { true, false, true };
		// final boolean[] nativeNonNativeMatches = { true, false, true };
		// final boolean[] returnTypeOrNewMatches = { false, false, true, true };
		// final boolean[] fqClassNameMatches = { false, false, false, true };
		// final boolean[] operationNameMatches = { false, false, true };
		// final boolean[] paramListMatches = { false, false, true, true };

		for (int visibilityIdy = 0; visibilityIdy < visibilities.length; visibilityIdy++) {
			for (int staticNonStaticIdx = 0; staticNonStaticIdx < staticNonStatics.length; staticNonStaticIdx++) {
				for (int nativeNonNativeIdx = 0; nativeNonNativeIdx < nativeNonNatives.length; nativeNonNativeIdx++) {
					for (int returnTypeOrNewIdx = 0; returnTypeOrNewIdx < returnTypesOrNews.length; returnTypeOrNewIdx++) {
						for (int fqClassNameIdx = 0; fqClassNameIdx < fqClassNames.length; fqClassNameIdx++) {
							for (int operationNameIdx = 0; operationNameIdx < operationNames.length; operationNameIdx++) {
								for (int paramListIdx = 0; paramListIdx < paramLists.length; paramListIdx++) {
									for (final String white : whites) {
										for (final String whiteOrEmpty : whiteAndNoWhite) {
											final StringBuilder patternBuilder = new StringBuilder();
											patternBuilder.append(whiteOrEmpty);
											if (visibilities[visibilityIdy].length() > 0) {
												patternBuilder.append(visibilities[visibilityIdy]).append(white);
											}
											if (staticNonStatics[staticNonStaticIdx].length() > 0) {
												patternBuilder.append(staticNonStatics[staticNonStaticIdx]).append(white);
											}
											if (nativeNonNatives[nativeNonNativeIdx].length() > 0) {
												patternBuilder.append(nativeNonNatives[nativeNonNativeIdx]).append(white);
											}
											patternBuilder.append(returnTypesOrNews[returnTypeOrNewIdx]).append(white);
											patternBuilder.append(fqClassNames[fqClassNameIdx]).append('.');
											patternBuilder.append(operationNames[operationNameIdx]).append(whiteOrEmpty).append('(').append(whiteOrEmpty);
											if (paramLists[paramListIdx].length() > 0) {
												patternBuilder.append(paramLists[paramListIdx]).append(whiteOrEmpty);
											}
											patternBuilder.append(')').append(whiteOrEmpty);

											final String patternStr = patternBuilder.toString();

											// System.out.println(i++ + ": " + pattern);

											final boolean expected =
													visibilityMatches[visibilityIdy]
															&& staticNonStaticMatches[staticNonStaticIdx]
															&& nativeNonNativeMatches[nativeNonNativeIdx]
															&& returnTypeOrNewMatches[returnTypeOrNewIdx]
															&& fqClassNameMatches[fqClassNameIdx]
															&& operationNameMatches[operationNameIdx]
															&& paramListMatches[paramListIdx];

											this.checkCombination(patternStr, visibilities[visibilityIdy], staticNonStatics[staticNonStaticIdx]
													, nativeNonNatives[nativeNonNativeIdx]
													, returnTypesOrNews[returnTypeOrNewIdx]
													, fqClassNames[fqClassNameIdx]
													, operationNames[operationNameIdx]
													, paramLists[paramListIdx]);

											// final Pattern pattern = myParser.parseToPattern(patternStr);
											// final Matcher m = pattern.matcher(signature01);
											// final boolean result = m.matches();
											// Assert.assertEquals("expected: " + expected + ", but was: " + result, expected, result);
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

	@Test
	public void constructorTest() throws InvalidPatternException {
		final PatternParser parser = new PatternParser();
		final String constructorPattern = "new ..*.*(..)"; // should match all constructors and nothing else

		final String constructorSignature = "public package.Class.constructor()";
		final String methodSignature = "public void package.Class.method()";

		final Matcher constructorMatcher = parser.parseToPattern(constructorPattern).matcher("");
		Assert.assertTrue(constructorMatcher.reset(constructorSignature).matches());
		Assert.assertFalse(constructorMatcher.reset(methodSignature).matches());

		final SignatureConstructor positiveSignature = new SignatureConstructor();
		positiveSignature.addVisibilityVariant("public").addVisibilityVariant("private").addVisibilityVariant("protected").addVisibilityVariant("")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("")
				.addfqClassNameVariant("package.Class").addfqClassNameVariant("").addfqClassNameVariant("Class")
				.addoperationNameVariant("constructor").addoperationNameVariant("hierGehtAlles")
				.addparameterListVariant("").addparameterListVariant("int").addparameterListVariant("int, Class");
		final List<String> positiveSignatureList = positiveSignature.getSignatures();
		for (final String signature : positiveSignatureList) {
			Assert.assertTrue("pattern: " + constructorPattern + ", signature: " + signature, constructorMatcher.reset(signature).matches());
		}

		final SignatureConstructor negativeSignature = new SignatureConstructor();
		negativeSignature.addVisibilityVariant("public").addVisibilityVariant("private").addVisibilityVariant("protected").addVisibilityVariant("")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("*")
				.addfqClassNameVariant("package.Class").addfqClassNameVariant("").addfqClassNameVariant("Class")
				.addoperationNameVariant("constructor").addoperationNameVariant("hierGehtAlles")
				.addparameterListVariant("").addparameterListVariant("int").addparameterListVariant("int, Class");
		final List<String> wrongVisibilitySignatureList = negativeSignature.getSignatures();
		for (final String signature : wrongVisibilitySignatureList) {
			Assert.assertFalse(constructorMatcher.reset(signature).matches());
		}
	}
}
