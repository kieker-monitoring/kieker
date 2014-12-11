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
import java.util.Collection;
import java.util.Properties;

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

	private static final String PROPERTY_KEY_IDENTIFIER = AdditionalFiltersStep.class.getSimpleName();
	private static final String PROPERTY_KEY_SELECT_TRACES = PROPERTY_KEY_IDENTIFIER + ".selectOnlyTraces";
	private static final String PROPERTY_KEY_SELECT_TRACES_INPUT = PROPERTY_KEY_IDENTIFIER + ".selectOnlyTracesInput";
	private static final String PROPERTY_KEY_FILTER_TRACES = PROPERTY_KEY_IDENTIFIER + ".filterTraces";
	private static final String PROPERTY_KEY_FILTER_TRACES_INPUT = PROPERTY_KEY_IDENTIFIER + ".filterTracesInput";
	private static final String PROPERTY_KEY_IGNORE_BEFORE = PROPERTY_KEY_IDENTIFIER + ".ignoreBefore";
	private static final String PROPERTY_KEY_IGNORE_BEFORE_INPUT = PROPERTY_KEY_IDENTIFIER + ".ignoreBeforeInput";
	private static final String PROPERTY_KEY_IGNORE_AFTER = PROPERTY_KEY_IDENTIFIER + ".ignoreAfter";
	private static final String PROPERTY_KEY_IGNORE_AFTER_INPUT = PROPERTY_KEY_IDENTIFIER + ".ignoreAfterInput";

	private static final String SELECT_ONLY_TRACES_TOOLTIP = "Consider only the traces whose trace IDs are listed. Defaults to all traces.";
	private static final String FILTER_TRACES_TOOLTIP = "Don't consider the traces whose trace IDs are listed. Defaults to no traces.";

	private final JLabel infoLabel = new JLabel("<html>In this step you manage additional filters and selections for the trace analysis.</html>");
	private final JPanel expandingPanel = new JPanel();
	private final JCheckBox selectOnlyTraces = new JCheckBox("Select Only Traces with Following IDs:");
	private final JTextField selectOnlyTracesInput = new JTextField("1 2 3 4 42");
	private final JCheckBox filterTraces = new JCheckBox("Filter Traces with Following IDs Out:");
	private final JTextField filterTracesInput = new JTextField("1 2 3 4 42");
	private final JCheckBox ignoreBefore = new JCheckBox("Ignore Executions Before the Following Date (yyyyMMdd-HHmmss or timestamp):");
	private final JCheckBox ignoreAfter = new JCheckBox("Ignore Executions After the Following Date (yyyyMMdd-HHmmss or timestamp):");
	private final JTextField ignoreBeforeInput = new JTextField(Integer.toString(0));
	private final JTextField ignoreAfterInput = new JTextField(Integer.toString(Integer.MAX_VALUE));

	public AdditionalFiltersStep() {
		this.addAndLayoutComponents();
		this.addLogicToComponents();
		this.addToolTipsToComponents();
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

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.filterTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.filterTracesInput, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.ignoreBefore, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.ignoreBeforeInput, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.ignoreAfter, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.ignoreAfterInput, gridBagConstraints);

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

		this.filterTraces.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				AdditionalFiltersStep.this.filterTracesInput.setEnabled(AdditionalFiltersStep.this.filterTraces.isSelected());
			}
		});

		this.filterTraces.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (AdditionalFiltersStep.this.filterTraces.isSelected()) {
					AdditionalFiltersStep.this.selectOnlyTraces.setSelected(false);
				}
			}
		});

		this.selectOnlyTraces.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (AdditionalFiltersStep.this.selectOnlyTraces.isSelected()) {
					AdditionalFiltersStep.this.filterTraces.setSelected(false);
				}
			}
		});

		this.ignoreAfter.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				AdditionalFiltersStep.this.ignoreAfterInput.setEnabled(AdditionalFiltersStep.this.ignoreAfter.isSelected());
			}
		});

		this.ignoreBefore.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				AdditionalFiltersStep.this.ignoreBeforeInput.setEnabled(AdditionalFiltersStep.this.ignoreBefore.isSelected());
			}
		});
	}

	private void addToolTipsToComponents() {
		this.selectOnlyTraces.setToolTipText(SELECT_ONLY_TRACES_TOOLTIP);
		this.filterTraces.setToolTipText(FILTER_TRACES_TOOLTIP);
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

		if (this.filterTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_FILTERTRACES);
			final String[] ids = this.filterTracesInput.getText().split(" ");
			for (final String id : ids) {
				parameters.add(id);
			}
		}

		if (this.ignoreBefore.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE);
			parameters.add(this.ignoreBeforeInput.getText());
		}

		if (this.ignoreAfter.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE);
			parameters.add(this.ignoreAfterInput.getText());
		}
	}

	@Override
	public void loadDefaultConfiguration() {
		this.selectOnlyTraces.setSelected(false);
		this.selectOnlyTracesInput.setText("1 2 3 4 42");

		this.filterTraces.setSelected(false);
		this.filterTracesInput.setText("1 2 3 4 42");

		this.ignoreAfter.setSelected(false);
		this.ignoreBefore.setSelected(false);

		this.selectOnlyTracesInput.setEnabled(false);
		this.filterTracesInput.setEnabled(false);

		this.ignoreAfterInput.setEnabled(false);
		this.ignoreBeforeInput.setEnabled(false);
	}

	@Override
	public void saveCurrentConfiguration(final Properties properties) {
		properties.setProperty(PROPERTY_KEY_SELECT_TRACES, Boolean.toString(this.selectOnlyTraces.isSelected()));
		properties.setProperty(PROPERTY_KEY_SELECT_TRACES_INPUT, this.selectOnlyTracesInput.getText());
		properties.setProperty(PROPERTY_KEY_FILTER_TRACES, Boolean.toString(this.filterTraces.isSelected()));
		properties.setProperty(PROPERTY_KEY_FILTER_TRACES_INPUT, this.filterTracesInput.getText());
		properties.setProperty(PROPERTY_KEY_IGNORE_BEFORE, Boolean.toString(this.ignoreBefore.isSelected()));
		properties.setProperty(PROPERTY_KEY_IGNORE_BEFORE_INPUT, this.ignoreBeforeInput.getText());
		properties.setProperty(PROPERTY_KEY_IGNORE_AFTER, Boolean.toString(this.ignoreAfter.isSelected()));
		properties.setProperty(PROPERTY_KEY_IGNORE_AFTER_INPUT, this.ignoreAfterInput.getText());

		this.selectOnlyTracesInput.setEnabled(this.selectOnlyTraces.isSelected());
		this.filterTracesInput.setEnabled(this.filterTraces.isSelected());
		this.ignoreBeforeInput.setEnabled(this.ignoreBefore.isSelected());
		this.ignoreAfterInput.setEnabled(this.ignoreAfter.isSelected());
	}

	@Override
	public void loadCurrentConfiguration(final Properties properties) {
		this.selectOnlyTraces.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_SELECT_TRACES)));
		this.selectOnlyTracesInput.setText(properties.getProperty(PROPERTY_KEY_SELECT_TRACES_INPUT));
		this.filterTraces.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_FILTER_TRACES)));
		this.filterTracesInput.setText(properties.getProperty(PROPERTY_KEY_FILTER_TRACES_INPUT));
		this.ignoreBefore.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_IGNORE_BEFORE)));
		this.ignoreBeforeInput.setText(properties.getProperty(PROPERTY_KEY_IGNORE_BEFORE_INPUT));
		this.ignoreAfter.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_IGNORE_AFTER)));
		this.ignoreAfterInput.setText(properties.getProperty(PROPERTY_KEY_IGNORE_AFTER_INPUT));
	}

	@Override
	public boolean isNextStepAllowed() {
		return true;
	}
}
