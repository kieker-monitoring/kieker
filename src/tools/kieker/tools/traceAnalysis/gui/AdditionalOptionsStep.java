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
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import kieker.tools.traceAnalysis.Constants;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class AdditionalOptionsStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final JLabel infoLabel = new JLabel("<html>In this step you manage additional options for the trace analysis.</html>");
	private final JPanel expandingPanel = new JPanel();
	private final JCheckBox verbose = new JCheckBox("Verbosely list used parameters and processed traces");
	private final JCheckBox ignoreInvalidTraces = new JCheckBox("Ignore Invalid Traces");
	private final JCheckBox ignoreAssumedCalls = new JCheckBox("Draw Assumed Calls As Usual Calls");
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

		this.maxTraceDurationMSInput.setValue(600000L);
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
			parameters.add("--" + Constants.CMD_OPT_NAME_VERBOSE);
		}

		if (this.ignoreInvalidTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES);
		}

		if (this.useShortLabels.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_SHORTLABELS);
		}

		if (this.includeSelfLoops.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_INCLUDESELFLOOPS);
		}

		if (this.maxTraceDurationMS.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_MAXTRACEDURATION);
			parameters.add(((Long) this.maxTraceDurationMSInput.getValue()).toString());
		}

		if (this.ignoreAssumedCalls.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_IGNORE_ASSUMED);
		}
	}

	@Override
	public void saveCurrentConfiguration(final Writer writer) throws IOException {
		writer.write(Boolean.toString(this.verbose.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.ignoreInvalidTraces.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.useShortLabels.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.includeSelfLoops.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.maxTraceDurationMS.isSelected()));
		writer.write("\n");

		writer.write(Long.toString((Long) this.maxTraceDurationMSInput.getValue()));
		writer.write("\n");

		writer.write(Boolean.toString(this.ignoreAssumedCalls.isSelected()));
		writer.write("\n");
	}

	@Override
	public void loadCurrentConfiguration(final Scanner scanner) throws IOException {
		try {
			this.verbose.setSelected(scanner.nextBoolean());
			this.ignoreInvalidTraces.setSelected(scanner.nextBoolean());
			this.useShortLabels.setSelected(scanner.nextBoolean());
			this.includeSelfLoops.setSelected(scanner.nextBoolean());
			this.maxTraceDurationMS.setSelected(scanner.nextBoolean());
			this.maxTraceDurationMSInput.setValue(scanner.nextLong());
			this.ignoreAssumedCalls.setSelected(scanner.nextBoolean());
		} catch (final NoSuchElementException ex) {
			this.setDefaultValues();
			throw new IOException(ex);
		}
	}

	private void setDefaultValues() {
		this.verbose.setSelected(false);
		this.ignoreInvalidTraces.setSelected(false);
		this.useShortLabels.setSelected(false);
		this.includeSelfLoops.setSelected(false);
		this.maxTraceDurationMS.setSelected(false);
		this.ignoreAssumedCalls.setSelected(true);
		this.maxTraceDurationMSInput.setValue(600000);
	}
}
