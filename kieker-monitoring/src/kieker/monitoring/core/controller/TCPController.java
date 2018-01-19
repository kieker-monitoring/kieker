package kieker.monitoring.core.controller;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.tcp.SingleSocketRecordReader;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.listener.MonitoringCommandListener;

/**
 *
 * @author Marc Adolf
 *
 *
 */
public class TCPController extends AbstractController implements IRemoteController {

	private static final int MESSAGE_BUFFER_SIZE = 65535;
	/** The log for this component. */
	private final Log log;
	// maybe not necessary
	private final String domain;
	private final int port;
	private final IRecordReceivedListener listener;
	private final SingleSocketRecordReader tcpReader;
	private final boolean tcpEnabled;

	protected TCPController(final Configuration configuration) {
		super(configuration);

		this.log = LogFactory.getLog(this.getClass());
		this.listener = new MonitoringCommandListener(this.monitoringController);
		this.domain = configuration.getStringProperty(ConfigurationFactory.ACTIVATE_TCP_DOMAIN);
		this.port = Integer.parseInt(configuration.getStringProperty(ConfigurationFactory.ACTIVATE_TCP_REMOTE_PORT));
		this.tcpEnabled = configuration.getBooleanProperty(ConfigurationFactory.ACTIVATE_TCP);
		this.tcpReader = new SingleSocketRecordReader(this.port, MESSAGE_BUFFER_SIZE, this.log, this.listener);

	}

	@Override
	public String getControllerDomain() {
		return this.domain;
	}

	@Override
	protected void init() {
		if (this.tcpEnabled) {
			this.tcpReader.run();
		}

	}

	@Override
	protected void cleanup() {
		this.tcpReader.terminate();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(255);
		sb.append("TCPController: ");
		if (this.tcpEnabled) {
			sb.append("TCP enabled (Domain: '");
			sb.append(this.domain);
			sb.append("')\n");
		} else {
			sb.append("TCP disabled\n");
		}
		return sb.toString();
	}

}
