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

package kieker.tools.traceAnalysis.plugins.visualization.util.dot;

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
	public static final String DOT_STYLE_DASHED = "dashed";
	public static final String DOT_ARROWHEAD_OPEN = "open";
	public static final double DOT_DEFAULT_FONTSIZE = 0.0;
	public static final String DOT_DOT_RANKDIR_LR = "LR";
	public static final String DOT_FILLCOLOR_WHITE = "white";
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
		final StringBuilder dot = new StringBuilder("digraph " + name);
		dot.append(" {\n label=<" + label);
		dot.append(">;\n fontcolor=\"" + fontcolor);
		dot.append("\";\n fontname=\"" + fontname);
		dot.append("\";\n fontsize=\"" + Double.toString(fontsize));
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
		final StringBuilder dot = new StringBuilder(" node [");
		dot.append("style=\"" + style);
		dot.append("\",shape=\"" + shape);
		dot.append("\",color=\"" + framecolor);
		dot.append("\",fontcolor=\"" + fontcolor);
		dot.append("\",fontname=\"" + fontname);
		dot.append("\",fontsize=\"" + Double.toString(fontsize));
		dot.append("\",imagescale=\"" + imagescale);
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
		final StringBuilder dot = new StringBuilder(" edge [");
		dot.append("style=\"" + style);
		dot.append("\",arrowhead=\"" + arrowhead);
		dot.append("\",labelfontname=\"" + labelfontname);
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
		final StringBuilder dot = new StringBuilder(prefix == null ? "" : prefix); // NOCS
		dot.append("\"" + nodeId + "\" [");
		dot.append(label == null ? "" : "label=\"" + label); // NOCS
		dot.append(shape == null ? "" : "\",shape=\"" + shape); // NOCS
		dot.append(style == null ? "" : "\",style=\"" + style); // NOCS
		dot.append(framecolor == null ? "" : "\",color=\"" + framecolor); // NOCS
		dot.append(fillcolor == null ? "" : "\",fillcolor=\"" + fillcolor); // NOCS
		dot.append(fontcolor == null ? "" : "\",fontcolor=\"" + fontcolor); // NOCS
		dot.append(fontsize == DotFactory.DOT_DEFAULT_FONTSIZE ? "" : "\",fontsize=\"" + Double.toString(fontsize)); // NOCS
		dot.append(imageFilename == null ? "" : "\",image=\"" + imageFilename); // NOCS
		dot.append("\"" + (misc == null ? "" : misc) + "]\n"); // NOCS
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
		final StringBuilder dot = new StringBuilder(prefix + "subgraph \"cluster_" + name);
		dot.append("\" {\n" + prefix + " label = \"" + label);
		dot.append(shape == null ? "" : "\";\n" + prefix + " shape = \"" + shape); // NOCS
		dot.append(style == null ? "" : "\";\n" + prefix + " style = \"" + style); // NOCS
		dot.append(framecolor == null ? "" : "\";\n" + prefix + " pencolor = \"" + framecolor); // NOCS
		dot.append(fillcolor == null ? "" : "\";\n" + prefix + " fillcolor = \"" + fillcolor); // NOCS
		dot.append(fontcolor == null ? "" : "\";\n" + prefix + " fontcolor = \"" + fontcolor); // NOCS
		dot.append(fontsize == DotFactory.DOT_DEFAULT_FONTSIZE ? "" : "\";\n" + prefix + " fontsize = \"" + Double.toString(fontsize)); // NOCS
		dot.append("\";" + (misc == null ? "" : misc) + "\n"); // NOCS
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
		final StringBuilder dot = new StringBuilder(prefix).append(String.format("%s->%s[", from, to));
		if (style != null) {
			dot.append(String.format("style=\"%s\"", style));
		}
		if (arrowhead != null) {
			dot.append(String.format("%s arrowhead=\"%s\"", style == null ? "" : ",", arrowhead)); // NOCS
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
		final StringBuilder dot = new StringBuilder(prefix).append(String.format("%s->%s", from, to)).append(String.format("[label=%s", label));
		if (style != null) {
			dot.append(String.format(", style=\"%s\"", style));
		}
		if (arrowhead != null) {
			dot.append(String.format("%s arrowhead=\"%s\"", style == null ? "" : ",", arrowhead)); // NOCS
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
		return String.format("%s\"%s\" -> \"%s\" [label=\" \",taillabel=\"%1.1f%%\",headlabel=\"%1.1f%%\"];\n", prefix, from, to, taillabel * 100.0,
				headlabel * 100.0);
	}

}
