/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Simple frontend to {@link TraceAnalysisTool}.
 * 
 * @author Robert von Massow
 * 
 */
// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/220
public class TraceAnalysisGui extends JFrame implements ActionListener {
	private static final long serialVersionUID = 11333L;
	private final JList l;
	private final DefaultListModel lm = new DefaultListModel();
	private final JTextField outdir;
	private final JFrame frame;
	private final JButton remB;
	private final JButton addB;
	private final JButton run;
	private final JTextField prefix;
	private final JCheckBox plotSq = new JCheckBox();
	private final JCheckBox plotComp = new JCheckBox();
	private final JCheckBox plotCont = new JCheckBox();
	private final JCheckBox plotOp = new JCheckBox();
	private final JCheckBox plotAgg = new JCheckBox();
	private final JCheckBox plotCall = new JCheckBox();
	private final JCheckBox printMsg = new JCheckBox();
	private final JCheckBox printExe = new JCheckBox();
	private final JCheckBox printInv = new JCheckBox();
	private final JCheckBox printEquiv = new JCheckBox();
	private GridBagLayout g;
	private final JTextField traces = new JTextField(36);
	private final JRadioButton disabled = new JRadioButton();
	private final JRadioButton allocation = new JRadioButton();
	private final JRadioButton assembly = new JRadioButton();
	private final ButtonGroup bg = new ButtonGroup();
	private final JTextField from = new JTextField("-1", 8);
	private final JTextField to = new JTextField("-1", 8);
	private final JTextField duration = new JTextField("-1", 8);
	private final JCheckBox shrtlbls = new JCheckBox();
	private final JCheckBox ignInvalid = new JCheckBox();

