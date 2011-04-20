package kieker.monitoring.writer;

import java.util.Properties;
import java.util.Set;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller, Robert von Massow
 */
public abstract class AbstractMonitoringWriter implements IMonitoringWriter {
	private static final Log log = LogFactory.getLog(AbstractMonitoringWriter.class);

	protected final Configuration configuration;
	protected IMonitoringController monitoringController;

	/**
	 * 
	 * @param IWriterController
	 * @param configuration
	 */
	protected AbstractMonitoringWriter(final Configuration configuration) {
		try {
			// somewhat dirty hack...
			final Properties defaultProps = this.getDefaultProperties();
			if (defaultProps != null) {
				configuration.setDefaultProperties(defaultProps);
			}
		} catch (final IllegalAccessException ex) {
			AbstractMonitoringWriter.log.error("Unable to set writer custom default properties");
		}
		this.configuration = configuration;
	}

	/**
	 * This method should be overwritten, iff the writer is external to Kieker and
	 * thus its default configuration is not included in the default config file.
	 * 
	 * @return
	 */
	protected Properties getDefaultProperties() {
		return null;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Writer: '");
		sb.append(this.getClass().getName());
		sb.append("'\n\tConfiguration:");
		final Set<String> keys = this.configuration.stringPropertyNames();
		if (keys.isEmpty()) {
			sb.append("\n\t\tNo Configuration");
		} else {
			for (final String property : keys) {
				sb.append("\n\t\t");
				sb.append(property);
				sb.append("='");
				sb.append(this.configuration.getProperty(property));
				sb.append("'");
			}
		}
		return sb.toString();
	}

	@Override
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);

	@Override
	public abstract void terminate();

	@Override
	public final void setController(final IMonitoringController monitoringController) throws Exception {
		this.monitoringController = monitoringController;
		this.init();
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}

	abstract protected void init() throws Exception;
}
