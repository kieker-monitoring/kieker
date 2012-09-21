package kieker.test.monitoring.junit.core.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import kieker.monitoring.core.helper.InvalidPatternException;
import kieker.monitoring.core.helper.PatternParser;

public class TestPatternParser { // TODO: extends AbstractKiekerTest

	PatternParser parser = new PatternParser();

	@Test
	public void testPatternParser() throws InvalidPatternException {
		final String pattern = "*";
		final String signature = "ganz egal";
		final Pattern p = this.parser.parseToPattern(pattern);
		final Matcher m = p.matcher(signature);
		Assert.assertTrue(m.matches());
	}

	@Test
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

		final String signature01 = "public static void package.Class.method(A, B)";
		final boolean[] visibilityMatches = { true, false, false, false, true };
		final boolean[] staticNonStaticMatches = { true, false, true };
		final boolean[] nativeNonNativeMatches = { true, false, true };
		final boolean[] returnTypeOrNewMatches = { false, false, true, true };
		final boolean[] fqClassNameMatches = { false, false, false, true };
		final boolean[] operationNameMatches = { false, false, true };
		final boolean[] paramListMatches = { false, false, true, true };

		// Bug: * in param list should not match empty:
		// final String signature01 = "public static native void package.Class.method()";
		// final boolean[] visibilityMatches = { true, false, false, false, true };
		// final boolean[] staticNonStaticMatches = { true, false, true };
		// final boolean[] nativeNonNativeMatches = { true, false, true };
		// final boolean[] returnTypeOrNewMatches = { false, false, true, true };
		// final boolean[] fqClassNameMatches = { false, false, false, true };
		// final boolean[] operationNameMatches = { false, false, true };
		// final boolean[] paramListMatches = { true, false, false, true };

		// java.lang.AssertionError: 98472Unexpected match result.
		// pattern: native * ..*.* ( A, B )
		// signature: public static native void package.Class.method(A, B) expected:<true> but was:<false>
		// final String signature01 = "public static native void package.Class.method(A, B)";
		// final boolean[] visibilityMatches = { true, false, false, false, true };
		// final boolean[] staticNonStaticMatches = { true, false, true };
		// final boolean[] nativeNonNativeMatches = { true, false, true };
		// final boolean[] returnTypeOrNewMatches = { false, false, true, true };
		// final boolean[] fqClassNameMatches = { false, false, false, true };
		// final boolean[] operationNameMatches = { false, false, true };
		// final boolean[] paramListMatches = { false, false, true, true };

