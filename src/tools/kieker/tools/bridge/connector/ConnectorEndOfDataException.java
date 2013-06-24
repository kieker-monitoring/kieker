/**
 * 
 */
package kieker.tools.bridge.connector;

/**
 * @author rju
 * 
 */
public class ConnectorEndOfDataException extends Exception {

	public ConnectorEndOfDataException(final String message) {
		super(message);
	}

	public ConnectorEndOfDataException(final String message, final Exception e) {
		super(message, e);
	}

}
