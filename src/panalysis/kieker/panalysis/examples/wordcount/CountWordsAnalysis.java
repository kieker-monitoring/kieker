package kieker.panalysis.examples.wordcount;

import kieker.panalysis.MethodCallPipe;
import kieker.panalysis.base.AbstractSource;
import kieker.panalysis.base.Analysis;

public class CountWordsAnalysis extends Analysis {

	private AbstractSource<?> startStage;

	@Override
	public void init() {
		super.init();

		final FindFilesSource findFilesStage = new FindFilesSource(0, ".");
		final CountWordsStage countWordsStage = new CountWordsStage(1);
		final OutputWordsCountSink outputWordsCountStage = new OutputWordsCountSink(2);

		MethodCallPipe.connect(findFilesStage, FindFilesSource.OUTPUT_PORT.FILE, countWordsStage, CountWordsStage.INPUT_PORT.FILE);
		MethodCallPipe.connect(countWordsStage, CountWordsStage.OUTPUT_PORT.WORDSCOUNT, outputWordsCountStage, OutputWordsCountSink.INPUT_PORT.FILE_WORDCOUNT_TUPLE);

		this.startStage = findFilesStage;
	}

	@Override
	public void start() {
		super.start();
		this.startStage.execute();
	}

	public static void main(final String[] args) {
		final CountWordsAnalysis analysis = new CountWordsAnalysis();
		analysis.init();
		analysis.start();
		// analysis.terminate();
	}
}
