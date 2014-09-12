/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.writer.filesystem;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.controller.IMonitoringController;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public abstract class AbstractTestLogRotationMaxLogFiles extends AbstractKiekerTest {

	private static final int MAXENTRIESINFILE = 2;

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (Rule has to be public)

	/**
	 * Default constructor.
	 */
	public AbstractTestLogRotationMaxLogFiles() {
		// empty default constructor
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesUnlimited0() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(-1, 0);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesUnlimited1() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(-1, 1);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesUnlimited10() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(-1, 10);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesUnlimitedZero0() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(0, 0);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesUnlimitedZero1() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(0, 1);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesUnlimitedZero10() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(0, 10);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedOne0() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(1, 0);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedOne1() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(1, 1);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedOne10() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(1, 10);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTwo0() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(2, 0);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTwo1() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(2, 1);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTwo2() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(2, 2);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTwo3() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(2, 3);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTwo10() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(2, 10);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTen0() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(10, 0);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTen2() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(10, 2);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTen10() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(10, 10);
	}

	/**
	 * A test for the maximal log files of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogFilesLimitedTen11() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogFiles(10, 11);
	}

	private final void checkMaxLogFiles(final int maxLogFiles, final int writtenFiles) throws IOException {
		final IMonitoringController ctrl = this.createController(this.tmpFolder.getRoot().getCanonicalPath(), MAXENTRIESINFILE, maxLogFiles);
		final int writtenRecords = writtenFiles * MAXENTRIESINFILE;
		// write records
		for (int i = 0; i < writtenRecords; i++) {
			ctrl.newMonitoringRecord(new EmptyRecord());
		}
		// finish writing
		ctrl.terminateMonitoring();

		final File[] logDirs = new File(this.tmpFolder.getRoot().getCanonicalPath()).listFiles();
		Assert.assertEquals(1, logDirs.length);
		final int expectedNumberFiles;
		if (maxLogFiles > 0) {
			expectedNumberFiles = Math.min(maxLogFiles, writtenFiles);
		} else {
			expectedNumberFiles = writtenFiles;
		}

		// -1 for mapping file
		Assert.assertEquals(expectedNumberFiles, logDirs[0].list().length - 1);
	}

	protected abstract IMonitoringController createController(final String path, final int maxEntriesInFile, final int maxLogFiles);
}
