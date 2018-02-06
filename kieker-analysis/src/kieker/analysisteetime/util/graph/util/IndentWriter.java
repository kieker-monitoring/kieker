package kieker.analysisteetime.util.graph.util;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class IndentWriter extends Writer {

	private final Writer writer;

	private int indented; // = 0
	private char indentChar = DEFAULT_INDENT_CHAR;
	private int indentLength = DEFAULT_INDENT_LENGTH;

	public static final char DEFAULT_INDENT_CHAR = '\t';
	public static final int DEFAULT_INDENT_LENGTH = 1;

	public IndentWriter(final Writer writer) {
		super();
		this.writer = writer;
	}

	public IndentWriter(final Writer writer, final char indentChar, final int indentLength) {
		this(writer);
		this.indentChar = indentChar;
		this.indentLength = indentLength;
	}

	public void indent() {
		this.indented++;
	}

	public void unindent() {
		if (this.indented > 0) {
			this.indented--;
		}
	}

	public void writeln(final String str) throws IOException {
		this.writer.write(this.getIndentChars() + str + '\n');
	}

	private String getIndentChars() {
		return new String(new char[this.indented * this.indentLength]).replace('\0', this.indentChar);
	}

	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {
		this.writer.write(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		this.writer.flush();
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

}
