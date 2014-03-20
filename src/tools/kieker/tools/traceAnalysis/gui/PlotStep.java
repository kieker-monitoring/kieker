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
 * In this step of the trace analysis wizard, the user can choose the plots to draw.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class PlotStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final JLabel infoLabel = new JLabel("In this step you choose which plots the tool should generate.");

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

		final GridBagConstraints infoLabelConstraints = new GridBagConstraints();
		infoLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
		infoLabelConstraints.anchor = GridBagConstraints.NORTHWEST;
		infoLabelConstraints.insets.set(5, 5, 5, 5);
		infoLabelConstraints.weightx = 1.0;
		infoLabelConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.infoLabel, infoLabelConstraints);

		final GridBagConstraints deploymentSequenceDiagramsConstraints = new GridBagConstraints();
		deploymentSequenceDiagramsConstraints.gridwidth = GridBagConstraints.REMAINDER;
		deploymentSequenceDiagramsConstraints.insets.set(5, 5, 0, 0);
		deploymentSequenceDiagramsConstraints.weightx = 0.0;
		deploymentSequenceDiagramsConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.deploymentSequenceDiagrams, deploymentSequenceDiagramsConstraints);

		final GridBagConstraints assemblySequenceDiagramsConstraints = new GridBagConstraints();
		assemblySequenceDiagramsConstraints.gridwidth = GridBagConstraints.REMAINDER;
		assemblySequenceDiagramsConstraints.insets.set(0, 5, 0, 0);
		assemblySequenceDiagramsConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.assemblySequenceDiagrams, assemblySequenceDiagramsConstraints);

		final GridBagConstraints deploymentComponentDependencyGraphConstraints = new GridBagConstraints();
		deploymentComponentDependencyGraphConstraints.gridwidth = GridBagConstraints.RELATIVE;
		deploymentComponentDependencyGraphConstraints.insets.set(0, 5, 0, 0);
		deploymentComponentDependencyGraphConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.deploymentComponentDependencyGraph, deploymentComponentDependencyGraphConstraints);

		final GridBagConstraints deploymentComponentDependencyGraphResponseTimeConstraints = new GridBagConstraints();
		deploymentComponentDependencyGraphResponseTimeConstraints.gridwidth = GridBagConstraints.REMAINDER;
		deploymentComponentDependencyGraphResponseTimeConstraints.insets.set(0, 5, 0, 0);
		deploymentComponentDependencyGraphResponseTimeConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.deploymentComponentDependencyGraphResponseTime, deploymentComponentDependencyGraphResponseTimeConstraints);

		final GridBagConstraints assemblyComponentDependencyGraphConstraints = new GridBagConstraints();
		assemblyComponentDependencyGraphConstraints.gridwidth = GridBagConstraints.RELATIVE;
		assemblyComponentDependencyGraphConstraints.insets.set(0, 5, 0, 0);
		assemblyComponentDependencyGraphConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.assemblyComponentDependencyGraph, assemblyComponentDependencyGraphConstraints);

		final GridBagConstraints assemblyComponentDependencyGraphResponseTimeConstraints = new GridBagConstraints();
		assemblyComponentDependencyGraphResponseTimeConstraints.gridwidth = GridBagConstraints.REMAINDER;
		assemblyComponentDependencyGraphResponseTimeConstraints.insets.set(0, 5, 0, 0);
		assemblyComponentDependencyGraphResponseTimeConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.assemblyComponentDependencyGraphResponseTime, assemblyComponentDependencyGraphResponseTimeConstraints);

		final GridBagConstraints containerDependencyGraphConstraints = new GridBagConstraints();
		containerDependencyGraphConstraints.gridwidth = GridBagConstraints.REMAINDER;
		containerDependencyGraphConstraints.insets.set(0, 5, 0, 0);
		containerDependencyGraphConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.containerDependencyGraph, containerDependencyGraphConstraints);

		final GridBagConstraints deploymentOperationDependencyGraphConstraints = new GridBagConstraints();
		deploymentOperationDependencyGraphConstraints.gridwidth = GridBagConstraints.RELATIVE;
		deploymentOperationDependencyGraphConstraints.insets.set(0, 5, 0, 0);
		deploymentOperationDependencyGraphConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.deploymentOperationDependencyGraph, deploymentOperationDependencyGraphConstraints);

		final GridBagConstraints deploymentOperationDependencyGraphResponseTimeConstraints = new GridBagConstraints();
		deploymentOperationDependencyGraphResponseTimeConstraints.gridwidth = GridBagConstraints.REMAINDER;
		deploymentOperationDependencyGraphResponseTimeConstraints.insets.set(0, 5, 0, 0);
		deploymentOperationDependencyGraphResponseTimeConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.deploymentOperationDependencyGraphResponseTime, deploymentOperationDependencyGraphResponseTimeConstraints);

		final GridBagConstraints assemblyOperationDependencyGraphConstraints = new GridBagConstraints();
		assemblyOperationDependencyGraphConstraints.gridwidth = GridBagConstraints.RELATIVE;
		assemblyOperationDependencyGraphConstraints.insets.set(0, 5, 0, 0);
		assemblyOperationDependencyGraphConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.assemblyOperationDependencyGraph, assemblyOperationDependencyGraphConstraints);

		final GridBagConstraints assemblyOperationDependencyGraphResponseTimeConstraints = new GridBagConstraints();
		assemblyOperationDependencyGraphResponseTimeConstraints.gridwidth = GridBagConstraints.REMAINDER;
		assemblyOperationDependencyGraphResponseTimeConstraints.insets.set(0, 5, 0, 0);
		assemblyOperationDependencyGraphResponseTimeConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.assemblyOperationDependencyGraphResponseTime, assemblyOperationDependencyGraphResponseTimeConstraints);

		final GridBagConstraints aggregatedDeploymentCallTreeConstraints = new GridBagConstraints();
		aggregatedDeploymentCallTreeConstraints.gridwidth = GridBagConstraints.REMAINDER;
		aggregatedDeploymentCallTreeConstraints.insets.set(0, 5, 0, 0);
		aggregatedDeploymentCallTreeConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.aggregatedDeploymentCallTree, aggregatedDeploymentCallTreeConstraints);

		final GridBagConstraints aggregatedAssemblyCallTreeConstraints = new GridBagConstraints();
		aggregatedAssemblyCallTreeConstraints.gridwidth = GridBagConstraints.REMAINDER;
		aggregatedAssemblyCallTreeConstraints.insets.set(0, 5, 0, 0);
		aggregatedAssemblyCallTreeConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.aggregatedAssemblyCallTree, aggregatedAssemblyCallTreeConstraints);

		final GridBagConstraints callTreesConstraints = new GridBagConstraints();
		callTreesConstraints.gridwidth = GridBagConstraints.REMAINDER;
		callTreesConstraints.insets.set(0, 5, 0, 0);
		callTreesConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.callTrees, callTreesConstraints);

		final GridBagConstraints allGraphsConstraints = new GridBagConstraints();
		allGraphsConstraints.gridwidth = GridBagConstraints.REMAINDER;
		allGraphsConstraints.insets.set(10, 5, 5, 5);
		allGraphsConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.allGraphs, allGraphsConstraints);

		final GridBagConstraints expandingPanelConstraints = new GridBagConstraints();
		expandingPanelConstraints.gridwidth = GridBagConstraints.REMAINDER;
		expandingPanelConstraints.weighty = 1.0;
		expandingPanelConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.expandingPanel, expandingPanelConstraints);
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

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		// TODO Use the constants instead of hard coded strings
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
