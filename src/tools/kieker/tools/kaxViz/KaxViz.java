package kieker.tools.kaxViz;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.PluginInputPortReference;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public final class KaxViz extends JFrame {
	private static final long serialVersionUID = 1969467089938687452L;
	private static final Log LOG = LogFactory.getLog(KaxViz.class);
	private final AnalysisController analysisController;
	private final mxGraph graph;

	public KaxViz(final AnalysisController analysisController) {
		super(analysisController.getProjectName());
		this.analysisController = analysisController;
		final JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		final JMenu layoutMenu = new JMenu("Layout");
		menuBar.add(layoutMenu);
		final JMenuItem autoLayout = new JMenuItem("Auto Layout");
		autoLayout.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent arg0) {
				KaxViz.this.autoGraphLayout();
			}
		});
		layoutMenu.add(autoLayout);
		// setup basic graph
		final mxGraph graph = new mxGraph();
		graph.setAllowDanglingEdges(false);
		graph.setAllowLoops(false);
		graph.setCellsEditable(false);
		graph.setCellsResizable(false);
		graph.setDisconnectOnMove(false);
		graph.setGridEnabled(true);
		graph.setGridSize(10);
		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setConnectable(false); // Inhibit edge creation in the graph.
		this.getContentPane().add(graphComponent);
		this.graph = graph;
		// add the actual graph
		this.displayGraph();
	}

	private void displayGraph() {
		final mxGraph graph = this.graph;
		final Map<AbstractPlugin, mxCell> mapPlugin2Graph = new HashMap<AbstractPlugin, mxCell>();
		final Map<AbstractRepository, mxCell> mapRepository2Graph = new HashMap<AbstractRepository, mxCell>();
		graph.getModel().beginUpdate();
		try {
			// step 1: add all plugins!
			for (final AbstractReaderPlugin reader : this.analysisController.getReaders()) {
				final mxCell cell = this.createReader(reader);
				mapPlugin2Graph.put(reader, cell);
				graph.addCell(cell);
			}
			for (final AbstractAnalysisPlugin filter : this.analysisController.getFilters()) {
				final mxCell cell = this.createFilter(filter);
				mapPlugin2Graph.put(filter, cell);
				graph.addCell(cell);
			}
			for (final AbstractRepository repo : this.analysisController.getRepositories()) {
				final mxCell cell = this.createRepository(repo);
				mapRepository2Graph.put(repo, cell);
				graph.addCell(cell);
			}
			// step 2: connect all plugins!
			for (final AbstractReaderPlugin reader : this.analysisController.getReaders()) {
				final mxCell output = mapPlugin2Graph.get(reader);
				for (final String outputPort : reader.getAllOutputPortNames()) {
					for (final PluginInputPortReference inputPortReference : reader.getConnectedPlugins(outputPort)) {
						final mxCell input = mapPlugin2Graph.get(inputPortReference.getPlugin());
						final String description = "[" + outputPort + "] -> [" + inputPortReference.getInputPortName() + "]";
						graph.insertEdge(null, null, description, output, input);
					}
				}
				for (final Entry<String, AbstractRepository> repository : reader.getCurrentRepositories().entrySet()) {
					final mxCell input = mapRepository2Graph.get(repository.getValue());
					graph.insertEdge(null, null, repository.getKey(), output, input);
				}
			}
			for (final AbstractAnalysisPlugin filter : this.analysisController.getFilters()) {
				final mxCell output = mapPlugin2Graph.get(filter);
				for (final String outputPort : filter.getAllOutputPortNames()) {
					for (final PluginInputPortReference inputPortReference : filter.getConnectedPlugins(outputPort)) {
						final mxCell input = mapPlugin2Graph.get(inputPortReference.getPlugin());
						final String description = "[" + outputPort + "] -> [" + inputPortReference.getInputPortName() + "]";
						graph.insertEdge(null, null, description, output, input);
					}
				}
				for (final Entry<String, AbstractRepository> repository : filter.getCurrentRepositories().entrySet()) {
					final mxCell input = mapRepository2Graph.get(repository.getValue());
					graph.insertEdge(null, null, repository.getKey(), output, input);
				}
			}
			// step 3: auto layout!
			this.autoGraphLayout();
		} finally {
			graph.getModel().endUpdate();
		}
	}

	final void autoGraphLayout() {
		final mxGraph graph = this.graph;
		new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
	}

	private final mxCell createReader(final AbstractReaderPlugin plugin) {
		final String description = "<<Reader>>\n" + plugin.getName() + " : " + plugin.getClass().getSimpleName();
		final mxGeometry geometry = new mxGeometry(0, 0, 200, 40);
		final mxCell vertex = new mxCell(description, geometry, null);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}

	private final mxCell createFilter(final AbstractAnalysisPlugin plugin) {
		final String description = "<<Filter>>\n" + plugin.getName() + " : " + plugin.getClass().getSimpleName();
		final mxGeometry geometry = new mxGeometry(0, 0, 200, 40);
		final mxCell vertex = new mxCell(description, geometry, null);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}

	private final mxCell createRepository(final AbstractRepository repository) {
		final String description = "<<Repository>>\n : " + repository.getClass().getSimpleName();
		final mxGeometry geometry = new mxGeometry(0, 0, 200, 40);
		final mxCell vertex = new mxCell(description, geometry, null);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}

	public final static void main(final String[] args) {
		if (args.length == 0) {
			KaxViz.LOG.error("Expecting the file name of a *.kax file as a single argument.");
			System.exit(1);
		}
		try {
			final KaxViz frame = new KaxViz(new AnalysisController(new File(args[0])));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setSize(800, 600);
			frame.setVisible(true);
		} catch (final Exception ex) {
			KaxViz.LOG.error("Error", ex);
		}
	}
}
