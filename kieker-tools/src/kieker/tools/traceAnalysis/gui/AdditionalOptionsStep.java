/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import kieker.tools.AbstractCommandLineTool;
import kieker.tools.traceAnalysis.Constants;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class AdditionalOptionsStep extends AbstractStep { // NOPMD (long class)

	private static final long serialVersionUID = 1L;

	private static final String PROPERTY_KEY_IDENTIFIER = AdditionalOptionsStep.class.getSimpleName();
	private static final String PROPERTY_KEY_VERBOSE = PROPERTY_KEY_IDENTIFIER + ".verbose";
	private static final String PROPERTY_KEY_IGNORE_INVALID_TRACES = PROPERTY_KEY_IDENTIFIER + ".ignoreInvalidTraces";
	private static final String PROPERTY_KEY_IGNORE_ASSUMED_CALLS = PROPERTY_KEY_IDENTIFIER + ".ignoreAssumedCalls";
	private static final String PROPERTY_KEY_REPAIR_EVENT_BASED_TRACES = PROPERTY_KEY_IDENTIFIER + ".repairEventBasedTraces";
	private static final String PROPERTY_KEY_USE_SHORT_LABELS = PROPERTY_KEY_IDENTIFIER + ".useShortLabels";
	private static final String PROPERTY_KEY_INCLUDE_SELF_LOOPS = PROPERTY_KEY_IDENTIFIER + ".includeSelfLoops";
	private static final String PROPERTY_KEY_MAX_TRACE_DURATION = PROPERTY_KEY_IDENTIFIER + ".maxTraceDurationMS";
	private static final String PROPERTY_KEY_MAX_TRACE_DURATION_INPUT = PROPERTY_KEY_IDENTIFIER + ".maxTraceDurationMSInput";
	private static final String PROPERTY_KEY_TRACE_COLORING_MAP = PROPERTY_KEY_IDENTIFIER + ".traceColoringMap";
	private static final String PROPERTY_KEY_TRACE_COLORING_MAP_INPUT = PROPERTY_KEY_IDENTIFIER + ".traceColoringMapInput";
	private static final String PROPERTY_KEY_DESCRIPTION = PROPERTY_KEY_IDENTIFIER + ".description";
	private static final String PROPERTY_KEY_DESCRIPTION_INPUT = PROPERTY_KEY_IDENTIFIER + ".descriptionInput";

	private static final String IGNORE_INVALID_TRACES_TOOLTIP = "The execution ignores the occurence of invalid traces so they will not effect the execution.";
	private static final String IGNORE_ASSUMED_CALLS_TOOLTIP = "Assumed calls are visualized just as regular calls.";
	private static final String USE_SHORT_LABELS_TOOLTIP = "Abbreviated labels (e.g. package names) are used in visualizations.";
	private static final String INCLUDE_SELF_LOOPS_TOOLTIP = "Self-loops are included in vizualisations.";
	private static final String MAX_TRACE_DURATION_MS = "Threshold (in ms) after which incomplete traces become invalid. Defaults to 600.000 (i.e, 10 minutes)";
	private static final String TRACE_COLORING_MAP_TOOLTIP = "<html>Color traces according to the given color map (properties file).<br>"
			+ "(key: trace ID, value: color in hex format, e.g., 0xff0000 for red; use trace ID 'default' to specify the default color.)</html> ";
	private static final String DESCRIPTION_TOOLTIP = "<html>Adds descriptions to elements according to the given file (properties file).<br>"
			+ "(key:component ID, e.g., @1; value: description)</html>";
	private static final String REPAIR_EVENT_BASED_TRACES_TOOLTIP = "If selected, BeforeEvents with missing AfterEvents e.g. "
			+ "because of software crash will be repaired.";

	private final JLabel infoLabel = new JLabel("<html>In this step you manage additional options for the trace analysis.</html>");
	private final JPanel expandingPanel = new JPanel();
	private final JCheckBox verbose = new JCheckBox("Verbosely list used parameters and processed traces");
	private final JCheckBox ignoreInvalidTraces = new JCheckBox("Ignore Invalid Traces");
	private final JCheckBox ignoreAssumedCalls = new JCheckBox("Draw Assumed Calls As Usual Calls");
	private final JCheckBox repairEventBasedTraces = new JCheckBox("Repair Event Based Traces");
	private final JCheckBox useShortLabels = new JCheckBox("Use Short Labels");
	private final JCheckBox includeSelfLoops = new JCheckBox("Include Self Loops");
	private final JCheckBox maxTraceDurationMS = new JCheckBox("Maximal Duration of Traces in Milliseconds:");
	private final JSpinner maxTraceDurationMSInput = new JSpinner();
	private final JCheckBox traceColoringMap = new JCheckBox("Use Trace Coloring Map File:");
	private final JTextField traceColoringMapInput = new JTextField(".");
	private final JButton traceColoringMapChooseButton = new JButton("Choose");
	private final JCheckBox description = new JCheckBox("Use Description File:");
	private final JTextField descriptionInput = new JTextField(".");
	private final JButton descriptionChooseButton = new JButton("Choose");

	public AdditionalOptionsStep() {
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

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(5, 5, 0, 0);
		this.add(this.verbose, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.ignoreAssumedCalls, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.ignoreInvalidTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.repairEventBasedTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.useShortLabels, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.includeSelfLoops, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.maxTraceDurationMS, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.maxTraceDurationMSInput, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.traceColoringMap, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.traceColoringMapInput, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.traceColoringMapChooseButton, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.description, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.descriptionInput, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.descriptionChooseButton, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.expandingPanel, gridBagConstraints);
	}

	@Override
	@SuppressWarnings("synthetic-access")
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		if (this.verbose.isSelected()) {
			parameters.add("--" + AbstractCommandLineTool.CMD_OPT_NAME_VERBOSE_LONG);
		}

		if (this.ignoreInvalidTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES);
		}

		if (this.repairEventBasedTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_REPAIR_EVENT_BASED_TRACES);
		}

		if (this.useShortLabels.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_SHORTLABELS);
		}

		if (this.includeSelfLoops.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_INCLUDESELFLOOPS);
		}

		if (this.maxTraceDurationMS.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_MAXTRACEDURATION);
			parameters.add(this.maxTraceDurationMSInput.getValue().toString());
		}

		if (this.ignoreAssumedCalls.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_IGNORE_ASSUMED);
		}

		if (this.description.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_ADD_DESCRIPTIONS);
			parameters.add(this.descriptionInput.getText());
		}

		if (this.traceColoringMap.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TRACE_COLORING);
			parameters.add(this.traceColoringMapInput.getText());
		}
	}

	private void addLogicToComponents() {
		this.descriptionChooseButton.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser fileChooser = new JFileChooser(AdditionalOptionsStep.this.descriptionInput.getText());
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				if (fileChooser.showOpenDialog(AdditionalOptionsStep.this) == JFileChooser.APPROVE_OPTION) {
					AdditionalOptionsStep.this.descriptionInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		this.traceColoringMapChooseButton.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser fileChooser = new JFileChooser(AdditionalOptionsStep.this.traceColoringMapInput.getText());
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				if (fileChooser.showOpenDialog(AdditionalOptionsStep.this) == JFileChooser.APPROVE_OPTION) {
					AdditionalOptionsStep.this.traceColoringMapInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		this.description.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent e) {
				AdditionalOptionsStep.this.descriptionInput.setEnabled(AdditionalOptionsStep.this.description.isSelected());
				AdditionalOptionsStep.this.descriptionChooseButton.setEnabled(AdditionalOptionsStep.this.description.isSelected());
			}
		});

		this.traceColoringMap.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent e) {
				AdditionalOptionsStep.this.traceColoringMapInput.setEnabled(AdditionalOptionsStep.this.traceColoringMap.isSelected());
				AdditionalOptionsStep.this.traceColoringMapChooseButton.setEnabled(AdditionalOptionsStep.this.traceColoringMap.isSelected());
			}
		});

		this.maxTraceDurationMS.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent e) {
				AdditionalOptionsStep.this.maxTraceDurationMSInput.setEnabled(AdditionalOptionsStep.this.maxTraceDurationMS.isSelected());
			}
		});
	}

	private void addToolTipsToComponents() {
		this.ignoreAssumedCalls.setToolTipText(IGNORE_ASSUMED_CALLS_TOOLTIP);
		this.ignoreInvalidTraces.setToolTipText(IGNORE_INVALID_TRACES_TOOLTIP);
		this.useShortLabels.setToolTipText(USE_SHORT_LABELS_TOOLTIP);
		this.includeSelfLoops.setToolTipText(INCLUDE_SELF_LOOPS_TOOLTIP);
		this.maxTraceDurationMS.setToolTipText(MAX_TRACE_DURATION_MS);
		this.traceColoringMap.setToolTipText(TRACE_COLORING_MAP_TOOLTIP);
		this.description.setToolTipText(DESCRIPTION_TOOLTIP);
		this.repairEventBasedTraces.setToolTipText(REPAIR_EVENT_BASED_TRACES_TOOLTIP);
	}

	@Override
	public void loadDefaultConfiguration() {
		this.maxTraceDurationMSInput.setValue(600000);
		this.traceColoringMapInput.setEnabled(false);
		this.descriptionInput.setEnabled(false);
		this.descriptionChooseButton.setEnabled(false);
		this.traceColoringMapInput.setEnabled(false);
		this.traceColoringMapChooseButton.setEnabled(false);
		this.maxTraceDurationMSInput.setEnabled(false);
	}

	@Override
	public void saveCurrentConfiguration(final Properties properties) {
		properties.setProperty(PROPERTY_KEY_VERBOSE, Boolean.toString(this.verbose.isSelected()));
		properties.setProperty(PROPERTY_KEY_IGNORE_INVALID_TRACES, Boolean.toString(this.ignoreInvalidTraces.isSelected()));
		properties.setProperty(PROPERTY_KEY_REPAIR_EVENT_BASED_TRACES, Boolean.toString(this.repairEventBasedTraces.isSelected()));
		properties.setProperty(PROPERTY_KEY_USE_SHORT_LABELS, Boolean.toString(this.useShortLabels.isSelected()));
		properties.setProperty(PROPERTY_KEY_INCLUDE_SELF_LOOPS, Boolean.toString(this.includeSelfLoops.isSelected()));
		properties.setProperty(PROPERTY_KEY_MAX_TRACE_DURATION, Boolean.toString(this.maxTraceDurationMS.isSelected()));
		properties.setProperty(PROPERTY_KEY_MAX_TRACE_DURATION_INPUT, Integer.toString((Integer) this.maxTraceDurationMSInput.getValue()));
		properties.setProperty(PROPERTY_KEY_IGNORE_ASSUMED_CALLS, Boolean.toString(this.ignoreAssumedCalls.isSelected()));
		properties.setProperty(PROPERTY_KEY_TRACE_COLORING_MAP, Boolean.toString(this.traceColoringMap.isSelected()));
		properties.setProperty(PROPERTY_KEY_TRACE_COLORING_MAP_INPUT, this.traceColoringMapInput.getText());
		properties.setProperty(PROPERTY_KEY_DESCRIPTION, Boolean.toString(this.description.isSelected()));
		properties.setProperty(PROPERTY_KEY_DESCRIPTION_INPUT, this.descriptionInput.getText());
	}

	@Override
	public void loadCurrentConfiguration(final Properties properties) {
		this.verbose.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_VERBOSE)));
		this.ignoreInvalidTraces.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_IGNORE_INVALID_TRACES)));
		this.repairEventBasedTraces.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_REPAIR_EVENT_BASED_TRACES)));
		this.useShortLabels.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_USE_SHORT_LABELS)));
		this.includeSelfLoops.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_INCLUDE_SELF_LOOPS)));
		this.maxTraceDurationMS.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_MAX_TRACE_DURATION)));
		this.ignoreAssumedCalls.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_IGNORE_ASSUMED_CALLS)));
		this.traceColoringMap.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_TRACE_COLORING_MAP)));
		this.description.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_DESCRIPTION)));
		this.descriptionInput.setText(properties.getProperty(PROPERTY_KEY_DESCRIPTION_INPUT));
		this.traceColoringMapInput.setText(properties.getProperty(PROPERTY_KEY_TRACE_COLORING_MAP_INPUT));
		this.maxTraceDurationMSInput.setValue(Integer.parseInt(properties.getProperty(PROPERTY_KEY_MAX_TRACE_DURATION_INPUT)));

		this.descriptionInput.setEnabled(this.description.isSelected());
		this.descriptionChooseButton.setEnabled(this.description.isSelected());
		this.traceColoringMapInput.setEnabled(this.traceColoringMap.isSelected());
		this.traceColoringMapChooseButton.setEnabled(this.traceColoringMap.isSelected());
		this.maxTraceDurationMSInput.setEnabled(this.maxTraceDurationMS.isSelected());
	}

	@Override
	public boolean isNextStepAllowed() {
		return true;
	}
}
