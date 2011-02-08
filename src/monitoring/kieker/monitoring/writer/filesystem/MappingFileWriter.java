package kieker.monitoring.writer.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
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
			writeMapping(idObj.intValue(), clazz.getName());
		}
		return idObj;
	}

	private final void writeMapping(final int id, final String className) {
		log.info("Registered monitoring record type with id '" + id + "':" + className);
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			fos = new FileOutputStream(this.mappingFile, true); // append
			pw = new PrintWriter(fos);
			pw.println("$" + id + "=" + className);
		} catch (Exception ex) {
			MappingFileWriter.log.fatal("Failed to register record type", ex);
		} finally {
			try {
				pw.close();
				fos.close();
			} catch (IOException ex) {
				log.error("IO Exception", ex);
			}
		}
	}
}
