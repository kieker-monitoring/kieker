package kieker.monitoring.core.helper;

import java.util.regex.Pattern;

public class PatternParser {

	public Pattern parseToPattern(final String pattern) throws InvalidPatternException {
		final StringBuilder sb = new StringBuilder();
		if (pattern.equals("*")) {
			sb.append(".*");
		} else {
			String[] array = pattern.trim().split("[\\(\\)]");
			final String prefix = array[0];
			final String params;
			if (array.length == 1) {
				params = null;
			} else if (array.length == 2) {
				params = array[1];
			} else {
				throw new InvalidPatternException("Invalid number of brackets.");
			}

			array = prefix.trim().split("\\s+");
			String ret_type;
			String fq_name;
			String[] modifierList = null;

			final int length = array.length;
			switch (length) {
			case 2:
				ret_type = array[0];
				fq_name = array[1];
				break;
			case 3:
				modifierList = new String[1];
				modifierList[0] = array[0];
				ret_type = array[1];
				fq_name = array[2];
				break;
			case 4:
				modifierList = new String[2];
				modifierList[0] = array[0];
				modifierList[1] = array[1];
				ret_type = array[2];
				fq_name = array[3];
				break;
			case 5:
				modifierList = new String[3];
				modifierList[0] = array[0];
				modifierList[1] = array[1];
				modifierList[2] = array[2];
				ret_type = array[3];
				fq_name = array[4];
				break;
			default:
				throw new InvalidPatternException("invalid pattern");
			}

			final int index = fq_name.lastIndexOf(".");
			if ((index == -1) || (index == (fq_name.length() - 1))) {
				throw new InvalidPatternException("Invalid fully qualified type or method name.");
			}
			final String fq_type = fq_name.substring(0, index);
			final String method_name = fq_name.substring(index + 1);

			sb.append(this.parseModifierConstraintList(modifierList));
			sb.append(this.parseRetType(ret_type));
			sb.append(this.parseFQType(fq_type));
			sb.append("\\.");
			sb.append(this.parseMethodName(method_name));
			sb.append("\\(");
			if (params != null) {
				sb.append(this.parseParameterList(params.trim().split(",")));
			}
			sb.append("\\)");
		}
		final Pattern result = Pattern.compile(sb.toString());
		return result;
	}

	private String parseMethodName(final String methodName) throws InvalidPatternException {
		try {
			return this.parseIdentifier(methodName);
		} catch (final InvalidPatternException e) {
			throw new InvalidPatternException("Invalid method name. -> " + e.getMessage());
		}
	}

	private String parseParameterList(final String[] paramList) throws InvalidPatternException {
		if (paramList[0].equals("")) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		final int length = paramList.length;
		if (paramList[0].trim().equals("..")) {
			sb.append("(\\s)?([\\p{javaJavaIdentifierPart}\\.]|((\\s)?,(\\s)?))*(\\s)?");
		} else {
			sb.append("(\\s)?");
			try {
				sb.append(this.parseFQType(paramList[0].trim()));
			} catch (final InvalidPatternException e) {
				throw new InvalidPatternException("Invalid parameter list. -> " + e.getMessage());
			}
			sb.append("(\\s)?");
		}
		for (int i = 1; i < length; i++) {
			if (paramList[i].trim().equals("..")) {
				sb.append(",(\\s)?([\\p{javaJavaIdentifierPart}\\.]|((\\s)?,(\\s)?))*(\\s)?");
			} else if (paramList[i].equals("")) {
				throw new InvalidPatternException("Invalid parameter list.");
			} else {
				sb.append(",(\\s)?");
				try {
					sb.append(this.parseFQType(paramList[i].trim()));
				} catch (final InvalidPatternException e) {
					throw new InvalidPatternException("Invalid parameter list. -> " + e.getMessage());
				}
				sb.append("(\\s)?");
			}
		}
		return sb.toString();
	}

