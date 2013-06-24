/**
 * 
 */
package kieker.tools.bridge.connector;

/**
 * @author rju
 * 
 */
public class ConnectorDataTransmissionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7621182316652816606L;

	public ConnectorDataTransmissionException(final String message, final Exception e) {
		super(message, e);
	}

	public ConnectorDataTransmissionException(final String message) {
		super(message);
	}

}
