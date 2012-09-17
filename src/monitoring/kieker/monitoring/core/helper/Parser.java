package kieker.monitoring.core.helper;

import java.util.regex.Pattern;

public class Parser {

	public Pattern parseToPattern(final String pattern) throws InvalidPatternException {
		final StringBuilder regex = new StringBuilder();

		String[] array = pattern.trim().split("[\\(\\)]");
		if (array.length != 2) {
			throw new InvalidPatternException("invalid number of brackets");
		}
		final String params = array[1];
		final String prefix = array[0];

		array = prefix.trim().split("\\s");
		String visibility = null;
		String _static = null;
		String ret_type;
		String fq_name;

		final int length = array.length;
		switch (length) {
		case 2:
			ret_type = array[0];
			fq_name = array[1];
			break;
		case 3:
			if (array[0].equals("*")) {
				visibility = "*";
				_static = "*";
			} else if (array[0].equals("static")) {
				_static = array[0];
			} else {
				visibility = array[0];
			}
			ret_type = array[1];
			fq_name = array[2];
			break;
		case 4:
			visibility = array[0];
			_static = array[1];
			ret_type = array[2];
			fq_name = array[3];
			break;
		default:
			throw new InvalidPatternException("invalid pattern");
		}

		if (visibility != null) {
			regex.append(this.parseVisibility(visibility));
		}
		if (_static != null) {
			regex.append(this.parseStatic(_static));
		}
		ret_type = this.parseRetType(ret_type);
		if (!ret_type.equals("")) {
			regex.append(ret_type);
			regex.append("\\s");
		}
		regex.append(this.parseFQType(fq_name));
		regex.append("(\\s)?");
		regex.append("\\(");
		regex.append(this.parseParameterList(params));
		regex.append("\\)");

		final Pattern result = Pattern.compile(regex.toString());
		return result;
	}

	private String parseParameterList(final String paramList) throws InvalidPatternException {
		final StringBuilder sb = new StringBuilder();
		final String[] array = paramList.split(",");
		final int length = array.length;
		if (length == 0) {
			return "";
		}
		if (array[0].trim().equals("..")) {
			sb.append("(\\s)?([\\w\\.]|((\\s)?,(\\s)?))*(\\s)?");
		} else {
			sb.append("(\\s)?");
			sb.append(this.parseFQType(array[0].trim()));
			sb.append("(\\s)?");
		}
		for (int i = 1; i < length; i++) {
			if (array[i].trim().equals("..")) {
				sb.append(",(\\s)?([\\w\\.]|((\\s)?,(\\s)?))*(\\s)?");
			} else {
				sb.append(",(\\s)?");
				sb.append(this.parseFQType(array[i].trim()));
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
			sb.append("(\\w)*");
		} else {
			throw new InvalidPatternException("Identifier starts with invalid symbol.");
		}
		for (int i = 1; i < array.length; i++) {
			if (Character.isJavaIdentifierPart(array[i])) {
				sb.append(Character.toString(array[i]));
			} else if (array[i] == '*') {
				sb.append("(\\w)*");
			} else {
				throw new InvalidPatternException("Identifier includes invalid symbol.");
			}
		}
		return sb.toString();
	}

	private String parseFQType(final String fq_type) throws InvalidPatternException {
		final String[] array = fq_type.split("\\.");
		final int length = array.length;
		if (length == 1) {
			return this.parseIdentifier(fq_type);
		}
		int start = 0;
		final StringBuilder sb = new StringBuilder();
		if ((length > 2) && array[0].equals("") && array[1].equals("") && array[2].equals("")) {
			throw new InvalidPatternException("Too many dots in a row.");
		} else if (array[0].equals("") && array[1].equals("")) {
			sb.append("([\\w\\.])*");
			sb.append("\\.");
			start = 2;
		} else if (array[0].equals("")) {
			throw new InvalidPatternException("leading dot");
		}
		for (int i = start; i < (length - 1); i++) {
			if (array[i].equals("") && array[i + 1].equals("")) {
				throw new InvalidPatternException("Too many dots in a row.");
			} else if (array[i].equals("")) {
				sb.append("([\\w\\.])*");
				sb.append("\\.");
			} else {
				sb.append(this.parseIdentifier(array[i]));
				sb.append("\\.");
			}
		}
		sb.append(this.parseIdentifier(array[length - 1]));
		return sb.toString();
	}

	private String parseRetType(final String ret_type) throws InvalidPatternException {
		if (ret_type.equals("new")) {
			return "";
		} else {
			return this.parseFQType(ret_type);
		}
	}

	private String parseStatic(final String _static) throws InvalidPatternException {
		if (_static.equals("static")) {
			return "static\\s";
		} else if (_static.equals("*")) {
			return "(static\\s)?";
		} else {
			throw new InvalidPatternException("Ivalid static part.");
		}
	}

	private String parseVisibility(final String visibility) throws InvalidPatternException {
		if (visibility.equals("public")) {
			return "public\\s";
		} else if (visibility.equals("private")) {
			return "private\\s";
		} else if (visibility.equals("protected")) {
			return "protected\\s";
		} else if (visibility.equals("*")) {
			return "(public\\s|private\\s|protected\\s)?";
		} else {
			throw new InvalidPatternException("Invalid visibility part.");
		}
	}

}
