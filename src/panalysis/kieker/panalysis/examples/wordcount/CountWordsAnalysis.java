package kieker.panalysis.examples.wordcount;

import kieker.panalysis.base.Analysis;
import kieker.panalysis.base.Source;

public class CountWordsAnalysis extends Analysis {

	private Source<?> startStage;

	@Override
	public void init() {
		super.init();

		OutputWordsCountSink outputWordsCountStage = new OutputWordsCountSink(2);

		CountWordsStage countWordsStage = new CountWordsStage(1);
		countWordsStage.addOutputPortListener(CountWordsStage.PORT.WORDSCOUNT, outputWordsCountStage);

		FindFilesSource findFilesStage = new FindFilesSource(0, ".");
		findFilesStage.addOutputPortListener(FindFilesSource.PORT.FILE, countWordsStage);

		startStage = findFilesStage;
	}

	@Override
	public void start() {
		super.start();
		startStage.execute();
	}

	public static void main(final String[] args) {
		CountWordsAnalysis analysis = new CountWordsAnalysis();
		analysis.init();
		analysis.start();
//		analysis.terminate();
	}
}
