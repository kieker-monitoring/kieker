package kieker.panalysis.examples.wordcount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import kieker.panalysis.base.Filter;

public class CountWordsStage extends Filter<CountWordsStage.PORT> {

	public static enum PORT {
		EXCEPTION, WORDSCOUNT
	}

	public CountWordsStage(final long id) {
		super(id, PORT.class);
	}

	@Override
	public void execute() {

	}

	@SuppressWarnings("unchecked")
	public void onPortChanged(final Object record) {
		int wordsCount = 0;
		final File file = (File) record;
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					final String[] words = line.split("\\s");
					wordsCount += words.length;
				}
			} finally {
				reader.close();
			}
			this.deliver(PORT.WORDSCOUNT, Arrays.asList(file, wordsCount));
		} catch (final FileNotFoundException e) {
			this.deliver(PORT.EXCEPTION, e);
		} catch (final IOException e) {
			this.deliver(PORT.EXCEPTION, e);
		}
	}

}
