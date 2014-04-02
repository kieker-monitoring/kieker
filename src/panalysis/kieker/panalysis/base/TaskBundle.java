package kieker.panalysis.base;

import java.util.List;

public class TaskBundle {

	final protected Stage<?> stage;
	final protected List<Object> tasks;

	public TaskBundle(final Stage<?> stage, final List<Object> tasks) {
		this.stage = stage;
		this.tasks = tasks;
	}

	public Stage<?> getStage() {
		return this.stage;
	}

	public List<Object> getTasks() {
		return this.tasks;
	}
}
