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
package kieker.tools.trace.analysis;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import kieker.tools.trace.analysis.filter.visualization.VisualizationConstants;

/**
 * Check whether a proper decorator is used.
 *
 * @author David Georg Reichelt
 * @author Reiner Jung
 * @since 2.0.0
 */
public class DecoratorValidator implements IParameterValidator {

	@Override
	public void validate(final String name, final String value) throws ParameterException {
		if (!VisualizationConstants.RESPONSE_TIME_DECORATOR_FLAG_NS.equals(value)
				&& !VisualizationConstants.RESPONSE_TIME_DECORATOR_FLAG_US.equals(value)
				&& !VisualizationConstants.RESPONSE_TIME_DECORATOR_FLAG_MS.equals(value)
				&& !VisualizationConstants.RESPONSE_TIME_DECORATOR_FLAG_S.equals(value)
				&& !StringConstants.RESPONSE_TIME_COLORING_DECORATOR_FLAG.equals(value)) {
			throw new ParameterException(String.format("Parameter %s requires on of the following decorators: responseTimes-ns, -us, -ms or -s"));
		}
	}

}
