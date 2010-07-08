package kieker.analysis.plugin.util.dot;

//package kieker.tpan.plugins.util.dot;
//
///*
// * ==================LICENCE=========================
// * Copyright 2006-2010 Kieker Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * ==================================================
// */
//
//import java.awt.Color;
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Properties;
//
///**
// * This class provides some static methods to present a dependency hierarchy of
// * structure elements in Graphviz Dot form.
// * Parameters are controlled through properties file.
// *
// * @author Nina Marwede
// */
//class PresentationDot {
//
//private static PropertiesExtended props;
//private static File imageDir;
//
//private static boolean includeCaption;
//private static boolean includeExplanation;
//private static boolean includeDeploymentContexts;
//private static boolean includeComponents;
//private static boolean includeOperations;
//private static boolean includePseudocolors;
//private static boolean includeRatings;
//private static int includeHistograms;
//private static boolean includeWeights;
//private static boolean weightsRelative;
//private static boolean includeLegend;
//private static boolean stretchColors;
//private static String neutralColor;
//private static String graphRemovePrefix;    // may be null if property not set
//
///**
// * Private empty constructor to mark this class as not useful to instantiate because is has only static members.
// */
//private PresentationDot(){
//}
//
///**
// * Creates a String from the operation's base properties to be used in .dot files.
// * @param op operation to process
// * @return dot code representation of this node, without connections
// * @throws IOException on error in histogram temp file creation
// */
//private static StringBuilder operationToDotNode( StructureElement op ) throws IOException {
//    boolean root = op.isRoot();
//    boolean max = op.isMax();
//    String label = includeComponents ? op.getShortName() : op.getName();
//    if( includeRatings ){
////        label += /* root ? "" : */ String.format( "\\n[ %d/%d | %d | %1.3f | %1.3f | %1.2f%% ]", op.getAnomalyCount(), op.getExecutionCount(), op.getTrueAnomalyStatus(), op.getAnomalyRating(), op.getCauseRating(), op.getPercent() );
//        label += root ? "" : String.format( "\\n[ %d/%d | %1.3f | %1.3f | %1.2f%% ]", op.getAnomalyCount(), op.getExecutionCount(), op.getAnomalyRating(), op.getCauseRating(), op.getPercent() );
//    }
//    String shape = /* op.getTrueAnomalyStatus() > 0 ? "note" : */ null;     // seems to add a layer that hides the histograms :-(
//    String style = max ? "filled,bold" : null;
//    String framecolor = max && includePseudocolors ? props.getStringProp( "opFrameColorMax" ) : props.getStringProp( "opFrameColor" );
//    Color color = !root && includePseudocolors ? PseudoColor.getColor( Util.STRUCTURE_OPERATION, op.getCauseRating() ) : Color.WHITE;
//    String fillcolor = root && includePseudocolors ? neutralColor : PseudoColor.getHexColorString( color );
//    String fontcolor = max && includePseudocolors && stretchColors ? props.getStringProp( "graphFontColorInversed" ) : null;
//    String histogramFilename = imageDir.getAbsolutePath() + File.separator + op.getName() + "." + props.getStringProp( "histogramTempFileType" );
//    String misc = root ? ",root=true" : "";		// root option seems not to do anything
//    if( !root && includeHistograms > 0 ){
//        label += "\\n\\n\\n";
//        Color lowerBgColor = includePseudocolors ? Color.BLACK : Color.WHITE; // new Color( 239, 243, 247 );
//        Util.writeHistogramImageFile( histogramFilename, getHistogram( op ), props.getIntProp( "histogramHeight" ), color, lowerBgColor );
//    }
//    return DotFactory.createNode( root ? " " : "   ", op.getName(), removePrefix( label ), shape, style, framecolor, fillcolor, fontcolor, 0.0, !root && includeHistograms > 0 ? histogramFilename : null, misc );
//}
//
///**
// * Creates a String from the operation's dependencies to be used in .dot files.
// * ( Using dot "weight" attribute seems to lead to an "trouble in init_rank" error in dot. )
// * @param op operation to process
// * @return dot code representation of the outgoing connections from this node
// */
//private static StringBuilder operationConnectionsToDot( StructureElement op ){
//    StringBuilder dot = new StringBuilder();
//    for( StructureElement neighbor : op.outgoing.keySet() ){
//        if( includeWeights ){
//            if( weightsRelative ){  // relative weights
//                dot.append( DotFactory.createConnection( " ", op.getName(), neighbor.getName(), op.outgoingRel.get( neighbor ), neighbor.incomingRel.get( op ) ) );
//            }
//            else{                   // absolute weights
//                dot.append( DotFactory.createConnection( " ", op.getName(), neighbor.getName(), op.outgoing.get( neighbor ) ) );
//            }
//        }
//        else{                       // no weights
//            dot.append( DotFactory.createConnection( " ", op.getName(), neighbor.getName() ) );
//        }
//    }
//    return dot;
//}
//
///**
// * Creates a String from the component's base properties to be used in .dot files.
// * @param comp component to process
// * @return dot code representation of this node, without connections
// * @throws IOException on error in histogram temp file creation
// */
//private static StringBuilder componentToDotNode( StructureElement comp ) throws IOException {
//    StringBuilder dot = new StringBuilder();
//    boolean root = comp.isRoot();
//    boolean max = comp.isMax();
//    boolean lonely = comp.isLonely();
//    String label = String.format( "\\n[ %d/%d | %1.3f | %1.2f%% ]", comp.getAnomalyCount(), comp.getExecutionCount(), comp.getCauseRating(), comp.getPercent() );
//    String style = "filled" + ( max ? ",bold" : "" );
//    String framecolor = max && includePseudocolors && !lonely ? props.getStringProp( "compFrameColorMax" ) : props.getStringProp( "compFrameColor" );
//    Color color = !root && includePseudocolors ? PseudoColor.getColor( Util.STRUCTURE_COMPONENT, comp.getCauseRating() ) : Color.WHITE;
//    String fillcolor = ( root || lonely ) && includePseudocolors ? neutralColor : PseudoColor.getHexColorString( color );
//    String fontcolor = max && includePseudocolors && stretchColors && !lonely ? props.getStringProp( "graphFontColorInversed" ) : props.getStringProp( "graphFontColor" );
//    double fontsize = props.getDoubleProp( "compFontSize" );
//    if( includeOperations ){
//        if( !root ){        // root is not surrounded by a cluster
//            label = comp.getName() + ( !root && includeRatings ? label : "" );
//            dot.append( DotFactory.createCluster( "  ", comp.getName(), removePrefix( label ), "", style, framecolor, fillcolor, fontcolor, fontsize, null ) );
//        }
//        for( StructureElement op : comp.children ){
//            dot.append( operationToDotNode( op ) );
//        }
//        if( !root ){
//            dot.append( "  }\n" );
//        }
//    }
//    else{   // no operations, only self
//        label = comp.getName() + ( !root && includeRatings ? label : "" );
//        String shape = /* comp.getTrueAnomalyStatus() > 0 ? "note" :*/ null;  // "component" is also available, but does not look nice in the graph
//        String histogramFilename = imageDir.getAbsolutePath() + File.separator + comp.getName() + "." + props.getStringProp( "histogramTempFileType" );
//        String misc = root ? ",root=true" : "";
//        if( !root && includeHistograms > 0 ){
//            label += "\\n\\n\\n";
//            Color lowerBgColor = includePseudocolors ? Color.BLACK : Color.WHITE;
//            Util.writeHistogramImageFile( histogramFilename, getHistogram( comp ), props.getIntProp( "histogramHeight" ), color, lowerBgColor );
//        }
//        dot.append( DotFactory.createNode( "  ", comp.getName(), removePrefix( label ), shape, style, framecolor, includePseudocolors ? fillcolor : "white", fontcolor, fontsize, !root && includeHistograms > 0 ? histogramFilename : null, misc ) );
//    }
//    return dot;
//}
//
///**
// * Creates a String from the operation dependencies to be used in .dot files.
// * @param comp component to process
// * @return dot code representation of the outgoing connections from this node
// */
//private static StringBuilder componentConnectionsToDot( StructureElement comp ){
//    StringBuilder dot = new StringBuilder();
//    if( includeOperations ){
//        for( StructureElement op : comp.getChildren() ){
//            dot.append( PresentationDot.operationConnectionsToDot( op ) );
//        }
//    }
//    else{   // no operations, only self
//        for( StructureElement neighbor : comp.outgoing.keySet() ){
//            if( includeWeights ){
//                if( weightsRelative ){  // relative weights
//                    dot.append( DotFactory.createConnection( " ", comp.getName(), neighbor.getName(), comp.outgoingRel.get( neighbor ), neighbor.incomingRel.get( comp ) ) );
//                }
//                else{                   // absolute weights
//                    dot.append( DotFactory.createConnection( " ", comp.getName(), neighbor.getName(), comp.getOutgoingConnectionCount( neighbor ) ) );
//                }
//            }
//            else{                       // no weights
//                dot.append( DotFactory.createConnection( " ", comp.getName(), neighbor.getName() ) );
//            }
//        }
//    }
//    return dot;
//}
//
///**
// * Creates a String from the underlying operation and component objects
// * to be used in .dot files.
// * @param dc deployment context to process
// * @return dot code representation of this node
// * @throws IOException on error in histogram temp file creation
// */
//private static StringBuilder deploymentContextToDot( StructureDeploymentContext dc ) throws IOException {
//    StringBuilder dot = new StringBuilder();
//    boolean root = dc.isRoot();
//    boolean max = dc.isMax();
//    boolean lonely = dc.isLonely();
//    String label = root ? "$" : dc.getName() + ( includeRatings ? String.format( "\\n[ %d/%d | %1.2f | %1.2f%% ]", dc.getAnomalyCount(), dc.getExecutionCount(), dc.getCauseRating(), dc.getPercent() ) : "" );
//    String style = "filled" + ( max && !includeComponents ? ",bold" : "" );
//    String framecolor = max && includePseudocolors && !lonely ? props.getStringProp( "dcFrameColorMax" ) : props.getStringProp( "dcFrameColor" );
//    Color color = root ? null : PseudoColor.getColor( Util.STRUCTURE_DEPLOYMENTCONTEXT, dc.getCauseRating() );
//    String fillcolor = root || lonely ? neutralColor : PseudoColor.getHexColorString( color );
//    String fontcolor = max && includePseudocolors && stretchColors && !lonely ? props.getStringProp( "graphFontColorInversed" ) : props.getStringProp( "graphFontColor" );
//    double fontsize = props.getDoubleProp( "dcFontSize" );
//
//    if( !includeComponents && !includeOperations ){ // only draw myself as node
//        String histogramFilename = imageDir.getAbsolutePath() + File.separator + dc.getName() + "." + props.getStringProp( "histogramTempFileType" );
//        String misc = root ? ",root=true" : "";
//        if( !root && includeHistograms > 0 ){
//            label += "\\n\\n\\n";
//            Color lowerBgColor = includePseudocolors ? Color.BLACK : Color.WHITE;
//            Util.writeHistogramImageFile( histogramFilename, getHistogram( dc ), props.getIntProp( "histogramHeight" ), color, lowerBgColor );
//        }
//        dot.append( DotFactory.createNode( " ", dc.getName(), removePrefix( label ), null, style, framecolor, includePseudocolors ? fillcolor : "white", fontcolor, fontsize, !root && includeHistograms > 0 ? histogramFilename : null, misc ) );
//        for( StructureElement neighbor : dc.outgoing.keySet() ){
//            if( includeWeights ){
//                if( weightsRelative ){  // relative weights
//                    dot.append( DotFactory.createConnection( " ", dc.getName(), neighbor.getName(), dc.outgoingRel.get( neighbor ), neighbor.incomingRel.get( dc ) ) );
//                }
//                else{                   // absolute weights
//                    dot.append( DotFactory.createConnection( " ", dc.getName(), neighbor.getName(), dc.getOutgoingConnectionCount( neighbor ) ) );
//                }
//            }
//            else{                       // no weights
//                dot.append( DotFactory.createConnection( " ", dc.getName(), neighbor.getName() ) );
//            }
//        }
//    }
//    else{   // includeComponents || includeOperations
//        if( !root ){                // root is not surrounded by a cluster
//            dot.append( DotFactory.createCluster( " ", dc.getName(), removePrefix( label ), "", style, framecolor, includePseudocolors ? fillcolor : "white", fontcolor, fontsize, null ) );
//        }
//        for( StructureElement comp : dc.getChildren() ){
//            if( includeComponents ){
//                dot.append( componentToDotNode( comp ) );
//            }
//            else if( includeOperations ){
//                for( StructureElement op : comp.getChildren() ){
//                    dot.append( operationToDotNode( op ) );
//                }
//            }
//        }
//        if( !root ){
//            dot.append( " }\n" );
//        }
//        for( StructureElement comp : dc.getChildren() ){
//            if( includeComponents ){
//                dot.append( componentConnectionsToDot( comp ) );	// separated from nodes because of clustering
//            }
//            else if( includeOperations ){
//                for( StructureElement op : comp.getChildren() ){
//                    dot.append( operationConnectionsToDot( op ) );
//                }
//            }
//        }
//    }
//    return dot;
//}
//
///**
// * Creates a String from the application's hierarchical elements to be used in .dot files.
// * @param app application to process
// * @param imageDirectory directory to write temporary image files to
// * @return dot code representation of this application
// */
//static CharSequence applicationToDot( Application app, File imageDirectory ){
//    imageDir = imageDirectory;
//    PseudoColor.setColorStretching( stretchColors );
//
//    StringBuilder label = new StringBuilder( includeCaption ? "Application &quot;" + app.getName() + "&quot; -- anomalies correlated by &quot;" + app.getAlgorithmName() + "&quot; -- image created on " + ( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ) ).format( new Date() ) : "" );
//    if( includeExplanation ){
//        label.append( "<br/> <br/><font point-size=\"11.0\">What does this picture show?  --  If all options are switched on, the following features can be seen:</font><br align=\"left\"/><font point-size=\"10.0\">" );
//        label.append( "- metadata: application name (e.g. experiment number), algorithm that was used, timestamp of image creation<br align=\"left\"/>" );
//        label.append( "- the name of each element<br align=\"left\"/>" );
//        label.append( "- the dependencies in the calling hierarchy: which element uses services of which elements<br align=\"left\"/>" );
//        label.append( "- the times each connection is executed (or the relative usage, if selected)<br align=\"left\"/>" );
//        label.append( "- the number of anomal executions per element, considered by the anomaly detector<br align=\"left\"/>" );
//        label.append( "- histograms of all execution anomaly scores or response times per element<br align=\"left\"/>" );
////        label.append( "- the true anomaly status of each element (if this information is available)<br align=\"left\"/>" );
//        label.append( "- the anomaly rating on operation level, and the cause rating of each element<br align=\"left\"/>" );
//        label.append( "- the cause percent value of each element -- this sums up to 100% on each level<br align=\"left\"/>" );
//        label.append( "- a pseudo color shade assigned to each element based on their respective cause rating<br align=\"left\"/>" );
//        label.append( "- a special indication mark for the one element with the highest cause rating on each level<br align=\"left\"/>" );
//        label.append( "</font>" );
//    }
//    String fontcolor = props.getStringProp( "graphFontColor" );
//    String fontname = props.getStringProp( "graphFontName" );
//    double fontsize = props.getDoubleProp( "graphFontSize" );
//    StringBuilder dot = DotFactory.createHeader( "AnomalyCorrelation", label.toString(), fontcolor, fontname, fontsize );
//    dot.append( " labelloc=\"t\";\n" );
//    dot.append( " remincross=\"true\";\n" );
////	dot.append( " center=false;\n" );				// doesn't change anything
////	dot.append( " orientation=\"landscape\";\n" );
////	dot.append( " nodesep=\"0.5\";\n" );					// doesn't help with the component spacing
//
//    String framecolor = props.getStringProp( "opFrameColor" );
//    fontsize = props.getDoubleProp( "opFontSize" );
//    String imagescale = "both";
//    dot.append( DotFactory.createNodeDefaults( "filled", "box", framecolor, fontcolor, fontname, fontsize, imagescale ) );
//
//    String style = "dashed";
//    String arrowhead = "open";
//    dot.append( DotFactory.createEdgeDefaults( style, arrowhead, fontname ) );
//
//    try{
//        if( includeDeploymentContexts ){
//            for( StructureDeploymentContext dc : app.getDeploymentContexts() ){
//                dot.append( deploymentContextToDot( dc ) );
//            }
//        }
//        else{
//            if( includeComponents ){
//                for( StructureComponent comp : app.getComponents() ){
//                    dot.append( componentToDotNode( comp ) );
//                }
//                for( StructureComponent comp : app.getComponents() ){
//                    dot.append( componentConnectionsToDot( comp ) );
//                }
//            }
//            else{
//                if( includeOperations ){
//                    for( StructureOperation op : app.getOperations() ){
//                        dot.append( operationToDotNode( op ) );
//                    }
//                    for( StructureOperation op : app.getOperations() ){
//                        dot.append( operationConnectionsToDot( op ) );
//                    }
//                }
//                else{   // nothing ??
//                    dot.append( DotFactory.createNode( " ", "warning", "WARNING: No graph elements selected.\\nCheck your presentation properties file.", null, null, null, "white", null, 0.0, null, null ) );
//                }
//            }
//        }
//    }
//    catch( IOException ex ){
//        Util.writeOut( Util.LEVEL_ERROR, "< WARNING: IOException while creating an histogram temporary file: " + ex.getMessage() );
//    }
//
//    if( includeLegend ){
//        dot.append( DotFactory.createCluster( " ", "legend", "Legend:", "none", "", "", "white", "", props.getDoubleProp( "dcFontSize" ), "" ) );
//        //dot.append( DotFactory.createNode( "  ", "legend_label", "Legend:", "none", "", "white", "white", null, 0.0, null ) );
//        dot.append( DotFactory.createNode( "  ", "legend_neutral", "These elements\\nhave no meaning\\nfor classification.", null, null, null, neutralColor, null, 0.0, null, null ) );
//        dot.append( DotFactory.createNode( "  ", "legend_anomal", "These elements\\nmight be\\nthe cause.", null, null, null, PseudoColor.getHexColorString( PseudoColor.getColor( -1, 1.0 ) ), null, 0.0, null, null ) );
//        dot.append( DotFactory.createNode( "  ", "legend_unclass", "These elements\\ncan not be\\nexactly classified.", null, null, null, PseudoColor.getHexColorString( PseudoColor.getColor( -1, 0.0 ) ), null, 0.0, null, null ) );
//        dot.append( DotFactory.createNode( "  ", "legend_normal", "These elements\\nare probably\\nNOT the cause.", null, null, null, PseudoColor.getHexColorString( PseudoColor.getColor( -1, -1.0 ) ), null, 0.0, null, null ) );
//        dot.append( "  legend_neutral -> legend_anomal -> legend_unclass -> legend_normal [style=\"invis\"]\n }\n" );
//    }
//    dot.append( "}\n" );
//    return dot;
//}
//
///**
// * Fetches a histogram of the anomaly scores or response times (as configured via properties)
// * of all executions of the specified element.
// * The number of buckets (histogram columns) is also set via properties.
// * @param elem element to get the raw data from
// * @return array of percentages [0.0,1.0], all zeroes for root
// */
//private static double[] getHistogram( StructureElement elem ){
//    List<Double> data;
//    switch( includeHistograms ){
//        case 1:
//            data = elem.getChildrensAnomalyScores();
//            break;
//        case 2:
//            data = Util.longListToDoubleList( elem.getChildrensResponseTimes() );
//            break;
//        default:
//            throw new IllegalArgumentException( "Invalid histogram type (property 'includeHistograms'): " + includeHistograms );
//    }
//    return Util.createHistogram( data, props.getIntProp( "histogramWidth" ) );
//}
//
///**
// * Loads dot presentation specific properties from the specified file name.
// * @param filename name of the file to load properties from
// * @param priority properties to take high priority values from
// */
//static void loadProperties( String filename, Properties priority ){
//    props = new PropertiesExtended( filename, priority );
//    includeCaption = props.getIntProp( "graphIncludeCaption" ) == 1;
//    includeExplanation = props.getIntProp( "graphIncludeExplanation" ) == 1;
//    includeDeploymentContexts = props.getIntProp( "graphIncludeDeploymentContexts" ) == 1;
//    includeComponents = props.getIntProp( "graphIncludeComponents" ) == 1;
//    includeOperations = props.getIntProp( "graphIncludeOperations" ) == 1;
//    includePseudocolors = props.getIntProp( "graphIncludePseudocolors" ) == 1;
//    includeRatings = props.getIntProp( "graphIncludeRatings" ) == 1;
//    includeWeights = props.getIntProp( "graphIncludeEdgeWeights" ) == 1;
//    includeLegend = props.getIntProp( "graphIncludeLegend" ) == 1;
//    includeHistograms = props.getIntProp( "graphHistogramType" );
//    stretchColors = props.getIntProp( "graphStretchPseudocolorsToFullSpectrum" ) == 1;
//    weightsRelative = props.getIntProp( "graphEdgeWeightMethod" ) == 1;
//    neutralColor = props.getStringProp( "neutralColor" );
//    graphRemovePrefix = props.getStringProp( "graphRemovePrefix" ); // may be null
//}
//
///**
// * Check the local properties for consistency from hardcoded requirements.
// * @return true if no error found.
// */
//static boolean checkProperties(){
//    if( props == null ){
//        return false;
//    }
//    boolean error = false;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_STRING, "graphFontName" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_STRING, "histogramTempFileType" ) )error = true;
////    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_STRING, "graphRemovePrefix" ) )error = true; // optional
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "graphFontColor" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "graphFontColorInversed" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "dcFrameColor" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "dcFrameColorMax" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "compFrameColor" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "compFrameColorMax" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "opFrameColor" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "opFrameColorMax" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_COLOR, "neutralColor" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_DOUBLE, "graphFontSize" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_DOUBLE, "dcFontSize" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_DOUBLE, "compFontSize" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_DOUBLE, "opFontSize" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludeCaption" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludeExplanation" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludeDeploymentContexts" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludeComponents" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludeOperations" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludePseudocolors" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludeRatings" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludeEdgeWeights" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphIncludeLegend" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphHistogramType" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphStretchPseudocolorsToFullSpectrum" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "graphEdgeWeightMethod" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "histogramWidth" ) )error = true;
//    if( !props.checkProperty( PropertiesExtended.PROPERTY_TYPE_INT, "histogramHeight" ) )error = true;
//    return !error;      // return true if check passed   = if no error occurred
//}
//
//private static String removePrefix( String label ){
//    if( graphRemovePrefix != null && label.startsWith( graphRemovePrefix ) ){
//        return label.substring( graphRemovePrefix.length() );
//    }
//    return label;
//}
//
//}
