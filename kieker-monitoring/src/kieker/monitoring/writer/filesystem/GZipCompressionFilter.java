/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.filesystem;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

/**
 * Zip compression filter for the writer pool.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public class GZipCompressionFilter implements ICompressionFilter {

	@Override
	public OutputStream chain(final OutputStream outputStream, final Path fileName) throws IOException {
		return new GZIPOutputStream(outputStream);
	}

}
