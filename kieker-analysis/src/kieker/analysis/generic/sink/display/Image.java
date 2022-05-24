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

package kieker.analysis.generic.sink.display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * his class is currently under development, mostly for test purposes, and not designed for productive deployment.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.6
 */
public class Image extends AbstractDisplay {

	private final BufferedImage internalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

	/**
	 * Creates a new instance of this class.
	 */
	public Image() {
		// No code necessary
	}

	public BufferedImage getImage() {
		return this.internalImage;
	}

	public Graphics2D getGraphics() {
		return this.internalImage.createGraphics();
	}
}
