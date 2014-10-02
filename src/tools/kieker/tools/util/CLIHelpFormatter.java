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

package kieker.tools.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Slightly changes default output of Common CLI's {@link HelpFormatter} starting the descriptions in a new line rather then
 * indented in a column right to the longest option and adding an empty line between options.
 * 
 * The class has been implemented by copying in the {@link #renderOptions(StringBuffer, int, Options, int, int)} original
 * implementation and then modifying it step by step; hence, it includes third-party code from Apache's Commons CLI, licensed under Apache License, Version 2.0.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.7
 */
public class CLIHelpFormatter extends HelpFormatter {

	/**
	 * Creates a new instance of this class.
	 */
	public CLIHelpFormatter() {
		super();
	}

	// we can't do better than suppressing some warnings due to the Common CLI's API:
	@SuppressWarnings("unchecked")
	@Override
	protected StringBuffer renderOptions(final StringBuffer sb, final int width, final Options options, final int leftPad, final int descPad) {
		final String lpad = this.createPadding(leftPad);
		final String dpad = this.createPadding(8); // we use a fixed value instead of descPad

		StringBuilder optBuf;

		final List<Option> optList = new ArrayList<Option>(options.getOptions());
		Collections.sort(optList, this.getOptionComparator());

		for (final Iterator<Option> i = optList.iterator(); i.hasNext();) {
			final Option option = i.next();

			optBuf = new StringBuilder(8);

			if (option.getOpt() == null) {
				optBuf.append(lpad).append("   ").append(this.getLongOptPrefix()).append(option.getLongOpt());
			} else {
				optBuf.append(lpad).append(this.getOptPrefix()).append(option.getOpt());

				if (option.hasLongOpt()) {
					optBuf.append(',').append(this.getLongOptPrefix()).append(option.getLongOpt());
				}
			}

			if (option.hasArg()) {
				if (option.hasArgName()) {
					optBuf.append(" <").append(option.getArgName()).append('>');
				} else {
					optBuf.append(' ');
				}
			}

			sb.append(optBuf.toString()).append(this.getNewLine());

			optBuf = new StringBuilder();
			optBuf.append(dpad);

			if (option.getDescription() != null) {
				optBuf.append(option.getDescription());
			}

			this.renderWrappedText(sb, width, dpad.length(), optBuf.toString());

			if (i.hasNext()) {
				sb.append(this.getNewLine());
				sb.append(this.getNewLine());
			}
		}

		return sb;
	}
}
