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
public abstract class AbstractTestLogRotationMaxLogSize extends AbstractKiekerTest {

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (Rule has to be public)

	protected final int bytesPerRecord;

	public AbstractTestLogRotationMaxLogSize(final int bytesPerRecord) {
		this.bytesPerRecord = bytesPerRecord;
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeUnlimited1() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(-1, 2, 512);
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeUnlimited0() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(0, 1, 1024);
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeLimited1m8w4r() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(1, 2, 256);
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeLimited1m4w4r() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(1, 1, 256);
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeLimited1m1w1r() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(1, 1, 1024);
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeLimited4m2w2r() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(4, 1, 512);
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeLimited4m8w8r() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(4, 4, 512);
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeLimited4m10w8r() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(4, 5, 512);
	}

	/**
	 * A test for the maximal log size of the log rotation
	 * .
	 * 
	 * @throws IOException
	 *             If something went wrong while writing the log.
	 */
	@Test
	public final void testMaxLogSizeLimited4m1w1r() throws IOException { // NOPMD (assert in checkMaxLogFiles)
		this.checkMaxLogSize(4, 5, 5 * 1024);
	}

	/**
	 * 
	 * @param maxLogSize
	 *            in MiB
	 * @param writtenSize
	 *            in MiB
	 * @param fileSize
	 *            in KiB
	 * @throws IOException
	 */
	private final void checkMaxLogSize(final int maxLogSize, final int writtenSize, final int fileSize) throws IOException {
		final int maxEntriesInFile = AbstractTestLogRotationMaxLogSize.div(1024 * fileSize, this.bytesPerRecord);
		final IMonitoringController ctrl = this.createController(this.tmpFolder.getRoot().getCanonicalPath(), maxEntriesInFile, maxLogSize);
		final int writtenRecords = AbstractTestLogRotationMaxLogSize.div(1024 * 1024 * writtenSize, this.bytesPerRecord);
		// write records
		for (int i = 0; i < writtenRecords; i++) {
			ctrl.newMonitoringRecord(new EmptyRecord());
		}
		// finish writing
		ctrl.terminateMonitoring();

		final File[] logDirs = new File(this.tmpFolder.getRoot().getCanonicalPath()).listFiles();
		Assert.assertEquals(1, logDirs.length);
		final int writtenFiles = AbstractTestLogRotationMaxLogSize.div(writtenRecords, maxEntriesInFile);
		final int expectedNumberFiles;
		if (maxLogSize > 0) {
			final int actualFileSize = maxEntriesInFile * this.bytesPerRecord;
			expectedNumberFiles = Math.min(writtenFiles, AbstractTestLogRotationMaxLogSize.div(maxLogSize * 1024 * 1024, actualFileSize));
		} else {
			expectedNumberFiles = writtenFiles;
		}
		// System.out.println("Written: " + writtenFiles + " remaining: " + expectedNumberFiles);
		// -1 for mapping file
		Assert.assertEquals(expectedNumberFiles, logDirs[0].list().length - 1);
	}

	protected abstract IMonitoringController createController(final String path, final int maxEntriesInFile, final int maxLogSize);

	/**
	 * This is a simple integer division which rounds up.
	 * 
	 * @param num
	 *            The dividend of the operation.
	 * @param div
	 *            The divisor of the operation.
	 * @return The quotient of the given parameters, but rounded up.
	 */
	private static final int div(final int num, final int div) {
		return ((num + div) - 1) / div;
	}
}
