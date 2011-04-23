package kieker.analysis.plugin;

/**
 * @author Andre van Hoorn
 */
public interface IAnalysisPlugin {
    /**
     * Initiates the start of a component.
     * This method is called once when a TpanInstance's run() method is called.
     * This implementation must not be blocking!
     * Asynchronous consumers would spawn (an) aynchronous thread(s) in this
     * method.
     *
     * @return true on success; false otherwise.
     */
    public boolean execute();

    /**
     * Initiates a termination of the component. The value of the parameter
     * error indicates whether an error occured.
     *
     * @param error true iff an error occured.
     */
    public void terminate(boolean error);
}
