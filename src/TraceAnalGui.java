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

public class TraceAnalGui extends JFrame implements ActionListener {

	private final JFrame frame;
	private final JButton remB, addB, run;
	final JList l;
	final DefaultListModel lm = new DefaultListModel();
	private final JTextField outdir;
	private final JTextField prefix;
	private final JCheckBox plotSq = new JCheckBox(),
			plotComp = new JCheckBox(), plotCont = new JCheckBox(),
			plotOp = new JCheckBox(), plotAgg = new JCheckBox(),
			plotCall = new JCheckBox(), printCall = new JCheckBox(),
			printMsg = new JCheckBox(), printExe = new JCheckBox(),
			printInv = new JCheckBox(), printEquiv = new JCheckBox();
	private GridBagLayout g;
	private final JTextField traces = new JTextField(36);

	private final JRadioButton disabled = new JRadioButton(),
			allocation = new JRadioButton(), assembly = new JRadioButton();
	private final ButtonGroup bg = new ButtonGroup();
	private final JTextField from = new JTextField("-1", 8),
			to = new JTextField("-1", 8), before = new JTextField("-1", 8);

	/*
	 * [--plot-Component-Dependency-Graph] [--plot-Container-Dependency-Graph]
	 * [--plot-Operation-Dependency-Graph] [--plot-Aggregated-Call-Tree]
	 * [--plot-Call-Trees] [--print-Message-Traces] [--print-Execution-Traces]
	 * [--print-invalid-Execution-Traces] [--print-Equivalence-Classes]
	 */
	public TraceAnalGui() {
		this.frame = this;
		this.setResizable(false);
		final JPanel p = (JPanel) this.getContentPane();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.setLayout(new GridBagLayout());
		final JLabel label = new JLabel("Input:");

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
			sp
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
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
				final int ret = fc.showOpenDialog(TraceAnalGui.this.frame);
				if (JFileChooser.APPROVE_OPTION == ret) {
					TraceAnalGui.this.outdir.setText(fc.getSelectedFile()
							.getAbsolutePath().toString());
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

		gc.anchor = GridBagConstraints.WEST;
		{

			p.add(new JLabel("Operations:"), gc);
			gc.gridx = 0;
			gc.gridwidth = 4;
			gc.weightx = 0;
			p.add(new JLabel("Plot Sequence Diagrams"), gc);
			gc.gridx = 4;
			gc.gridwidth = 2;
			p.add(this.plotSq, gc);
			gc.gridwidth = 6;
			gc.gridx = 0;
			p.add(this.plotDiags(), gc);
			p.add(this.treePanel(), gc);
			p.add(this.tracePanel(), gc);
			p.add(this.equivSelect(), gc);
			p.add(this.ignorePanel(), gc);
		}

		this.run = new JButton();
		this.run.setText("Run");
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = gc.weighty = 1;
		gc.gridx = 7;
		gc.gridheight = 17;
		p.add(this.run, gc);
		/*
		 * usage: [--plot-Sequence-Diagrams] [--plot-Component-Dependency-Graph]
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
		this.from.setToolTipText("[yyyyMMdd-HHmmss] or -1 to disable filter");
		this.to.setToolTipText("[yyyyMMdd-HHmmss] or -1 to disable filter");
		g.gridheight = 1;
		ign.add(this.from, g);
		ign.add(new JLabel("and"), g);
		ign.add(this.to, g);

		this.before.setToolTipText("in ms, -1 to disable filter");
		ign.add(new JLabel("Max. trace duration"));
		ign.add(this.before);
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
		return BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final TraceAnalGui frame = new TraceAnalGui();
	}

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
		}
	}

}
