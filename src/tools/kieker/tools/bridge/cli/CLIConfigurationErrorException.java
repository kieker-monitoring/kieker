/**
 * 
 */
package kieker.tools.bridge.cli;

import java.net.URISyntaxException;

/**
 * @author rju
 * 
 */
public class CLIConfigurationErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1187180806531494898L;

	public CLIConfigurationErrorException(final String message) {
		super(message);
	}

	public CLIConfigurationErrorException(final String message, final URISyntaxException exception) {
		super(message, exception);
	}

}
