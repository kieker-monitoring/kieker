/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.ui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.trace.analysis.TraceAnalysisToolMain;
import kieker.tools.trace.analysis.gui.AbstractStep;
import kieker.tools.trace.analysis.gui.AdditionalFiltersStep;
import kieker.tools.trace.analysis.gui.AdditionalOptionsStep;
import kieker.tools.trace.analysis.gui.ConversionStep;
import kieker.tools.trace.analysis.gui.FinalStep;
import kieker.tools.trace.analysis.gui.PlotStep;
import kieker.tools.trace.analysis.gui.PrintStep;
import kieker.tools.trace.analysis.gui.WelcomeStep;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.9
 */
public class TraceAnalysisGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TraceAnalysisGUI.class);

	private final CardLayout mainPanelLayout = new CardLayout();
	private final JPanel mainPanel = new JPanel(this.mainPanelLayout);
	private final JButton previousButton = new JButton("Previous");
	private final JButton nextButton = new JButton("Next");

	private final StartTraceAnalysisActionListener startTraceAnalysisClickListener = new StartTraceAnalysisActionListener();
	private final ConversionStep conversionStep = new ConversionStep();
	private final WelcomeStep welcomeStep = new WelcomeStep();
	private final FinalStep finalStep = new FinalStep(this.startTraceAnalysisClickListener);
	private final AbstractStep[] steps = { this.welcomeStep, new PlotStep(), new PrintStep(), new AdditionalOptionsStep(), new AdditionalFiltersStep(),
		this.conversionStep, this.finalStep, };
	private int currentStepIndex;

	/**
	 * Creating UI.
	 */
	public TraceAnalysisGUI() {
		super("Trace Analysis Tool - GUI");

		this.addAndLayoutComponents();
		this.initializeComponents();
		this.addLogicToComponents();
		this.initializeWindow();
		this.loadCurrentConfiguration();
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

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent arg0) {
				TraceAnalysisGUI.this.nextStep();
			}
		});

		this.previousButton.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent e) {
				TraceAnalysisGUI.this.previousStep();
			}
		});
	}

	private void nextStep() {
		if (this.steps[this.currentStepIndex].isNextStepAllowed()) {
			this.currentStepIndex++;
			this.mainPanelLayout.next(this.mainPanel);
			this.previousButton.setEnabled(true);
			this.nextButton.setEnabled(this.currentStepIndex < (this.steps.length - 1));

			this.saveCurrentConfiguration();
		}
	}

	private void previousStep() {
		this.currentStepIndex--;
		this.mainPanelLayout.previous(this.mainPanel);
		this.nextButton.setEnabled(true);
		this.previousButton.setEnabled(this.currentStepIndex > 0);

		this.saveCurrentConfiguration();
	}

	private void loadCurrentConfiguration() {
		InputStreamReader propertiesFileInputStream = null;
		try {
			propertiesFileInputStream = new InputStreamReader(new FileInputStream("TraceAnalysisGUI.properties"), "UTF-8");
			final Properties properties = new Properties();
			properties.load(propertiesFileInputStream);
			for (final AbstractStep step : this.steps) {
				step.loadCurrentConfiguration(properties);
			}
		} catch (final Exception ex) { // NOPMD NOCS
			for (final AbstractStep step : this.steps) {
				step.loadDefaultConfiguration();
			}
		} finally {
			if (null != propertiesFileInputStream) {
				try {
					propertiesFileInputStream.close();
				} catch (final IOException e) {
					LOGGER.warn("Could not close input stream", e);
				}
			}
		}
	}

	private void saveCurrentConfiguration() {
		final Properties properties = new Properties();
		for (final AbstractStep step : this.steps) {
			step.saveCurrentConfiguration(properties);
		}

		OutputStreamWriter stream = null;
		try {
			stream = new OutputStreamWriter(new FileOutputStream("TraceAnalysisGUI.properties"), "UTF-8");
			properties.store(stream, null);
		} catch (final IOException ex) {
			LOGGER.warn("Configuration could not be saved", ex);
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (final IOException e) {
					LOGGER.warn("Could not close output stream", e);
				}
			}
		}
	}

	private void initializeWindow() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		int maxHeight = 1;
		int maxWidth = 1;
		for (final AbstractStep panel : this.steps) {
			this.mainPanel.add(panel, panel.toString());

			maxHeight = Math.max(maxHeight, panel.getPreferredSize().height);
			maxWidth = Math.max(maxWidth, panel.getPreferredSize().width);
		}
		this.setSize(maxWidth, maxHeight);
		this.setLocationRelativeTo(null);
	}

	private void startTraceAnalysis() {
		final Collection<String> parameters = new ArrayList<>();

		for (final AbstractStep step : this.steps) {
			step.addSelectedTraceAnalysisParameters(parameters);
		}

		this.previousButton.setEnabled(false);
		this.finalStep.disableButtons();
		final Thread thread = new Thread() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				TraceAnalysisToolMain.runEmbedded(parameters.toArray(new String[parameters.size()]));

				TraceAnalysisGUI.this.conversionStep.convert(TraceAnalysisGUI.this.welcomeStep.getOutputDirectory());
				TraceAnalysisGUI.this.previousButton.setEnabled(true);
				TraceAnalysisGUI.this.finalStep.enableButtons();
			}
		};

		thread.start();
	}

	/**
	 * Main function.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(final String[] args) {
		final TraceAnalysisGUI gui = new TraceAnalysisGUI();
		gui.setVisible(true);
	}

	/**
	 * @author Nils Christian Ehmke
	 *
	 * @since 1.9
	 */
	private class StartTraceAnalysisActionListener implements ActionListener {

		public StartTraceAnalysisActionListener() {
			// No code necessary
		}

		@Override
		@SuppressWarnings("synthetic-access")
		public void actionPerformed(final ActionEvent e) {
			TraceAnalysisGUI.this.startTraceAnalysis();
		}

	}
}
