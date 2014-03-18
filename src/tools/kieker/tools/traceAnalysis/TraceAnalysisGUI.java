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
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

	private final JPanel[] steps = { new WelcomeStep(), new PlotStep(), new PrintStep(), new AdditionalOptionsStep(), new FinalStep() };
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

	public static void main(final String[] args) {
		final TraceAnalysisGUI gui = new TraceAnalysisGUI();
		gui.setVisible(true);
	}

	private class WelcomeStep extends JPanel {

		private static final long serialVersionUID = 1L;

		private final String currentPath = new File(".").getAbsolutePath();

		private final JLabel welcomeLabel = new JLabel("<html>Welcome to Kieker's Trace Analysis GUI.<br/>This wizard helps you to generate visual representatons " +
				"based on trace analysis of your records.<br/><br/>Please specify the input and output directories.</html>");
		private final JLabel inputDirectoryLabel = new JLabel("Input Directory: ");
		private final JLabel outputDirectoryLabel = new JLabel("Output Directory: ");
		private final JTextField inputDirectoryField = new JTextField(this.currentPath, 25);
		private final JTextField outputDirectoryField = new JTextField(this.currentPath, 25);
		private final JButton inputDirectoryChooseButton = new JButton("Choose");
		private final JButton outputDirectoryChooseButton = new JButton("Choose");
		private final JPanel expandingPanel = new JPanel();

		public WelcomeStep() {
			this.addAndLayoutComponents();
		}

		private void addAndLayoutComponents() {
			this.setLayout(new GridBagLayout());

			final GridBagConstraints gridBagConstraints = new GridBagConstraints();

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			this.add(this.welcomeLabel, gridBagConstraints);

			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridwidth = 1;
			this.add(this.inputDirectoryLabel, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.inputDirectoryField, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridx = 3;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.inputDirectoryChooseButton, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.outputDirectoryLabel, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.outputDirectoryField, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 3;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.outputDirectoryChooseButton, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets.set(5, 5, 5, 5);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.expandingPanel, gridBagConstraints);
		}

	}

	private class PlotStep extends JPanel {

		private static final long serialVersionUID = 1L;

		private final JLabel infoLabel = new JLabel("<html>In this step you choose which plots the tool should generate.</html>");

		private final JCheckBox deploymentSequenceDiagrams = new JCheckBox("Deployment Sequence Diagrams");
		private final JCheckBox assemblySequenceDiagrams = new JCheckBox("Assembly Sequence Diagrams");
		private final JCheckBox deploymentComponentDependencyGraph = new JCheckBox("Deployment Component Dependency Graph");
		private final JCheckBox assemblyComponentDependencyGraph = new JCheckBox("Assembly Component Dependency Graph");
		private final JCheckBox containerDependencyGraph = new JCheckBox("Plot Container Dependency Graph");
		private final JCheckBox allGraphs = new JCheckBox("Select All");
		private final JPanel expandingPanel = new JPanel();

		public PlotStep() {
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
			this.add(this.deploymentSequenceDiagrams, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(0, 5, 0, 0);
			this.add(this.assemblySequenceDiagrams, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(0, 5, 0, 0);
			this.add(this.deploymentComponentDependencyGraph, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(0, 5, 0, 0);
			this.add(this.assemblyComponentDependencyGraph, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(0, 5, 0, 0);
			this.add(this.containerDependencyGraph, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(10, 5, 5, 5);
			this.add(this.allGraphs, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.expandingPanel, gridBagConstraints);
		}
	}

	private class PrintStep extends JPanel {

		private static final long serialVersionUID = 1L;

		private final JLabel infoLabel = new JLabel("<html>In this step you choose which prints the tool should generate.</html>");

		private final JCheckBox messageTraces = new JCheckBox("Message Traces");
		private final JCheckBox executionTraces = new JCheckBox("Execution Traces");
		private final JCheckBox invalidExecutionTraces = new JCheckBox("Invalid Execution Traces");
		private final JCheckBox allPrints = new JCheckBox("Select All");
		private final JPanel expandingPanel = new JPanel();

		public PrintStep() {
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
			this.add(this.messageTraces, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(0, 5, 0, 0);
			this.add(this.executionTraces, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(0, 5, 0, 0);
			this.add(this.invalidExecutionTraces, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.insets.set(10, 5, 5, 5);
			this.add(this.allPrints, gridBagConstraints);

			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.expandingPanel, gridBagConstraints);
		}
	}

	private class AdditionalOptionsStep extends JPanel {

		private static final long serialVersionUID = 1L;

		private final JLabel infoLabel = new JLabel("<html>In this step you manage additional options for the trace analysis.</html>");
		private final JPanel expandingPanel = new JPanel();

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
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			this.add(this.expandingPanel, gridBagConstraints);
		}
	}

	private class FinalStep extends JPanel {
		private static final long serialVersionUID = 1L;

		private final JLabel infoLabel = new JLabel("<html>All necessary information have been gathered. You can now start the trace analysis.</html>");
		private final JButton startButton = new JButton("Start");
		private final JTextArea logArea = new JTextArea(20, 60);
		private final JScrollPane logScrollPane = new JScrollPane(this.logArea);

		public FinalStep() {
			this.initializeComponents();
			this.addAndLayoutComponents();
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
	}

}
