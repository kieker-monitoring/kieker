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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ConversionStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final String currentPath = new File(".").getAbsolutePath();

	private final JLabel infoLabel = new JLabel("<html>In this step you manage Graphviz and Pic2Plot in order to convert the results from the trace "
			+ "analysis.<br/><br/>WARNING: If selected, this step will overwrite existing files in your output directory.</html>");
	private final JPanel expandingPanel1 = new JPanel();
	private final JPanel expandingPanel2 = new JPanel();
	private final JCheckBox performStep = new JCheckBox("Perform File Conversion");
	private final JLabel graphvizDirectoryLabel = new JLabel("Graphviz Directory: ");
	private final JLabel pic2plotDirectoryLabel = new JLabel("Pic2Plot Directory: ");
	private final JTextField graphvizDirectoryField = new JTextField(this.currentPath);
	private final JTextField pic2plotDirectoryField = new JTextField(this.currentPath);
	private final JButton graphvizDirectoryChooseButton = new JButton("Choose");
	private final JButton pic2plotDirectoryChooseButton = new JButton("Choose");
	private final JLabel outputFormat = new JLabel("Output Format: ");
	private final JComboBox outputFormatField = new JComboBox(new String[] { "PNG", "JPEG", "SVG" });

	public ConversionStep() {
		this.addAndLayoutComponents();
		this.addLogicToComponents();
		this.setDefaultValues();
	}

	private void addAndLayoutComponents() {
		this.setLayout(new GridBagLayout());

		final GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.add(this.infoLabel, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(5, 5, 0, 0);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		this.add(this.performStep, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.graphvizDirectoryLabel, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.graphvizDirectoryField, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.graphvizDirectoryChooseButton, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.pic2plotDirectoryLabel, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.pic2plotDirectoryField, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.pic2plotDirectoryChooseButton, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		this.add(this.outputFormat, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		this.add(this.outputFormatField, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		this.add(this.expandingPanel1, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		this.add(this.expandingPanel2, gridBagConstraints);
	}

	private void addLogicToComponents() {
		this.graphvizDirectoryChooseButton.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser fileChooser = new JFileChooser(ConversionStep.this.graphvizDirectoryField.getText());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (fileChooser.showOpenDialog(ConversionStep.this) == JFileChooser.APPROVE_OPTION) {
					ConversionStep.this.graphvizDirectoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		this.pic2plotDirectoryChooseButton.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser fileChooser = new JFileChooser(ConversionStep.this.pic2plotDirectoryField.getText());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (fileChooser.showOpenDialog(ConversionStep.this) == JFileChooser.APPROVE_OPTION) {
					ConversionStep.this.pic2plotDirectoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
	}

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {}

	@Override
	public void saveCurrentConfiguration(final Writer writer) throws IOException {

	}

	@Override
	public void loadCurrentConfiguration(final Scanner scanner) throws IOException {

	}

	private void setDefaultValues() {

	}

	public void convert() {

	}

}
