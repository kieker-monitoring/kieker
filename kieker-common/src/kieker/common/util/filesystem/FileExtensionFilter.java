/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.util.filesystem;

import java.io.File;
import java.io.FilenameFilter;

/**
 * File extension filters.
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class FileExtensionFilter implements FilenameFilter {

	/** A filter ensuring that the name of the file starts with {@value FSUtil.FILE_PREFIX} and ends with {@value FSUtil.MAP_FILE_EXTENSION}. */
	public static final FileExtensionFilter BIN = new FileExtensionFilter(FSUtil.BINARY_FILE_EXTENSION);
	public static final FileExtensionFilter DAT = new FileExtensionFilter(FSUtil.DAT_FILE_EXTENSION);
	public static final FileExtensionFilter DEFLATE = new FileExtensionFilter(FSUtil.DEFLATE_FILE_EXTENSION);
	public static final FileExtensionFilter GZIP = new FileExtensionFilter(FSUtil.GZIP_FILE_EXTENSION);
	public static final FileExtensionFilter MAP = new FileExtensionFilter(FSUtil.MAP_FILE_EXTENSION);
	public static final FileExtensionFilter XZ = new FileExtensionFilter(FSUtil.XZ_FILE_EXTENSION);
	public static final FileExtensionFilter ZIP = new FileExtensionFilter(FSUtil.ZIP_FILE_EXTENSION);

	private final String fileExtension;

	/**
	 * create filename filter.
	 * 
	 * @param fileExtension
	 *            extension
	 */
	public FileExtensionFilter(final String fileExtension) {
		super();
		this.fileExtension = fileExtension;
	}

	@Override
	public boolean accept(final File dir, final String name) {
		if (!name.startsWith(FSUtil.FILE_PREFIX)) {
			return false;
		}
		return name.endsWith(this.fileExtension);
	}

	public String getExtensionName() {
		return this.fileExtension;
	}

}
