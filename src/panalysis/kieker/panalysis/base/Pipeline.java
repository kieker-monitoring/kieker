package kieker.panalysis.base;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Pipeline {

	private final List<Stage<?>> stages = new LinkedList<Stage<?>>();
	private int freeId = 0;

	public void addStage(final Stage<?> stage) {
		stage.setId(this.freeId++);
		this.stages.add(stage);
	}

	public void start() {
		this.stages.get(0).execute();
	}

	public Collection<Stage<?>> getStages() {
		return this.stages;
	}
}
