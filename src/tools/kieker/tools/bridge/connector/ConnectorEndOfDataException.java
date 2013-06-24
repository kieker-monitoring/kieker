/**
 * 
 */
package kieker.tools.bridge.connector;

/**
 * @author rju
 * 
 */
public class ConnectorEndOfDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3984491886499581526L;

	public ConnectorEndOfDataException(final String message) {
		super(message);
	}

	public ConnectorEndOfDataException(final String message, final Exception e) {
		super(message, e);
	}

}
