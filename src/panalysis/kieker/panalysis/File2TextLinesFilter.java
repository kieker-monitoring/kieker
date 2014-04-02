package kieker.panalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import kieker.panalysis.base.Filter;

public class File2TextLinesFilter extends Filter<File2TextLinesFilter.INPUT_PORT, File2TextLinesFilter.OUTPUT_PORT> {

	static public enum INPUT_PORT {
		FILE
	}

	static public enum OUTPUT_PORT {
		TEXT_LINE
	}

	private String charset; // = "UTF-8"

	public File2TextLinesFilter(final long id) {
		super(id, INPUT_PORT.class, OUTPUT_PORT.class);
	}

	@Override
	public INPUT_PORT chooseInputPort() {
		return INPUT_PORT.FILE;
	}

	public void execute(final INPUT_PORT inputPort) {
		final File file = (File) this.take(inputPort);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), this.charset));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() != 0) {
					this.put(OUTPUT_PORT.TEXT_LINE, line);
				} // else ignore empty line
			}
		} catch (final FileNotFoundException e) {
			this.logger.error("", e);
		} catch (final IOException e) {
			this.logger.error("", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException e) {
				this.logger.warn("", e);
			}
		}
	}
}
