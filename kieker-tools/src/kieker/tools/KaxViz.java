/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.emf.common.util.EList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

import kieker.analysis.AnalysisController;
import kieker.analysis.model.analysisMetaModel.MIAnalysisComponent;
import kieker.analysis.model.analysisMetaModel.MIFilter;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIPort;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIReader;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIRepositoryConnector;

/**
 * A simple visualization of analysis configurations.
 *
 * @author Jan Waller, Nils Christian Ehmke
 *
 * @since 1.5
 */
public final class KaxViz extends AbstractCommandLineTool {

	static final Logger LOGGER = LoggerFactory.getLogger(KaxViz.class); // NOPMD package for inner class

	private String kaxFilename;
	private String svgFilename;

	private KaxViz() {
		super(true);
	}

	/**
	 * Starts the visualization of a .kax file.
	 *
	 * @param args
	 *            The command line arguments (including the name of the .kax file in question).
	 */
	public static final void main(final String[] args) {
		new KaxViz().start(args);
	}

	@Override
	protected void addAdditionalOptions(final Options options) {
		final Option inputOption = new Option("i", "input", true, "the analysis project file (.kax) loaded");
		final Option outputoption = new Option("svg", true, "name of svg saved on close");

		inputOption.setArgName("filename");
		outputoption.setArgName("filename");

		options.addOption(inputOption);
		options.addOption(outputoption);
	}

	@Override
	protected boolean readPropertiesFromCommandLine(final CommandLine commandLine) {
		this.kaxFilename = commandLine.getOptionValue('i');
		this.svgFilename = commandLine.getOptionValue("svg");

		return this.assertInputFileExists();
	}

	private boolean assertInputFileExists() {
		if (this.kaxFilename == null) {
			LOGGER.error("No input file configured");
			return false;
		}

		return true;
	}

	@Override
	protected boolean performTask() {
		try {
			final KaxVizFrame frame = new KaxVizFrame(this.kaxFilename, AnalysisController.loadFromFile(new File(this.kaxFilename)), this.svgFilename);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setSize(800, 600);
			frame.setVisible(true);
		} catch (final IOException ex) {
			LOGGER.error("The given file could not be loaded", ex);
			return false;
		} catch (final Exception ex) { // NOPMD NOCS (log all errors)
			LOGGER.error("Error", ex);
			return false;
		}
		return true;
	}

	private static class KaxVizFrame extends JFrame {
		private static final long serialVersionUID = 1969467089938687452L;

		private static final int FILTER_HEIGHT = 80;
		private static final int FILTER_WIDTH = 200;
		private static final int FILTER_SPACE = 30;

		private static final String STYLE_READER_COLOR = mxConstants.STYLE_FILLCOLOR + "=#A3C592;";
		private static final String STYLE_FILTER_COLOR = mxConstants.STYLE_FILLCOLOR + "=#C3D9FF;";
		private static final String STYLE_REPOSITORY_COLOR = mxConstants.STYLE_FILLCOLOR + "=#EAE385;";

		private static final String STYLE_READER = STYLE_READER_COLOR
				+ mxConstants.STYLE_STROKECOLOR + "=#000000;"
				+ mxConstants.STYLE_FONTCOLOR + "=#000000;";

		private static final String STYLE_FILTER = STYLE_FILTER_COLOR
				+ mxConstants.STYLE_STROKECOLOR + "=#000000;"
				+ mxConstants.STYLE_FONTCOLOR + "=#000000;";

		private static final String STYLE_REPOSITORY = STYLE_REPOSITORY_COLOR
				+ mxConstants.STYLE_ROUNDED + "=1;"
				+ mxConstants.STYLE_STROKECOLOR + "=#000000;"
				+ mxConstants.STYLE_FONTCOLOR + "=#000000;";

		private static final String STYLE_PORT = mxConstants.STYLE_NOLABEL + "=1;"
				+ mxConstants.STYLE_STROKECOLOR + "=#000000;"
				+ mxConstants.STYLE_FONTCOLOR + "=#000000;"
				+ mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_ITALIC + ";";

		private static final String STYLE_CONNECTION = mxConstants.STYLE_EDGE + "=orthogonalEdgeStyle;";

