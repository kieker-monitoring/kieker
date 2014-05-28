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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class WelcomeStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final String currentPath = new File(".").getAbsolutePath();

	private final JLabel welcomeLabel = new JLabel("<html>Welcome to Kicker's Trace Analysis GUI.<br/>This wizard helps you to generate visual representatons "
			+ "based on trace analysis of your records.<br/><br/>Please specify the input and output directories.</html>");
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

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser fileChooser = new JFileChooser(WelcomeStep.this.inputDirectoryField.getText());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (fileChooser.showOpenDialog(WelcomeStep.this) == JFileChooser.APPROVE_OPTION) {
					WelcomeStep.this.inputDirectoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		this.outputDirectoryChooseButton.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
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
	public boolean isNextStepAllowed() {
		final File inputDirectory = new File(this.inputDirectoryField.getText());
		if (!inputDirectory.isDirectory()) {
			final int result = JOptionPane.showConfirmDialog(this, "The input directory does not exist. Continue?", "Input Directory",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (JOptionPane.NO_OPTION == result) {
				return false;
			}
		}

		final File outputDirectory = new File(this.outputDirectoryField.getText());
		if (outputDirectory.isDirectory()) {
			return true;
		} else {
			final int result = JOptionPane.showConfirmDialog(this, "The output directory does not exist. Create it?", "Create Output Directory",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (JOptionPane.YES_OPTION == result) {
				return outputDirectory.mkdirs();
			} else {
				return false;
			}
		}
	}

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		parameters.add("-i");
		parameters.add("\"" + this.inputDirectoryField.getText() + "\"");

		parameters.add("-o");
		parameters.add("\"" + this.outputDirectoryField.getText() + "\"");
	}

}