	private String parseIdentifier(final String identifier) throws InvalidPatternException {
		final char[] array = identifier.toCharArray();
		final StringBuilder sb = new StringBuilder();
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

	private String parseFQType(final String fq_type) throws InvalidPatternException {
		if (fq_type.contains("...") || fq_type.endsWith(".") || fq_type.equals("")) {
			throw new InvalidPatternException("Invalid fully qualified type.");
		}
		final String[] array = fq_type.split("\\.");
		final int length = array.length;
		if (length == 1) {
			try {
				return this.parseIdentifier(fq_type);
			} catch (final InvalidPatternException e) {
				throw new InvalidPatternException("Invalid fully qualified type. -> " + e.getMessage());
			}
		}
		int start = 0;
		final StringBuilder sb = new StringBuilder();
		// test if fq_type starts with ..
		if (array[0].equals("") && array[1].equals("")) {
			sb.append("(([\\p{javaJavaIdentifierPart}\\.])*\\.)?");
			start = 2;
		} else if (array[0].equals("")) {
			throw new InvalidPatternException("Invalid fully qualified type: leading dot");
		}
		for (int i = start; i < (length - 1); i++) {
			if (array[i].equals("")) {
				sb.append("(([\\p{javaJavaIdentifierPart}\\.])*\\.)?");
			} else {
				try {
					sb.append(this.parseIdentifier(array[i]));
				} catch (final InvalidPatternException e) {
					throw new InvalidPatternException("Invalid fully qualified type. -> " + e.getMessage());
				}
				sb.append("\\.");
			}
		}
		try {
			sb.append(this.parseIdentifier(array[length - 1]));
		} catch (final InvalidPatternException e) {
			throw new InvalidPatternException("Invalid fully qualified type. -> " + e.getMessage());
		}
		return sb.toString();
	}

	private String parseRetType(final String ret_type) throws InvalidPatternException {
		if (ret_type.equals("new")) {
			return "";
		} else {
			try {
				return this.parseFQType(ret_type) + "\\s";
			} catch (final InvalidPatternException e) {
				throw new InvalidPatternException("Invalid return type. -> " + e.getMessage());
			}
		}
	}

	private String parseModifierConstraintList(final String[] modifierList) throws InvalidPatternException {
		if (modifierList == null) {
			return "((public|private|protected)\\s)?((static)\\s)?((native)\\s)?";
		}
		final StringBuilder sb = new StringBuilder();
		final int number = modifierList.length;
		if (number == 1) {
			if (modifierList[0].equals("public")) {
				sb.append("public\\s(static\\s)?(native\\s)?");
			} else if (modifierList[0].equals("private")) {
				sb.append("private\\s(static\\s)?(native\\s)?");
			} else if (modifierList[0].equals("protected")) {
				sb.append("protected\\s(static\\s)?(native\\s)?");
			} else if (modifierList[0].equals("package")) {
				sb.append("(static\\s)?(native\\s)?");
			} else if (modifierList[0].equals("static")) {
				sb.append("((public|private|protected)\\s)?static\\s(native\\s)?");
			} else if (modifierList[0].equals("non_static")) {
				sb.append("((public|private|protected)\\s)?(native\\s)?");
			} else if (modifierList[0].equals("native")) {
				sb.append("((public|private|protected)\\s)?(static\\s)?native\\s");
			} else if (modifierList[0].equals("non_native")) {
				sb.append("((public|private|protected)\\s)?(static\\s)?");
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
		} else if (number == 2) {
			if (modifierList[0].equals("public")) {
				if (modifierList[1].equals("static")) {
					sb.append("public\\sstatic\\s(native\\s)?");
				} else if (modifierList[1].equals("non_static")) {
					sb.append("public\\s(native\\s)?");
				} else if (modifierList[1].equals("native")) {
					sb.append("public\\s(static\\s)?native\\s");
				} else if (modifierList[1].equals("non_native")) {
					sb.append("public\\s(static\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if (modifierList[0].equals("private")) {
				if (modifierList[1].equals("static")) {
					sb.append("private\\sstatic\\s(native\\s)?");
				} else if (modifierList[1].equals("non_static")) {
					sb.append("private\\s(native\\s)?");
				} else if (modifierList[1].equals("native")) {
					sb.append("private\\s(static\\s)?native\\s");
				} else if (modifierList[1].equals("non_native")) {
					sb.append("private\\s(static\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if (modifierList[0].equals("protected")) {
				if (modifierList[1].equals("static")) {
					sb.append("protected\\sstatic\\s(native\\s)?");
				} else if (modifierList[1].equals("non_static")) {
					sb.append("protected\\s(native\\s)?");
				} else if (modifierList[1].equals("native")) {
					sb.append("protected\\s(static\\s)?native\\s");
				} else if (modifierList[1].equals("non_native")) {
					sb.append("protected\\s(static\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if (modifierList[0].equals("package")) {
				if (modifierList[1].equals("static")) {
					sb.append("static\\s(native\\s)?");
				} else if (modifierList[1].equals("non_static")) {
					sb.append("(native\\s)?");
				} else if (modifierList[1].equals("native")) {
					sb.append("(static\\s)?native\\s");
				} else if (modifierList[1].equals("non_native")) {
					sb.append("(static\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if (modifierList[0].equals("static")) {
				if (modifierList[1].equals("native")) {
					sb.append("((public|private|protected)\\s)?static\\snative\\s");
				} else if (modifierList[1].equals("non_native")) {
					sb.append("((public|private|protected)\\s)?static\\s");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else if (modifierList[0].equals("non_static")) {
				if (modifierList[1].equals("native")) {
					sb.append("((public|private|protected)\\s)?native\\s");
				} else if (modifierList[1].equals("non_native")) {
					sb.append("((public|private|protected)\\s)?");
				} else {
					throw new InvalidPatternException("Invalid modifier.");
				}
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
		} else if (number == 3) {
			if (modifierList[0].equals("public")) {
				sb.append("public\\s");
			} else if (modifierList[0].equals("private")) {
				sb.append("private\\s");
			} else if (modifierList[0].equals("protected")) {
				sb.append("protected\\s");
			} else if (modifierList[0].equals("package")) {
				// nothing to do
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
			if (modifierList[1].equals("static")) {
				sb.append("static\\s");
			} else if (modifierList[1].equals("non_static")) {
				// nothing to do
			} else {
				throw new InvalidPatternException("Invalid modifier.");
			}
			if (modifierList[2].equals("native")) {
				sb.append("native\\s");
			} else if (modifierList[2].equals("non_native")) {
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
