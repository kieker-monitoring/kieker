/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

	/**
	 * Private constructor to avoid initialization.
	 */
	private PatternParser() {
		// private default constructor
	}

	/**
	 * Parses the given pattern string and converts it into a {@link Pattern} instance.
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
			if ((openingParenthesis == -1) || (closingParenthesis == -1) || (openingParenthesis != trimPattern.lastIndexOf('('))
					|| (closingParenthesis != trimPattern.lastIndexOf(')')) || (openingParenthesis > closingParenthesis)) {
				throw new InvalidPatternException("Invalid parentheses");
			}

			String retType;
			String fqName;
			String[] modifierList = null;
			final String[] array = trimPattern.substring(0, openingParenthesis).trim().split("\\s+"); // NOPMD
			switch (array.length) {
			case 2:
				retType = array[0];
				fqName = array[1];
				break;
			case 3:
				modifierList = new String[1];
				modifierList[0] = array[0];
				retType = array[1];
				fqName = array[2];
				break;
			case 4:
				modifierList = new String[2];
				modifierList[0] = array[0];
				modifierList[1] = array[1];
				retType = array[2];
				fqName = array[3];
				break;
			case 5:
				modifierList = new String[3];
				modifierList[0] = array[0];
				modifierList[1] = array[1];
				modifierList[2] = array[2];
				retType = array[3];
				fqName = array[4];
				break;
			case 6:
				modifierList = new String[4];
				modifierList[0] = array[0];
				modifierList[1] = array[1];
				modifierList[2] = array[2];
				modifierList[3] = array[3];
				retType = array[4];
				fqName = array[5];
				break;
			case 7:
				modifierList = new String[5];
				modifierList[0] = array[0];
				modifierList[1] = array[1];
				modifierList[2] = array[2];
				modifierList[3] = array[3];
				modifierList[4] = array[4];
				retType = array[5];
				fqName = array[6];
				break;
			case 8:
				modifierList = new String[6];
				modifierList[0] = array[0];
				modifierList[1] = array[1];
				modifierList[2] = array[2];
				modifierList[3] = array[3];
				modifierList[4] = array[4];
				modifierList[5] = array[5];
				retType = array[6];
				fqName = array[7];
				break;
			default:
				throw new InvalidPatternException("invalid pattern");
			}

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
			sb.append(PatternParser.parseRetType(retType));
			sb.append(PatternParser.parseFQType(fqType));
			sb.append("\\.");
			sb.append(PatternParser.parseMethodName(methodName));
			sb.append("\\(");
			sb.append(PatternParser.parseParameterList(params.trim().split(",")));
			sb.append("\\)");
			sb.append(PatternParser.parseThrowsPattern(throwsPattern));
		}
		return Pattern.compile(sb.toString());
	}

	private static final String parseMethodName(final String methodName) throws InvalidPatternException {
		try {
			return PatternParser.parseIdentifier(methodName);
		} catch (final InvalidPatternException ex) {
			throw new InvalidPatternException("Invalid method name.", ex);
		}
	}

	private static final String parseParameterList(final String[] paramList) throws InvalidPatternException {
		final int length = paramList.length;
		if (length == 1) {
			if (paramList[0].trim().length() == 0) {
				return "";
			} else if ("..".equals(paramList[0].trim())) {
				return ".*";
			}
		}

		boolean startsWithDotdot = false;
		final StringBuilder sb = new StringBuilder(255);
		if ("..".equals(paramList[0].trim())) {
			sb.append("(((\\s)?[\\p{javaJavaIdentifierPart}\\.])*\\p{javaJavaIdentifierPart}+(\\s)?,)*");
			startsWithDotdot = true;
		} else if ("*".equals(paramList[0].trim())) {
			sb.append("(\\s)?(\\p{javaJavaIdentifierPart})+(\\s)?");
		} else if (paramList[0].trim().length() == 0) {
			throw new InvalidPatternException("Invalid parameter list.");
		} else {
			try {
				sb.append("(\\s)?");
				sb.append(PatternParser.parseFQType(paramList[0].trim()));
				sb.append("(\\s)?");
			} catch (final InvalidPatternException ex) {
				throw new InvalidPatternException("Invalid parameter list.", ex);
			}
		}
		int start = 1;
		if ((length > 1) && startsWithDotdot) {
			start = 2;
			if ("..".equals(paramList[1].trim())) {
				sb.append("(((\\s)?[\\p{javaJavaIdentifierPart}\\.])*\\p{javaJavaIdentifierPart}+(\\s)?)*");
			} else if ("*".equals(paramList[1].trim())) {
				sb.append("(\\s)?(\\p{javaJavaIdentifierPart})+(\\s)?");
			} else if (paramList[1].trim().length() == 0) {
				throw new InvalidPatternException("Invalid parameter list.");
			} else {
				try {
					sb.append("(\\s)?");
					sb.append(PatternParser.parseFQType(paramList[1].trim()));
					sb.append("(\\s)?");
				} catch (final InvalidPatternException ex) {
					throw new InvalidPatternException("Invalid parameter list.", ex);
				}
			}
		}

		for (int i = start; i < length; i++) {
			if ("..".equals(paramList[i].trim())) {
				sb.append("(,((\\s)?[\\p{javaJavaIdentifierPart}\\.])*\\p{javaJavaIdentifierPart}+(\\s)?)*");
			} else if ("*".equals(paramList[i].trim())) {
				sb.append(",(\\s)?(\\p{javaJavaIdentifierPart})+(\\s)?");
			} else if (paramList[i].trim().length() == 0) {
				throw new InvalidPatternException("Invalid parameter list.");
			} else {
				try {
					sb.append(",(\\s)?");
					sb.append(PatternParser.parseFQType(paramList[i].trim()));
					sb.append("(\\s)?");
				} catch (final InvalidPatternException ex) {
					throw new InvalidPatternException("Invalid parameter list.", ex);
				}
			}
		}

		return sb.toString();
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
		final String[] array = fqType.split("\\.");
		final int length = array.length;
		if (length == 1) {
			try {
				return PatternParser.parseIdentifier(fqType);
			} catch (final InvalidPatternException ex) {
				throw new InvalidPatternException("Invalid fully qualified type.", ex);
			}
		}
		int start = 0;
		final StringBuilder sb = new StringBuilder(128);
		// test if fq_type starts with ..
		if ((array[0].length() == 0) && (array[1].length() == 0)) {
			sb.append("(([\\p{javaJavaIdentifierPart}\\.])*\\.)?");
			start = 2;
		} else if (array[0].length() == 0) {
			throw new InvalidPatternException("Invalid fully qualified type: leading dot");
		}
		for (int i = start; i < (length - 1); i++) {
			if (array[i].length() == 0) {
				sb.append("(([\\p{javaJavaIdentifierPart}\\.])*\\.)?");
			} else {
				try {
					sb.append(PatternParser.parseIdentifier(array[i]));
				} catch (final InvalidPatternException ex) {
					throw new InvalidPatternException("Invalid fully qualified type.", ex);
				}
				sb.append("\\.");
			}
		}
		try {
			sb.append(PatternParser.parseIdentifier(array[length - 1]));
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

	private static final String parseModifierConstraintList(final String[] modifierList) throws InvalidPatternException {
		if (modifierList == null) {
			return "((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?";
		}
		final Map<String, Integer> allowedModifiersWithOrder = new HashMap<String, Integer>(); // NOPMD (no conc. access)
		allowedModifiersWithOrder.put("public", 0);
		allowedModifiersWithOrder.put("private", 0);
		allowedModifiersWithOrder.put("protected", 0);
		allowedModifiersWithOrder.put("package", 0);
		allowedModifiersWithOrder.put("abstract", 1);
		allowedModifiersWithOrder.put("non_abstract", 1);
		allowedModifiersWithOrder.put("static", 2);
		allowedModifiersWithOrder.put("non_static", 2);
		allowedModifiersWithOrder.put("final", 3);
		allowedModifiersWithOrder.put("non_final", 3);
		allowedModifiersWithOrder.put("synchronized", 4);
		allowedModifiersWithOrder.put("non_synchronized", 4);
		allowedModifiersWithOrder.put("native", 5);
		allowedModifiersWithOrder.put("non_native", 5);
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

	private static void onSixModifiers(final String[] modifierList, final StringBuilder sb) throws InvalidPatternException {
		if ("public".equals(modifierList[0])) {
			sb.append("public\\s");
		} else if ("private".equals(modifierList[0])) {
			sb.append("private\\s");
		} else if ("protected".equals(modifierList[0])) {
			sb.append("protected\\s");
		} else if ("package".equals(modifierList[0])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			throw new InvalidPatternException("Invalid modifier.");
		}
		if ("abstract".equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if ("non_abstract".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			throw new InvalidPatternException("Invalid modifier.");
		}
		if ("static".equals(modifierList[2])) {
			sb.append("static\\s");
		} else if ("non_static".equals(modifierList[2])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			throw new InvalidPatternException("Invalid modifier.");
		}
		if ("final".equals(modifierList[3])) {
			sb.append("final\\s");
		} else if ("non_final".equals(modifierList[3])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			throw new InvalidPatternException("Invalid modifier.");
		}
		if ("synchronized".equals(modifierList[4])) {
			sb.append("synchronized\\s");
		} else if ("non_synchronized".equals(modifierList[4])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			throw new InvalidPatternException("Invalid modifier.");
		}
		if ("native".equals(modifierList[5])) {
			sb.append("native\\s");
		} else if ("non_native".equals(modifierList[5])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			throw new InvalidPatternException("Invalid modifier.");
		}
	}

	private static void onFiveModifiers(final String[] modifierList, final StringBuilder sb) {
		if ("public".equals(modifierList[0])) {
			sb.append("public\\s");
		} else if ("private".equals(modifierList[0])) {
			sb.append("private\\s");
		} else if ("protected".equals(modifierList[0])) {
			sb.append("protected\\s");
		} else if ("package".equals(modifierList[0])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("((public|private|protected)\\s)?");
		}
		if ("abstract".equals(modifierList[0]) || "abstract".equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if ("non_abstract".equals(modifierList[0]) || "non_abstract".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(abstract\\s)?");
		}
		if ("static".equals(modifierList[1]) || "static".equals(modifierList[2])) {
			sb.append("static\\s");
		} else if ("non_static".equals(modifierList[1]) || "non_static".equals(modifierList[2])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(static\\s)?");
		}
		if ("final".equals(modifierList[2]) || "final".equals(modifierList[3])) {
			sb.append("final\\s");
		} else if ("non_final".equals(modifierList[2]) || "non_final".equals(modifierList[3])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(final\\s)?");
		}
		if ("synchronized".equals(modifierList[3]) || "synchronized".equals(modifierList[4])) {
			sb.append("synchronized\\s");
		} else if ("non_synchronized".equals(modifierList[3]) || "non_synchronized".equals(modifierList[4])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(synchronized\\s)?");
		}
		if ("native".equals(modifierList[4])) {
			sb.append("native\\s");
		} else if ("non_native".equals(modifierList[4])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(native\\s)?");
		}
	}

	private static void onFourModifiers(final String[] modifierList, final StringBuilder sb) {
		if ("public".equals(modifierList[0])) {
			sb.append("public\\s");
		} else if ("private".equals(modifierList[0])) {
			sb.append("private\\s");
		} else if ("protected".equals(modifierList[0])) {
			sb.append("protected\\s");
		} else if ("package".equals(modifierList[0])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("((public|private|protected)\\s)?");
		}
		if ("abstract".equals(modifierList[0]) || "abstract".equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if ("non_abstract".equals(modifierList[0]) || "non_abstract".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(abstract\\s)?");
		}
		if ("static".equals(modifierList[0]) || "static".equals(modifierList[1]) || "static".equals(modifierList[2])) {
			sb.append("static\\s");
		} else if ("non_static".equals(modifierList[0]) || "non_static".equals(modifierList[1]) || "non_static".equals(modifierList[2])) { // NOCS NOPMD
																																			// (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(static\\s)?");
		}
		if ("final".equals(modifierList[1]) || "final".equals(modifierList[2]) || "final".equals(modifierList[3])) {
			sb.append("final\\s");
		} else if ("non_final".equals(modifierList[1]) || "non_final".equals(modifierList[2]) || "non_final".equals(modifierList[3])) { // NOCS NOPMD
																																		// (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(final\\s)?");
		}
		if ("synchronized".equals(modifierList[2]) || "synchronized".equals(modifierList[3])) {
			sb.append("synchronized\\s");
		} else if ("non_synchronized".equals(modifierList[2]) || "non_synchronized".equals(modifierList[3])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(synchronized\\s)?");
		}
		if ("native".equals(modifierList[3])) {
			sb.append("native\\s");
		} else if ("non_native".equals(modifierList[3])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(native\\s)?");
		}
	}

	private static void onThreeModifiers(final String[] modifierList, final StringBuilder sb) {
		if ("public".equals(modifierList[0])) {
			sb.append("public\\s");
		} else if ("private".equals(modifierList[0])) {
			sb.append("private\\s");
		} else if ("protected".equals(modifierList[0])) {
			sb.append("protected\\s");
		} else if ("package".equals(modifierList[0])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("((public|private|protected)\\s)?");
		}
		if ("abstract".equals(modifierList[0]) || "abstract".equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if ("non_abstract".equals(modifierList[0]) || "non_abstract".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(abstract\\s)?");
		}
		if ("static".equals(modifierList[0]) || "static".equals(modifierList[1]) || "static".equals(modifierList[2])) {
			sb.append("static\\s");
		} else if ("non_static".equals(modifierList[0]) || "non_static".equals(modifierList[1]) || "non_static".equals(modifierList[2])) { // NOCS NOPMD
																																			// (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(static\\s)?");
		}
		if ("final".equals(modifierList[0]) || "final".equals(modifierList[1]) || "final".equals(modifierList[2])) {
			sb.append("final\\s");
		} else if ("non_final".equals(modifierList[0]) || "non_final".equals(modifierList[1]) || "non_final".equals(modifierList[2])) { // NOCS NOPMD
																																		// (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(final\\s)?");
		}
		if ("synchronized".equals(modifierList[1]) || "synchronized".equals(modifierList[2])) {
			sb.append("synchronized\\s");
		} else if ("non_synchronized".equals(modifierList[1]) || "non_synchronized".equals(modifierList[2])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(synchronized\\s)?");
		}
		if ("native".equals(modifierList[2])) {
			sb.append("native\\s");
		} else if ("non_native".equals(modifierList[2])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(native\\s)?");
		}
	}

	private static void onTwoModifiers(final String[] modifierList, final StringBuilder sb) {
		if ("public".equals(modifierList[0])) {
			sb.append("public\\s");
		} else if ("private".equals(modifierList[0])) {
			sb.append("private\\s");
		} else if ("protected".equals(modifierList[0])) {
			sb.append("protected\\s");
		} else if ("package".equals(modifierList[0])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("((public|private|protected)\\s)?");
		}
		if ("abstract".equals(modifierList[0]) || "abstract".equals(modifierList[1])) {
			sb.append("abstract\\s");
		} else if ("non_abstract".equals(modifierList[0]) || "non_abstract".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(abstract\\s)?");
		}
		if ("static".equals(modifierList[0]) || "static".equals(modifierList[1])) {
			sb.append("static\\s");
		} else if ("non_static".equals(modifierList[0]) || "non_static".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(static\\s)?");
		}
		if ("final".equals(modifierList[0]) || "final".equals(modifierList[1])) {
			sb.append("final\\s");
		} else if ("non_final".equals(modifierList[0]) || "non_final".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(final\\s)?");
		}
		if ("synchronized".equals(modifierList[0]) || "synchronized".equals(modifierList[1])) {
			sb.append("synchronized\\s");
		} else if ("non_synchronized".equals(modifierList[0]) || "non_synchronized".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(synchronized\\s)?");
		}
		if ("native".equals(modifierList[1])) {
			sb.append("native\\s");
		} else if ("non_native".equals(modifierList[1])) { // NOCS NOPMD (EmptyIfStmt)
			// nothing to do
		} else {
			sb.append("(native\\s)?");
		}
	}

	private static void onOneModifier(final String[] modifierList, final StringBuilder sb) throws InvalidPatternException {
		if ("public".equals(modifierList[0])) {
			sb.append("public\\s(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("private".equals(modifierList[0])) {
			sb.append("private\\s(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("protected".equals(modifierList[0])) {
			sb.append("protected\\s(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("package".equals(modifierList[0])) {
			sb.append("(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("abstract".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?abstract\\s(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("non_abstract".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(static\\s)?(final\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("static".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(abstract\\s)?static\\s(final\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("non_static".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(abstract\\s)?(final\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("final".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(abstract\\s)?(static\\s)?final\\s(synchronized\\s)?(native\\s)?");
		} else if ("non_final".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(synchronized\\s)?(native\\s)?");
		} else if ("synchronized".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?synchronized\\s(native\\s)?");
		} else if ("non_synchronized".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?(native\\s)?");
		} else if ("native".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?native\\s");
		} else if ("non_native".equals(modifierList[0])) {
			sb.append("((public|private|protected)\\s)?(abstract\\s)?(static\\s)?(final\\s)?(synchronized\\s)?");
		} else {
			throw new InvalidPatternException("Invalid modifier.");
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
