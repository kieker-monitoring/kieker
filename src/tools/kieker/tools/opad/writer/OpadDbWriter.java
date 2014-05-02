package kieker.tools.opad.writer;

import java.sql.SQLException;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.dataexchange.IDatabaseConnection;
import kieker.tools.dataexchange.TransferDatabasePlainConnection;
import kieker.tools.opad.record.OpadOutputData;

/**
 * Class that connects to the interface singleton to write the OPAD data to the database.
 * 
 * @author Thomas Düllmann, Yannic Noller
 * @version 0.1
 */
@Plugin(description = "Writes the data from opad to db")
public class OpadDbWriter extends AbstractFilterPlugin {

	/**
	 * Input port that receives OpadOutputData objects.
	 */
	public static final String INPUT_PORT_NAME_OPAD_DATA = "opadData";

	/**
	 * Log object to log exceptions and debug information.
	 */
	private static final Log LOG = LogFactory.getLog(OpadDbWriter.class);

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            configuration for this writer
	 * @param projectContext
	 *            controller
	 */
	public OpadDbWriter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * Writes the data received through the input port to the database.
	 * 
	 * @param data
	 *            OpadOutputData that has to be written to the database
	 */
	@InputPort(name = OpadDbWriter.INPUT_PORT_NAME_OPAD_DATA, eventTypes = { OpadOutputData.class })
	public void writeToDB(final OpadOutputData data) {
		try {
			final IDatabaseConnection tdpc = TransferDatabasePlainConnection.getInstance();
			tdpc.insertAnomalyScore(data.getOperationSignature().getFullQualifiedOperationString(), data.getTimestamp(), (float) data.getAnomalyScore(),
					(float) data.getAnomalyThreshold());

		} catch (final SQLException e) {
			OpadDbWriter.LOG.debug("SQL Exception", e);
		}
	}
}
