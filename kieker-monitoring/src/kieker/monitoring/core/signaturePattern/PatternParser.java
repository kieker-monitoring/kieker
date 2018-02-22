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

package kieker.monitoring.core.signaturePattern;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Bjoern Weissenfels, Jan Waller
 *
 * @since 1.6
 */
public final class PatternParser {

	private static final String NON_NATIVE = "non_native";
	private static final String NATIVE = "native";
	private static final String NON_SYNCHRONIZED = "non_synchronized";
	private static final String SYNCHRONIZED = "synchronized";
	private static final String NON_FINAL = "non_final";
	private static final String FINAL = "final";
	private static final String NON_STATIC = "non_static";
	private static final String NON_ABSTRACT = "non_abstract";
	private static final String STATIC = "static";
	private static final String ABSTRACT = "abstract";
	private static final String PACKAGE = "package";
	private static final String MODIFIER_PROTECTED = "protected";
	private static final String MODIFIER_PRIVATE = "private";
	private static final String MODIFIER_PUBLIC = "public";

	private static final String FULLY_QUALFIED_NAME = "[\\p{javaJavaIdentifierPart}\\.])*\\p{javaJavaIdentifierPart}+";
	private static final String SIMPLE_NAME = "(\\p{javaJavaIdentifierPart})+";

	/**
	 * Private constructor to avoid initialization.
	 */
	private PatternParser() {
		// private default constructor
	}

	/**
	 * Parses the given pattern string and converts it into a {@link Pattern}
	 * instance.
	 *
	 * @param strPattern
	 *            The pattern string to parse.
	 * @return A corresponding pattern to the given string.
	 *
	 * @throws InvalidPatternException
	 *             If the given string is not a valid pattern.
	 */
	public static final Pattern parseToPattern(final String strPattern) throws InvalidPatternException {
		final String trimPattern = strPattern.trim();
		if (trimPattern.charAt(0) == SignatureFactory.PATTERN_PREFIX) {
			try {
				return Pattern.compile(trimPattern);
			} catch (final PatternSyntaxException pse) {
				throw new InvalidPatternException("Invalid regular expression", pse);
			}
		}
		final StringBuilder sb = new StringBuilder();
		if ("*".equals(trimPattern)) {
			sb.append(".*");
		} else {
			final int openingParenthesis = trimPattern.indexOf('(');
			final int closingParenthesis = trimPattern.indexOf(')');
			if ((openingParenthesis == -1) || (closingParenthesis == -1)
					|| (openingParenthesis != trimPattern.lastIndexOf('('))
					|| (closingParenthesis != trimPattern.lastIndexOf(')'))
					|| (openingParenthesis > closingParenthesis)) {
				throw new InvalidPatternException("Invalid parentheses");
			}

			String[] modifierList = null;
			final String[] tokens = trimPattern.substring(0, openingParenthesis).trim().split("\\s+"); // NOPMD

			final int numOfModifiers = tokens.length - 2;
			if (tokens.length > 2) {
				modifierList = new String[numOfModifiers];
				System.arraycopy(tokens, 0, modifierList, 0, numOfModifiers);
			}

			final String fqName = tokens[numOfModifiers + 1];

			final int index = fqName.lastIndexOf('.');
			if ((index == -1) || (index == (fqName.length() - 1))) {
				throw new InvalidPatternException("Invalid fully qualified type or method name.");
			}
			final String fqType = fqName.substring(0, index);
			final String methodName = fqName.substring(index + 1);

			final String params = trimPattern.substring(openingParenthesis + 1, closingParenthesis).trim();
			final String throwsPattern;
			final int throwsPatternStart = closingParenthesis + 1;
			if (throwsPatternStart < trimPattern.length()) {
				throwsPattern = trimPattern.substring(throwsPatternStart);
			} else {
				throwsPattern = null;
			}

			sb.append(PatternParser.parseModifierConstraintList(modifierList));
			sb.append(PatternParser.parseRetType(tokens[numOfModifiers])); // first token after modifiers in the return type
			sb.append(PatternParser.parseFQType(fqType));
			sb.append("\\.");
			sb.append(PatternParser.parseMethodName(methodName));
			sb.append("\\(");
			sb.append(PatternParser.parseParameterList(PatternParser.trimValues(params.trim().split(","))));
			sb.append("\\)");
			sb.append(PatternParser.parseThrowsPattern(throwsPattern));
		}
		return Pattern.compile(sb.toString());
	}

