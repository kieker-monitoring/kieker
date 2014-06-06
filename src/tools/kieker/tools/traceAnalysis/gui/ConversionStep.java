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
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ConversionStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private static final String PROPERTY_KEY_IDENTIFIER = ConversionStep.class.getSimpleName();
	private static final String PROPERTY_KEY_PERFORM_STEP = PROPERTY_KEY_IDENTIFIER + ".performStep";
	private static final String PROPERTY_KEY_GRAPHVIZ = PROPERTY_KEY_IDENTIFIER + ".graphvizDirectoryField";
	private static final String PROPERTY_KEY_PIC2PLOT = PROPERTY_KEY_IDENTIFIER + ".pic2plotDirectoryField";
	private static final String PROPERTY_KEY_OUTPUT_FORMAT = PROPERTY_KEY_IDENTIFIER + ".outputFormatField";

	private static final Log LOG = LogFactory.getLog(ConversionStep.class);
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
	private final JComboBox outputFormatField = new JComboBox(new String[] { "PNG", "JPEG", "SVG", "PDF" });

	public ConversionStep() {
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
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		// Nothing to add here
	}

	public void convert(final String outputDirectory) {
		if (this.performStep.isSelected()) {
			final File outputDir = new File(outputDirectory);
			final File[] dotFiles = outputDir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(final File dir, final String name) {
					return name.endsWith(".dot");
				}
			});
			final File[] picFiles = outputDir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(final File dir, final String name) {
					return name.endsWith(".pic");
				}
			});

			for (final File dotFile : dotFiles) {
				try {
					final Process p = Runtime.getRuntime().exec(
							new String[] { this.graphvizDirectoryField.getText() + "/dot", "-O",
								"-T" + this.outputFormatField.getSelectedItem().toString().toLowerCase(),
								dotFile.getAbsolutePath() });
					p.waitFor();
				} catch (final IOException e) {
					LOG.warn("An exception occurred", e);
				} catch (final InterruptedException e) {
					LOG.warn("An exception occurred", e);
				}
			}

			for (final File picFile : picFiles) {
				try {
					final Process p = Runtime.getRuntime().exec(
							new String[] { this.pic2plotDirectoryField.getText() + "/pic2plot",
								"-T" + this.outputFormatField.getSelectedItem().toString().toLowerCase(),
								picFile.getAbsolutePath() });
					final InputStream s = p.getInputStream();
					final OutputStream writer = new FileOutputStream(picFile.getAbsolutePath() + "."
							+ this.outputFormatField.getSelectedItem().toString().toLowerCase());
					int r;
					final byte[] buffer = new byte[10 * 1024];
					while ((r = s.read(buffer)) != -1) {
						writer.write(buffer, 0, r);
					}
					writer.close();
					s.close();
					p.waitFor();
				} catch (final IOException e) {
					LOG.warn("An exception occurred", e);
				} catch (final InterruptedException e) {
					LOG.warn("An exception occurred", e);
				}
			}

		}
	}

	@Override
	public void loadDefaultConfiguration() {
		this.graphvizDirectoryField.setText(this.currentPath);
		this.pic2plotDirectoryField.setText(this.currentPath);
		this.outputFormatField.setSelectedIndex(0);
	}

	@Override
	public void saveCurrentConfiguration(final Properties properties) {
		properties.setProperty(PROPERTY_KEY_PERFORM_STEP, Boolean.toString(this.performStep.isSelected()));
		properties.setProperty(PROPERTY_KEY_GRAPHVIZ, this.graphvizDirectoryField.getText());
		properties.setProperty(PROPERTY_KEY_PIC2PLOT, this.pic2plotDirectoryField.getText());
		properties.setProperty(PROPERTY_KEY_OUTPUT_FORMAT, Integer.toString(this.outputFormatField.getSelectedIndex()));
	}

	@Override
	public void loadCurrentConfiguration(final Properties properties) {
		this.performStep.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_PERFORM_STEP)));
		this.graphvizDirectoryField.setText(properties.getProperty(PROPERTY_KEY_GRAPHVIZ));
		this.pic2plotDirectoryField.setText(properties.getProperty(PROPERTY_KEY_PIC2PLOT));
		this.outputFormatField.setSelectedIndex(Integer.parseInt(properties.getProperty(PROPERTY_KEY_OUTPUT_FORMAT)));
	}

	@Override
	public boolean isNextStepAllowed() {
		return true;
	}

}
