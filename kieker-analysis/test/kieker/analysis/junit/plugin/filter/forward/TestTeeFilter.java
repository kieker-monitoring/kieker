/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.junit.plugin.filter.forward;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.stage.forward.TeeFilter;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * This test makes sure that the {@link TeeFilter} writes (with regard to the append property) correctly into files.
 *
 * @author Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.10
 */
public class TestTeeFilter extends AbstractKiekerTest {

	public TestTeeFilter() {
		// empty default constructor
	}

	@Test
	public void testFileAppend() throws IOException {
		final File tempFile = File.createTempFile("kieker", ".tmp");

		this.executeAnalysis(tempFile, false, Arrays.asList(1, 2, 3));
		this.executeAnalysis(tempFile, true, Arrays.asList(4, 5, 6));

		final List<Integer> contentOfFile = this.readFromFile(tempFile);
		Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), contentOfFile);
	}

	@Test
	public void testFileOverwrite() throws IOException, IllegalStateException, AnalysisConfigurationException {
		final File tempFile = File.createTempFile("kieker", ".tmp");

		this.executeAnalysis(tempFile, false, Arrays.asList(1, 2, 3));
		this.executeAnalysis(tempFile, false, Arrays.asList(4, 5, 6));

		final List<Integer> contentOfFile = this.readFromFile(tempFile);
		Assert.assertEquals(Arrays.asList(4, 5, 6), contentOfFile);
	}

	private void executeAnalysis(final File tempFile, final boolean append, final List<Integer> objectsToWrite) {
		final TeeFilter teeFilter = new TeeFilter(tempFile.getAbsolutePath(), null, append);

		StageTester.test(teeFilter).and().send(objectsToWrite).to(teeFilter.getInputPort()).start();
	}

	private List<Integer> readFromFile(final File tempFile) throws FileNotFoundException {
		final List<Integer> result = new ArrayList<>();
		Scanner scanner = null;

		try {
			scanner = new Scanner(tempFile, TeeFilter.DEFAULT_ENCODING);
			// The tee filter writes something like "TeeFilter-3(Integer) 4" - therefore we have to parse this String a little bit.
			while (scanner.hasNextLine()) {
				final String line = scanner.nextLine();
				result.add(Integer.parseInt(line.split(" ")[1]));
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return result;
	}
}