	/*
	 * [--plot-Component-Dependency-Graph] [--plot-Container-Dependency-Graph]
	 * [--plot-Operation-Dependency-Graph] [--plot-Aggregated-Call-Tree]
	 * [--plot-Call-Trees] [--print-Message-Traces] [--print-Execution-Traces]
	 * [--print-invalid-Execution-Traces] [--print-Equivalence-Classes]
	 */
	public TraceAnalysisGui() {
		this.frame = this;
		this.setResizable(false);
		final JPanel p = (JPanel) this.getContentPane();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.setLayout(new GridBagLayout());
		JLabel label = new JLabel("Input:");

		final GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridheight = 1;
		gc.gridwidth = 3; // NOCS (MagicNumberCheck)
		gc.ipadx = 3; // NOCS (MagicNumberCheck)
		gc.ipady = 4; // NOCS (MagicNumberCheck)
		gc.weightx = .5; // NOCS (MagicNumberCheck)
		gc.insets = new Insets(8, 8, 8, 8); // NOCS (MagicNumberCheck)
		gc.gridx = 0; // NOCS (MagicNumberCheck)
		// gc.gridy = 0;
		p.add(label, gc);
		gc.gridx = 3; // NOCS (MagicNumberCheck)
		gc.gridwidth = 3; // NOCS (MagicNumberCheck)
		final JPanel pa = new JPanel();

		final JPanel p2 = new JPanel(new BorderLayout());
		this.l = new JList(this.lm);
		this.l.setPreferredSize(new Dimension(300, 100)); // NOCS (MagicNumberCheck)
		final JScrollPane sp = new JScrollPane(this.l);
		sp.setPreferredSize(new Dimension(320, 100)); // NOCS (MagicNumberCheck)
		pa.setLayout(new BorderLayout());
		// sp.add(this.l);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.addB = new JButton("add");
		this.addB.addActionListener(this);
		this.remB = new JButton("remove");
		this.remB.addActionListener(this);
		p2.add(this.addB, BorderLayout.NORTH);
		p2.add(this.remB, BorderLayout.SOUTH);
		pa.add(p2, BorderLayout.EAST);
		pa.add(sp, BorderLayout.CENTER);

		p.add(pa, gc);
		gc.gridx = 0; // NOCS (MagicNumberCheck)
		gc.gridwidth = 2; // NOCS (MagicNumberCheck)
		// gc.gridy = 1;
		p.add(new JLabel("Output:"), gc);
		this.outdir = new JTextField(36); // NOCS (MagicNumberCheck)
		this.outdir.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				final int ret = fc.showOpenDialog(TraceAnalysisGui.this.frame);
				if (JFileChooser.APPROVE_OPTION == ret) {
					TraceAnalysisGui.this.outdir.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		gc.gridx = 3; // NOCS (MagicNumberCheck)
		gc.gridwidth = 3; // NOCS (MagicNumberCheck)
		p.add(this.outdir, gc);

		gc.gridx = 0; // NOCS (MagicNumberCheck)
		gc.gridwidth = 2; // NOCS (MagicNumberCheck)
		// gc.gridy = 2;
		p.add(new JLabel("Prefix:"), gc);

		gc.gridx = 3; // NOCS (MagicNumberCheck)

		gc.gridwidth = 3; // NOCS (MagicNumberCheck)
		this.prefix = new JTextField(36); // NOCS (MagicNumberCheck)
		p.add(this.prefix, gc);
		gc.gridwidth = 2; // NOCS (MagicNumberCheck)
		gc.gridx = 0;
		p.add(new JLabel("Traces:"), gc);
		gc.gridx = 3; // NOCS (MagicNumberCheck)
		gc.gridwidth = 3; // NOCS (MagicNumberCheck)
		p.add(this.traces, gc);
		// gc.gridy = 3;
		gc.gridx = 0;
		gc.insets = new Insets(8, 8, 8, 8); // NOCS (MagicNumberCheck)
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 1; // NOCS (MagicNumberCheck)
		gc.gridwidth = 6; // NOCS (MagicNumberCheck)
		gc.insets = new Insets(0, 0, 0, 0); // NOCS (MagicNumberCheck)
		p.add(new JSeparator(SwingConstants.HORIZONTAL), gc);

		gc.insets = new Insets(8, 8, 8, 8); // NOCS (MagicNumberCheck)

		gc.anchor = GridBagConstraints.WEST;
		gc.gridwidth = 4; // NOCS (MagicNumberCheck)
		p.add(new JLabel("Operations:"), gc);
		gc.gridx = 4; // NOCS (MagicNumberCheck)
		gc.gridwidth = 1; // NOCS (MagicNumberCheck)
		gc.anchor = GridBagConstraints.LINE_END;
		label = new JLabel("Shrt Lbls");
		label.setToolTipText("Short Labels");
		p.add(label, gc);
		gc.gridwidth = 1; // NOCS (MagicNumberCheck)
		gc.gridx = 5; // NOCS (MagicNumberCheck)
		gc.weightx = 0; // NOCS (MagicNumberCheck)
		p.add(this.shrtlbls, gc);
		gc.gridx = 0; // NOCS (MagicNumberCheck)
		gc.gridwidth = 2; // NOCS (MagicNumberCheck)
		gc.weightx = 1; // NOCS (MagicNumberCheck)
		p.add(new JLabel("Plot Sequence Diagrams"), gc);
		gc.gridx = 3; // NOCS (MagicNumberCheck)
		gc.gridwidth = 1; // NOCS (MagicNumberCheck)
		p.add(this.plotSq, gc);
		gc.gridx = 4; // NOCS (MagicNumberCheck)
		gc.gridwidth = 1; // NOCS (MagicNumberCheck)
		gc.weightx = 1; // NOCS (MagicNumberCheck)
		p.add(new JLabel("Ignore invalid traces"), gc);
		gc.gridwidth = 1; // NOCS (MagicNumberCheck)
		gc.gridx = 5; // NOCS (MagicNumberCheck)
		p.add(this.ignInvalid, gc);
		gc.gridwidth = 6; // NOCS (MagicNumberCheck)
		gc.gridx = 0; // NOCS (MagicNumberCheck)
		p.add(this.plotDiags(), gc);
		p.add(this.treePanel(), gc);
		p.add(this.tracePanel(), gc);
		p.add(this.equivSelect(), gc);
		p.add(this.ignorePanel(), gc);

		this.run = new JButton();
		this.run.addActionListener(this);
		this.run.setText("Run");
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = gc.weighty = 1; // NOCS (MagicNumberCheck)
		gc.gridx = 7; // NOCS (MagicNumberCheck)
		gc.gridheight = 17; // NOCS (MagicNumberCheck)
		p.add(this.run, gc);

		this.pack();
		this.setVisible(true);
	}

	private Component ignorePanel() {
		final JPanel ign = new JPanel(this.g);
		final TitledBorder b = this.tBorder();
		b.setTitle("Temporal Selection");

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1; // NOCS (MagicNumberCheck)
		gbc.insets = new Insets(4, 4, 4, 4); // NOCS (MagicNumberCheck)

		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridheight = 1; // NOCS (MagicNumberCheck)
		ign.add(new JLabel("Select Traces between"), gbc);
		this.from.setToolTipText(Constants.DATE_FORMAT_PATTERN + " or -1 to disable filter");
		this.to.setToolTipText(Constants.DATE_FORMAT_PATTERN + " or -1 to disable filter");
		gbc.gridheight = 1; // NOCS (MagicNumberCheck)
		ign.add(this.from, gbc);
		ign.add(new JLabel("and"), gbc);
		ign.add(this.to, gbc);

		this.duration.setToolTipText("in ms, -1 to disable filter");
		ign.add(new JLabel("Max. trace duration"));
		ign.add(this.duration);
		ign.setBorder(b);
		return ign;
	}

	/**
	 * @return
	 */
	/**
	 * @return
	 */
	private Component equivSelect() {
		final JPanel equ = new JPanel(this.g);
		final TitledBorder b = this.tBorder();
		b.setTitle("Equivalence Mode");
		this.bg.add(this.assembly);
		this.bg.add(this.allocation);
		this.bg.add(this.disabled);

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.insets = new Insets(5, 5, 5, 8); // NOCS (MagicNumberCheck)
		gbc.weightx = 1.25; // NOCS (MagicNumberCheck)
		equ.add(new JLabel("Messages"), gbc);
		gbc.weightx = 0; // NOCS (MagicNumberCheck)
		equ.add(this.allocation, gbc);
		gbc.weightx = 1.3; // NOCS (MagicNumberCheck)
		// g.gridy = 1;
		equ.add(new JLabel("Executions"), gbc);
		gbc.weightx = 0; // NOCS (MagicNumberCheck)
		equ.add(this.assembly, gbc);
		gbc.weightx = 1.3; // NOCS (MagicNumberCheck)
		equ.add(new JLabel("Disabled"), gbc);
		gbc.weightx = 0; // NOCS (MagicNumberCheck)
		equ.add(this.disabled, gbc);
		this.disabled.setSelected(true);
		gbc.fill = GridBagConstraints.BOTH;
		equ.setBorder(b);
		return equ;
	}

	private Component tracePanel() {
		final JPanel deps = new JPanel(this.g); // new GridBagLayout());
		final TitledBorder b = this.tBorder();
		b.setTitle("Traces");
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1; // NOCS (MagicNumberCheck)
		gbc.insets = new Insets(5, 5, 5, 8); // NOCS (MagicNumberCheck)
		gbc.weightx = 1.2; // NOCS (MagicNumberCheck)
		deps.add(new JLabel("Messages"), gbc);
		gbc.weightx = 0; // NOCS (MagicNumberCheck)
		deps.add(this.printMsg, gbc);
		gbc.weightx = 1.3; // NOCS (MagicNumberCheck)
		// g.gridy = 1;
		deps.add(new JLabel("Executions"), gbc);
		gbc.weightx = 0; // NOCS (MagicNumberCheck)
		deps.add(this.printExe, gbc);
		gbc.weightx = .5; // NOCS (MagicNumberCheck)
		deps.add(new JLabel("Invalid Executions"), gbc);
		gbc.weightx = 0; // NOCS (MagicNumberCheck)
		deps.add(this.printInv, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		deps.setBorder(b);
		return deps;
	}

	private Component treePanel() {
		final JPanel deps = new JPanel(this.g); // new GridBagLayout());
		final TitledBorder b = this.tBorder();
		b.setTitle("Call Trees");
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1; // NOCS (MagicNumberCheck)
		gbc.insets = new Insets(5, 5, 5, 8); // NOCS (MagicNumberCheck)
		gbc.weightx = 1; // NOCS (MagicNumberCheck)
		deps.add(new JLabel("Single"), gbc);
		gbc.weightx = 0; // NOCS (MagicNumberCheck)
		deps.add(this.plotCall, gbc);
		gbc.weightx = .8; // NOCS (MagicNumberCheck)
		deps.add(new JLabel("Aggregated"), gbc);
		gbc.weightx = 0; // NOCS (MagicNumberCheck)
		deps.add(this.plotAgg, gbc);
		gbc.weightx = 1.6; // NOCS (MagicNumberCheck)
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3; // NOCS (MagicNumberCheck)
		final JPanel p = new JPanel();
		deps.add(p, gbc);
		deps.setBorder(b);
		return deps;
	}

	private Component plotDiags() {
		this.g = new GridBagLayout();
		final JPanel deps = new JPanel(this.g);
		final TitledBorder b = this.tBorder();
		b.setTitle("Dependency Graphs");
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 8); // NOCS (MagicNumberCheck)
		gbc.weightx = 1; // NOCS (MagicNumberCheck)
		deps.add(new JLabel("Container Level"), gbc);
		gbc.weightx = 0;
		deps.add(this.plotCont, gbc);
		gbc.weightx = 1;
		deps.add(new JLabel("Component Level"), gbc);
		gbc.weightx = 0;
		deps.add(this.plotComp, gbc);
		gbc.weightx = 1;
		deps.add(new JLabel("Operation Level"), gbc);
		gbc.weightx = 0;
		deps.add(this.plotOp, gbc);
		deps.setBorder(b);
		return deps;
	}

	private TitledBorder tBorder() {
		return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		new TraceAnalysisGui();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == this.addB) {
			final JFileChooser choose = new JFileChooser();
			choose.setMultiSelectionEnabled(true);
			choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			final int ret = choose.showOpenDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				for (final File f : choose.getSelectedFiles()) {
					System.out.println(f);
					this.lm.addElement(f.getAbsoluteFile());
				}
			}
			System.out.println(this.lm.getSize());
		} else if (e.getSource() == this.remB) {
			if (this.lm.getSize() > 0) {
				this.lm.remove(this.l.getSelectedIndex());
			}
		} else if (e.getSource() == this.run) {
			final List<String> b = new Vector<String>();
			this.appendCmd(Constants.CMD_OPT_NAME_INPUTDIRS, b);
			for (final Object s : new IterableEnumeration(this.lm.elements())) {
				this.appendStr(s.toString(), b);
			}
			this.appendCmd(Constants.CMD_OPT_NAME_OUTPUTDIR, b);
			this.appendStr(this.outdir.getText(), b);
			if (this.prefix.getText().matches("\\w")) {
				this.appendCmd(Constants.CMD_OPT_NAME_OUTPUTFNPREFIX, b);
				this.appendStr(this.prefix.getText(), b);
			}
			if (this.plotSq.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS, b);
			}
			if (this.plotComp.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG, b);
			}
			if (this.plotCont.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG, b);
			}
			if (this.plotOp.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG, b);
			}
			if (this.plotAgg.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE, b);
			}
			if (this.plotCall.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES, b);
			}
			if (this.printMsg.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES, b);
			}
			if (this.printExe.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES, b);
			}
			if (this.printInv.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES, b);
			}
			if (this.printEquiv.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT, b);
			}
			// if (this.allocation.isSelected()) {
			// this.appendCmd(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE, b);
			// this.appendStr(Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION,
			// b);
			// } else if (this.assembly.isSelected()) {
			// this.appendCmd(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE, b);
			// this.appendStr(
			// Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY,
			// b);
			// }
			if (this.traces.getText().matches("\\d")) {
				this.appendCmd(Constants.CMD_OPT_NAME_SELECTTRACES, b);
				this.appendStr(this.traces.getText(), b);
			}
			if (!this.from.getText().matches("^-1")) {
				this.appendCmd(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE, b);
				this.from.getText();
			}
			if (!this.to.getText().matches("^-1")) {
				this.appendCmd(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE, b);
				this.appendStr(this.to.getText(), b);
			}
			if (!this.duration.getText().matches("^-1")) {
				this.appendCmd(Constants.CMD_OPT_NAME_MAXTRACEDURATION, b);
				this.appendStr(this.duration.getText(), b);
			}
			if (this.shrtlbls.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_SHORTLABELS, b);
			}
			if (this.ignInvalid.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES, b);
			}
			TraceAnalysisTool.main(b.toArray(new String[b.size()]));
			/*
			 * usage: [--plot-Sequence-Diagrams]
			 * [--plot-Component-Dependency-Graph]
			 * [--plot-Container-Dependency-Graph]
			 * [--plot-Operation-Dependency-Graph] [--plot-Aggregated-Call-Tree]
			 * [--plot-Call-Trees] [--print-Message-Traces]
			 * [--print-Execution-Traces] [--print-invalid-Execution-Traces]
			 * [--print-Equivalence-Classes] [--select-traces <id0 ... idn>]
			 * [--trace-equivalence-mode <allocation|assembly|disabled>]
			 * [--ignore-invalid-traces] [--max-trace-duration <duration in ms>]
			 * [--ignore-records-before-date <yyyyMMdd-HHmmss>]
			 * [--ignore-records-after-date <yyyyMMdd-HHmmss>] [--short-labels]
			 */

		}
	}

	private void appendCmd(final String cmdName, final List<String> b) {
		b.add("--" + cmdName);
	}

	private void appendStr(final String str, final List<String> b) {
		b.add(str);
	}
}

class IterableEnumeration<T> implements Iterable<T>, Iterator<T> {

	private final Enumeration<T> enumeration;

	public IterableEnumeration(final Enumeration<T> enumeration) {
		this.enumeration = enumeration;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return this.enumeration.hasMoreElements();
	}

	@Override
	public T next() {
		return this.enumeration.nextElement();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
