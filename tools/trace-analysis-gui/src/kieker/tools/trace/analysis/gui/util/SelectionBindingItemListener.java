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

package kieker.tools.trace.analysis.gui.util;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

/**
 * An item listener which selects or deselects a given check box if the owning checkbox is selected or deselected.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public final class SelectionBindingItemListener implements ItemListener {

	private final JCheckBox bindedCheckBox;
	private final boolean inverted;

	public SelectionBindingItemListener(final JCheckBox bindedCheckBox, final boolean inverted) {
		this.bindedCheckBox = bindedCheckBox;
		this.inverted = inverted;
	}

	@Override
	public void itemStateChanged(final ItemEvent event) {
		if (this.inverted ^ (ItemEvent.SELECTED == event.getStateChange())) {
			this.bindedCheckBox.setSelected(!this.inverted);
		}
	}

}
