package kieker.panalysis.examples.wordcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import kieker.panalysis.base.AbstractFilter;

public class CountWordsStage extends AbstractFilter<CountWordsStage.INPUT_PORT, CountWordsStage.OUTPUT_PORT> {

	public static enum OUTPUT_PORT {
		EXCEPTION, WORDSCOUNT
	}

	public static enum INPUT_PORT {
		FILE
	}

	private long overallDuration = 0;
	private final Pattern pattern;

	public CountWordsStage() {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
		this.pattern = Pattern.compile("[^\\p{Graph}]");
	}

	@SuppressWarnings("unchecked")
	public void execute() {
		final long start = System.currentTimeMillis();

		final File file = (File) this.take(INPUT_PORT.FILE);

		int wordsCount = 0;
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.length() > 0) {
						final String[] words = this.pattern.split(line);
						// System.out.println("" + Arrays.toString(words));
						wordsCount += words.length;
					}
				}
			} finally {
				reader.close();
			}
			this.put(OUTPUT_PORT.WORDSCOUNT, Arrays.asList(file, wordsCount));
		} catch (final FileNotFoundException e) {
			this.put(OUTPUT_PORT.EXCEPTION, e);
		} catch (final IOException e) {
			this.put(OUTPUT_PORT.EXCEPTION, e);
		}

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

}
