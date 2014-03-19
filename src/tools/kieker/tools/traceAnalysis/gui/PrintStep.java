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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

			public void itemStateChanged(final ItemEvent event) {
				PrintStep.this.messageTraces.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.executionTraces.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.invalidExecutionTraces.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.systemModel.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.deploymentEquivalenceClasses.setSelected(PrintStep.this.allPrints.isSelected());
				PrintStep.this.assemblyEquivalenceClasses.setSelected(PrintStep.this.allPrints.isSelected());
			}
		});
	};

	public void deliverParameters(final Collection<String> parameters) {
		if (this.messageTraces.isSelected()) {
			parameters.add("--print-Message-Traces");
		}

		if (this.executionTraces.isSelected()) {
			parameters.add("--print-Execution-Traces");
		}

		if (this.invalidExecutionTraces.isSelected()) {
			parameters.add("--print-invalid-Execution-Traces");
		}

		if (this.systemModel.isSelected()) {
			parameters.add("--print-System-Model");
		}

		if (this.deploymentEquivalenceClasses.isSelected()) {
			parameters.add("--print-Deployment-Equivalence-Classes");
		}
		if (this.assemblyEquivalenceClasses.isSelected()) {
			parameters.add("--print-Assembly-Equivalence-Classes");
		}
	}
}
