/**
 * 
 */
package kieker.tools.bridge;

/**
 * @author rju
 * 
 */
public class ConnectorDataTransmissionException extends Exception {

	public ConnectorDataTransmissionException(final String message, final Exception e) {
		super(message, e);
	}

	public ConnectorDataTransmissionException(final String message) {
		super(message);
	}

}
