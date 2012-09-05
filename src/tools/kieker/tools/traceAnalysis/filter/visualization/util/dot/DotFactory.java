/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.util.dot;

/**
 * This class provides a collection of static methods to compile Graphviz Dot elements
 * from string attributes and properties.
 * These elements may be compiled to complete Dot files externally.
 * 
 * @see <a href="http://www.graphviz.org/">Graphviz - Graph Visualization Software</a>
 * @see <a href="http://de.wikipedia.org/wiki/Graphviz">Graphviz - Wikipedia</a>
 * 
 * @author Nina Marwede
 */
public final class DotFactory {

	public static final String DOT_SHAPE_BOX = "box";
	public static final String DOT_SHAPE_BOX3D = "box3d";
	public static final String DOT_SHAPE_NONE = "none";
	public static final String DOT_SHAPE_OVAL = "oval";
	public static final String DOT_STYLE_SOLID = "solid";
	public static final String DOT_STYLE_DASHED = "dashed";
	public static final String DOT_ARROWHEAD_OPEN = "open";
	public static final double DOT_DEFAULT_FONTSIZE = 0.0;
	public static final String DOT_DOT_RANKDIR_LR = "LR";
	public static final String DOT_FILLCOLOR_WHITE = "white";
	public static final String DOT_FILLCOLOR_GRAY = "gray";
	public static final String DOT_STYLE_FILLED = "filled";

	/**
	 * Private empty constructor to mark this class as not useful to instantiate because is has only static members.
	 */
	private DotFactory() {}

	/**
	 * Constructs dot code for a directed graph (digraph) file header.
	 * (Should be called only once per dot file.)
	 * 
	 * @param name
	 * @param label
	 *            text to show at the label locacion (specified elsewhere) with HTML braces (&lt;&gt;)
	 * @param fontcolor
	 * @param fontname
	 * @param fontsize
	 * @return digraph header as dot code
	 */
	public static StringBuilder createHeader(final String name, final String label, final String fontcolor, final String fontname, final double fontsize) {
		final StringBuilder dot = new StringBuilder("digraph ");
		dot.append(name);
		dot.append(" {\n label=<");
		dot.append(label);
		dot.append(">;\n fontcolor=\"");
		dot.append(fontcolor);
		dot.append("\";\n fontname=\"");
		dot.append(fontname);
		dot.append("\";\n fontsize=\"");
		dot.append(Double.toString(fontsize));
		dot.append("\";\n");
		return dot;
	}

	/**
	 * Constructs dot code for the node defaults.
	 * (Should be called only once per dot file.)
	 * 
	 * @param style
	 * @param shape
	 * @param framecolor
	 * @param fontcolor
	 * @param fontname
	 * @param fontsize
	 * @return graph node defaults as dot code
	 */
	public static StringBuilder createNodeDefaults(final String style, final String shape, final String framecolor, final String fontcolor, final String fontname,
			final double fontsize, final String imagescale) {
		final StringBuilder dot = new StringBuilder(" node [style=\"");
		dot.append(style);
		dot.append("\",shape=\"");
		dot.append(shape);
		dot.append("\",color=\"");
		dot.append(framecolor);
		dot.append("\",fontcolor=\"");
		dot.append(fontcolor);
		dot.append("\",fontname=\"");
		dot.append(fontname);
		dot.append("\",fontsize=\"");
		dot.append(fontsize);
		dot.append("\",imagescale=\"");
		dot.append(imagescale);
		dot.append("\"];\n");
		return dot;
	}

	/**
	 * Constructs dot code for the edge defaults.
	 * (Should be called only once per dot file.)
	 * 
	 * @param style
	 * @param arrowhead
	 * @param labelfontname
	 *            label font nodeId, works only for headlabel and taillabel, not simple label
	 * @return graph edge defaults as dot code
	 */
	public static StringBuilder createEdgeDefaults(final String style, final String arrowhead, final String labelfontname) {
		final StringBuilder dot = new StringBuilder(" edge [style=\"");
		dot.append(style);
		dot.append("\",arrowhead=\"");
		dot.append(arrowhead);
		dot.append("\",labelfontname=\"");
		dot.append(labelfontname);
		dot.append("\"];\n");
		return dot;
	}

	/**
	 * Constructs dot code for a node from the specified elements.
	 * Null values can be used for all parameters except nodeId -- dot will use defaults then.
	 * 
	 * @param prefix
	 *            usually spaces, dependent on hierarchy - only for nice ascii formatting inside the dot code
	 * @param nodeId
	 * @param label
	 * @param shape
	 * @param style
	 * @param framecolor
	 * @param fillcolor
	 * @param fontcolor
	 * @param fontsize
	 * @param imageFilename
	 * @param misc
	 * @return graph node as dot code
	 */
	public static StringBuilder createNode(final String prefix, final String nodeId, final String label, final String shape, final String style,
			final String framecolor, final String fillcolor, final String fontcolor, final double fontsize, final String imageFilename, final String misc) {
		final StringBuilder dot = new StringBuilder();
		if (prefix != null) {
			dot.append(prefix);
		}
		dot.append('"');
		dot.append(nodeId);
		dot.append("\" [");
		if (label != null) {
			dot.append("label=\"");
			dot.append(label);
			dot.append("\",");
		}
		if (shape != null) {
			dot.append("shape=\"");
			dot.append(shape);
			dot.append("\",");
		}
		if (style != null) {
			dot.append("style=\"");
			dot.append(style);
			dot.append("\",");
		}
		if (framecolor != null) {
			dot.append("color=\"");
			dot.append(framecolor);
			dot.append("\",");
		}
		if (fillcolor != null) {
			dot.append("fillcolor=\"");
			dot.append(fillcolor);
			dot.append("\",");
		}
		if (fontcolor != null) {
			dot.append("fontcolor=\"");
			dot.append(fontcolor);
			dot.append("\",");
		}
		if (fontsize != DOT_DEFAULT_FONTSIZE) {
			dot.append("fontsize=\"");
			dot.append(fontsize);
			dot.append("\",");
		}
		if (imageFilename != null) {
			dot.append("image=\"");
			dot.append(imageFilename);
			dot.append('"');
		}
		if (misc != null) {
			dot.append(misc);
		}
		dot.append("]\n");
		return dot;
	}

