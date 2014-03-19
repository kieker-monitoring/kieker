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
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class FinalStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private final JLabel infoLabel = new JLabel("<html>All necessary information have been gathered. You can now start the trace analysis.</html>");
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
		this.startButton.addActionListener(this.startTraceAnalysisClickListener);
	}

	public void deliverParameters(final Collection<String> parameters) {}
}
