package kieker.analysis.behavior.mtree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Fixture {

	static class Action {
		char cmd;
		Data data;
		Data queryData;
		double radius;
		int limit;

		private Action(final char cmd, final Data data, final Data queryData, final double radius, final int limit) {
			this.cmd = cmd;
			this.data = data;
			this.queryData = queryData;
			this.radius = radius;
			this.limit = limit;
		}
	}

	private final int dimensions;
	List<Action> actions;

	static String path(final String fixtureName) {
		return "cpp/tests/fixtures/" + fixtureName + ".txt";
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

	private Fixture(final int dimensions) {
		this.dimensions = dimensions;
		this.actions = new ArrayList<>();
	}

	private Data readData(final List<String> fields) {
		final int[] values = new int[this.dimensions];
		for (int d = 0; d < this.dimensions; d++) {
			values[d] = Integer.parseInt(fields.remove(0));
		}
		return new Data(values);
	}
}
