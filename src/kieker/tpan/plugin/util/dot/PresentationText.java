//package kieker.tpan.plugins.util.dot;
//
//import java.util.Arrays;
//import java.util.Collection;
//
///**
// * This class provides some static methods to present some structure elements
// * in nice and detailed text form.
// *
// * @author Nina
// */
//class PresentationText {
//
///**
// * Private empty constructor to mark this class as not useful to instantiate because is has only static members.
// */
//private PresentationText(){
//}
//
///**
// * Constructs a string representation of the specified operation.
// * @param op operation
// * @return one text line to be used in a table-like view
// */
//private static String operationToString( StructureElement op ){
//    return String.format( "              [ %5d/%5d | %+1.3f | %+1.3f ]   %s\n", op.getAnomalyCount(), op.getExecutionCount(), op.getAnomalyRating(), op.getCauseRating(), op.getShortName() );
//}
//
///**
// * Constructs a string representation of the specified component.
// * Information is automatically fetched from subordinate elements.
// * @param comp component
// * @return a text block to be used in a table-like view
// */
//private static StringBuilder componentToString( StructureElement comp ){
//    StringBuilder output = new StringBuilder( String.format( "   %6.2f%%  Component: \"%s\"\n", comp.getPercent(), comp.getName() ) );
//    for( StructureElement op : comp.getChildren() ){
//        output.append( operationToString( op ) );
//    }
//    return output;
//}
//
///**
// * Constructs a string representation of the specified deployment context.
// * Information is automatically fetched from subordinate elements.
// * @param dc deployment context
// * @return a text block to be used in a table-like view
// */
//private static StringBuilder deploymentContextToString( StructureElement dc ){
//	StringBuilder output = new StringBuilder( "  ---------------------------\n  Deployment Context: \"" + dc.getName() + "\"\n" );
//	for( StructureElement comp : dc.getChildren() ){
//		output.append( componentToString( comp ) );
//	}
//	return output;
//}
//
///**
// * Constructs a string that contains detailed informations about all elements
// * of an application, down to operation level.
// * @param app application to present
// * @return a text block to be used in a table-like view
// */
//static CharSequence applicationToText( Application app ){
//    StringBuilder result = new StringBuilder( "  Application: \"" + app.getName() + "\" -- anomalies correlated by \"" + app.getAlgorithmName() + "\"\n" );
//    for( StructureElement dc : app.getDeploymentContexts() ){
//        if( !dc.isRoot() ){
//            result.append( deploymentContextToString( dc ) );
//        }
//    }
//    return result;
//}
//
///**
// * Constructs a table of components with their assigned rating to be the cause of failure,
// * in descending order by this rating.
// * @param components components to list
// * @return text representation of the components
// */
//static CharSequence componentsOrderedByCauseRating( Collection<StructureComponent> components ){
//    StringBuilder result = new StringBuilder();
//    StructureComponent[] comps = components.toArray( new StructureComponent[ components.size() ] );
//    Arrays.sort( comps );
//    for( int i = comps.length - 1; i >= 0; i-- ){   // backwards
//        if( !comps[i].isRoot() ){
//            result.append( String.format( "   %6.2f%%  %s\n", comps[i].getPercent(), comps[i].getName() ) );
//        }
//    }
//    return result;
//}
//
//}
