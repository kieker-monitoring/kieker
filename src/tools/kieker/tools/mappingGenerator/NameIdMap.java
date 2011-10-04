/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.mappingGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn
 */
public class NameIdMap {

	private static final Log log = LogFactory.getLog(NameIdMap.class);
	private final Hashtable<String, Integer> name2IdMap = new Hashtable<String, Integer>();
	private final TreeMap<Integer, String> id2NameMap = new TreeMap<Integer, String>();
	private AtomicInteger nextId = new AtomicInteger(0);

	/**
	 * Constructor for an empty map.
	 */
	public NameIdMap() {
	}

	/**
	 * Adds the name and returns the id.
	 * If the name has been registered before, the existing
	 * id is returned.
	 * 
	 * @param name
	 * @return the id for the given name.
	 */
	public int registerName(String name) {
		Integer idObj = this.name2IdMap.get(name);
		if (idObj != null) {
			return idObj;
		}
		idObj = this.nextId.getAndIncrement();
		this.name2IdMap.put(name, idObj);
		this.id2NameMap.put(idObj, name);
		return idObj;
	}

	public void writeMapToFile(String filename) throws IOException {
		File f = new File(filename);
		PrintWriter pw = null;
		pw = new PrintWriter(f);
		synchronized (this.id2NameMap) {
			Iterator<Integer> ids = this.id2NameMap.keySet().iterator();
			Iterator<String> names = this.id2NameMap.values().iterator();
			while (ids.hasNext() && names.hasNext()) {
				pw.println(ids.next() + "=" + names.next());
			}
		}
		pw.close();
		log.info("Wrote mapping file to " + f.getCanonicalPath());
	}

	/**
	 * Reads and returns a mapping from the given file.
	 * 
	 * @param filename
	 * @throws java.io.IOException
	 */
	public static NameIdMap readMapFromFile(String filename) throws IOException {
		NameIdMap inst = new NameIdMap();
		StringTokenizer st;
		File mappingFile = new File(filename);
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(mappingFile));
			String line;

			while ((line = in.readLine()) != null) {
				try {
					st = new StringTokenizer(line, "=");
					int numTokens = st.countTokens();
					if (numTokens == 0) {
						continue;
					}
					if (numTokens != 2) {
						throw new IllegalArgumentException("Invalid number of tokens (" + numTokens + ") Expecting 2");
					}
					String idStr = st.nextToken();
					// the leading $ is optional
					Integer id = Integer.valueOf(idStr.startsWith("$") ? idStr.substring(1) : idStr);
					String name = st.nextToken();
					inst.id2NameMap.put(id, name);
					inst.name2IdMap.put(name, id);
				} catch (RuntimeException e) {
					log.error("Failed to parse line: {" + line + "} from file " + mappingFile.getAbsolutePath(), e);
					break;
				}
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					log.error("Exception", e);
				}
			}
		}
		return inst;
	}
}
