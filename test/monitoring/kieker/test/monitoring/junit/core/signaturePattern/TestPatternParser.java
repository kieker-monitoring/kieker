/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.core.signaturePattern;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternParser;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.signaturePattern.PatternConstructor;
import kieker.test.monitoring.util.signaturePattern.SignatureConstructor;

/**
 * @author Bjoern Weissenfels, Andre van Hoorn, Jan Waller
 * 
 * @since 1.6
 */
public class TestPatternParser extends AbstractKiekerTest {

	/**
	 * Default constructor.
	 */
	public TestPatternParser() {
		// empty default constructor
	}

	@Ignore("taking too long")
	@Test
	// works : 810.000 tests 2.531.250.000
	public void whitespaceTest() throws InvalidPatternException {
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

		final List<String> positivePatternList = positivePattern.createPatterns();
		for (final String pattern : positivePatternList) {
			Assert.assertTrue("pattern: " + pattern + "; signature: " + signature, PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}
	}

	@Ignore("taking too long")
	@Test
	// 442.368 tests
	public void parameterListTest() throws InvalidPatternException {
		final String signature = "public static void default.package.Clazz.getVal(int, java.lang.String)";

		final PatternConstructor positivePattern = new PatternConstructor();
		positivePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("*,..*")
				.addparameterListVariant(".., int, java.lang.String").addparameterListVariant("int, java.lang.String, ..").addparameterListVariant(".., ..")
				.addparameterListVariant("int,.., java.lang.String").addparameterListVariant("int,..,.., java.lang.String, ..")
				.addthrowsListVariant("")
				.addwhitespaceVariant(" ");

		final List<String> positivePatternList = positivePattern.createPatterns();
		for (final String pattern : positivePatternList) {
			Assert.assertTrue(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}
	}

	@Ignore("taking too long")
	@Test
	// 3.932.160 tests
	public void signatureTest() throws InvalidPatternException {
		final String signature = "public static void default.package.Clazz.getVal(int, java.lang.String) throws Exception";

		final PatternConstructor positivePattern = new PatternConstructor();
		positivePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("*,..*")
				.addparameterListVariant(".., int, java.lang.String").addparameterListVariant("int, java.lang.String, ..")
				.addparameterListVariant("int,.., java.lang.String")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");

		final List<String> positivePatternList = positivePattern.createPatterns();
		for (final String pattern : positivePatternList) {
			Assert.assertTrue(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongVisibilityPattern = new PatternConstructor();
		wrongVisibilityPattern.addVisibilityVariant("private").addVisibilityVariant("protected").addVisibilityVariant("package")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongVisibilityPatternList = wrongVisibilityPattern.createPatterns();
		for (final String pattern : wrongVisibilityPatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongAbstractPattern = new PatternConstructor();
		wrongAbstractPattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongAbstractPatternList = wrongAbstractPattern.createPatterns();
		for (final String pattern : wrongAbstractPatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongStaticPattern = new PatternConstructor();
		wrongStaticPattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("non_static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongStaticPatternList = wrongStaticPattern.createPatterns();
		for (final String pattern : wrongStaticPatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongFinalPattern = new PatternConstructor();
		wrongFinalPattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongFinalPatternList = wrongFinalPattern.createPatterns();
		for (final String pattern : wrongFinalPatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongSynchronizedPattern = new PatternConstructor();
		wrongSynchronizedPattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongSynchronizedPatternList = wrongSynchronizedPattern.createPatterns();
		for (final String pattern : wrongSynchronizedPatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongNativePattern = new PatternConstructor();
		wrongNativePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongNativePatternList = wrongNativePattern.createPatterns();
		for (final String pattern : wrongNativePatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongRetTypePattern = new PatternConstructor();
		wrongRetTypePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("new").addreturnTypeVariant("int")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongRetTypePatternList = wrongRetTypePattern.createPatterns();
		for (final String pattern : wrongRetTypePatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongClassNamePattern = new PatternConstructor();
		wrongClassNamePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("Clazz").addfqClassNameVariant("package.Clazz").addfqClassNameVariant("default.Clazz")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongClassNamePatternList = wrongClassNamePattern.createPatterns();
		for (final String pattern : wrongClassNamePatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongOperationNamePattern = new PatternConstructor();
		wrongOperationNamePattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("setVal").addoperationNameVariant("get").addoperationNameVariant("getValue")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongOperationNamePatternList = wrongOperationNamePattern.createPatterns();
		for (final String pattern : wrongOperationNamePatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongParamListPattern = new PatternConstructor();
		wrongParamListPattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("").addparameterListVariant("int").addparameterListVariant("int,String").addparameterListVariant("java.lang.String")
				.addthrowsListVariant("Exception").addthrowsListVariant("..")
				.addwhitespaceVariant(" ");
		final List<String> wrongParamListPatternList = wrongParamListPattern.createPatterns();
		for (final String pattern : wrongParamListPatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}

		final PatternConstructor wrongThrowsListPattern = new PatternConstructor();
		wrongThrowsListPattern.addVisibilityVariant("").addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("non_abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("non_final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("non_synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("non_native")
				.addreturnTypeVariant("*").addreturnTypeVariant("..*").addreturnTypeVariant("void").addreturnTypeVariant("v*d")
				.addfqClassNameVariant("..*").addfqClassNameVariant("default.package.Clazz").addfqClassNameVariant("default..Clazz")
				.addfqClassNameVariant("..package.C*z")
				.addoperationNameVariant("*").addoperationNameVariant("getVal").addoperationNameVariant("get*")
				.addparameterListVariant("..").addparameterListVariant("int, java.lang.String").addparameterListVariant("int,..").addparameterListVariant("..*,..*")
				.addthrowsListVariant("").addthrowsListVariant("SpecialException")
				.addwhitespaceVariant(" ");
		final List<String> wrongThrowsListPatternList = wrongThrowsListPattern.createPatterns();
		for (final String pattern : wrongThrowsListPatternList) {
			Assert.assertFalse(PatternParser.parseToPattern(pattern).matcher(signature).matches());
		}
	}

	@Test
	// 160 tests
	public void patternTest() throws InvalidPatternException {
		final String pattern = "public void ..Clazz.get*(int, ..) throws ..";
		final Matcher matcher = PatternParser.parseToPattern(pattern).matcher("");

		final SignatureConstructor positiveSignature = new SignatureConstructor();
		positiveSignature.addVisibilityVariant("public")
				.addAbstractNonAbstractVariant("").addAbstractNonAbstractVariant("abstract")
				.addStaticNonStaticVariant("").addStaticNonStaticVariant("static")
				.addFinalNonFinalVariant("").addFinalNonFinalVariant("final")
				.addSynchronizedNonSynchronizedVariant("").addSynchronizedNonSynchronizedVariant("synchronized")
				.addNativeNonNativeVariant("").addNativeNonNativeVariant("native")
				.addreturnTypeVariant("void")
				.addfqClassNameVariant("default.package.Clazz")
				.addoperationNameVariant("getVal").addoperationNameVariant("get")
				.addparameterListVariant("int, java.lang.String").addparameterListVariant("int")
				.addthrowsListVariant("").addthrowsListVariant("Exception").addthrowsListVariant("OtherException");
		final List<String> positiveSignatureList = positiveSignature.getSignatures();
		for (final String signature : positiveSignatureList) {
			Assert.assertTrue(matcher.reset(signature).matches());
		}

		final SignatureConstructor wrongVisibilitySignature = new SignatureConstructor();
		wrongVisibilitySignature.addVisibilityVariant("private").addVisibilityVariant("protected").addVisibilityVariant("").addVisibilityVariant("..")
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

	private void checkCombination(final String patternStr, final String visibility, final String staticNonStatic, final String nativeNonNative,
			final String returnTypeOrNew, final String fqClassName, final String operationName, final String paramList) throws InvalidPatternException {

		final StringBuilder signatureBuilder = new StringBuilder(128);

		// Visibility
		if ("public".equals(visibility)) {
			signatureBuilder.append("public ");
		} else if ("private".equals(visibility)) {
			signatureBuilder.append("private ");
		} else if ("package".equals(visibility)) { // NOPMD NOCS
			// nothing to do
		} else if ("protected".equals(visibility)) {
			signatureBuilder.append("protected ");
		} else if ("".equals(visibility)) { // NOPMD NOCS
			// nothing to do
		} else {
			Assert.fail("Invalid visibility: " + visibility);
		}

		// Static/Non-static
		if ("static".equals(staticNonStatic)) {
			signatureBuilder.append("static ");
		} else if ("non_static".equals(staticNonStatic)) { // NOPMD NOCS
			// nothing to do
		} else if ("".equals(staticNonStatic)) { // NOPMD NOCS
			// nothing to do
		} else {
			Assert.fail("Invalid staticNonStatic: " + staticNonStatic);
		}

		// Static/Non-static
		if ("native".equals(nativeNonNative)) {
			signatureBuilder.append("native ");
		} else if ("non_native".equals(nativeNonNative)) { // NOPMD NOCS
			// nothing to do
		} else if ("".equals(nativeNonNative)) { // NOPMD NOCS
			// nothing to do
		} else {
			Assert.fail("Invalid nativeNonNative: " + nativeNonNative);
		}

		// Return type/Constructor
		if ("new".equals(returnTypeOrNew)) { // NOPMD NOCS
			// nothing to do
		} else if ("*".equals(returnTypeOrNew)) {
			signatureBuilder.append("void ");
		} else if ("..*".equals(returnTypeOrNew)) {
			signatureBuilder.append("java.util.List ");
		} else if ("java.lang.String".equals(returnTypeOrNew)) {
			signatureBuilder.append("java.lang.String ");
		} else {
			Assert.fail("Invalid returnTypeOrNew: " + returnTypeOrNew);
		}

		// classname
		if ("a.b.C".equals(fqClassName)) {
			signatureBuilder.append("a.b.C");
		} else if ("a.b.*".equals(fqClassName)) {
			signatureBuilder.append("a.b.C");
		} else if ("*".equals(fqClassName)) {
			signatureBuilder.append('C');
		} else if ("..*".equals(fqClassName)) {
			signatureBuilder.append("a.b.C");
		} else {
			Assert.fail("Invalid fqClassName: " + fqClassName);
		}
		signatureBuilder.append('.');

		// Operation name
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

		// Parameter list
		if ("".equals(paramList)) { // NOPMD NOCS
			// nothing to do
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

		final Pattern pattern = PatternParser.parseToPattern(patternStr);
		final Matcher m = pattern.matcher(signature);

		Assert.assertTrue("Unexpected match result.\nregExp: " + pattern.toString() + "\npattern: " + pattern
				+ "\nsignature: " + signature, m.matches());
	}

	@Test
	// 103.680 tests
	public void testBasic() throws InvalidPatternException {
		final String[] visibilities = { "public", "private", "package", "protected", "" };
		final String[] staticNonStatics = { "static", "non_static", "" };
		final String[] nativeNonNatives = { "native", "non_native", "" };
		final String[] returnTypesOrNews = { "java.lang.String", "new", "*", "..*" };
		final String[] fqClassNames = { "a.b.C", "a.b.*", "*", "..*" };
		final String[] operationNames = { "doIt", "get*", "*" };
		final String[] paramLists = { "", "*", "A, B", ".." };
		final String[] whites = { " ", "  ", "\t" };
		final String[] whiteAndNoWhite = { " ", "  ", "\t",
			"", }; // in addition to whites

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
			for (int staticNonStaticIdx = 0; staticNonStaticIdx < staticNonStatics.length; staticNonStaticIdx++) { // NOCS
				for (int nativeNonNativeIdx = 0; nativeNonNativeIdx < nativeNonNatives.length; nativeNonNativeIdx++) { // NOCS
					for (int returnTypeOrNewIdx = 0; returnTypeOrNewIdx < returnTypesOrNews.length; returnTypeOrNewIdx++) { // NOCS
						for (int fqClassNameIdx = 0; fqClassNameIdx < fqClassNames.length; fqClassNameIdx++) { // NOCS
							for (int operationNameIdx = 0; operationNameIdx < operationNames.length; operationNameIdx++) { // NOCS
								for (int paramListIdx = 0; paramListIdx < paramLists.length; paramListIdx++) { // NOCS
									for (final String white : whites) { // NOCS
										for (final String whiteOrEmpty : whiteAndNoWhite) { // NOCS
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

											final Pattern pattern = PatternParser.parseToPattern(patternStr);
											final Matcher m = pattern.matcher(signature01);
											final boolean result = m.matches();
											Assert.assertEquals("expected: " + expected + ", but was: " + result, expected, result);
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
		final String constructorPattern = "new ..*.*(..)"; // should match all constructors and nothing else

		final String constructorSignature = "public package.Class.constructor()";
		final String methodSignature = "public void package.Class.method()";

		final Matcher constructorMatcher = PatternParser.parseToPattern(constructorPattern).matcher("");
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
