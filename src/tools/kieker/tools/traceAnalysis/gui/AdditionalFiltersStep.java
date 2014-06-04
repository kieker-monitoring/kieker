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

package kieker.tools.traceAnalysis.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kieker.tools.traceAnalysis.Constants;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class AdditionalFiltersStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final JLabel infoLabel = new JLabel("<html>In this step you manage additional filters and selections for the trace analysis.</html>");
	private final JPanel expandingPanel = new JPanel();
	private final JCheckBox selectOnlyTraces = new JCheckBox("Select Only Traces with Following IDs:");
	private final JTextField selectOnlyTracesInput = new JTextField("1 2 3 4 42");

	public AdditionalFiltersStep() {
		this.addAndLayoutComponents();
		this.addLogicToComponents();
		this.setDefaultState();
	}

	private void addAndLayoutComponents() {
		this.setLayout(new GridBagLayout());

		final GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.infoLabel, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.selectOnlyTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.selectOnlyTracesInput, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.expandingPanel, gridBagConstraints);
	}

	private void addLogicToComponents() {
		this.selectOnlyTraces.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				AdditionalFiltersStep.this.selectOnlyTracesInput.setEnabled(AdditionalFiltersStep.this.selectOnlyTraces.isSelected());
			}
		});
	}

	private void setDefaultState() {
		this.selectOnlyTracesInput.setEnabled(false);
	}

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		if (this.selectOnlyTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_SELECTTRACES);
			final String[] ids = this.selectOnlyTracesInput.getText().split(" ");
			for (final String id : ids) {
				parameters.add(id);
			}
		}
	}

	@Override
	public void saveCurrentConfiguration(final Writer writer) throws IOException {
		writer.write(Boolean.toString(this.selectOnlyTraces.isSelected()));
		writer.write("\n");

		writer.write(this.selectOnlyTracesInput.getText());
		writer.write("\n");
	}

	@Override
	public void loadCurrentConfiguration(final Scanner scanner) throws IOException {
		try {
			this.selectOnlyTraces.setSelected(scanner.nextBoolean());
			scanner.nextLine();
			this.selectOnlyTracesInput.setText(scanner.nextLine());
		} catch (final NoSuchElementException ex) {
			this.setDefaultValues();
			throw new IOException(ex);
		}
	}

	private void setDefaultValues() {
		this.selectOnlyTraces.setSelected(false);
		this.selectOnlyTracesInput.setText("1 2 3 4 42");
	}
}
