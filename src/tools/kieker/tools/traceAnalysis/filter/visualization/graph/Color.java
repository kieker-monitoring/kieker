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

package kieker.tools.traceAnalysis.filter.visualization.graph;

/**
 * (RGB-based) Color implementation for the visualization package.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public class Color {

	/**
	 * The predefined color black.
	 */
	public static final Color BLACK = new Color((byte) 0x00, (byte) 0x00, (byte) 0x00);
	/**
	 * The predefined color red.
	 */
	public static final Color RED = new Color((byte) 0xFF, (byte) 0x00, (byte) 0x00);
	/**
	 * The predefined color green.
	 */
	public static final Color GREEN = new Color((byte) 0x00, (byte) 0xFF, (byte) 0x00);
	/**
	 * The predefined color blue.
	 */
	public static final Color BLUE = new Color((byte) 0x00, (byte) 0x00, (byte) 0xFF);
	/**
	 * The predefined color gray.
	 */
	public static final Color GRAY = new Color((byte) 0x80, (byte) 0x80, (byte) 0x80);
	/**
	 * The predefined color white.
	 */
	public static final Color WHITE = new Color((byte) 0xFF, (byte) 0xFF, (byte) 0xFF);

	private final int rgb;

	/**
	 * Creates a new color from the given values.
	 * 
	 * @param red
	 *            The color's red value
	 * @param green
	 *            The color's green value
	 * @param blue
	 *            The color's blue value
	 */
	public Color(final byte red, final byte green, final byte blue) {
		this.rgb = ((red << 16) | (green << 8) | blue) & 0x00FFFFFF;
	}

	/**
	 * Creates a new color with the given RGB value.
	 * 
	 * @param rgb
	 *            The RGB value of the new color (see {@link #getRGB()} for more information)
	 */
	public Color(final int rgb) {
		this.rgb = rgb;
	}

	/**
	 * Returns this color's RGB value as an {@code int}. The blue value is stored in (the least
	 * significant) byte 0, green in byte 1, and red in byte 2.
	 * 
	 * @return See above
	 */
	public int getRGB() {
		return this.rgb;
	}

	@Override
	public int hashCode() {
		return this.rgb;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Color)) {
			return false;
		}

		final Color otherColor = (Color) other;
		return this.getRGB() == otherColor.getRGB();
	}

}
