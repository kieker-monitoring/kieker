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

package kieker.tools.trace.analysis.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.visualization.graph.Color;

/**
 * Implementation of a trace color repository, which associates colors to traces. These colors can, for instance,
 * be used to highlight traces in graph renderings.
 *
 * @author Holger Knoche
 *
 * @since 1.6
 */
@Repository(name = "Trace color repository",
		description = "Provides color information for trace coloring",
		configuration = {
			@Property(name = TraceColorRepository.CONFIG_PROPERTY_NAME_TRACE_COLOR_FILE_NAME, defaultValue = "")
		})
public class TraceColorRepository extends AbstractRepository {

	/**
	 * Name of the configuration property that contains the file name of the trace color file.
	 */
	public static final String CONFIG_PROPERTY_NAME_TRACE_COLOR_FILE_NAME = "traceColorFileName";

	private static final String DEFAULT_KEYWORD = "default";
	private static final String COLLISION_KEYWORD = "collision";

	private static final String COLOR_REGEX = "0x([0-9|a-f]{6})";
	private static final Pattern COLOR_PATTERN = Pattern.compile(COLOR_REGEX);

	private static final String DELIMITER_REGEX = "=";

	private static final String ENCODING = "UTF-8";

	private final ConcurrentMap<Long, Color> colorMap;
	private final Color defaultColor;
	private final Color collisionColor;

	/**
	 * Creates a new description repository using the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use
	 * @param projectContext
	 *            The project context for this plugin.
	 *
	 * @throws IOException
	 *             If an I/O error occurs during initialization
	 */
	public TraceColorRepository(final Configuration configuration, final IProjectContext projectContext) throws IOException {
		this(configuration, TraceColorRepository.readDataFromFile(configuration.getStringProperty(CONFIG_PROPERTY_NAME_TRACE_COLOR_FILE_NAME)), projectContext);
	}

	/**
	 * Creates a new color repository with the given data.
	 *
	 * @param configuration
	 *            The configuration to use
	 * @param colorData
	 *            The color data to use for this repository
	 * @param projectContext
	 *            The project context to use for this repository.
	 */
	public TraceColorRepository(final Configuration configuration, final TraceColorRepositoryData colorData, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.colorMap = colorData.getColorMap();
		this.defaultColor = colorData.getDefaultColor();
		this.collisionColor = colorData.getCollisionColor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		return this.configuration;
	}

	/**
	 * Returns the color map stored in this repository.
	 *
	 * @return See above
	 */
	public Map<Long, Color> getColorMap() {
		return Collections.unmodifiableMap(this.colorMap);
	}

	/**
	 * Returns the color to use for elements which are not defined in the color map.
	 *
	 * @return See above
	 */
	public Color getDefaultColor() {
		return this.defaultColor;
	}

	/**
	 * Returns the color to use for elements for which no unique color can be determined.
	 *
	 * @return See above
	 */
	public Color getCollisionColor() {
		return this.collisionColor;
	}

	private static Long parseTraceId(final String input) {
		try {
			return Long.parseLong(input);
		} catch (final NumberFormatException e) {
			return null;
		}
	}

	private static Color parseColor(final String input) {
		final Matcher matcher = COLOR_PATTERN.matcher(input);
		if (!matcher.matches()) {
			return null;
		}

		final int rgbValue = Integer.parseInt(matcher.group(1), 16);
		return new Color(rgbValue);
	}

	/**
	 * Initializes a trace color repository from a given file.
	 *
	 * @param fileName
	 *            The name of the file to read from
	 * @param projectContext
	 *            The project context to use.
	 * @return The initialized trace color repository
	 *
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public static TraceColorRepository createFromFile(final String fileName, final IProjectContext projectContext) throws IOException {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_TRACE_COLOR_FILE_NAME, fileName);
		return new TraceColorRepository(configuration, TraceColorRepository.readDataFromFile(fileName), projectContext);
	}

	private static TraceColorRepositoryData readDataFromFile(final String fileName) throws IOException {
		BufferedReader reader = null;
		try {
			reader = Files.newBufferedReader(Paths.get(fileName), Charset.forName(ENCODING));
			final ConcurrentMap<Long, Color> colorMap = new ConcurrentHashMap<>();
			Color defaultColor = Color.BLACK;
			Color collisionColor = Color.GRAY;

			while (true) {
				final String currentLine = reader.readLine();
				if (currentLine == null) {
					break;
				}

				final String[] parts = currentLine.split(DELIMITER_REGEX);
				if (parts.length != 2) {
					continue;
				}

				final String traceName = parts[0];
				final String colorSpecification = parts[1];

				final Color traceColor = TraceColorRepository.parseColor(colorSpecification);

				if (DEFAULT_KEYWORD.equals(traceName)) {
					if (traceColor != null) {
						defaultColor = traceColor;
					}
				} else if (COLLISION_KEYWORD.equals(traceName)) {
					if (traceColor != null) {
						collisionColor = traceColor;
					}
				} else {
					final Long traceId = TraceColorRepository.parseTraceId(traceName);

					if ((traceId != null) && (traceColor != null)) {
						colorMap.put(traceId, traceColor);
					}
				}
			}

			return new TraceColorRepositoryData(colorMap, defaultColor, collisionColor);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * This class groups the data required for a {@link TraceColorRepository}.
	 *
	 * @author Holger Knoche
	 *
	 * @since 1.6
	 */
	public static class TraceColorRepositoryData {
		private final ConcurrentMap<Long, Color> colorMap;
		private final Color defaultColor;
		private final Color collisionColor;

		/**
		 * Creates a new data object using the given data.
		 *
		 * @param colorMap
		 *            The color map (trace id -> color) to use
		 * @param defaultColor
		 *            The default color to use
		 * @param collisionColor
		 *            The collision color to use
		 */
		public TraceColorRepositoryData(final ConcurrentMap<Long, Color> colorMap, final Color defaultColor, final Color collisionColor) {
			this.colorMap = colorMap;
			this.defaultColor = defaultColor;
			this.collisionColor = collisionColor;
		}

		ConcurrentMap<Long, Color> getColorMap() { // NOPMD package for outer class
			return this.colorMap;
		}

		Color getDefaultColor() { // NOPMD package for outer class
			return this.defaultColor;
		}

		Color getCollisionColor() { // NOPMD package for outer class
			return this.collisionColor;
		}

	}

}
