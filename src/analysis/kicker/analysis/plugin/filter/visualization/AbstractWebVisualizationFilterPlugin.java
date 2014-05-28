/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.analysis.plugin.filter.visualization;

import kicker.analysis.IProjectContext;
import kicker.analysis.plugin.filter.AbstractFilterPlugin;
import kicker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public abstract class AbstractWebVisualizationFilterPlugin extends AbstractFilterPlugin implements IWebVisualizationFilterPlugin {

	public AbstractWebVisualizationFilterPlugin(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

}
