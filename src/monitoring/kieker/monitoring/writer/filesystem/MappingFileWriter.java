package kieker.monitoring.writer.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public final class MappingFileWriter {
	private static final Log log = LogFactory.getLog(MappingFileWriter.class);

	private final File mappingFile;
	private int nextId = 1; // first ID is 1
	// TODO instead of synchronized access better a concurrent structure?
	private final Hashtable<Class<? extends IMonitoringRecord>, Integer> class2idMap = new Hashtable<Class<? extends IMonitoringRecord>, Integer>();

	public MappingFileWriter(final String mappingFileFn) throws IOException {
		this.mappingFile = new File(mappingFileFn);
		this.mappingFile.createNewFile();
	}
	
	public final synchronized int idForRecordTypeClass(final Class<? extends IMonitoringRecord> clazz) {
		Integer idObj = this.class2idMap.get(clazz);
		if (idObj == null) {
			this.class2idMap.put(clazz, idObj = this.nextId++);
			this.writeMapping(idObj.intValue(), clazz.getName());
		}
		return idObj;
	}

	private final void writeMapping(final int id, final String className) {
		MappingFileWriter.log.info("Registered monitoring record type with id '" + id + "':" + className);
		try {
			final PrintWriter pw = new PrintWriter(new FileOutputStream(this.mappingFile, true));
			pw.println("$" + id + "=" + className);
			pw.close();
		} catch (final Exception ex) {
			MappingFileWriter.log.fatal("Failed to register record type", ex);
		}
	}
}
