/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.filter.visualization.util.dot;

/**
 * This class provides a collection of static methods to compile Graphviz Dot elements
 * from string attributes and properties.
 * These elements may be compiled to complete Dot files externally.
 * 
 * @see <a href="http://www.graphviz.org/">Graphviz - Graph Visualization Software</a>
 * @see <a href="http://de.wikipedia.org/wiki/Graphviz">Graphviz - Wikipedia</a>
 * 
 * @author Nina Marwede
 * 
 * @since 1.1
 */
public final class DotFactory {

	/** Determines to use the "box" shape. */
	public static final String DOT_SHAPE_BOX = "box";
	/** Determines to use the "3D box" shape. */
	public static final String DOT_SHAPE_BOX3D = "box3d";
	/** Determines to use no shape at all. */
	public static final String DOT_SHAPE_NONE = "none";
	/** Determines to use the "oval" shape. */
	public static final String DOT_SHAPE_OVAL = "oval";
	/** Determines to use the "solid" style for a line or a frame. */
	public static final String DOT_STYLE_SOLID = "solid";
	/** Determines to use the "dashed" style for a line or a frame. */
	public static final String DOT_STYLE_DASHED = "dashed";
	/** Determines to use an open arrow head for an edge. */
	public static final String DOT_ARROWHEAD_OPEN = "open";
	/** Determines the default font size. */
	public static final double DOT_DEFAULT_FONTSIZE = 0.0;
	/** Determines to use the rank direction left to right . */
	public static final String DOT_DOT_RANKDIR_LR = "LR";
	/** A constant for the color "white". */
	public static final String DOT_FILLCOLOR_WHITE = "white";
	/** A constant for the color "gray". */
	public static final String DOT_FILLCOLOR_GRAY = "gray";
	/** Determines to the use the "filled" style for a component. */
	public static final String DOT_STYLE_FILLED = "filled";

	/**
	 * Private empty constructor to mark this class as not useful to instantiate because is has only static members.
	 */
	private DotFactory() {}

	/**
	 * This method constructs the neccesary dot code for a directed graph (digraph) file header (Should be called only once per dot file).
	 * 
	 * @param name
	 *            The name of the digraph.
	 * @param label
	 *            text to show at the label location (specified elsewhere) with HTML braces (&lt;&gt;)
	 * @param fontcolor
	 *            The font color.
	 * @param fontname
	 *            The name of the font.
	 * @param fontsize
	 *            The size of the font.
	 * 
	 * @return The digraph header as valid dot code.
	 */
	public static StringBuilder createHeader(final String name, final String label, final String fontcolor, final String fontname, final double fontsize) {
		final StringBuilder dot = new StringBuilder(256);
		dot.append("digraph ")
		   .append(name)
		   .append(" {\n label=<")
		   .append(label)
		   .append(">;\n fontcolor=\"")
		   .append(fontcolor)
		   .append("\";\n fontname=\"")
		   .append(fontname)
		   .append("\";\n fontsize=\"")
		   .append(Double.toString(fontsize))
		   .append("\";\n");
		return dot;
	}

	/**
	 * This method constructs the necessary dot code for the node defaults. Those values will be used if a parameter within a call of the other methods is null
	 * (Should be called only once per dot file).
	 * 
	 * @param style
	 *            The default style of the nodes.
	 * @param shape
	 *            The default shape of the nodes.
	 * @param framecolor
	 *            The default frame color of the nodes.
	 * @param fontcolor
	 *            The default font color of the nodes.
	 * @param fontname
	 *            The default font names of the nodes.
	 * @param fontsize
	 *            The default font size of the nodes.
	 * @param imagescale
	 *            The image scale property. This attribute determines how an image fills the "parent" node.
	 * 
	 * @return The graph node defaults as valid dot code.
	 */
	public static StringBuilder createNodeDefaults(final String style, final String shape, final String framecolor, final String fontcolor, final String fontname,
			final double fontsize, final String imagescale) {
		final StringBuilder dot = new StringBuilder(256);
		dot.append(" node [style=\"")
		   .append(style)
		   .append("\",shape=\"")
		   .append(shape)
		   .append("\",color=\"")
		   .append(framecolor)
		   .append("\",fontcolor=\"")
		   .append(fontcolor)
		   .append("\",fontname=\"")
		   .append(fontname)
		   .append("\",fontsize=\"")
		   .append(fontsize)
		   .append("\",imagescale=\"")
		   .append(imagescale)
		   .append("\"];\n");
		return dot;
	}

	/**
	 * Constructs dot code for the edge defaults.
	 * (Should be called only once per dot file.)
	 * 
	 * @param style
	 *            The default style for the edges.
	 * @param arrowhead
	 *            The defualt arrow head for the edges.
	 * @param labelfontname
	 *            label font nodeId, works only for headlabel and taillabel, not simple label
	 * @return graph edge defaults as dot code
	 */
	public static StringBuilder createEdgeDefaults(final String style, final String arrowhead, final String labelfontname) {
		final StringBuilder dot = new StringBuilder(128);
		dot.append(" edge [style=\"")
		   .append(style)
		   .append("\",arrowhead=\"")
		   .append(arrowhead)
		   .append("\",labelfontname=\"")
		   .append(labelfontname)
		   .append("\"];\n");
		return dot;
	}

