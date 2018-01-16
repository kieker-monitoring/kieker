package kieker.monitoring.core.controller;

import java.io.IOException;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.MonitoringCommandListener;
import kieker.common.record.tcp.SingleSocketRecordReader;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writer.tcp.SingleSocketTcpWriter;

/**
 *
 * @author Marc Adolf
 *
 *
 */
public class TCPController extends AbstractController implements IRemoteController {

	private static final int MESSAGE_BUFFER_SIZE = 65535;
	/** The log for this component. */
	protected final Log log;
	private final SingleSocketTcpWriter tcpWriter;
	private final String domain;
	private final int port;
	private final IRecordReceivedListener listener;
	private final SingleSocketRecordReader tcpReader;

	protected TCPController(final Configuration configuration) throws IOException {
		super(configuration);

		this.tcpWriter = new SingleSocketTcpWriter(configuration);
		this.log = LogFactory.getLog(this.getClass());
		this.listener = new MonitoringCommandListener();
		this.domain = configuration.getStringProperty(ConfigurationFactory.ACTIVATE_TCP_DOMAIN);
		this.port = Integer.parseInt(configuration.getStringProperty(ConfigurationFactory.ACTIVATE_TCP_REMOTE_PORT));
		this.tcpReader = new SingleSocketRecordReader(this.port, MESSAGE_BUFFER_SIZE, this.log, this.listener);

	}

	@Override
	public String getControllerDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
