package kieker.analysisteetime.plugin.reader.tcp.dualsocket;

import java.nio.ByteBuffer;

import org.slf4j.Logger;

import kieker.analysisteetime.plugin.reader.IRecordReceivedListener;
import kieker.analysisteetime.plugin.reader.RecordDeserializer;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.ILookup;

import teetime.framework.OutputPort;
import teetime.util.stage.io.network.AbstractTcpReader;

class DualSocketTcpLogic extends AbstractTcpReader implements IRecordReceivedListener {

	private final OutputPort<IMonitoringRecord> outputPort;
	private final RecordDeserializer recordDeserializer;

	public DualSocketTcpLogic(int port, int bufferCapacity, Logger logger, ILookup<String> stringRegistry,
			OutputPort<IMonitoringRecord> outputPort) {
		super(port, bufferCapacity, logger);

		this.outputPort = outputPort;
		this.recordDeserializer = new RecordDeserializer(this, stringRegistry);
	}

	@Override
	protected boolean onBufferReceived(ByteBuffer buffer) {
		// identify record class
		if (buffer.remaining() < AbstractMonitoringRecord.TYPE_SIZE_INT) {
			return false;
		}
		final int clazzId = buffer.getInt();

		return this.recordDeserializer.deserializeRecord(clazzId, buffer);
	}

	@Override
	public void onRecordReceived(IMonitoringRecord record) {
		this.outputPort.send(record);
	}
}