/***************************************************************************
 * Copyright 2012 by
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

package kieker.tools.traceAnalysis.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.visualization.graph.Color;

/**
 * Implementation of a trace color repository, which associates colors to traces. These colors can, for instance,
 * be used to highlight traces in graph renderings.
 * 
 * @author Holger Knoche
 * 
 */
@Repository(name = "Trace color repository",
		description = "Provides color information for trace coloring")
public class TraceColorRepository extends AbstractRepository<Configuration> {

	private static final String DEFAULT_KEYWORD = "default";
	private static final String COLLISION_KEYWORD = "collision";

	private static final String COLOR_REGEX = "0x([0-9|a-f]{6})";
	private static final Pattern COLOR_PATTERN = Pattern.compile(COLOR_REGEX);

	private static final String DELIMITER_REGEX = "=";

	private final Map<Long, Color> colorMap;
	private final Color defaultColor;
	private final Color collisionColor;

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

	public static TraceColorRepository createFromFile(final String fileName) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
		final Map<Long, Color> colorMap = new HashMap<Long, Color>();
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
			}
			else if (COLLISION_KEYWORD.equals(traceName)) {
				if (traceColor != null) {
					collisionColor = traceColor;
				}
			}
			else {
				final Long traceId = TraceColorRepository.parseTraceId(traceName);

				if ((traceId != null) && (traceColor != null)) {
					colorMap.put(traceId, traceColor);
				}
			}
		}

		reader.close();

		return new TraceColorRepository(new Configuration(), colorMap, defaultColor, collisionColor);
	}

	/**
	 * Creates a new color repository with the given data.
	 * 
	 * @param configuration
	 *            The configuration to use
	 * @param colorMap
	 *            The color map, which associates colors to trace IDs
	 * @param defaultColor
	 *            The color to use for traces which have no specific color
	 * @param collisionColor
	 *            The color to use for elements which can not be associated to a single trace
	 */
	public TraceColorRepository(final Configuration configuration, final Map<Long, Color> colorMap, final Color defaultColor, final Color collisionColor) {
		super(configuration);
		this.colorMap = colorMap;
		this.defaultColor = defaultColor;
		this.collisionColor = collisionColor;
	}

	public Configuration getCurrentConfiguration() {
		return this.configuration;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return null;
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

}
