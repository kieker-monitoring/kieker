package kieker.monitoring.writer.filesystem.async;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.MappingFileWriter;

public class BinaryFsWriterThread extends AbstractFsWriterThread {
	private static final Log LOG = LogFactory.getLog(BinaryFsWriterThread.class);

	private DataOutputStream out = null;

	public BinaryFsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile) {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile);
		this.fileExtension = ".bin";
	}

	@Override
	protected void write(final IMonitoringRecord monitoringRecord) throws IOException {
		this.out.writeInt(this.monitoringController.getIdForString(monitoringRecord.getClass().getName()));
		this.out.writeLong(monitoringRecord.getLoggingTimestamp());
		for (final Object recordField : monitoringRecord.toArray()) {
			if (recordField instanceof String) {
				this.out.writeInt(this.monitoringController.getIdForString((String) recordField));
			} else if (recordField instanceof Integer) {
				this.out.writeInt((Integer) recordField);
			} else if (recordField instanceof Long) {
				this.out.writeLong((Long) recordField);
			} else if (recordField instanceof Float) {
				this.out.writeFloat((Float) recordField);
			} else if (recordField instanceof Double) {
				this.out.writeDouble((Double) recordField);
			} else if (recordField instanceof Byte) {
				this.out.writeByte((Byte) recordField);
			} else if (recordField instanceof Short) {
				this.out.writeShort((Short) recordField);
			} else if (recordField instanceof Boolean) {
				this.out.writeBoolean((Boolean) recordField);
			} else {
				BinaryFsWriterThread.LOG.warn("Failed to write recordField of type " + recordField.getClass());
				this.out.writeByte((byte) 0);
			}
		}
	}

	@Override
	protected void prepareFile() throws IOException {
		if (this.out != null) {
			this.out.close();
		}
		this.out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.getFilename())));
	}

	@Override
	protected void cleanup() {
		if (this.out != null) {
			try {
				this.out.close();
			} catch (final IOException ex) {
				BinaryFsWriterThread.LOG.error("Failed to close channel.", ex);
			}
		}
	}
}
