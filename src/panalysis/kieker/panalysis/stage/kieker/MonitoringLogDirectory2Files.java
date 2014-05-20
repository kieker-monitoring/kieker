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
package kieker.panalysis.stage.kieker;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.common.util.filesystem.FSUtil;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.stage.Directory2FilesFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class MonitoringLogDirectory2Files extends Directory2FilesFilter {

	public final IInputPort<Directory2FilesFilter, String> filePrefixInputPort = this.createInputPort();

	/**
	 * @author Christian Wulf
	 * 
	 * @since 1.10
	 */
	static class MonitoringLogFileFilter implements FileFilter {
		private String filePrefix;

		public boolean accept(final File pathname) {
			final String name = pathname.getName();
			return pathname.isFile()
					&& name.startsWith(this.filePrefix)
					&& (name.endsWith(FSUtil.NORMAL_FILE_EXTENSION) || BinaryCompressionMethod.hasValidFileExtension(name));
		}

		public String getFilePrefix() {
			return this.filePrefix;
		}

		public void setFilePrefix(final String filePrefix) {
			this.filePrefix = filePrefix;
		}
	}

	private static final Comparator<File> FILE_COMPARATOR = new Comparator<File>() {
		public final int compare(final File f1, final File f2) {
			return f1.compareTo(f2); // simplified (we expect no dirs!)
		}
	};

	/**
	 * @since 1.10
	 */
	public MonitoringLogDirectory2Files() {
		super(new MonitoringLogFileFilter(), FILE_COMPARATOR);
	}

	@Override
	protected boolean execute(final Context<Directory2FilesFilter> context) {
		final String filePrefix = context.tryTake(this.filePrefixInputPort);
		if (filePrefix == null) {
			return false;
		}

		((MonitoringLogFileFilter) this.getFilter()).setFilePrefix(filePrefix);

		return super.execute(context);
	}
}
