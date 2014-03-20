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
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

/**
 * The final step of the trace analysis wizard contains of a log area and a button to start the trace analysis asynchronously.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class FinalStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final JLabel infoLabel = new JLabel("All necessary information have been gathered. You can now start the trace analysis.");
	private final JButton startButton = new JButton("Start");
	private final JTextArea logArea = new JTextArea(20, 60);
	private final JScrollPane logScrollPane = new JScrollPane(this.logArea);
	private final ActionListener startTraceAnalysisClickListener;

	public FinalStep(final ActionListener startTraceAnalysisClickListener) {
		this.startTraceAnalysisClickListener = startTraceAnalysisClickListener;

		this.initializeComponents();
		this.addAndLayoutComponents();
		this.addLogicToComponents();
	}

	private void initializeComponents() {
		this.logArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	private void addAndLayoutComponents() {
		this.setLayout(new GridBagLayout());

		final GridBagConstraints infoLabelConstraint = new GridBagConstraints();
		infoLabelConstraint.gridwidth = GridBagConstraints.REMAINDER;
		infoLabelConstraint.anchor = GridBagConstraints.NORTHWEST;
		infoLabelConstraint.insets.set(5, 5, 5, 5);
		infoLabelConstraint.weightx = 1.0;
		infoLabelConstraint.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.infoLabel, infoLabelConstraint);

		final GridBagConstraints startButtonConstraint = new GridBagConstraints();
		startButtonConstraint.gridwidth = GridBagConstraints.REMAINDER;
		startButtonConstraint.anchor = GridBagConstraints.NORTHWEST;
		startButtonConstraint.insets.set(5, 5, 5, 5);
		this.add(this.startButton, startButtonConstraint);

		final GridBagConstraints logScrollPaneConstraint = new GridBagConstraints();
		logScrollPaneConstraint.gridwidth = GridBagConstraints.REMAINDER;
		logScrollPaneConstraint.gridheight = GridBagConstraints.REMAINDER;
		logScrollPaneConstraint.anchor = GridBagConstraints.CENTER;
		logScrollPaneConstraint.insets.set(5, 5, 5, 5);
		logScrollPaneConstraint.weighty = 1.0;
		logScrollPaneConstraint.fill = GridBagConstraints.BOTH;
		this.add(this.logScrollPane, logScrollPaneConstraint);
	}

	private void addLogicToComponents() {
		this.startButton.addActionListener(this.startTraceAnalysisClickListener);
	}

	@Override
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		// No parameters can be selected in this step
	}
}
