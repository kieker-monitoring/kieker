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
package kieker.tools;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.IPlugin;
import kieker.analysis.plugin.IPlugin.PluginInputPortReference;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.w3c.dom.Document;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

/**
 * A simple visualization of Analysis Configurations.
 * 
 * @author Jan Waller
 */
public final class KaxViz extends JFrame {
	private static final long serialVersionUID = 1969467089938687452L;
	private static final Log LOG = LogFactory.getLog(KaxViz.class);

	private static final int FILTER_HEIGHT = 80;
	private static final int FILTER_WIDTH = 200;
	private static final int FILTER_SPACE = 30;

	private transient final AnalysisController analysisController;
	transient final mxGraph graph; // package visible for inner class

	public KaxViz(final String filename, final AnalysisController analysisController, final String outFilename) {
		super(analysisController.getProjectName() + " (" + filename + ((null != outFilename) ? " -> " + outFilename : "") + ")");
		this.analysisController = analysisController;

		// // Menu
		// final JMenuBar menuBar = new JMenuBar();
		// this.setJMenuBar(menuBar);
		// final JMenu layoutMenu = new JMenu("Layout");
		// menuBar.add(layoutMenu);
		// final JMenuItem autoLayout = new JMenuItem("Auto Layout");
		// autoLayout.addActionListener(new ActionListener() {
		//
		// public final void actionPerformed(final ActionEvent arg0) {
		// KaxViz.this.autoGraphLayout();
		// }
		// });
		// layoutMenu.add(autoLayout);

		if (null != outFilename) {
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(final WindowEvent e) {
					final Document doc = mxCellRenderer.createSvgDocument(KaxViz.this.graph, null, 1d, Color.WHITE, KaxViz.this.graph.getGraphBounds());
					try {
						mxUtils.writeFile(mxXmlUtils.getXml(doc), outFilename);
					} catch (final IOException ex) {
						KaxViz.LOG.error("Failed to save Visualization of kax-File.", ex);
					}
				}
			});
		}

		// setup basic graph
		final mxGraph graph = new mxGraph();
		// graph.setGridEnabled(true); // true
		// graph.setGridSize(10); // 10
		// graph.setDefaultOverlap(0.5); // 0.5
		// graph.setDefaultParent(null); // null
		// graph.setAlternateEdgeStyle(null); // null
		// graph.setEnabled(true); // true
		graph.setCellsLocked(false); // true
		graph.setCellsEditable(false); // true
		graph.setCellsResizable(false); // true
		// graph.setCellsMovable(true); // true
		// graph.setCellsBendable(true); // true
		// graph.setCellsSelectable(true); // true
		graph.setCellsDeletable(false); // true
		graph.setCellsCloneable(false); // true
		graph.setCellsDisconnectable(false); // true
		// graph.setLabelsClipped(false); // false
		// graph.setEdgeLabelsMovable(true); // true
		// graph.setVertexLabelsMovable(false); // false
		graph.setDropEnabled(false); // true
		graph.setSplitEnabled(false); // true
		// graph.setAutoSizeCells(false); // false
		// graph.setMaximumGraphBounds(null); // null
		// graph.setMinimumGraphSize(null); // null
		// graph.setBorder(0); // 0
		graph.setKeepEdgesInForeground(true); // false
		graph.setCollapseToPreferredSize(false); // true
		graph.setAllowNegativeCoordinates(false); // true
		// graph.setConstrainChildren(true); // true
		graph.setExtendParents(false); // true
		graph.setExtendParentsOnAdd(false); // true
		// graph.setResetViewOnRootChange(true); // true
		// graph.setResetEdgesOnResize(false); // false
		// graph.setResetEdgesOnMove(false); // false
		graph.setResetEdgesOnConnect(false); // true
		// graph.setAllowLoops(false); // false
		// graph.setMultiplicities(null); // null
		// graph.setDefaultLoopStyle(mxEdgeStyle.Loop); // mxEdgeStyle.Loop
		// graph.setMultigraph(true); // true
		// graph.setConnectableEdges(false); // false
		// graph.setAllowDanglingEdges(false); // false
		// graph.setCloneInvalidEdges(false); // false
		graph.setDisconnectOnMove(false); // true
		// graph.setLabelsVisible(true); // true
		// graph.setHtmlLabels(false); // false
		// graph.setSwimlaneNesting(true); // true
		// graph.setChangesRepaintThreshold(1000); // 1000
		// graph.setAutoOrigin(false); // false
		// graph.setEventsEnabled(true); // true

		// setup the graphComponent
		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setConnectable(false); // Inhibit edge creation in the graph.
		graphComponent.setGridVisible(true); // Show the grid
		graphComponent.setFoldingEnabled(false); // prevent folding of vertexes
		new mxRubberband(graphComponent); // add rubberband selection
		this.getContentPane().add(graphComponent);
		this.graph = graph;

		// add the actual graph
		this.displayGraph();
	}

	private void displayGraph() {
		final mxGraph graph = this.graph;
		final Map<IPlugin, mxCell> mapPlugin2Graph = new HashMap<IPlugin, mxCell>();
		final Map<IPlugin, Map<String, mxCell>> mapPluginInputPorts2Graph = new HashMap<IPlugin, Map<String, mxCell>>();
		final Map<IPlugin, Map<String, mxCell>> mapPluginOutputPorts2Graph = new HashMap<IPlugin, Map<String, mxCell>>();
		final Map<AbstractRepository, mxCell> mapRepository2Graph = new HashMap<AbstractRepository, mxCell>();
		// draw the graph
		graph.getModel().beginUpdate();
		try {
			int x = 0;
			// step 1: add all plugins!
			for (final AbstractReaderPlugin reader : this.analysisController.getReaders()) {
				final mxCell vertex = this.createReader(reader, x++);
				mapPlugin2Graph.put(reader, vertex);
				mapPluginOutputPorts2Graph.put(reader, this.createOutputPorts(reader, vertex));
			}
			for (final AbstractFilterPlugin filter : this.analysisController.getFilters()) {
				final mxCell vertex = this.createFilter(filter, x++);
				mapPlugin2Graph.put(filter, vertex);
				mapPluginInputPorts2Graph.put(filter, this.createInputPorts(filter, vertex));
				mapPluginOutputPorts2Graph.put(filter, this.createOutputPorts(filter, vertex));
			}
			for (final AbstractRepository repo : this.analysisController.getRepositories()) {
				final mxCell cell = this.createRepository(repo, x++);
				mapRepository2Graph.put(repo, cell);
			}
			// step 2: connect all plugins!
			final Collection<IPlugin> allPlugins = new LinkedList<IPlugin>();
			allPlugins.addAll(this.analysisController.getReaders());
			allPlugins.addAll(this.analysisController.getFilters());
			for (final IPlugin outputPlugin : allPlugins) {
				final Map<String, mxCell> mapOutputPorts2Graph = mapPluginOutputPorts2Graph.get(outputPlugin);
				for (final String outputPortName : outputPlugin.getAllOutputPortNames()) {
					for (final PluginInputPortReference inputPortReference : outputPlugin.getConnectedPlugins(outputPortName)) {
						final mxCell outputPluginCell = mapPlugin2Graph.get(outputPlugin);
						final mxCell outputPortCell = mapOutputPorts2Graph.get(outputPortName);
						final IPlugin inputPlugin = inputPortReference.getPlugin();
						final mxCell inputPluginCell = mapPlugin2Graph.get(inputPlugin);
						final String inputPortName = inputPortReference.getInputPortName();
						final mxCell inputPortCell = mapPluginInputPorts2Graph.get(inputPlugin).get(inputPortName);
						inputPortCell.setStyle("noLabel=0;spacingTop=3;verticalLabelPosition=bottom;portConstraint=north");
						outputPortCell.setStyle("noLabel=0;verticalLabelPosition=top;portConstraint=south");
						final mxCell edge = (mxCell) graph.insertEdge(null, null, "", outputPluginCell, inputPluginCell, "edgeStyle=orthogonalEdgeStyle");
						edge.setSource(outputPortCell);
						edge.setTarget(inputPortCell);
					}
				}
				for (final Entry<String, AbstractRepository> repository : outputPlugin.getCurrentRepositories().entrySet()) {
					final mxCell output = mapPlugin2Graph.get(outputPlugin);
					final mxCell input = mapRepository2Graph.get(repository.getValue());
					graph.insertEdge(null, null, repository.getKey(), output, input, "edgeStyle=orthogonalEdgeStyle");
				}
			}
			// step 3: auto layout!
			// this.autoGraphLayout();
		} finally {
			// finish the drawing
			graph.getModel().endUpdate();
		}
	}

	// final void autoGraphLayout() {
	// final mxGraph graph = this.graph;
	// new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
	// }

	private final Map<String, mxCell> createInputPorts(final AbstractPlugin plugin, final mxCell vertex) {
		final Map<String, mxCell> port2graph = new HashMap<String, mxCell>();
		final String[] portNames = plugin.getAllInputPortNames();
		for (int i = 0; i < portNames.length; i++) {
			final mxGeometry portGeometry = new mxGeometry((i + 1d) / (portNames.length + 1), 0, 10, 10);
			portGeometry.setOffset(new mxPoint(0, 0));
			portGeometry.setRelative(true);
			final mxCell port = new mxCell(portNames[i], portGeometry, "noLabel=1;spacingTop=3;verticalLabelPosition=bottom;portConstraint=north");
			port.setVertex(true);
			this.graph.addCell(port, vertex);
			port2graph.put(portNames[i], port);
		}
		return port2graph;
	}

	private final Map<String, mxCell> createOutputPorts(final AbstractPlugin plugin, final mxCell vertex) {
		final Map<String, mxCell> port2graph = new HashMap<String, mxCell>();
		final String[] portNames = plugin.getAllOutputPortNames();
		for (int i = 0; i < portNames.length; i++) {
			final mxGeometry portGeometry = new mxGeometry((i + 1d) / (portNames.length + 1), 1, 10, 10);
			portGeometry.setOffset(new mxPoint(0, -10));
			portGeometry.setRelative(true);
			final mxCell port = new mxCell(portNames[i], portGeometry, "noLabel=1;verticalLabelPosition=top;portConstraint=south");
			port.setVertex(true);
			this.graph.addCell(port, vertex);
			port2graph.put(portNames[i], port);
		}
		return port2graph;
	}

	private final mxCell createReader(final AbstractReaderPlugin plugin, final int c) {
		final mxCell vertex = new mxCell("<<Reader>>\n" + plugin.getName() + " : " + plugin.getPluginName(),
				new mxGeometry(KaxViz.FILTER_SPACE, KaxViz.FILTER_SPACE + (c * (KaxViz.FILTER_HEIGHT + KaxViz.FILTER_SPACE)),
						KaxViz.FILTER_WIDTH, KaxViz.FILTER_HEIGHT), null);
		vertex.setVertex(true);
		this.graph.addCell(vertex);
		return vertex;
	}

	private final mxCell createFilter(final AbstractFilterPlugin plugin, final int c) {
		final mxCell vertex = new mxCell("<<Filter>>\n" + plugin.getName() + " : " + plugin.getPluginName(),
				new mxGeometry(KaxViz.FILTER_SPACE, KaxViz.FILTER_SPACE + (c * (KaxViz.FILTER_HEIGHT + KaxViz.FILTER_SPACE)),
						KaxViz.FILTER_WIDTH, KaxViz.FILTER_HEIGHT), null);
		vertex.setVertex(true);
		this.graph.addCell(vertex);
		return vertex;
	}

	private final mxCell createRepository(final AbstractRepository repository, final int c) {
		final mxCell vertex = new mxCell("<<Repository>>\n : " + repository.getClass().getSimpleName(),
				new mxGeometry(KaxViz.FILTER_SPACE, KaxViz.FILTER_SPACE + (c * (KaxViz.FILTER_HEIGHT + KaxViz.FILTER_SPACE)),
						KaxViz.FILTER_WIDTH, KaxViz.FILTER_HEIGHT), "rounded=1");
		vertex.setVertex(true);
		this.graph.addCell(vertex);
		return vertex;
	}

	/**
	 * Starts the Visualization of a .kax file.
	 * 
	 * @param args
	 *            the name of the .kax file
	 */
	public final static void main(final String[] args) {
		// create cmdline options
		final Options options = new Options();
		final Option inputOption = new Option("i", "input", true, "the analysis project file (.kax) loaded");
		inputOption.setRequired(true);
		inputOption.setArgName("filename");
		options.addOption(inputOption);
		final Option outputoption = new Option("svg", true, "name of svg saved on close");
		outputoption.setArgName("filename");
		options.addOption(outputoption);

		// parse cmdline options
		final String kaxFilename;
		final String svgFilename;
		try {
			final CommandLineParser parser = new BasicParser();
			final CommandLine line = parser.parse(options, args);
			kaxFilename = line.getOptionValue('i');
			svgFilename = line.getOptionValue("svg");
		} catch (final ParseException ex) {
			System.out.println(ex.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(KaxViz.class.getName(), options, true);
			return;
		}

		// start tool
		try {
			final KaxViz frame = new KaxViz(kaxFilename, new AnalysisController(new File(kaxFilename)), svgFilename);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setSize(800, 600);
			frame.setVisible(true);
		} catch (final Exception ex) {
			KaxViz.LOG.error("Error", ex);
		}
	}
}
