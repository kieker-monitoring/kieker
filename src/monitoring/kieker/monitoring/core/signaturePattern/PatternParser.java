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

package kieker.monitoring.core.signaturePattern;

import java.util.regex.Pattern;

/**
 * TODO: review and restructure this class
 * 
 * @author Bjoern Weissenfels, Jan Waller
 */
public final class PatternParser {

	private PatternParser() {
		// private default constructor
	}

	public static final Pattern parseToPattern(final String pattern) throws InvalidPatternException {
		final String trimPattern = pattern.trim();
		final StringBuilder sb = new StringBuilder();
		if ("*".equals(trimPattern)) {
			sb.append(".*");
		} else {
			String[] array = trimPattern.split("[\\(\\)]");
			final String params;
			if (array.length == 1) {
				params = null;
			} else if (array.length == 2) {
				params = array[1];
			} else {
				throw new InvalidPatternException("Invalid number of brackets.");
			}
			array = array[0].trim().split("\\s+");
			String retType;
			String fqName;
			String[] modifierList = null;

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
			default:
				throw new InvalidPatternException("invalid pattern");
			}

			final int index = fqName.lastIndexOf('.');
			if ((index == -1) || (index == (fqName.length() - 1))) {
				throw new InvalidPatternException("Invalid fully qualified type or method name.");
			}
			final String fqType = fqName.substring(0, index);
			final String methodName = fqName.substring(index + 1);

			sb.append(PatternParser.parseModifierConstraintList(modifierList));
			sb.append(PatternParser.parseRetType(retType));
			sb.append(PatternParser.parseFQType(fqType));
			sb.append("\\.");
			sb.append(PatternParser.parseMethodName(methodName));
			sb.append("\\(");
			if (params != null) {
				sb.append(PatternParser.parseParameterList(params.trim().split(",")));
			}
			sb.append("\\)");
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
		int start = 1;
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
			return "((public|private|protected)\\s)?((static)\\s)?((native)\\s)?";
		}
		final StringBuilder sb = new StringBuilder();
		final int number = modifierList.length;
		if (number == 1) {
			if ("public".equals(modifierList[0])) {
				sb.append("public\\s(static\\s)?(native\\s)?");
			} else if ("private".equals(modifierList[0])) {
				sb.append("private\\s(static\\s)?(native\\s)?");
			} else if ("protected".equals(modifierList[0])) {
				sb.append("protected\\s(static\\s)?(native\\s)?");
			} else if ("package".equals(modifierList[0])) {
				sb.append("(static\\s)?(native\\s)?");
			} else if ("static".equals(modifierList[0])) {
				sb.append("((public|private|protected)\\s)?static\\s(native\\s)?");
			} else if ("non_static".equals(modifierList[0])) {
				sb.append("((public|private|protected)\\s)?(native\\s)?");
			} else if ("native".equals(modifierList[0])) {
				sb.append("((public|private|protected)\\s)?(static\\s)?native\\s");
			} else if ("non_native".equals(modifierList[0])) {
				sb.append("((public|private|protected)\\s)?(static\\s)?");
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
		} else if (number == 2) {
			if ("public".equals(modifierList[0])) {
				if ("static".equals(modifierList[1])) {
					sb.append("public\\sstatic\\s(native\\s)?");
				} else if ("non_static".equals(modifierList[1])) {
					sb.append("public\\s(native\\s)?");
				} else if ("native".equals(modifierList[1])) {
					sb.append("public\\s(static\\s)?native\\s");
				} else if ("non_native".equals(modifierList[1])) {
					sb.append("public\\s(static\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if ("private".equals(modifierList[0])) {
				if ("static".equals(modifierList[1])) {
					sb.append("private\\sstatic\\s(native\\s)?");
				} else if ("non_static".equals(modifierList[1])) {
					sb.append("private\\s(native\\s)?");
				} else if ("native".equals(modifierList[1])) {
					sb.append("private\\s(static\\s)?native\\s");
				} else if ("non_native".equals(modifierList[1])) {
					sb.append("private\\s(static\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if ("protected".equals(modifierList[0])) {
				if ("static".equals(modifierList[1])) {
					sb.append("protected\\sstatic\\s(native\\s)?");
				} else if ("non_static".equals(modifierList[1])) {
					sb.append("protected\\s(native\\s)?");
				} else if ("native".equals(modifierList[1])) {
					sb.append("protected\\s(static\\s)?native\\s");
				} else if ("non_native".equals(modifierList[1])) {
					sb.append("protected\\s(static\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if ("package".equals(modifierList[0])) {
				if ("static".equals(modifierList[1])) {
					sb.append("static\\s(native\\s)?");
				} else if ("non_static".equals(modifierList[1])) {
					sb.append("(native\\s)?");
				} else if ("native".equals(modifierList[1])) {
					sb.append("(static\\s)?native\\s");
				} else if ("non_native".equals(modifierList[1])) {
					sb.append("(static\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if ("static".equals(modifierList[0])) {
				if ("native".equals(modifierList[1])) {
					sb.append("((public|private|protected)\\s)?static\\snative\\s");
				} else if ("non_native".equals(modifierList[1])) {
					sb.append("((public|private|protected)\\s)?static\\s");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if ("non_static".equals(modifierList[0])) {
				if ("native".equals(modifierList[1])) {
					sb.append("((public|private|protected)\\s)?native\\s");
				} else if ("non_native".equals(modifierList[1])) {
					sb.append("((public|private|protected)\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
		} else if (number == 3) {
			if ("public".equals(modifierList[0])) {
				sb.append("public\\s");
			} else if ("private".equals(modifierList[0])) {
				sb.append("private\\s");
			} else if ("protected".equals(modifierList[0])) {
				sb.append("protected\\s");
			} else if ("package".equals(modifierList[0])) { // NOPMD NOCS (nothing to do)
				// nothing to do
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
			if ("static".equals(modifierList[1])) {
				sb.append("static\\s");
			} else if ("non_static".equals(modifierList[1])) { // NOPMD NOCS (nothing to do)
				// nothing to do
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
			if ("native".equals(modifierList[2])) {
				sb.append("native\\s");
			} else if ("non_native".equals(modifierList[2])) { // NOPMD NOCS (nothing to do)
				// nothing to do
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
		} else {
			throw new InvalidPatternException("Too many modifier.");
		}
		return sb.toString();
	}
}
