package kieker.monitoring.core.controller;

/**
 * @author Jan Waller, Robert von Massow
 */
public interface IMonitoringController extends IStateController, IWriterController, ISamplingController {

	public abstract String getState();
}
