package kieker.analysis.reader;

import kieker.analysis.plugin.AbstractPlugin;
import kieker.common.configuration.Configuration;

/**
 * This class should be used as a base for every reader used within
 * <i>Kieker</i>. Every reader should follow the following behaviour:<br>
 * <ul>
 * <li>It should create and register its ports (only output).
 * <li>The readed data should be send to the output ports.
 * </ul>
 * 
 * @author Nils Christian Ehmke
 */
public abstract class AbstractReaderPlugin extends AbstractPlugin implements IMonitoringReader {

	/**
	 * Each Plugin requires a constructor with a single Configuration object.
	 * 
	 * @param configuration
	 *            The configuration which should be used to initialize the
	 *            object.
	 */
	public AbstractReaderPlugin(final Configuration configuration) {
		super(configuration);
	}

}
