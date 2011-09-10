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
public class TraceAnalysisGui extends JFrame implements ActionListener {
// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/220
	private static final long serialVersionUID = 11333L;
	private final JFrame frame;
	private final JButton remB, addB, run;
	final JList l;
	final DefaultListModel lm = new DefaultListModel();
	private final JTextField outdir;
	private final JTextField prefix;
	private final JCheckBox plotSq = new JCheckBox(),
	plotComp = new JCheckBox(), plotCont = new JCheckBox(),
	plotOp = new JCheckBox(), plotAgg = new JCheckBox(),
	plotCall = new JCheckBox(), //printCall = new JCheckBox(),
	printMsg = new JCheckBox(), printExe = new JCheckBox(),
	printInv = new JCheckBox(), printEquiv = new JCheckBox();
	private GridBagLayout g;
	private final JTextField traces = new JTextField(36);
	private final JRadioButton disabled = new JRadioButton(),
	allocation = new JRadioButton(), assembly = new JRadioButton();
	private final ButtonGroup bg = new ButtonGroup();
	private final JTextField from = new JTextField("-1", 8),
	to = new JTextField("-1", 8), duration = new JTextField("-1", 8);
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
		gc.gridwidth = 3;
		gc.ipadx = 3;
		gc.ipady = 4;
		gc.weightx = .5;
		gc.insets = new Insets(8, 8, 8, 8);
		gc.gridx = 0;
		// gc.gridy = 0;
		p.add(label, gc);
		gc.gridx = 3;
		gc.gridwidth = 3;
		final JPanel pa = new JPanel();
		{
			final JPanel p2 = new JPanel(new BorderLayout());
			this.l = new JList(this.lm);
			this.l.setPreferredSize(new Dimension(300, 100));
			final JScrollPane sp = new JScrollPane(this.l);
			sp.setPreferredSize(new Dimension(320, 100));
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
		}
		p.add(pa, gc);
		gc.gridx = 0;
		gc.gridwidth = 2;
		// gc.gridy = 1;
		p.add(new JLabel("Output:"), gc);
		this.outdir = new JTextField(36);
		this.outdir.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				final int ret = fc.showOpenDialog(TraceAnalysisGui.this.frame);
				if (JFileChooser.APPROVE_OPTION == ret) {
					TraceAnalysisGui.this.outdir.setText(fc.getSelectedFile().getAbsolutePath().toString());
				}
			}
		});
		gc.gridx = 3;
		gc.gridwidth = 3;
		p.add(this.outdir, gc);

		gc.gridx = 0;
		gc.gridwidth = 2;
		// gc.gridy = 2;
		p.add(new JLabel("Prefix:"), gc);

		gc.gridx = 3;

		gc.gridwidth = 3;
		this.prefix = new JTextField(36);
		p.add(this.prefix, gc);
		gc.gridwidth = 2;
		gc.gridx = 0;
		p.add(new JLabel("Traces:"), gc);
		gc.gridx = 3;
		gc.gridwidth = 3;
		p.add(this.traces, gc);
		// gc.gridy = 3;
		gc.gridx = 0;
		gc.insets = new Insets(8, 8, 8, 8);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 1;
		gc.gridwidth = 6;
		gc.insets = new Insets(0, 0, 0, 0);
		p.add(new JSeparator(SwingConstants.HORIZONTAL), gc);

		gc.insets = new Insets(8, 8, 8, 8);
		{
			gc.anchor = GridBagConstraints.WEST;
			gc.gridwidth = 4;
			p.add(new JLabel("Operations:"), gc);
			gc.gridx = 4;
			gc.gridwidth = 1;
			gc.anchor = GridBagConstraints.LINE_END;
			label = new JLabel("Shrt Lbls");
			label.setToolTipText("Short Labels");
			p.add(label, gc);
			gc.gridwidth = 1;
			gc.gridx = 5;
			gc.weightx = 0;
			p.add(this.shrtlbls, gc);
			gc.gridx = 0;
			gc.gridwidth = 2;
			gc.weightx = 1;
			p.add(new JLabel("Plot Sequence Diagrams"), gc);
			gc.gridx = 3;
			gc.gridwidth = 1;
			p.add(this.plotSq, gc);
			gc.gridx = 4;
			gc.gridwidth = 1;
			gc.weightx = 1;
			p.add(new JLabel("Ignore invalid traces"), gc);
			gc.gridwidth = 1;
			gc.gridx = 5;
			p.add(this.ignInvalid, gc);
			gc.gridwidth = 6;
			gc.gridx = 0;
			p.add(this.plotDiags(), gc);
			p.add(this.treePanel(), gc);
			p.add(this.tracePanel(), gc);
			p.add(this.equivSelect(), gc);
			p.add(this.ignorePanel(), gc);
		}

		this.run = new JButton();
		this.run.addActionListener(this);
		this.run.setText("Run");
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = gc.weighty = 1;
		gc.gridx = 7;
		gc.gridheight = 17;
		p.add(this.run, gc);

		this.pack();
		this.setVisible(true);
	}

	private Component ignorePanel() {
		final JPanel ign = new JPanel(this.g);
		final TitledBorder b = this.tBorder();
		b.setTitle("Temporal Selection");

		final GridBagConstraints g = new GridBagConstraints();
		g.gridwidth = 1;
		g.insets = new Insets(4, 4, 4, 4);

		g.anchor = GridBagConstraints.NORTH;
		g.gridheight = 1;
		ign.add(new JLabel("Select Traces between"), g);
		this.from.setToolTipText(Constants.DATE_FORMAT_PATTERN
				+ " or -1 to disable filter");
		this.to.setToolTipText(Constants.DATE_FORMAT_PATTERN
				+ " or -1 to disable filter");
		g.gridheight = 1;
		ign.add(this.from, g);
		ign.add(new JLabel("and"), g);
		ign.add(this.to, g);

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

		final GridBagConstraints g = new GridBagConstraints();
		g.gridwidth = 1;
		g.insets = new Insets(5, 5, 5, 8);
		g.weightx = 1.25;
		equ.add(new JLabel("Messages"), g);
		g.weightx = 0;
		equ.add(this.allocation, g);
		g.weightx = 1.3;
		// g.gridy = 1;
		equ.add(new JLabel("Executions"), g);
		g.weightx = 0;
		equ.add(this.assembly, g);
		g.weightx = 1.3;
		equ.add(new JLabel("Disabled"), g);
		g.weightx = 0;
		equ.add(this.disabled, g);
		this.disabled.setSelected(true);
		g.fill = GridBagConstraints.BOTH;
		equ.setBorder(b);
		return equ;
	}

	private Component tracePanel() {
		final JPanel deps = new JPanel(this.g);// new GridBagLayout());
		final TitledBorder b = this.tBorder();
		b.setTitle("Traces");
		final GridBagConstraints g = new GridBagConstraints();
		g.gridwidth = 1;
		g.insets = new Insets(5, 5, 5, 8);
		g.weightx = 1.2;
		deps.add(new JLabel("Messages"), g);
		g.weightx = 0;
		deps.add(this.printMsg, g);
		g.weightx = 1.3;
		// g.gridy = 1;
		deps.add(new JLabel("Executions"), g);
		g.weightx = 0;
		deps.add(this.printExe, g);
		g.weightx = .5;
		deps.add(new JLabel("Invalid Executions"), g);
		g.weightx = 0;
		deps.add(this.printInv, g);
		g.fill = GridBagConstraints.BOTH;
		deps.setBorder(b);
		return deps;
	}

	private Component treePanel() {
		final JPanel deps = new JPanel(this.g);// new GridBagLayout());
		final TitledBorder b = this.tBorder();
		b.setTitle("Call Trees");
		final GridBagConstraints g = new GridBagConstraints();
		g.gridwidth = 1;
		g.insets = new Insets(5, 5, 5, 8);
		g.weightx = 1;
		deps.add(new JLabel("Single"), g);
		g.weightx = 0;
		deps.add(this.plotCall, g);
		g.weightx = .8;
		deps.add(new JLabel("Aggregated"), g);
		g.weightx = 0;
		deps.add(this.plotAgg, g);
		g.weightx = 1.6;
		g.fill = GridBagConstraints.BOTH;
		g.gridwidth = 3;
		final JPanel p = new JPanel();
		deps.add(p, g);
		deps.setBorder(b);
		return deps;
	}

	private Component plotDiags() {
		final JPanel deps = new JPanel(this.g = new GridBagLayout());
		final TitledBorder b = this.tBorder();
		b.setTitle("Dependency Graphs");
		final GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(5, 5, 5, 8);
		g.weightx = 1;
		deps.add(new JLabel("Container Level"), g);
		g.weightx = 0;
		deps.add(this.plotCont, g);
		g.weightx = 1;
		deps.add(new JLabel("Component Level"), g);
		g.weightx = 0;
		deps.add(this.plotComp, g);
		g.weightx = 1;
		deps.add(new JLabel("Operation Level"), g);
		g.weightx = 0;
		deps.add(this.plotOp, g);
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
			final Vector<String> b = new Vector<String>();
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
				this.appendCmd(
						Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG,
						b);
			}
			if (this.plotCont.isSelected()) {
				this.appendCmd(
						Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG,
						b);
			}
			if (this.plotOp.isSelected()) {
				this.appendCmd(
						Constants.CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG,
						b);
			}
			if (this.plotAgg.isSelected()) {
				this.appendCmd(
						Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE, b);
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
				this.appendCmd(
						Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES, b);
			}
			if (this.printEquiv.isSelected()) {
				this.appendCmd(Constants.CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT, b);
			}
			//            if (this.allocation.isSelected()) {
			//                this.appendCmd(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE, b);
			//                this.appendStr(Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION,
			//                        b);
			//            } else if (this.assembly.isSelected()) {
			//                this.appendCmd(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE, b);
			//                this.appendStr(
			//                        Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY,
			//                        b);
			//            }
			if (this.traces.getText().matches("\\d")) {
				this.appendCmd(Constants.CMD_OPT_NAME_SELECTTRACES, b);
				this.appendStr(this.traces.getText(), b);
			}
			if (!this.from.getText().matches("^-1")) {
				this.appendCmd(Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE,
						b);
				this.from.getText();
			}
			if (!this.to.getText().matches("^-1")) {
				this.appendCmd(
						Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE,
						b);
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
			TraceAnalysisTool.main(b.toArray(new String[]{}));
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

	private void appendCmd(final String cmdName, final Vector<String> b) {
		b.add("--" + cmdName);
	}

	private void appendStr(final String str, final Vector<String> b) {
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
