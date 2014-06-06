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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kieker.tools.traceAnalysis.Constants;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class WelcomeStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private static final String PROPERTY_KEY_IDENTIFIER = WelcomeStep.class.getSimpleName();
	private static final String PROPERTY_KEY_OUTPUT_DIRECTORY = PROPERTY_KEY_IDENTIFIER + ".outputDirectory";
	private static final String PROPERTY_KEY_INPUT_DIRECTORY = PROPERTY_KEY_IDENTIFIER + ".inputDirectory";

	private final JLabel welcomeLabel = new JLabel("<html>Welcome to Kieker's Trace Analysis GUI.<br/>This wizard helps you generating visual representatons "
			+ "based on a trace analysis of your records.<br/><br/>In this step you choose the input input and output directories.</html>");
	private final JLabel inputDirectoryLabel = new JLabel("Input Directory: ");
	private final JLabel outputDirectoryLabel = new JLabel("Output Directory: ");
	private final JTextField inputDirectoryTextField = new JTextField();
	private final JTextField outputDirectoryTextField = new JTextField();
	private final JButton inputDirectoryChooseButton = new JButton("Choose");
	private final JButton outputDirectoryChooseButton = new JButton("Choose");
	private final JPanel expandingPanel = new JPanel();

	public WelcomeStep() {
		this.addAndLayoutComponents();
		this.addLogicToComponents();
	}

	private void addAndLayoutComponents() {
		this.setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.welcomeLabel, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.inputDirectoryLabel, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.inputDirectoryTextField, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.inputDirectoryChooseButton, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.outputDirectoryLabel, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.outputDirectoryTextField, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.outputDirectoryChooseButton, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.expandingPanel, gridBagConstraints);

		this.inputDirectoryTextField.setToolTipText("The input directory contains usually monitoring records for the analysis.");
		this.outputDirectoryTextField.setToolTipText("The output directory is used to write the visual representations from the analysis.");
	}

	private void addLogicToComponents() {
		this.inputDirectoryChooseButton.addActionListener(new ChooseDirectoryActionListener(this.inputDirectoryTextField, this));
		this.outputDirectoryChooseButton.addActionListener(new ChooseDirectoryActionListener(this.outputDirectoryTextField, this));
	}

	@Override
	public boolean isNextStepAllowed() {
		final File inputDirectory = new File(this.inputDirectoryTextField.getText());
		if (!inputDirectory.isDirectory()) {
			final int result = JOptionPane.showConfirmDialog(this, "The input directory does not exist. Continue?", "Input Directory", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (JOptionPane.NO_OPTION == result) {
				return false;
			}
		}

		final File outputDirectory = new File(this.outputDirectoryTextField.getText());
		if (outputDirectory.isDirectory()) {
			return true;
		} else {
			final int result = JOptionPane.showConfirmDialog(this, "The output directory does not exist. Create it?", "Output Directory", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (JOptionPane.YES_OPTION == result) {
				return outputDirectory.mkdirs();
			} else {
				return false;
			}
		}
	}

	public String getOutputDirectory() {
		return this.outputDirectoryTextField.getText();
	}

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		parameters.add("--" + Constants.CMD_OPT_NAME_INPUTDIRS);
		parameters.add("\"" + this.inputDirectoryTextField.getText() + "\"");

		parameters.add("--" + Constants.CMD_OPT_NAME_OUTPUTDIR);
		parameters.add("\"" + this.outputDirectoryTextField.getText() + "\"");
	}

	@Override
	public void saveCurrentConfiguration(final Properties properties) {
		properties.setProperty(PROPERTY_KEY_INPUT_DIRECTORY, this.inputDirectoryTextField.getText());
		properties.setProperty(PROPERTY_KEY_OUTPUT_DIRECTORY, this.outputDirectoryTextField.getText());
	}

	@Override
	public void loadCurrentConfiguration(final Properties properties) {
		this.inputDirectoryTextField.setText(properties.getProperty(PROPERTY_KEY_INPUT_DIRECTORY));
		this.outputDirectoryTextField.setText(properties.getProperty(PROPERTY_KEY_OUTPUT_DIRECTORY));
	}

	@Override
	public void loadDefaultConfiguration() {
		final String currentPath = new File(".").getAbsolutePath();

		this.inputDirectoryTextField.setText(currentPath);
		this.outputDirectoryTextField.setText(currentPath);
	}

	private static class ChooseDirectoryActionListener implements ActionListener {

		private final JTextField textField;
		private final Component parent;

		public ChooseDirectoryActionListener(final JTextField textField, final Component parent) {
			this.textField = textField;
			this.parent = parent;
		}

		@Override
		public void actionPerformed(final ActionEvent event) {
			final JFileChooser fileChooser = new JFileChooser(this.textField.getText());
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (fileChooser.showOpenDialog(this.parent) == JFileChooser.APPROVE_OPTION) {
				this.textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		}

	}

}
