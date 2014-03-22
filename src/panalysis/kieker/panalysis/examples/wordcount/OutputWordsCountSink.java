package kieker.panalysis.examples.wordcount;

import java.io.File;
import java.util.List;

import kieker.panalysis.base.Sink;

public class OutputWordsCountSink extends Sink {

	public OutputWordsCountSink(final long id) {
		super(id);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	public void onPortChanged(final Object record) {
		final List<?> tuple = (List<?>) record;
		final File file = (File) tuple.get(0);
		final Number wordsCount = (Number) tuple.get(1);
		System.out.println(wordsCount + " words in file '" + file.getName() + "'");
	}

}