		private static final String STYLE_CONNECTION_REPOSITORY = mxConstants.STYLE_EDGE + "=orthogonalEdgeStyle;"
				+ mxConstants.STYLE_DASHED + "=1;"
				+ mxConstants.STYLE_FONTCOLOR + "=#000000;"
				+ mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_ITALIC + ";";

		final transient mxGraph graph; // NOPMD NOCS (package visible for inner class)

		private final transient MIProject mProject;

		public KaxVizFrame(final String filename, final MIProject mProject, final String outFilename) {
			super(((mProject.getName() != null) ? (mProject.getName() + " ") : "") + "(" + filename + ((null != outFilename) ? " -> " + outFilename : "") + ")"); // NOCS
			this.mProject = mProject;

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
						final Document doc = mxCellRenderer.createSvgDocument(KaxVizFrame.this.graph, null, 1d, Color.WHITE,
								KaxVizFrame.this.graph.getGraphBounds());
						try {
							mxUtils.writeFile(mxXmlUtils.getXml(doc), outFilename);
						} catch (final IOException ex) {
							LOGGER.error("Failed to save Visualization of kax-File.", ex);
						}
					}
				});
			}

			// setup basic graph
			this.graph = new mxGraph();
			// graph.setGridEnabled(true); // true
			// graph.setGridSize(10); // 10
			// graph.setDefaultOverlap(0.5); // 0.5
			// graph.setDefaultParent(null); // null
			// graph.setAlternateEdgeStyle(null); // null
			// graph.setEnabled(true); // true
			this.graph.setCellsLocked(false); // true
			this.graph.setCellsEditable(false); // true
			this.graph.setCellsResizable(false); // true
			// graph.setCellsMovable(true); // true
			// graph.setCellsBendable(true); // true
			// graph.setCellsSelectable(true); // true
			this.graph.setCellsDeletable(false); // true
			this.graph.setCellsCloneable(false); // true
			this.graph.setCellsDisconnectable(false); // true
			// graph.setLabelsClipped(false); // false
			// graph.setEdgeLabelsMovable(true); // true
			// graph.setVertexLabelsMovable(false); // false
			this.graph.setDropEnabled(false); // true
			this.graph.setSplitEnabled(false); // true
			// graph.setAutoSizeCells(false); // false
			// graph.setMaximumGraphBounds(null); // null
			// graph.setMinimumGraphSize(null); // null
			// graph.setBorder(0); // 0
			this.graph.setKeepEdgesInForeground(true); // false
			this.graph.setCollapseToPreferredSize(false); // true
			this.graph.setAllowNegativeCoordinates(false); // true
			// graph.setConstrainChildren(true); // true
			this.graph.setExtendParents(false); // true
			this.graph.setExtendParentsOnAdd(false); // true
			// graph.setResetViewOnRootChange(true); // true
			// graph.setResetEdgesOnResize(false); // false
			// graph.setResetEdgesOnMove(true); // false
			this.graph.setResetEdgesOnConnect(false); // true
			// graph.setAllowLoops(false); // false
			// graph.setMultiplicities(null); // null
			// graph.setDefaultLoopStyle(mxEdgeStyle.Loop); // mxEdgeStyle.Loop
			// graph.setMultigraph(true); // true
			// graph.setConnectableEdges(false); // false
			this.graph.setAllowDanglingEdges(false); // false
			// graph.setCloneInvalidEdges(false); // false
			this.graph.setDisconnectOnMove(false); // true
			// graph.setLabelsVisible(true); // true
			// graph.setHtmlLabels(false); // false
			// graph.setSwimlaneNesting(true); // true
			// graph.setChangesRepaintThreshold(1000); // 1000
			// graph.setAutoOrigin(false); // false
			// graph.setEventsEnabled(true); // true

			// setup the graphComponent
			final mxGraphComponent graphComponent = new mxGraphComponent(this.graph);
			graphComponent.setConnectable(false); // Inhibit edge creation in the graph.
			graphComponent.setGridVisible(true); // Show the grid
			graphComponent.setFoldingEnabled(false); // prevent folding of vertexes
			new com.mxgraph.swing.handler.mxRubberband(graphComponent); // add rubberband selection
			this.getContentPane().add(graphComponent);

			// add the actual graph
			this.displayGraph();

			// repaint fix
			this.graph.addListener(mxEvent.CELLS_MOVED, new mxIEventListener() {
				@Override
				public void invoke(final Object sender, final mxEventObject evt) {
					KaxVizFrame.this.graph.repaint();
				}
			});
		}

		private void displayGraph() {
			final Map<MIPlugin, mxCell> mapPlugin2Graph = new HashMap<>(); // NOPMD (no concurrent access)
			final Map<MIPlugin, Map<String, mxCell>> mapPluginInputPorts2Graph = new HashMap<>(); // NOPMD (no concurrent access)
			final Map<MIPlugin, Map<String, mxCell>> mapPluginOutputPorts2Graph = new HashMap<>(); // NOPMD (no concurrent access)
			final Map<MIRepository, mxCell> mapRepository2Graph = new HashMap<>(); // NOPMD (no concurrent access)
			// draw the graph
			this.graph.getModel().beginUpdate();
			try {
				int x = 0;
				// step 1: add all plugins!
				for (final MIPlugin plugin : this.mProject.getPlugins()) {
					if (plugin instanceof MIReader) {
						final MIReader reader = (MIReader) plugin;
						final mxCell vertex = this.createReader(reader, x++);
						mapPlugin2Graph.put(reader, vertex);
						mapPluginOutputPorts2Graph.put(reader, this.createOutputPorts(reader, vertex, true));
					} else if (plugin instanceof MIFilter) {
						final MIFilter filter = (MIFilter) plugin;
						final mxCell vertex = this.createFilter(filter, x++);
						mapPlugin2Graph.put(filter, vertex);
						mapPluginInputPorts2Graph.put(filter, this.createInputPorts(filter, vertex));
						mapPluginOutputPorts2Graph.put(filter, this.createOutputPorts(filter, vertex, false));
					}
				}
				for (final MIRepository repo : this.mProject.getRepositories()) {
					final mxCell cell = this.createRepository(repo, x++);
					mapRepository2Graph.put(repo, cell);
				}
				// step 2: connect all plugins!
				for (final MIPlugin outputPlugin : this.mProject.getPlugins()) {
					final Map<String, mxCell> mapOutputPorts2Graph = mapPluginOutputPorts2Graph.get(outputPlugin); // NOPMD (no concurrent access)
					for (final MIOutputPort outputPort : outputPlugin.getOutputPorts()) {
						for (final MIInputPort inputPort : outputPort.getSubscribers()) {
							final mxCell outputPluginCell = mapPlugin2Graph.get(outputPlugin);
							final mxCell outputPortCell = mapOutputPorts2Graph.get(outputPort.getName());
							final MIPlugin inputPlugin = inputPort.getParent();
							final mxCell inputPluginCell = mapPlugin2Graph.get(inputPlugin);
							final String inputPortName = inputPort.getName();
							final mxCell inputPortCell = mapPluginInputPorts2Graph.get(inputPlugin).get(inputPortName);
							this.graph.setCellStyles(mxConstants.STYLE_NOLABEL, "0", new Object[] { inputPortCell, outputPortCell });

							final mxCell edge = (mxCell) this.graph.insertEdge(null, null, "", outputPluginCell, inputPluginCell, STYLE_CONNECTION);
							edge.setSource(outputPortCell);
							edge.setTarget(inputPortCell);
						}
					}
					for (final MIRepositoryConnector repositoryConnector : outputPlugin.getRepositories()) {
						if (repositoryConnector.getRepository() != null) {
							final MIRepository repository = repositoryConnector.getRepository();
							final mxCell output = mapPlugin2Graph.get(outputPlugin);
							final mxCell input = mapRepository2Graph.get(repository);
							this.graph.insertEdge(null, null, repository.getName(), output, input, STYLE_CONNECTION_REPOSITORY);
						}
					}
				}
				// step 3: auto layout!
				// this.autoGraphLayout();
			} finally {
				// finish the drawing
				this.graph.getModel().endUpdate();
			}
		}

		// final void autoGraphLayout() {
		// new mxOrthogonalLayout(this.graph).execute(this.graph.getDefaultParent());
		// }

		private final Map<String, mxCell> createInputPorts(final MIFilter plugin, final mxCell vertex) {
			final Map<String, mxCell> port2graph = new HashMap<>(); // NOPMD (no concurrent access)
			final String[] portNames = KaxVizFrame.getAllInputPortNames(plugin);
			for (int i = 0; i < portNames.length; i++) {
				final mxGeometry portGeometry = new mxGeometry((i + 1d) / (portNames.length + 1), -0.06, 10, 10);
				portGeometry.setOffset(new mxPoint(0, 0));
				portGeometry.setRelative(true);
				final mxCell port = new mxCell(
						portNames[i],
						portGeometry,
						STYLE_PORT + "spacingTop=3;verticalLabelPosition=bottom;portConstraint=north;" + STYLE_FILTER_COLOR);
				port.setVertex(true);
				this.graph.addCell(port, vertex);
				port2graph.put(portNames[i], port);
			}
			return port2graph;
		}

		private static String[] convertPortsToNameArray(final EList<? extends MIPort> eList) {
			final int len = eList.size();
			final String[] result = new String[len];

			for (int idx = 0; idx < len; idx++) {
				result[idx] = eList.get(idx).getName();
			}

			return result;
		}

		private static String[] getAllInputPortNames(final MIFilter plugin) {
			return KaxVizFrame.convertPortsToNameArray(plugin.getInputPorts());
		}

		private static String[] getAllOutputPortNames(final MIPlugin plugin) {
			return KaxVizFrame.convertPortsToNameArray(plugin.getOutputPorts());
		}

		private final Map<String, mxCell> createOutputPorts(final MIPlugin plugin, final mxCell vertex, final boolean reader) {
			final Map<String, mxCell> port2graph = new HashMap<>(); // NOPMD (no concurrent access)
			final String[] portNames = KaxVizFrame.getAllOutputPortNames(plugin);
			for (int i = 0; i < portNames.length; i++) {
				final mxGeometry portGeometry = new mxGeometry((i + 1d) / (portNames.length + 1), 1.06, 10, 10);
				portGeometry.setOffset(new mxPoint(0, -10));
				portGeometry.setRelative(true);
				final String fillcolor = reader ? STYLE_READER_COLOR : STYLE_FILTER_COLOR; // NOPMD NOCS (?:)
				final mxCell port = new mxCell(portNames[i], portGeometry,
						STYLE_PORT + "verticalLabelPosition=top;portConstraint=south;" + fillcolor);
				port.setVertex(true);
				this.graph.addCell(port, vertex);
				port2graph.put(portNames[i], port);
			}
			return port2graph;
		}

		private static String getShortClassName(final MIAnalysisComponent analysisComponent) {
			final String className = analysisComponent.getClassname();
			final int lastPointPos = className.lastIndexOf('.');

			return className.substring(lastPointPos + 1);
		}

		private final mxCell createReader(final MIReader reader, final int c) {
			final mxCell vertex = new mxCell("<<Reader>>\n" + reader.getName() + " : " + KaxVizFrame.getShortClassName(reader),
					new mxGeometry(FILTER_SPACE, FILTER_SPACE + (c * (FILTER_HEIGHT + FILTER_SPACE)),
							FILTER_WIDTH, FILTER_HEIGHT),
					STYLE_READER);
			vertex.setVertex(true);
			this.graph.addCell(vertex);
			return vertex;
		}

		private final mxCell createFilter(final MIFilter plugin, final int c) {
			final mxCell vertex = new mxCell("<<Filter>>\n" + plugin.getName() + " : " + KaxVizFrame.getShortClassName(plugin),
					new mxGeometry(FILTER_SPACE, FILTER_SPACE + (c * (FILTER_HEIGHT + FILTER_SPACE)),
							FILTER_WIDTH, FILTER_HEIGHT),
					STYLE_FILTER);
			vertex.setVertex(true);
			this.graph.addCell(vertex);
			return vertex;
		}

		private final mxCell createRepository(final MIRepository repository, final int c) {
			final mxCell vertex = new mxCell("<<Repository>>\n" + repository.getName() + " : " + KaxVizFrame.getShortClassName(repository),
					new mxGeometry(FILTER_SPACE, FILTER_SPACE + (c * (FILTER_HEIGHT + FILTER_SPACE)),
							FILTER_WIDTH, FILTER_HEIGHT),
					STYLE_REPOSITORY);
			vertex.setVertex(true);
			this.graph.addCell(vertex);
			return vertex;
		}

	}

}
