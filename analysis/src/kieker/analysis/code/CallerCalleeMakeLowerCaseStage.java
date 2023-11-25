/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.code;

import java.util.Locale;

import kieker.analysis.code.data.CallerCalleeEntry;

import teetime.stage.basic.AbstractFilter;

/**
 * Make names all lower case when case insensitive is requested.
 *
 * @author Reiner Jung
 * @since 1.0
 *
 */
public class CallerCalleeMakeLowerCaseStage extends AbstractFilter<CallerCalleeEntry> {

	private final boolean caseInsensitive;

	public CallerCalleeMakeLowerCaseStage(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	protected void execute(final CallerCalleeEntry element) throws Exception {
		final CallerCalleeEntry result = new CallerCalleeEntry(this.convertToLowerCase(element.getSourcePath()),
				this.convertToLowerCase(element.getSourceModule()), this.convertToLowerCase(element.getCaller()),
				this.convertToLowerCase(element.getTargetPath()), this.convertToLowerCase(element.getTargetModule()),
				this.convertToLowerCase(element.getCallee()));
		this.outputPort.send(result);
	}

	private String convertToLowerCase(final String string) {
		return this.caseInsensitive ? string.toLowerCase(Locale.ROOT) : string;
	}

}
