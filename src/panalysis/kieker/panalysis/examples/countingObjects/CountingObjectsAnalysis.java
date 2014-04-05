package kieker.panalysis.examples.countingObjects;

import kieker.panalysis.CountingFilter;
import kieker.panalysis.MethodCallPipe;
import kieker.panalysis.RepeaterSource;
import kieker.panalysis.TeeFilter;
import kieker.panalysis.base.Analysis;
import kieker.panalysis.base.IPipe;
import kieker.panalysis.base.Pipeline;
import kieker.panalysis.composite.CycledCountingFilter;
import kieker.panalysis.examples.wordcount.DirectoryName2Files;

public class CountingObjectsAnalysis extends Analysis {

	private RepeaterSource repeaterSource;
	private DirectoryName2Files findFilesStage;
	private CycledCountingFilter cycledCountingFilter;
	private Pipeline pipeline;
	private TeeFilter teeFilter;

	public CountingObjectsAnalysis() {
		// No code necessary
	}

	@Override
	public void init() {
		super.init();

		this.repeaterSource = new RepeaterSource(".", 100);
		this.findFilesStage = new DirectoryName2Files();
		this.cycledCountingFilter = new CycledCountingFilter(new MethodCallPipe(0L));
		this.teeFilter = new TeeFilter();

		new MethodCallPipe().connect(this.repeaterSource, RepeaterSource.OUTPUT_PORT.OUTPUT, this.findFilesStage,
				DirectoryName2Files.INPUT_PORT.DIRECTORY_NAME);
		new MethodCallPipe().connect(this.findFilesStage, DirectoryName2Files.OUTPUT_PORT.FILE, this.cycledCountingFilter,
				CycledCountingFilter.INPUT_PORT.INPUT_OBJECT);
		new MethodCallPipe().connect(this.cycledCountingFilter, CycledCountingFilter.OUTPUT_PORT.RELAYED_OBJECT, this.teeFilter,
				TeeFilter.INPUT_PORT.INPUT_OBJECT);

		this.pipeline = new Pipeline();
		this.pipeline.addStage(this.repeaterSource);
		this.pipeline.addStage(this.findFilesStage);
		this.pipeline.addStage(this.cycledCountingFilter);
		this.pipeline.addStage(this.teeFilter);
	}

	@Override
	public void start() {
		super.start();
		this.pipeline.start();
	}

	public static void main(final String[] args) {
		final CountingObjectsAnalysis analysis = new CountingObjectsAnalysis();
		analysis.init();
		final long start = System.currentTimeMillis();
		analysis.start();
		final long end = System.currentTimeMillis();
		// analysis.terminate();
		final long duration = end - start;
		System.out.println("duration: " + duration + " ms"); // NOPMD (Just for example purposes)

		final IPipe countingPipe = analysis.cycledCountingFilter.getInputPortPipes().get(CountingFilter.INPUT_PORT.CURRENT_COUNT);
		System.out.println("count: " + countingPipe.take()); // NOPMD (Just for example purposes)
	}
}
