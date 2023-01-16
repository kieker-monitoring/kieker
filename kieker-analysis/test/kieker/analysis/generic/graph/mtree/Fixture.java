/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.graph.mtree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
final class Fixture {

	private static final String FIXTURE_DIRECTORY = "test-resources/fixtures/";

	private final List<Action> actions;
	private final int dimensions;

	private Fixture(final int dimensions) {
		this.dimensions = dimensions;
		this.actions = new ArrayList<>();
	}

	static String path(final String fixtureName) {
		return FIXTURE_DIRECTORY + fixtureName + ".txt";
	}

	static Fixture load(final String fixtureName) {
		final String fixtureFileName = Fixture.path(fixtureName);
		BufferedReader fixtureFile = null;
		try {
			fixtureFile = new BufferedReader(new FileReader(fixtureFileName));

			final int dimensions = Integer.parseInt(fixtureFile.readLine());

			final int count = Integer.parseInt(fixtureFile.readLine());

			final Fixture fixture = new Fixture(dimensions);

			for (int i = 0; i < count; i++) {
				final String line = fixtureFile.readLine();
				final List<String> fields = new ArrayList<>(Arrays.asList(line.split("\\s+")));

				final char cmd = fields.remove(0).charAt(0);
				final Data data = fixture.readData(fields);
				final Data queryData = fixture.readData(fields);
				final double radius = Double.parseDouble(fields.remove(0));
				final int limit = Integer.parseInt(fields.remove(0));

				fixture.actions.add(new Action(cmd, data, queryData, radius, limit));

				assert fields.isEmpty();
			}

			return fixture;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				fixtureFile.close();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public List<Action> getActions() {
		return this.actions;
	}

	private Data readData(final List<String> fields) {
		final int[] values = new int[this.dimensions];
		for (int d = 0; d < this.dimensions; d++) {
			values[d] = Integer.parseInt(fields.remove(0));
		}
		return new Data(values);
	}

	static final class Action {
		private final char cmd;
		private final Data data;
		private final Data queryData;
		private final double radius;
		private final int limit;

		private Action(final char cmd, final Data data, final Data queryData, final double radius, final int limit) {
			this.cmd = cmd;
			this.data = data;
			this.queryData = queryData;
			this.radius = radius;
			this.limit = limit;
		}

		public char getCmd() {
			return this.cmd;
		}

		public Data getData() {
			return this.data;
		}

		public int getLimit() {
			return this.limit;
		}

		public Data getQueryData() {
			return this.queryData;
		}

		public double getRadius() {
			return this.radius;
		}
	}

}
