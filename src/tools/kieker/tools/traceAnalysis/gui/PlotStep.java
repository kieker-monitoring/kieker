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
public class PlotStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final JLabel infoLabel = new JLabel("<html>In this step you choose which plots the tool should generate.</html>");

	private final JCheckBox deploymentSequenceDiagrams = new JCheckBox("Deployment Sequence Diagrams");
	private final JCheckBox assemblySequenceDiagrams = new JCheckBox("Assembly Sequence Diagrams");
	private final JCheckBox deploymentComponentDependencyGraph = new JCheckBox("Deployment Component Dependency Graph");
	private final JCheckBox deploymentComponentDependencyGraphResponseTime = new JCheckBox("Response Times");
	private final JCheckBox assemblyComponentDependencyGraph = new JCheckBox("Assembly Component Dependency Graph");
	private final JCheckBox assemblyComponentDependencyGraphResponseTime = new JCheckBox("Response Times");
	private final JCheckBox containerDependencyGraph = new JCheckBox("Plot Container Dependency Graph");
	private final JCheckBox deploymentOperationDependencyGraph = new JCheckBox("Deployment Operation Dependency Graph");
	private final JCheckBox deploymentOperationDependencyGraphResponseTime = new JCheckBox("Response Times");
	private final JCheckBox assemblyOperationDependencyGraph = new JCheckBox("Assembly Operation Dependency Graph");
	private final JCheckBox assemblyOperationDependencyGraphResponseTime = new JCheckBox("Response Times");
	private final JCheckBox aggregatedDeploymentCallTree = new JCheckBox("Aggregated Deployment Call Tree");
	private final JCheckBox aggregatedAssemblyCallTree = new JCheckBox("Aggregated Assembly Call Tree");
	private final JCheckBox callTrees = new JCheckBox("Call Trees");
	private final JCheckBox allGraphs = new JCheckBox("Select All");
	private final JPanel expandingPanel = new JPanel();

	public PlotStep() {
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
		gridBagConstraints.weightx = 0.0;
		this.add(this.deploymentSequenceDiagrams, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.assemblySequenceDiagrams, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.deploymentComponentDependencyGraph, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.deploymentComponentDependencyGraphResponseTime, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.assemblyComponentDependencyGraph, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.assemblyComponentDependencyGraphResponseTime, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.containerDependencyGraph, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.deploymentOperationDependencyGraph, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.deploymentOperationDependencyGraphResponseTime, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.assemblyOperationDependencyGraph, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.assemblyOperationDependencyGraphResponseTime, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.aggregatedDeploymentCallTree, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.aggregatedAssemblyCallTree, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.callTrees, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(10, 5, 5, 5);
		this.add(this.allGraphs, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.expandingPanel, gridBagConstraints);
	}

	private void addLogicToComponents() {
		this.allGraphs.addItemListener(new ItemListener() {

			public void itemStateChanged(final ItemEvent event) {
				PlotStep.this.deploymentSequenceDiagrams.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.assemblySequenceDiagrams.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.deploymentComponentDependencyGraph.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.deploymentComponentDependencyGraphResponseTime.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.assemblyComponentDependencyGraph.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.assemblyComponentDependencyGraphResponseTime.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.containerDependencyGraph.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.deploymentOperationDependencyGraph.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.deploymentOperationDependencyGraphResponseTime.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.assemblyOperationDependencyGraph.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.assemblyOperationDependencyGraphResponseTime.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.aggregatedDeploymentCallTree.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.aggregatedAssemblyCallTree.setSelected(PlotStep.this.allGraphs.isSelected());
				PlotStep.this.callTrees.setSelected(PlotStep.this.allGraphs.isSelected());
			}
		});

		this.deploymentComponentDependencyGraph.addItemListener(new ItemListener() {

			public void itemStateChanged(final ItemEvent e) {
				if (!PlotStep.this.deploymentComponentDependencyGraph.isSelected()) {
					PlotStep.this.deploymentComponentDependencyGraphResponseTime.setSelected(false);
				}
			}
		});

		this.assemblyComponentDependencyGraph.addItemListener(new ItemListener() {

			public void itemStateChanged(final ItemEvent e) {
				if (!PlotStep.this.assemblyComponentDependencyGraph.isSelected()) {
					PlotStep.this.assemblyComponentDependencyGraphResponseTime.setSelected(false);
				}
			}
		});

		this.deploymentOperationDependencyGraph.addItemListener(new ItemListener() {

			public void itemStateChanged(final ItemEvent e) {
				if (!PlotStep.this.deploymentOperationDependencyGraph.isSelected()) {
					PlotStep.this.deploymentOperationDependencyGraphResponseTime.setSelected(false);
				}
			}
		});

		this.assemblyOperationDependencyGraph.addItemListener(new ItemListener() {

			public void itemStateChanged(final ItemEvent e) {
				if (!PlotStep.this.assemblyOperationDependencyGraph.isSelected()) {
					PlotStep.this.assemblyOperationDependencyGraphResponseTime.setSelected(false);
				}
			}
		});
	}

	public void deliverParameters(final Collection<String> parameters) {
		if (this.deploymentSequenceDiagrams.isSelected()) {
			parameters.add("--plot-Deployment-Sequence-Diagrams");
		}

		if (this.assemblySequenceDiagrams.isSelected()) {
			parameters.add("--plot-Assembly-Sequence-Diagrams");
		}

		if (this.deploymentComponentDependencyGraph.isSelected()) {
			parameters.add("--plot-Deployment-Component-Dependency-Graph");
			if (this.deploymentComponentDependencyGraphResponseTime.isSelected()) {
				parameters.add("responseTimes");
			}
		}

		if (this.assemblyComponentDependencyGraph.isSelected()) {
			parameters.add("--plot-Assembly-Component-Dependency-Graph");
			if (this.assemblyComponentDependencyGraphResponseTime.isSelected()) {
				parameters.add("responseTimes");
			}
		}

		if (this.containerDependencyGraph.isSelected()) {
			parameters.add("--plot-Container-Dependency-Graph");
		}

		if (this.deploymentOperationDependencyGraph.isSelected()) {
			parameters.add("--plot-Deployment-Operation-Dependency-Graph");
			if (this.deploymentOperationDependencyGraphResponseTime.isSelected()) {
				parameters.add("responseTimes");
			}
		}

		if (this.assemblyOperationDependencyGraph.isSelected()) {
			parameters.add("--plot-Assembly-Operation-Dependency-Graph");
			if (this.assemblyOperationDependencyGraphResponseTime.isSelected()) {
				parameters.add("responseTimes");
			}
		}
		if (this.aggregatedDeploymentCallTree.isSelected()) {
			parameters.add("--plot-Aggregated-Deployment-Call-Tree");
		}

		if (this.aggregatedAssemblyCallTree.isSelected()) {
			parameters.add("--plot-Aggregated-Assembly-Call-Tree");
		}

		if (this.callTrees.isSelected()) {
			parameters.add("--plot-Call-Trees");
		}

	}
}
