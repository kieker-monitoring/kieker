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

package kieker.tools.trace.analysis.gui;

import java.util.Collection;
import java.util.Properties;

import javax.swing.JPanel;

/**
 * An abstract base for all other steps within the trace analysis GUI.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public abstract class AbstractStep extends JPanel {

	private static final long serialVersionUID = 1L;

	public abstract void addSelectedTraceAnalysisParameters(final Collection<String> parameters);

	public abstract void loadDefaultConfiguration();

	public abstract void saveCurrentConfiguration(Properties properties);

	public abstract void loadCurrentConfiguration(Properties properties);

	public abstract boolean isNextStepAllowed();

}
