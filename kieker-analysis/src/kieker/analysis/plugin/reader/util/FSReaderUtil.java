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
package kieker.analysis.plugin.reader.util;

import kieker.analysis.plugin.reader.depcompression.AbstractDecompressionFilter;
import kieker.analysis.plugin.reader.depcompression.DeflateDecompressionFilter;
import kieker.analysis.plugin.reader.depcompression.GZipDecompressionFilter;
import kieker.analysis.plugin.reader.depcompression.NoneDecompressionFilter;
import kieker.analysis.plugin.reader.depcompression.XZDecompressionFilter;
import kieker.analysis.plugin.reader.depcompression.ZipDecompressionFilter;
import kieker.analysis.source.file.AbstractEventDeserializer;
import kieker.analysis.source.file.AbstractMapDeserializer;
import kieker.analysis.source.file.BinaryEventDeserializer;
import kieker.analysis.source.file.DatEventDeserializer;
import kieker.analysis.source.file.TextMapDeserializer;
import kieker.common.util.filesystem.FSUtil;

/**
 * This class is used during refactoring and architecture evolution to keep all readers running.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public final class FSReaderUtil {

	private FSReaderUtil() {
		// private default constructor for factory class.
	}

	public static String getExtension(final String name) {
		final int suffixStartPosition = name.lastIndexOf('.');

		if (suffixStartPosition == -1) {
			return null;
		} else {
			return name.substring(suffixStartPosition);
		}
	}

	public static Class<? extends AbstractDecompressionFilter> findDecompressionFilterByExtension(final String name) {
		final String extension = FSReaderUtil.getExtension(name);
		if (FSUtil.GZIP_FILE_EXTENSION.equals(extension)) {
			return GZipDecompressionFilter.class;
		} else if (FSUtil.DEFLATE_FILE_EXTENSION.equals(extension)) {
			return DeflateDecompressionFilter.class;
		} else if (FSUtil.XZ_FILE_EXTENSION.equals(extension)) {
			return XZDecompressionFilter.class;
		} else if (FSUtil.ZIP_FILE_EXTENSION.equals(extension)) {
			return ZipDecompressionFilter.class;
		} else {
			return NoneDecompressionFilter.class;
		}
	}

	public static boolean hasValidFileExtension(final String name) {
		final String extension = FSReaderUtil.getExtension(name);
		if (FSUtil.BINARY_FILE_EXTENSION.equals(extension) || FSUtil.DAT_FILE_EXTENSION.equals(extension)) {
			return true;
		} else {
			return !FSReaderUtil.findDecompressionFilterByExtension(name).equals(NoneDecompressionFilter.class);
		}
	}

	public static Class<? extends AbstractEventDeserializer> findEventDeserializer(final String logFileName) {
		final String extension = FSReaderUtil.getExtension(logFileName);

		if (FSUtil.DAT_FILE_EXTENSION.equals(extension)) {
			return DatEventDeserializer.class;
		} else if (FSUtil.BINARY_FILE_EXTENSION.equals(extension)) {
			return BinaryEventDeserializer.class;
		} else {
			return null;
		}
	}

	public static Class<? extends AbstractMapDeserializer> findMapDeserializer(final String mapFileName) {
		final String extension = FSReaderUtil.getExtension(mapFileName);

		if (FSUtil.MAP_FILE_EXTENSION.equals(extension)) {
			return TextMapDeserializer.class;
		} else {
			return null;
		}
	}

}
