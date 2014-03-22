package kieker.panalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.panalysis.base.Source;

public class FilesystemReaderSource extends Source<FilesystemReaderSource.PORT> {

	static public enum PORT {
		RECORD
	}

	private static final String CSV_SEPARATOR_CHARACTER = ";";

	private final Map<String, Class<? extends IMonitoringRecord>> recordTypePerName = new HashMap<String, Class<? extends IMonitoringRecord>>();

	public FilesystemReaderSource(final long id) {
		super(id, PORT.class);
		this.findAllMonitoringRecordTypes();
	}

	private void findAllMonitoringRecordTypes() {
		final Set<Class<? extends IMonitoringRecord>> types = new Reflections().getSubTypesOf(IMonitoringRecord.class);

		for (final Class<? extends IMonitoringRecord> monitoringRecordType : types) {
			this.recordTypePerName.put(monitoringRecordType.getName(), monitoringRecordType);
		}
	}

	@Override
	public void execute() {
		final String filename = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() != 0) {
					final IMonitoringRecord record = this.createRecordFromLine(line);
					if (record != null) {
						this.deliver(PORT.RECORD, record);
					}
				} // else ignore empty line
			}
		} catch (final FileNotFoundException e) {
			this.logger.error("", e);
		} catch (final IOException e) {
			this.logger.error("", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException e) {
				this.logger.warn("", e);
			}
		}
	}

	private IMonitoringRecord createRecordFromLine(final String line) {
		final String[] recordFields = line.split(CSV_SEPARATOR_CHARACTER);
		// TODO extract record values from line
		final long timestamp = 0;
		final String recordClassName = null;
		final String[] recordValues = null;

		final Class<? extends IMonitoringRecord> recordClazz = this.recordTypePerName.get(recordClassName);

		IMonitoringRecord record = null;
		try {
			record = AbstractMonitoringRecord.createFromStringArray(recordClazz, recordValues);
			record.setLoggingTimestamp(timestamp);
		} catch (final MonitoringRecordException e) {
			this.logger.warn("Cannot create monitoring record from class '"
					+ recordClazz.getName() + "' with values '"
					+ recordValues + "'");
		}
		return record;
	}
}