	/**
	 * Constructs dot code for a cluster from the specified elements.
	 * <strong>ATTENTION: Without closing bracket!</strong>
	 * ( "}" has to be appended by calling method.)
	 * 
	 * @param prefix
	 *            usually spaces, dependent on hierarchy - only for nice ascii formatting inside the dot code
	 * @param name
	 * @param label
	 * @param shape
	 * @param style
	 * @param framecolor
	 * @param fillcolor
	 * @param fontcolor
	 * @param fontsize
	 * @param misc
	 * @return graph cluster as dot code
	 */
	public static StringBuilder createCluster(final String prefix, final String name, final String label, final String shape, final String style,
			final String framecolor, final String fillcolor, final String fontcolor, final double fontsize, final String misc) {
		final StringBuilder dot = new StringBuilder(prefix);
		dot.append("subgraph \"cluster_");
		dot.append(name);
		dot.append("\" {\n");
		dot.append(prefix);
		dot.append(" label = \"");
		dot.append(label);
		if (shape != null) {
			dot.append("\";\n");
			dot.append(prefix);
			dot.append(" shape = \"");
			dot.append(shape);
		}
		if (style != null) {
			dot.append("\";\n");
			dot.append(prefix);
			dot.append(" style = \"");
			dot.append(style);
		}
		if (framecolor != null) {
			dot.append("\";\n");
			dot.append(prefix);
			dot.append(" pencolor = \"");
			dot.append(framecolor);
		}
		if (fillcolor != null) {
			dot.append("\";\n");
			dot.append(prefix);
			dot.append(" fillcolor = \"");
			dot.append(fillcolor);
		}
		if (fontcolor != null) {
			dot.append("\";\n");
			dot.append(prefix);
			dot.append(" fontcolor = \"");
			dot.append(fontcolor);
		}
		if (fontsize != DOT_DEFAULT_FONTSIZE) {
			dot.append("\";\n");
			dot.append(prefix);
			dot.append(" fontsize = \"");
			dot.append(fontsize);
		}
		dot.append("\";");
		if (misc != null) {
			dot.append(misc);
		}
		dot.append("\n");
		// closing bracket "}" has to be added by calling method !
		return dot;
	}

	/**
	 * Creates dot code for a connection.
	 * 
	 * @param prefix
	 *            usually spaces, dependent on hierarchy - only for nice ascii formatting inside the dot code
	 * @param from
	 * @param to
	 * @return graph connection as dot code
	 */
	public static String createConnection(final String prefix, final String from, final String to, final String style, final String arrowhead) {
		final StringBuilder dot = new StringBuilder(prefix);
		dot.append(from);
		dot.append("->");
		dot.append(to);
		if (style != null) {
			dot.append("style=\"");
			dot.append(style);
			dot.append('"');
		}
		if (arrowhead != null) {
			if (style != null) {
				dot.append(',');
			}
			dot.append(" arrowhead=\"");
			dot.append(arrowhead);
			dot.append('"');
		}
		dot.append("]");
		return dot.toString();
	}

	/**
	 * Creates dot code for a connection.
	 * Like (String,String,String), except a label can be added.
	 * 
	 * @param prefix
	 * @param from
	 * @param to
	 * @param label
	 * @return graph connection as dot code
	 */
	public static String createConnection(final String prefix, final String from, final String to, final String label, final String style, final String arrowhead) {
		final StringBuilder dot = new StringBuilder(prefix);
		dot.append(from);
		dot.append("->");
		dot.append(to);
		dot.append("[label=");
		dot.append(label);
		if (style != null) {
			dot.append(", style=\"");
			dot.append(style);
			dot.append('"');
		}
		if (arrowhead != null) {
			if (style != null) {
				dot.append(',');
			}
			dot.append(" arrowhead=\"");
			dot.append(arrowhead);
			dot.append('"');
		}
		dot.append("]");
		return dot.toString();
	}

	/**
	 * Creates dot code for a connection.
	 * Like (String,String,String), except a headlabel and a taillabel can be added.
	 * 
	 * @param prefix
	 * @param from
	 * @param to
	 * @param taillabel
	 * @param headlabel
	 * @return graph connection as dot code
	 */
	public static String createConnection(final String prefix, final String from, final String to, final double taillabel, final double headlabel) {
		final StringBuilder dot = new StringBuilder(prefix);
		dot.append('"');
		dot.append(from);
		dot.append("\" -> \"");
		dot.append(to);
		dot.append("\" [label=\" \",taillabel=\"");
		dot.append(taillabel * 100.0);
		dot.append("(\",headlabel=\"");
		dot.append(headlabel * 100.0);
		dot.append("\"];\n");
		return dot.toString();
	}
}
