package kieker.panalysis.base;

public interface Stage<InputPort extends Enum<InputPort>> {

	void setId(int id);

	int getId();

	void execute();

	/*
	 * Let the method uncommented for documentation purpose:<br>
	 * Since the execution can dependent on the state of one, a subset, or all input queues,<br>
	 * an overloaded version with one input port is useless.
	 */
	// void execute(InputPort inputPort);

	/*
	 * Let the method uncommented for documentation purpose:<br>
	 * Since the execution can dependent on one, a subset, or all input queues,<br>
	 * an overloaded version with a task bundle of one single input port is useless.
	 */
	// void execute(TaskBundle taskBundle);

}
