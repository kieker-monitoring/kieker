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

package kicker.tools.traceAnalysis.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kicker.tools.traceAnalysis.Constants;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class PrintStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final JLabel infoLabel = new JLabel("<html>In this step you choose which prints the tool should generate.</html>");

	private final JCheckBox messageTraces = new JCheckBox("Message Traces");
	private final JCheckBox executionTraces = new JCheckBox("Execution Traces");
	private final JCheckBox invalidExecutionTraces = new JCheckBox("Invalid Execution Traces");
	private final JCheckBox systemModel = new JCheckBox("System Model");
	private final JCheckBox deploymentEquivalenceClasses = new JCheckBox("Deployment Equivalence Classes");
	private final JCheckBox assemblyEquivalenceClasses = new JCheckBox("Assembly Equivalence Classes");
	private final JCheckBox allPrints = new JCheckBox("Select All");
	private final JPanel expandingPanel = new JPanel();

	public PrintStep() {
		this.addAndLayoutComponents();
		this.addLogicToComponents();
		this.setDefaultSelection();
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
		this.add(this.messageTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.executionTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.invalidExecutionTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.systemModel, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.deploymentEquivalenceClasses, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.assemblyEquivalenceClasses, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(10, 5, 5, 5);
		this.add(this.allPrints, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.expandingPanel, gridBagConstraints);
	}

	private void addLogicToComponents() {
		this.allPrints.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent event) {
				PrintStep.this.messageTraces.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.executionTraces.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.invalidExecutionTraces.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.systemModel.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.deploymentEquivalenceClasses.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.assemblyEquivalenceClasses.setSelected(PrintStep.this.allPrints.isSelected());
			}
		});
	}

	private void setDefaultSelection() {
		this.systemModel.setSelected(true);
	}

	@Override
	public boolean isNextStepAllowed() {
		final boolean nothingSelected = !(this.messageTraces.isSelected() || this.executionTraces.isSelected() || this.invalidExecutionTraces.isSelected()
				|| this.systemModel.isSelected() || this.deploymentEquivalenceClasses.isSelected() || this.assemblyEquivalenceClasses.isSelected());
		if (nothingSelected) {
			final int result = JOptionPane.showConfirmDialog(this, "No prints have been selected. Continue?", "No Prints Selected", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			return (JOptionPane.YES_OPTION == result);
		}
		return true;
	}

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		if (this.messageTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES);
		}

		if (this.executionTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES);
		}

		if (this.invalidExecutionTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES);
		}

		if (this.systemModel.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PRINTSYSTEMMODEL);
		}

		if (this.deploymentEquivalenceClasses.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT);
		}
		if (this.assemblyEquivalenceClasses.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT);
		}
	}
}
