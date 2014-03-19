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

package kieker.tools.traceAnalysis;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class TraceAnalysisGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private final CardLayout mainPanelLayout = new CardLayout();
	private final JPanel mainPanel = new JPanel(this.mainPanelLayout);
	private final JButton previousButton = new JButton("Previous");
	private final JButton nextButton = new JButton("Next");

	private final AbstractStep[] steps = { new WelcomeStep(), new PlotStep(), new PrintStep(), new AdditionalOptionsStep(), new FinalStep() };
	private int currentStepIndex = 0;

	public TraceAnalysisGUI() {
		super("Trace Analysis Tool - GUI");

		this.addAndLayoutComponents();
		this.initializeComponents();
		this.addLogicToComponents();
		this.initializeWindow();
	}

	private void addAndLayoutComponents() {
		final GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.getContentPane().add(this.mainPanel, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		this.getContentPane().add(this.previousButton, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.SOUTHEAST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		this.getContentPane().add(this.nextButton, gridBagConstraints);
	}

	private void initializeComponents() {
		this.previousButton.setEnabled(false);
	}

	private void addLogicToComponents() {
		this.nextButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent arg0) {
				TraceAnalysisGUI.this.nextStep();
			}
		});

		this.previousButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				TraceAnalysisGUI.this.previousStep();
			}
		});
	}

	private void nextStep() {
		this.currentStepIndex++;
		this.mainPanelLayout.next(this.mainPanel);
		this.previousButton.setEnabled(true);
		this.nextButton.setEnabled(this.currentStepIndex < (this.steps.length - 1));
	}

	private void previousStep() {
		this.currentStepIndex--;
		this.mainPanelLayout.previous(this.mainPanel);
		this.nextButton.setEnabled(true);
		this.previousButton.setEnabled(this.currentStepIndex > 0);
	}

	private void initializeWindow() {
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		int maxHeight = 1;
		int maxWidth = 1;
		for (final JPanel panel : this.steps) {
			this.mainPanel.add(panel, panel.toString());

			maxHeight = Math.max(maxHeight, panel.getPreferredSize().height);
			maxWidth = Math.max(maxWidth, panel.getPreferredSize().width);
		}
		this.setSize(maxWidth, maxHeight);
		this.setLocationRelativeTo(null);
	}

	private void startTraceAnalysis() {
		final Collection<String> parameters = new ArrayList<String>();

		for (final AbstractStep step : this.steps) {
			step.deliverParameters(parameters);
		}

		this.previousButton.setEnabled(false);

		final Thread thread = new Thread() {
			@Override
			public void run() {
				TraceAnalysisTool.main(parameters.toArray(new String[parameters.size()]));
				TraceAnalysisGUI.this.previousButton.setEnabled(true);
			}
		};

		thread.start();
	}

	public static void main(final String[] args) {
		final TraceAnalysisGUI gui = new TraceAnalysisGUI();
		gui.setVisible(true);
	}

	private abstract class AbstractStep extends JPanel {

		private static final long serialVersionUID = 1L;

		public abstract void deliverParameters(Collection<String> parameters);

		public AbstractStep() {

		}

	}

	private class WelcomeStep extends AbstractStep {

		private static final long serialVersionUID = 1L;

		private final String currentPath = new File(".").getAbsolutePath();

		private final JLabel welcomeLabel = new JLabel("<html>Welcome to Kieker's Trace Analysis GUI.<br/>This wizard helps you to generate visual representatons " +
				"based on trace analysis of your records.<br/><br/>Please specify the input and output directories.</html>");
		private final JLabel inputDirectoryLabel = new JLabel("Input Directory: ");
		private final JLabel outputDirectoryLabel = new JLabel("Output Directory: ");
		private final JTextField inputDirectoryField = new JTextField(this.currentPath);
		private final JTextField outputDirectoryField = new JTextField(this.currentPath);
		private final JButton inputDirectoryChooseButton = new JButton("Choose");
		private final JButton outputDirectoryChooseButton = new JButton("Choose");
		private final JPanel expandingPanel = new JPanel();

		public WelcomeStep() {
			this.addAndLayoutComponents();
			this.addLogicToComponents();
		}

		private void addAndLayoutComponents() {
			this.setLayout(new GridBagLayout());

			final GridBagConstraints gridBagConstraints = new GridBagConstraints();

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			this.add(this.welcomeLabel, gridBagConstraints);

			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.weightx = 0.0;
			this.add(this.inputDirectoryLabel, gridBagConstraints);

			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			this.add(this.inputDirectoryField, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.weightx = 0.0;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.inputDirectoryChooseButton, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.outputDirectoryLabel, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			this.add(this.outputDirectoryField, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.outputDirectoryChooseButton, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.expandingPanel, gridBagConstraints);
		}

		private void addLogicToComponents() {
			this.inputDirectoryChooseButton.addActionListener(new ActionListener() {

				public void actionPerformed(final ActionEvent event) {
					final JFileChooser fileChooser = new JFileChooser(WelcomeStep.this.inputDirectoryField.getText());
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					if (fileChooser.showOpenDialog(WelcomeStep.this) == JFileChooser.APPROVE_OPTION) {
						WelcomeStep.this.inputDirectoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
					}
				}
			});

			this.outputDirectoryChooseButton.addActionListener(new ActionListener() {

				public void actionPerformed(final ActionEvent arg0) {
					final JFileChooser fileChooser = new JFileChooser(WelcomeStep.this.outputDirectoryField.getText());
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					if (fileChooser.showOpenDialog(WelcomeStep.this) == JFileChooser.APPROVE_OPTION) {
						WelcomeStep.this.outputDirectoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
					}
				}
			});
		}

		@Override
		public void deliverParameters(final Collection<String> parameters) {
			parameters.add("-i");
			parameters.add("\"" + this.inputDirectoryField.getText() + "\"");

			parameters.add("-o");
			parameters.add("\"" + this.outputDirectoryField.getText() + "\"");
		}

	}

	private class PlotStep extends AbstractStep {

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

		@Override
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

	private class PrintStep extends AbstractStep {

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

		@Override
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

	private class AdditionalOptionsStep extends AbstractStep {

		private static final long serialVersionUID = 1L;

		private final JLabel infoLabel = new JLabel("<html>In this step you manage additional options for the trace analysis.</html>");
		private final JPanel expandingPanel = new JPanel();
		private final JCheckBox ignoreInvalidTraces = new JCheckBox("Ignore Invalid Traces");
		private final JCheckBox useShortLabels = new JCheckBox("Use Short Labels");
		private final JCheckBox includeSelfLoops = new JCheckBox("Include Self Loops");

		public AdditionalOptionsStep() {
			this.addAndLayoutComponents();
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
			this.add(this.ignoreInvalidTraces, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(0, 5, 0, 0);
			this.add(this.useShortLabels, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(0, 5, 0, 0);
			this.add(this.includeSelfLoops, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.expandingPanel, gridBagConstraints);
		}

		@Override
		public void deliverParameters(final Collection<String> parameters) {
			if (this.ignoreInvalidTraces.isSelected()) {
				parameters.add("--ignore-invalid-traces");
			}

			if (this.useShortLabels.isSelected()) {
				parameters.add("--short-labels");
			}

			if (this.includeSelfLoops.isSelected()) {
				parameters.add("--include-self-loops");
			}
		}
	}

	private class FinalStep extends AbstractStep {
		private static final long serialVersionUID = 1L;

		private final JLabel infoLabel = new JLabel("<html>All necessary information have been gathered. You can now start the trace analysis.</html>");
		private final JButton startButton = new JButton("Start");
		private final JTextArea logArea = new JTextArea(20, 60);
		private final JScrollPane logScrollPane = new JScrollPane(this.logArea);

		public FinalStep() {
			this.initializeComponents();
			this.addAndLayoutComponents();
			this.addLogicToComponents();
		}

		private void initializeComponents() {
			this.logArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		}

		private void addAndLayoutComponents() {
			this.setLayout(new GridBagLayout());

			final GridBagConstraints gridBagConstraints = new GridBagConstraints();

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			this.add(this.infoLabel, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.fill = GridBagConstraints.NONE;
			this.add(this.startButton, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			this.add(this.logScrollPane, gridBagConstraints);
		}

		private void addLogicToComponents() {
			this.startButton.addActionListener(new ActionListener() {

				public void actionPerformed(final ActionEvent arg0) {
					TraceAnalysisGUI.this.startTraceAnalysis();
				}
			});
		}

		@Override
		public void deliverParameters(final Collection<String> parameters) {}
	}

}