	private static String[] trimValues(final String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			strings[i] = strings[i].trim();
		}
		return strings;
	}

	private static final String parseMethodName(final String methodName) throws InvalidPatternException {
		try {
			return PatternParser.parseIdentifier(methodName);
		} catch (final InvalidPatternException ex) {
			throw new InvalidPatternException("Invalid method name.", ex);
		}
	}

	/**
	 * Parse parameter list.
	 *
	 * @param paramList
	 *            array of parameters, perfectly trimmed
	 * @return
	 * @throws InvalidPatternException
	 */
	private static final String parseParameterList(final String[] paramList) throws InvalidPatternException {
		if (paramList.length == 1) {
			if (paramList[0].length() == 0) {
				return "";
			} else if ("..".equals(paramList[0])) {
				return ".*";
			}
		}

		return PatternParser.parseMultipleParameters(paramList);
	}

	private static final String parseMultipleParameters(final String[] paramList) throws InvalidPatternException {
		final StringBuilder sb = new StringBuilder(255);

		final int length = paramList.length;

		int start = 1;

		if ("..".equals(paramList[0])) {
			sb.append("(((\\s)?" + FULLY_QUALFIED_NAME + "(\\s)?,)*");

			if (length > 1) {
				start = 2;
				PatternParser.createParameterRegex(sb, paramList[1]);
			}
		} else if ("*".equals(paramList[0])) {
			sb.append("(\\s)?(\\p{javaJavaIdentifierPart})+(\\s)?");
		} else if (paramList[0].length() == 0) {
			throw new InvalidPatternException("Invalid parameter list.");
		} else {
			try {
				sb.append("(\\s)?").append(PatternParser.parseFQType(paramList[0])).append("(\\s)?");
			} catch (final InvalidPatternException ex) {
				throw new InvalidPatternException("Invalid parameter list.", ex);
			}
		}

		for (int i = start; i < length; i++) {
			PatternParser.createParameterRegex(sb, paramList[i]);
		}

		return sb.toString();
	}

	/**
	 * Create the regular expression part for the given parameter.
	 *
	 * @param regexBuilder
	 *            used string builder
	 * @param parameter
	 *            the parameter to be processed
	 * @throws InvalidPatternException
	 *             on invalid pattern
	 */
	private static final void createParameterRegex(final StringBuilder regexBuilder, final String parameter) throws InvalidPatternException {
		if ("..".equals(parameter)) {
			regexBuilder.append("(,?((\\s)?" + FULLY_QUALFIED_NAME + "(\\s)?)*");
		} else if ("*".equals(parameter)) {
			regexBuilder.append(",?(\\s)?" + SIMPLE_NAME + "(\\s)?");
		} else if (parameter.length() == 0) {
			throw new InvalidPatternException("Invalid parameter list.");
		} else {
			try {
				regexBuilder.append(",?(\\s)?");
				regexBuilder.append(PatternParser.parseFQType(parameter));
				regexBuilder.append("(\\s)?");
			} catch (final InvalidPatternException ex) {
				throw new InvalidPatternException("Invalid parameter list.", ex);
			}
		}
	}

	private static final String parseIdentifier(final String identifier) throws InvalidPatternException {
		final char[] array = identifier.toCharArray();
		final StringBuilder sb = new StringBuilder(128);
		if (Character.isJavaIdentifierStart(array[0])) {
			sb.append(Character.toString(array[0]));
		} else if (array[0] == '*') {
			sb.append("(\\p{javaJavaIdentifierPart})*");
		} else {
			throw new InvalidPatternException("Identifier starts with invalid symbol.");
		}
		for (int i = 1; i < array.length; i++) {
			if (Character.isJavaIdentifierPart(array[i])) {
				sb.append(Character.toString(array[i]));
			} else if (array[i] == '*') {
				sb.append("(\\p{javaJavaIdentifierPart})*");
			} else {
				throw new InvalidPatternException("Identifier includes invalid symbol.");
			}
		}
		return sb.toString();
	}

	private static final String parseFQType(final String fqType) throws InvalidPatternException {
		if (fqType.contains("...") || fqType.endsWith(".") || (fqType.length() == 0)) {
			throw new InvalidPatternException("Invalid fully qualified type.");
		}
		final String[] tokens = fqType.split("\\.");
		if (tokens.length == 1) {
			try {
				return PatternParser.parseIdentifier(fqType);
			} catch (final InvalidPatternException ex) {
				throw new InvalidPatternException("Invalid fully qualified type.", ex);
			}
		} else {
			return PatternParser.parseFQTypeTokenized(tokens);
		}
	}

	private static String parseFQTypeTokenized(final String[] tokens) throws InvalidPatternException {
		int start = 0;
		final StringBuilder sb = new StringBuilder(128);
		// test if fq_type starts with ..
		if ((tokens[0].length() == 0) && (tokens[1].length() == 0)) {
			sb.append("(([\\p{javaJavaIdentifierPart}\\.])*\\.)?");
			start = 2;
		} else if (tokens[0].length() == 0) {
			throw new InvalidPatternException("Invalid fully qualified type: leading dot");
		}

		final int length = tokens.length;

		for (int i = start; i < (length - 1); i++) {
			if (tokens[i].length() == 0) {
				sb.append("(([\\p{javaJavaIdentifierPart}\\.])*\\.)?");
			} else {
				try {
					sb.append(PatternParser.parseIdentifier(tokens[i]));
				} catch (final InvalidPatternException ex) {
					throw new InvalidPatternException("Invalid fully qualified type.", ex);
				}
				sb.append("\\.");
			}
		}
		try {
			sb.append(PatternParser.parseIdentifier(tokens[length - 1]));
		} catch (final InvalidPatternException ex) {
			final InvalidPatternException newEx = new InvalidPatternException("Invalid fully qualified type.");
			throw (InvalidPatternException) newEx.initCause(ex);
		}
		return sb.toString();
	}

	private static final String parseRetType(final String retType) throws InvalidPatternException {
		if ("new".equals(retType)) {
			return "";
		} else {
			try {
				return PatternParser.parseFQType(retType) + "\\s";
			} catch (final InvalidPatternException ex) {
				throw new InvalidPatternException("Invalid return type.", ex);
			}
		}
	}

	private static final String parseModifierConstraintList(final String[] modifierList)
			throws InvalidPatternException {
		if (modifierList == null) {
			return "((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?";
		} else {
			return PatternParser.parseNonEmptyModifierContraintList(modifierList);
		}
	}

	private static String parseNonEmptyModifierContraintList(final String[] modifierList) throws InvalidPatternException {
		final Map<String, Integer> allowedModifiersWithOrder = new HashMap<>(); // NOPMD (no conc.
																				// access)
		allowedModifiersWithOrder.put(MODIFIER_PUBLIC, 0);
		allowedModifiersWithOrder.put(MODIFIER_PRIVATE, 0);
		allowedModifiersWithOrder.put(MODIFIER_PROTECTED, 0);
		allowedModifiersWithOrder.put(PACKAGE, 0);
		allowedModifiersWithOrder.put(ABSTRACT, 1);
		allowedModifiersWithOrder.put(NON_ABSTRACT, 1);
		allowedModifiersWithOrder.put(STATIC, 2);
		allowedModifiersWithOrder.put(NON_STATIC, 2);
		allowedModifiersWithOrder.put(FINAL, 3);
		allowedModifiersWithOrder.put(NON_FINAL, 3);
		allowedModifiersWithOrder.put(SYNCHRONIZED, 4);
		allowedModifiersWithOrder.put(NON_SYNCHRONIZED, 4);
		allowedModifiersWithOrder.put(NATIVE, 5);
		allowedModifiersWithOrder.put(NON_NATIVE, 5);
		final int numberOfModifiers = modifierList.length;
		// test whether modifiers are allowed and in the correct order
		Integer old = -1;
		for (int i = 0; i < numberOfModifiers; i++) {
			final Integer current = allowedModifiersWithOrder.get(modifierList[i]);
			if ((null == current) || (current < old)) {
				throw new InvalidPatternException("Invalid modifier");
			}
			old = current;
		}
		final StringBuilder sb = new StringBuilder();
		switch (numberOfModifiers) {
		case 1:
			PatternParser.onOneModifier(modifierList, sb);
			break;
		case 2:
			PatternParser.onTwoModifiers(modifierList, sb);
			break;
		case 3:
			PatternParser.onThreeModifiers(modifierList, sb);
			break;
		case 4:
			PatternParser.onFourModifiers(modifierList, sb);
			break;
		case 5:
			PatternParser.onFiveModifiers(modifierList, sb);
			break;
		case 6:
			PatternParser.onSixModifiers(modifierList, sb);
			break;
		default:
			throw new InvalidPatternException("Too many modifier.");
		}
		return sb.toString();
	}

	private static void onSixModifiers(final String[] modifierList, final StringBuilder sb)
			throws InvalidPatternException {
		PatternParser.appendScope(sb, modifierList[0], true);

		if (ABSTRACT.equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if (!NON_ABSTRACT.equals(modifierList[1])) {
			throw new InvalidPatternException("Invalid modifier.");
		}
		if (STATIC.equals(modifierList[2])) {
			sb.append("static\\s");
		} else if (!NON_STATIC.equals(modifierList[2])) {
			throw new InvalidPatternException("Invalid modifier.");
		}
		if (FINAL.equals(modifierList[3])) {
			sb.append("final\\s");
		} else if (!NON_FINAL.equals(modifierList[3])) {
			throw new InvalidPatternException("Invalid modifier.");
		}
		if (SYNCHRONIZED.equals(modifierList[4])) {
			sb.append("synchronized\\s");
		} else if (!NON_SYNCHRONIZED.equals(modifierList[4])) {
			throw new InvalidPatternException("Invalid modifier.");
		}

		PatternParser.checkNativeFail(sb, modifierList[5]);
	}

	private static void checkNativeFail(final StringBuilder sb, final String modifier) throws InvalidPatternException {
		if (NATIVE.equals(modifier)) {
			sb.append("native\\s");
		} else if (!NON_NATIVE.equals(modifier)) {
			throw new InvalidPatternException("Invalid modifier.");
		}
	}

	private static void onFiveModifiers(final String[] modifierList, final StringBuilder sb) throws InvalidPatternException {
		PatternParser.appendScope(sb, modifierList[0], false);

		if (ABSTRACT.equals(modifierList[0]) || ABSTRACT.equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if (!NON_ABSTRACT.equals(modifierList[0]) && !NON_ABSTRACT.equals(modifierList[1])) {
			sb.append("(abstract\\s)?");
		}
		if (STATIC.equals(modifierList[1]) || STATIC.equals(modifierList[2])) {
			sb.append("static\\s");
		} else if (!NON_STATIC.equals(modifierList[1]) && !NON_STATIC.equals(modifierList[2])) {
			sb.append("(static\\s)?");
		}
		if (FINAL.equals(modifierList[2]) || FINAL.equals(modifierList[3])) {
			sb.append("final\\s");
		} else if (!NON_FINAL.equals(modifierList[2]) && !NON_FINAL.equals(modifierList[3])) {
			sb.append("(final\\s)?");
		}
		if (SYNCHRONIZED.equals(modifierList[3]) || SYNCHRONIZED.equals(modifierList[4])) {
			sb.append("synchronized\\s");
		} else if (!NON_SYNCHRONIZED.equals(modifierList[3]) && !NON_SYNCHRONIZED.equals(modifierList[4])) {
			sb.append("(synchronized\\s)?");
		}

		PatternParser.checkNative(sb, modifierList[4]);
	}

	private static void onFourModifiers(final String[] modifierList, final StringBuilder sb) throws InvalidPatternException {
		PatternParser.appendScope(sb, modifierList[0], false);

		if (ABSTRACT.equals(modifierList[0]) || ABSTRACT.equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if (!NON_ABSTRACT.equals(modifierList[0]) && !NON_ABSTRACT.equals(modifierList[1])) {
			sb.append("(abstract\\s)?");
		}
		if (STATIC.equals(modifierList[0]) || STATIC.equals(modifierList[1]) || STATIC.equals(modifierList[2])) {
			sb.append("static\\s");
		} else if (!NON_STATIC.equals(modifierList[0]) && (!NON_STATIC.equals(modifierList[1])
				& !NON_STATIC.equals(modifierList[2]))) {
			sb.append("(static\\s)?");
		}
		if (FINAL.equals(modifierList[1]) || FINAL.equals(modifierList[2]) || FINAL.equals(modifierList[3])) {
			sb.append("final\\s");
		} else if ((!NON_FINAL.equals(modifierList[1]) & !NON_FINAL.equals(modifierList[2]))
				&& !NON_FINAL.equals(modifierList[3])) {
			sb.append("(final\\s)?");
		}
		if (SYNCHRONIZED.equals(modifierList[2]) || SYNCHRONIZED.equals(modifierList[3])) {
			sb.append("synchronized\\s");
		} else if (!NON_SYNCHRONIZED.equals(modifierList[2]) && !NON_SYNCHRONIZED.equals(modifierList[3])) {
			sb.append("(synchronized\\s)?");
		}

		PatternParser.checkNative(sb, modifierList[3]);
	}

	private static void onThreeModifiers(final String[] modifierList, final StringBuilder sb) throws InvalidPatternException {
		PatternParser.appendScope(sb, modifierList[0], false);
		if (ABSTRACT.equals(modifierList[0]) || ABSTRACT.equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if (!NON_ABSTRACT.equals(modifierList[0]) && !NON_ABSTRACT.equals(modifierList[1])) {
			sb.append("(abstract\\s)?");
		}
		if (STATIC.equals(modifierList[0]) || STATIC.equals(modifierList[1]) || STATIC.equals(modifierList[2])) {
			sb.append("static\\s");
		} else if (!NON_STATIC.equals(modifierList[0]) && !NON_STATIC.equals(modifierList[1])
				&& !NON_STATIC.equals(modifierList[2])) {
			sb.append("(static\\s)?");
		}
		if (FINAL.equals(modifierList[0]) || FINAL.equals(modifierList[1]) || FINAL.equals(modifierList[2])) {
			sb.append("final\\s");
		} else if (!NON_FINAL.equals(modifierList[0]) && !NON_FINAL.equals(modifierList[1])
				&& !NON_FINAL.equals(modifierList[2])) {
			sb.append("(final\\s)?");
		}
		if (SYNCHRONIZED.equals(modifierList[1]) || SYNCHRONIZED.equals(modifierList[2])) {
			sb.append("synchronized\\s");
		} else if (!NON_SYNCHRONIZED.equals(modifierList[1]) && NON_SYNCHRONIZED.equals(modifierList[2])) {
			sb.append("(synchronized\\s)?");
		}

		PatternParser.checkNative(sb, modifierList[2]);
	}

	private static void onTwoModifiers(final String[] modifierList, final StringBuilder sb) throws InvalidPatternException {
		PatternParser.appendScope(sb, modifierList[0], false);

		if (ABSTRACT.equals(modifierList[0]) || ABSTRACT.equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if (!NON_ABSTRACT.equals(modifierList[0]) && !NON_ABSTRACT.equals(modifierList[1])) {
			sb.append("(abstract\\s)?");
		}
		if (STATIC.equals(modifierList[0]) || STATIC.equals(modifierList[1])) {
			sb.append("static\\s");
		} else if (!NON_STATIC.equals(modifierList[0]) && !NON_STATIC.equals(modifierList[1])) {
			sb.append("(static\\s)?");
		}
		if (FINAL.equals(modifierList[0]) || FINAL.equals(modifierList[1])) {
			sb.append("final\\s");
		} else if (!NON_FINAL.equals(modifierList[0]) && !NON_FINAL.equals(modifierList[1])) {
			sb.append("(final\\s)?");
		}
		if (SYNCHRONIZED.equals(modifierList[0]) || SYNCHRONIZED.equals(modifierList[1])) {
			sb.append("synchronized\\s");
		} else if (!NON_SYNCHRONIZED.equals(modifierList[0]) && !NON_SYNCHRONIZED.equals(modifierList[1])) {
			sb.append("(synchronized\\s)?");
		}

		PatternParser.checkNative(sb, modifierList[1]);
	}

	private static void onOneModifier(final String[] modifierList, final StringBuilder sb)
			throws InvalidPatternException {
		final String[] tokens = { MODIFIER_PUBLIC, MODIFIER_PRIVATE, MODIFIER_PROTECTED, PACKAGE,
			ABSTRACT, NON_ABSTRACT, STATIC, NON_STATIC, FINAL, NON_FINAL, SYNCHRONIZED, NON_SYNCHRONIZED,
			NATIVE, NON_NATIVE, };
		final String[] outputs = {
			"public\\s(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?",
			"private\\s(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?",
			"protected\\s(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?",
			"(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?",
			"((public|private|protected)\\s)?abstract\\s(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?",
			"((public|private|protected)\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?",
			"((public|private|protected)\\s)?(abstract\\s)?static\\s(final\\s)?(synchronized\\s)?(native\\s)?",
			"((public|private|protected)\\s)?(abstract\\s)?(final\\s)?(synchronized\\s)?(native\\s)?",
			"((public|private|protected)\\s)?(abstract\\s)?(static\\s)?final\\s(synchronized\\s)?(native\\s)?",
			"((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(synchronized\\s)?(native\\s)?",
			"((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?synchronized\\s(native\\s)?",
			"((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?(native\\s)?",
			"((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?native\\s",
			"((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?",
		};

		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals(modifierList[0])) {
				sb.append(outputs[i]);
				return;
			}
		}

		/** no pattern matched, must be an invalid modifier. */
		throw new InvalidPatternException("Invalid modifier.");
	}

	/**
	 * Create scope modifier query.
	 *
	 * @param signatureRegex
	 *            string builder
	 * @param scope
	 *            scope string
	 * @param fail
	 *            if true do not assume illegal scope is another modifier
	 * @throws InvalidPatternException
	 *             on illegal modifier
	 */
	private static void appendScope(final StringBuilder signatureRegex, final String scope, final boolean fail) throws InvalidPatternException {
		if (MODIFIER_PUBLIC.equals(scope)) {
			signatureRegex.append("public\\s");
		} else if (MODIFIER_PRIVATE.equals(scope)) {
			signatureRegex.append("private\\s");
		} else if (MODIFIER_PROTECTED.equals(scope)) {
			signatureRegex.append("protected\\s");
		} else if (!PACKAGE.equals(scope)) {
			if (fail) {
				throw new InvalidPatternException("Invalid modifier.");
			} else {
				signatureRegex.append("((public|private|protected)\\s)?");
			}
		}
	}

	private static void checkNative(final StringBuilder signatureRegex, final String modifier) {
		if (NATIVE.equals(modifier)) {
			signatureRegex.append("native\\s");
		} else if (!NON_NATIVE.equals(modifier)) {
			signatureRegex.append("(native\\s)?");
		}
	}

	private static String parseThrowsPattern(final String throwsPattern) throws InvalidPatternException {
		if (null == throwsPattern) {
			return "";
		} else {
			final String trimThrowsPattern = throwsPattern.trim();
			if (!trimThrowsPattern.startsWith("throws")) {
				throw new InvalidPatternException("Invalid throws pattern.");
			}
			final String params = trimThrowsPattern.replaceFirst("throws(\\s+)", "");
			final String[] paramList = params.split(",");
			if ((paramList.length == 1) && "..".equals(paramList[0])) {
				return "(\\sthrows\\s.*)?";
			}
			try {
				return "\\sthrows\\s".concat(PatternParser.parseParameterList(paramList));
			} catch (final InvalidPatternException ex) {
				throw new InvalidPatternException("Invalid throws pattern.", ex);
			}
		}
	}
}
