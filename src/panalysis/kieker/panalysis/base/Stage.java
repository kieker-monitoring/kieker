package kieker.panalysis.base;

public interface Stage<InputPort extends Enum<InputPort>> {

	void execute();

	void execute(InputPort inputPort);
}
