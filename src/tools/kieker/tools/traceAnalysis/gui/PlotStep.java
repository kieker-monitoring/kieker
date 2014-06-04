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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kieker.tools.traceAnalysis.Constants;

/**
 * In this step of the trace analysis wizard, the user can choose the plots to draw.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class PlotStep extends AbstractStep { // NOPMD (number of fields)

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
		this.setDefaultSelection();
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

			@Override
			@SuppressWarnings("synthetic-access")
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

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (!PlotStep.this.deploymentComponentDependencyGraph.isSelected()) {
					PlotStep.this.deploymentComponentDependencyGraphResponseTime.setSelected(false);
				}
			}
		});

		this.deploymentComponentDependencyGraphResponseTime.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (PlotStep.this.deploymentComponentDependencyGraphResponseTime.isSelected()) {
					PlotStep.this.deploymentComponentDependencyGraph.setSelected(true);
				}
			}
		});

		this.assemblyComponentDependencyGraph.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (!PlotStep.this.assemblyComponentDependencyGraph.isSelected()) {
					PlotStep.this.assemblyComponentDependencyGraphResponseTime.setSelected(false);
				}
			}
		});

		this.assemblyComponentDependencyGraphResponseTime.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (PlotStep.this.assemblyComponentDependencyGraphResponseTime.isSelected()) {
					PlotStep.this.assemblyComponentDependencyGraph.setSelected(true);
				}
			}
		});

		this.deploymentOperationDependencyGraph.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (!PlotStep.this.deploymentOperationDependencyGraph.isSelected()) {
					PlotStep.this.deploymentOperationDependencyGraphResponseTime.setSelected(false);
				}
			}
		});

		this.deploymentOperationDependencyGraphResponseTime.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (PlotStep.this.deploymentOperationDependencyGraphResponseTime.isSelected()) {
					PlotStep.this.deploymentOperationDependencyGraph.setSelected(true);
				}
			}
		});

		this.assemblyOperationDependencyGraph.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (!PlotStep.this.assemblyOperationDependencyGraph.isSelected()) {
					PlotStep.this.assemblyOperationDependencyGraphResponseTime.setSelected(false);
				}
			}
		});

		this.assemblyOperationDependencyGraphResponseTime.addItemListener(new ItemListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void itemStateChanged(final ItemEvent e) {
				if (PlotStep.this.assemblyOperationDependencyGraphResponseTime.isSelected()) {
					PlotStep.this.assemblyOperationDependencyGraph.setSelected(true);
				}
			}
		});
	}

	private void setDefaultSelection() {
		this.assemblyComponentDependencyGraph.setSelected(true);
		this.assemblyComponentDependencyGraphResponseTime.setSelected(true);

		this.assemblyOperationDependencyGraph.setSelected(true);
		this.assemblyOperationDependencyGraphResponseTime.setSelected(true);

		this.deploymentComponentDependencyGraph.setSelected(true);
		this.deploymentComponentDependencyGraphResponseTime.setSelected(true);

		this.deploymentOperationDependencyGraph.setSelected(true);
		this.deploymentOperationDependencyGraphResponseTime.setSelected(true);
	}

	@Override
	public boolean isNextStepAllowed() {
		final boolean nothingSelected = !(this.deploymentSequenceDiagrams.isSelected() || this.assemblySequenceDiagrams.isSelected()
				|| this.deploymentComponentDependencyGraph.isSelected() || this.assemblyComponentDependencyGraph.isSelected()
				|| this.containerDependencyGraph.isSelected() || this.deploymentOperationDependencyGraph.isSelected()
				|| this.assemblyOperationDependencyGraph.isSelected() || this.aggregatedDeploymentCallTree.isSelected()
				|| this.aggregatedAssemblyCallTree.isSelected() || this.callTrees.isSelected());
		if (nothingSelected) {
			final int result = JOptionPane.showConfirmDialog(this, "No plots have been selected. Continue?", "No Plots Selected", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			return (JOptionPane.YES_OPTION == result);
		}
		return true;
	}

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		if (this.deploymentSequenceDiagrams.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS);
		}

		if (this.assemblySequenceDiagrams.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS);
		}

		if (this.deploymentComponentDependencyGraph.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG);
			if (this.deploymentComponentDependencyGraphResponseTime.isSelected()) {
				parameters.add(Constants.RESPONSE_TIME_DECORATOR_FLAG);
			}
		}

		if (this.assemblyComponentDependencyGraph.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG);
			if (this.assemblyComponentDependencyGraphResponseTime.isSelected()) {
				parameters.add(Constants.RESPONSE_TIME_DECORATOR_FLAG);
			}
		}

		if (this.containerDependencyGraph.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG);
		}

		if (this.deploymentOperationDependencyGraph.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG);
			if (this.deploymentOperationDependencyGraphResponseTime.isSelected()) {
				parameters.add(Constants.RESPONSE_TIME_DECORATOR_FLAG);
			}
		}

		if (this.assemblyOperationDependencyGraph.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG);
			if (this.assemblyOperationDependencyGraphResponseTime.isSelected()) {
				parameters.add(Constants.RESPONSE_TIME_DECORATOR_FLAG);
			}
		}
		if (this.aggregatedDeploymentCallTree.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE);
		}

		if (this.aggregatedAssemblyCallTree.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE);
		}

		if (this.callTrees.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES);
		}

	}

	@Override
	public void saveCurrentConfiguration(final FileWriter writer) throws IOException {
		writer.write(Boolean.toString(this.deploymentSequenceDiagrams.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.assemblySequenceDiagrams.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.deploymentComponentDependencyGraph.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.deploymentComponentDependencyGraphResponseTime.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.assemblyComponentDependencyGraph.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.assemblyComponentDependencyGraphResponseTime.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.containerDependencyGraph.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.deploymentOperationDependencyGraph.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.deploymentOperationDependencyGraphResponseTime.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.assemblyOperationDependencyGraph.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.assemblyOperationDependencyGraphResponseTime.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.aggregatedDeploymentCallTree.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.aggregatedAssemblyCallTree.isSelected()));
		writer.write("\n");

		writer.write(Boolean.toString(this.callTrees.isSelected()));
		writer.write("\n");
	}

	@Override
	public void loadCurrentConfiguration(final Scanner scanner) throws IOException {
		try {
			this.deploymentSequenceDiagrams.setSelected(scanner.nextBoolean());
			this.assemblySequenceDiagrams.setSelected(scanner.nextBoolean());
			this.deploymentComponentDependencyGraph.setSelected(scanner.nextBoolean());
			this.deploymentComponentDependencyGraphResponseTime.setSelected(scanner.nextBoolean());
			this.assemblyComponentDependencyGraph.setSelected(scanner.nextBoolean());
			this.assemblyComponentDependencyGraphResponseTime.setSelected(scanner.nextBoolean());
			this.containerDependencyGraph.setSelected(scanner.nextBoolean());
			this.deploymentOperationDependencyGraph.setSelected(scanner.nextBoolean());
			this.deploymentOperationDependencyGraphResponseTime.setSelected(scanner.nextBoolean());
			this.assemblyOperationDependencyGraph.setSelected(scanner.nextBoolean());
			this.assemblyOperationDependencyGraphResponseTime.setSelected(scanner.nextBoolean());
			this.aggregatedDeploymentCallTree.setSelected(scanner.nextBoolean());
			this.aggregatedAssemblyCallTree.setSelected(scanner.nextBoolean());
			this.callTrees.setSelected(scanner.nextBoolean());
		} catch (final NoSuchElementException ex) {
			this.setDefaultSelection();
			throw new IOException(ex);
		}
	}

}