		int i = 0;
		for (int visibilityIdy = 0; visibilityIdy < visibilities.length; visibilityIdy++) {
			for (int staticNonStaticIdx = 0; staticNonStaticIdx < staticNonStatics.length; staticNonStaticIdx++) {
				for (int nativeNonNativeIdx = 0; nativeNonNativeIdx < nativeNonNatives.length; nativeNonNativeIdx++) {
					for (int returnTypeOrNewIdx = 0; returnTypeOrNewIdx < returnTypesOrNews.length; returnTypeOrNewIdx++) {
						for (int fqClassNameIdx = 0; fqClassNameIdx < fqClassNames.length; fqClassNameIdx++) {
							for (int operationNameIdx = 0; operationNameIdx < operationNames.length; operationNameIdx++) {
								for (int paramListIdx = 0; paramListIdx < paramLists.length; paramListIdx++) {
									for (final String white : whites) {
										for (final String whiteOrEmpty : whiteAndNoWhite) {
											final StringBuilder strB = new StringBuilder();
											strB.append(whiteOrEmpty);
											if (visibilities[visibilityIdy].length() > 0) {
												strB.append(visibilities[visibilityIdy]).append(white);
											}
											if (staticNonStatics[staticNonStaticIdx].length() > 0) {
												strB.append(staticNonStatics[staticNonStaticIdx]).append(white);
											}
											if (nativeNonNatives[nativeNonNativeIdx].length() > 0) {
												strB.append(nativeNonNatives[nativeNonNativeIdx]).append(white);
											}
											strB.append(returnTypesOrNews[returnTypeOrNewIdx]).append(white);
											strB.append(fqClassNames[fqClassNameIdx]).append('.');
											strB.append(operationNames[operationNameIdx]).append(whiteOrEmpty).append('(').append(whiteOrEmpty);
											if (paramLists[paramListIdx].length() > 0) {
												strB.append(paramLists[paramListIdx]).append(whiteOrEmpty);
											}
											strB.append(')').append(whiteOrEmpty);

											final String pattern = strB.toString();

											// System.out.println(i++ + ": " + pattern);

											final boolean expected =
													visibilityMatches[visibilityIdy]
															&& staticNonStaticMatches[staticNonStaticIdx]
															&& nativeNonNativeMatches[nativeNonNativeIdx]
															&& returnTypeOrNewMatches[returnTypeOrNewIdx]
															&& fqClassNameMatches[fqClassNameIdx]
															&& operationNameMatches[operationNameIdx]
															&& paramListMatches[paramListIdx];

											final Matcher m = myParser.parseToPattern(pattern).matcher(signature01);

											Assert.assertEquals((i++) + "Unexpected match result.\npattern: " + pattern + "\nsignature: " + signature01,
													expected, m.matches());
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
	public void testPublicModifier() throws InvalidPatternException {
		final String pattern01 = "public static native ..* ..*.*(..)"; // should match all public static native methods
		final String pattern02 = "public static non_native ..* ..*.*(..)"; // should match all public static and not native methods
		final String pattern03 = "public static ..* ..*.*(..)"; // should match all public static methods, native or not
		final String pattern04 = "public non_static native ..* ..*.*(..)"; // should match all public native and not static methods
		final String pattern05 = "public non_static non_native ..* ..*.*(..)"; // should match all public and not static or native methods
		final String pattern06 = "public non_static ..* ..*.*(..)";
		final String pattern07 = "public native ..* ..*.*(..)";
		final String pattern08 = "public non_native ..* ..*.*(..)";
		final String pattern09 = "public ..* ..*.*(..)"; // should match all public methods
		final String pattern10 = "..* ..*.*(..)"; // should match all methods

		final String signature01 = "public static native void package.Class.method()"; // true: 01,03,07,09,10
		final String signature02 = "public static void package.Class.method()"; // true: 02,03,08,09,10
		final String signature03 = "public native void package.Class.method()"; // true: 04,06,07,09,10
		final String signature04 = "public void package.Class.method()"; // true: 05,06,08,09,10
		final String signature05 = "private void package.Class.method()"; // true: 10

		final Matcher m01 = this.parser.parseToPattern(pattern01).matcher("");
		Assert.assertTrue(m01.reset(signature01).matches());
		Assert.assertFalse(m01.reset(signature02).matches());
		Assert.assertFalse(m01.reset(signature03).matches());
		Assert.assertFalse(m01.reset(signature04).matches());
		Assert.assertFalse(m01.reset(signature05).matches());

		final Matcher m02 = this.parser.parseToPattern(pattern02).matcher("");
		Assert.assertFalse(m02.reset(signature01).matches());
		Assert.assertTrue(m02.reset(signature02).matches());
		Assert.assertFalse(m02.reset(signature03).matches());
		Assert.assertFalse(m02.reset(signature04).matches());
		Assert.assertFalse(m02.reset(signature05).matches());
	}

	@Test
	public void testConstructor() throws InvalidPatternException {
		final String constructorPattern = "new ..*.*(..)"; // should match all constructors and nothing else

		final String constructorSignature = "public package.Class.constructor()";
		final String methodSignature = "public void package.Class.method()";

		final Matcher constructorMatcher = this.parser.parseToPattern(constructorPattern).matcher(constructorSignature);
		Assert.assertTrue(constructorMatcher.reset(constructorSignature).matches());
		Assert.assertFalse(constructorMatcher.reset(methodSignature).matches());
	}
}