	/**
	 * Constructs dot code for a node from the specified elements.
	 * 
	 * @param prefix
	 *            usually spaces, dependent on hierarchy - only for nice ascii formatting inside the dot code
	 * @param nodeId
	 *            The id of the node.
	 * @param label
	 *            The label to be used within the node. This parameter can be null.
	 * @param shape
	 *            The shape of the node. This parameter can be null.
	 * @param style
	 *            The style of the node. This parameter can be null.
	 * @param framecolor
	 *            The color of the frame of the node. This parameter can be null.
	 * @param fillcolor
	 *            The color which will be used to fill the node. This parameter can be null.
	 * @param fontcolor
	 *            The color of the font. This parameter can be null.
	 * @param fontsize
	 *            The size of the font. This parameter can be null.
	 * @param imageFilename
	 *            The name of the image to be displayed inside the node.
	 * @param misc
	 *            This parameter can be used to add miscellaneous data and dot code.
	 * @param tooltip
	 *            The tooltip of this node. This parameter can be null.
	 * 
	 * @return The graph node as valid dot code
	 */
	public static StringBuilder createNode(final String prefix, final String nodeId, final String label, final String shape, final String style,
			final String framecolor, final String fillcolor, final String fontcolor, final double fontsize, final String imageFilename, final String misc,
			final String tooltip) {
		final StringBuilder dot = new StringBuilder(128);
		boolean addComma = false;
		if (prefix != null) {
			dot.append(prefix);
		}
		dot.append('"')
		   .append(nodeId)
		   .append("\" [");
		if (label != null) {
			dot.append("label=\"")
			   .append(label)
			   .append('"');
			addComma = true;
		}
		if (shape != null) {
			if (addComma) {
				dot.append(',');
			}
			dot.append("shape=\"")
			   .append(shape)
			   .append('"');
			addComma = true;
		}
		if (style != null) {
			if (addComma) {
				dot.append(',');
			}
			dot.append("style=\"")
			   .append(style)
			   .append('"');
			addComma = true;
		}
		if (framecolor != null) {
			if (addComma) {
				dot.append(',');
			}
			dot.append("color=\"")
			   .append(framecolor)
			   .append('"');
			addComma = true;
		}
		if (fillcolor != null) {
			if (addComma) {
				dot.append(',');
			}
			dot.append("fillcolor=\"")
			   .append(fillcolor)
			   .append('"');
			addComma = true;
		}
		if (fontcolor != null) {
			if (addComma) {
				dot.append(',');
			}
			dot.append("fontcolor=\"")
			   .append(fontcolor)
			   .append('"');
			addComma = true;
		}
		if (fontsize != DOT_DEFAULT_FONTSIZE) {
			if (addComma) {
				dot.append(',');
			}
			dot.append("fontsize=\"")
			   .append(fontsize)
			   .append('"');
			addComma = true;
		}
		if (imageFilename != null) {
			if (addComma) {
				dot.append(',');
			}
			dot.append("image=\"")
			   .append(imageFilename)
			   .append('"');
			addComma = true;
		}
		if (tooltip != null) {
			if (addComma) {
				dot.append(',');
			}
			dot.append("tooltip=\"")
			   .append(tooltip)
			   .append('"');
			addComma = true;
		}

		if (misc != null) {
			if (addComma) {
				dot.append(',');
			}
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
	 *            The name of the cluster.
	 * @param label
	 *            The label to be used for the cluster.
	 * @param shape
	 *            The shape of the cluster. This parameter can be null.
	 * @param style
	 *            The style of the frame. This parameter can be null.
	 * @param framecolor
	 *            The color of the frame. This parameter can be null.
	 * @param fillcolor
	 *            The color which will be used to fill the frame. This parameter can be null.
	 * @param fontcolor
	 *            The color of the font. This parameter can be null.
	 * @param fontsize
	 *            The size of the font. This parameter can be null.
	 * @param misc
	 *            This parameter can be used to add miscellaneous data and dot code (like further content). This parameter can be null.
	 * 
	 * @return A graph cluster as valid dot code - but <b>without</b> the closing bracket.
	 */
	public static StringBuilder createCluster(final String prefix, final String name, final String label, final String shape, final String style,
			final String framecolor, final String fillcolor, final String fontcolor, final double fontsize, final String misc) {
		final StringBuilder dot = new StringBuilder(256);
		dot.append(prefix)
		   .append("subgraph \"cluster_")
		   .append(name)
		   .append("\" {\n")
		   .append(prefix)
		   .append(" label = \"")
		   .append(label);
		if (shape != null) {
			dot.append("\";\n")
			   .append(prefix)
			   .append(" shape = \"")
			   .append(shape);
		}
		if (style != null) {
			dot.append("\";\n")
			   .append(prefix)
			   .append(" style = \"")
			   .append(style);
		}
		if (framecolor != null) {
			dot.append("\";\n")
			   .append(prefix)
			   .append(" pencolor = \"")
			   .append(framecolor);
		}
		if (fillcolor != null) {
			dot.append("\";\n")
			   .append(prefix)
			   .append(" fillcolor = \"")
			   .append(fillcolor);
		}
		if (fontcolor != null) {
			dot.append("\";\n")
			   .append(prefix)
			   .append(" fontcolor = \"")
			   .append(fontcolor);
		}
		if (fontsize != DOT_DEFAULT_FONTSIZE) {
			dot.append("\";\n")
			   .append(prefix)
			   .append(" fontsize = \"")
			   .append(fontsize);
		}
		dot.append("\";");
		if (misc != null) {
			dot.append(misc);
		}
		dot.append('\n');
		// closing bracket "}" has to be added by calling method !
		return dot;
	}

	/**
	 * Creates dot code for a connection. It is assumed that the typical properties (color etc) are already formatted for the usage within dot.
	 * 
	 * @param prefix
	 *            usually spaces, dependent on hierarchy - only for nice ascii formatting inside the dot code
	 * @param from
	 *            The string representation of the start node.
	 * @param to
	 *            The string representation of the destination node.
	 * @param color
	 *            The color of the edge. This parameter can be null.
	 * @param style
	 *            The style of the edge. This parameter can be null.
	 * @param arrowhead
	 *            The type of the arrowhead of the edge. This parameter can be null.
	 * 
	 * @return A graph connection as valid dot code.
	 */
	public static String createConnection(final String prefix, final String from, final String to, final String style, final String arrowhead, final String color) {
		final StringBuilder dot = new StringBuilder(128);
		dot.append(prefix)
		   .append(from)
		   .append("->")
		   .append(to);

		boolean firstFlag = true;

		if (style != null) {
			dot.append("style=\"")
			   .append(style)
			   .append('"');
			firstFlag = false;
		}
		if (arrowhead != null) {
			if (!firstFlag) {
				dot.append(',');
			}

			dot.append(" arrowhead=\"")
			   .append(arrowhead)
			   .append('"');
			firstFlag = false;
		}
		if (color != null) {
			if (!firstFlag) {
				dot.append(',');
			}

			dot.append(" color=\"")
			   .append(color)
			   .append('"');
			firstFlag = false;
		}
		dot.append(']');
		return dot.toString();
	}

	/**
	 * Creates dot code for a connection. It is the same as a call to {@link #createConnection(String, String, String, String, String, String)} except that
	 * a label can be added.
	 * 
	 * @param prefix
	 *            usually spaces, dependent on hierarchy - only for nice ascii formatting inside the dot code
	 * @param from
	 *            The string representation of the start node.
	 * @param to
	 *            The string representation of the destination node.
	 * @param label
	 *            The label of the edge.
	 * @param color
	 *            The color of the edge. This parameter can be null.
	 * @param style
	 *            The style of the edge. This parameter can be null.
	 * @param arrowhead
	 *            The type of the arrowhead of the edge. This parameter can be null.
	 * 
	 * @return A graph connection as valid dot code.
	 */
	public static String createConnection(final String prefix, final String from, final String to, final String label, final String style, final String arrowhead,
			final String color) {
		final StringBuilder dot = new StringBuilder(128);
		dot.append(prefix)
		   .append(from)
		   .append("->")
		   .append(to)
		   .append("[label=")
		   .append(label);
		if (style != null) {
			dot.append(", style=\"")
			   .append(style)
			   .append('"');
		}
		if (arrowhead != null) {
			dot.append(", arrowhead=\"")
			   .append(arrowhead)
			   .append('"');
		}
		if (color != null) {
			dot.append(", color=\"")
			   .append(color)
			   .append('"');
		}
		dot.append(']');
		return dot.toString();
	}

	/**
	 * This method creates dot code for a connection. It is the same as a call to {@link #createConnection(String, String, String, String, String, String)} except
	 * that color, style and arrowhead cannot be modified and that a headlabel and a taillabel can be added.
	 * 
	 * @param prefix
	 *            usually spaces, dependent on hierarchy - only for nice ascii formatting inside the dot code
	 * @param from
	 *            The string representation of the start node.
	 * @param to
	 *            The string representation of the destination node.
	 * @param taillabel
	 *            The label of the edge at the tail of the connection.
	 * @param headlabel
	 *            The label of the edge at the head of the connection.
	 * @return A graph connection as valid dot code.
	 */
	public static String createConnection(final String prefix, final String from, final String to, final double taillabel, final double headlabel) {
		final StringBuilder dot = new StringBuilder(128);
		dot.append(prefix)
		   .append('"')
		   .append(from)
		   .append("\" -> \"")
		   .append(to)
		   .append("\" [label=\" \",taillabel=\"")
		   .append(taillabel * 100.0)
		   .append("(\",headlabel=\"")
		   .append(headlabel * 100.0)
		   .append("\"];\n");
		return dot.toString();
	}
}
