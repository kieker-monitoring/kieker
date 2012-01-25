package kieker.analysis.reader;

import java.util.Map;

import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;

/**
 * This class should be used as a base for every reader used within <i>Kieker</i>.
 * 
 * @author Nils Christian Ehmke
 */
public abstract class AbstractReaderPlugin extends AbstractPlugin implements IMonitoringReader {

	/**
	 * Each Plugin requires a constructor with a single Configuration object.
	 * 
	 * @param configuration
	 *            The configuration which should be used to initialize the object.
	 */
	public AbstractReaderPlugin(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		super(configuration, repositories);
	}
}
