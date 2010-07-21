package kieker.tools.traceAnalysis.analysisPlugins.visualization.util.dot;

//package kieker.tpan.plugins.util.dot;
//
//import java.util.ArrayList;
//import java.util.List;
//import kieker.tpan.datamodel.system.Operation;
//
///**
// * This class implements unique functionality for operations,
// * which work as normal {@link StructureElement}s otherwise.
// *
// * Operations usually have a {@link StructureComponent} as parent,
// * they have {@link StructureExecution}s as children,
// * and they are linked among each other with a <em>uses</em> relation.
// *
// * @author Nina
// */
//class StructureOperation extends StructureElement {
//
//private Operation operation;
//private long anomalyCountBuffer = -1L;        // -1 == not initialized
//private long outlierCountBuffer = -1L;        // -1 == not initialized
//
//StructureOperation( Operation op, StructureComponent component ){
//    super( component, op.isRootOperation() );
//    this.operation = op;
//}
//
//public String getName(){
//    return operation.getName();
//}
//
//public String getShortName(){
//    return Util.reduceParameters( operation.getMethodname() );
//}
//
//Operation getOperation(){
//    return operation;
//}
//
//@Override
//long getOutlierCount(){
//    if( outlierCountBuffer < 0 ){     // initialized ?
//        outlierCountBuffer = super.getOutlierCount();
//    }
//    return outlierCountBuffer;
//}
//
//@Override
//long getAnomalyCount(){
//    if( anomalyCountBuffer < 0 ){     // initialized ?
//        anomalyCountBuffer = super.getAnomalyCount();
//    }
//    return anomalyCountBuffer;
//}
//
//@Override
//long getExecutionCount(){
//    return children.size();
//}
//
//@Override
//List<Double> getChildrensAnomalyScores(){
//    List<Double> result = new ArrayList<Double>();
//    for( StructureElement child : children ){
//        result.add( child.getAnomalyRating() );
//    }
//    return result;
//}
//
//@Override
//List<Long> getChildrensResponseTimes(){
//    List<Long> result = new ArrayList<Long>();
//    for( StructureElement child : children ){
//        result.add( ((StructureExecution)child).getResponseTime() );
//    }
//    return result;
//}
//
///**
// * Calculates the number of connections from the specified element to this element;
// * or <em>all</em> incoming connections.
// * @param other element to be questioned (supposed to be a component, or deployment context), or null
// * @return number of connections from the specified element; or all connections, if other == null
// */
//@Override
//long getIncomingConnectionCount( StructureElement other ){
//    long count = 0L;
//    for( StructureElement neighbor : incoming.keySet() ){
//        if( other == null || other == neighbor.getParent() || other == neighbor.getParent().getParent() ){
//            count += incoming.get( neighbor );
//        }
//    }
//    return count;
//}
//
///**
// * Calculates the number of connections from this element to the specified element;
// * or <em>all</em> outgoing connections.
// * @param other element to be questioned (supposed to be a component, or deployment context), or null
// * @return number of connections to the specified element; or all connections, if other == null
// */
//@Override
//long getOutgoingConnectionCount( StructureElement other ){
//    long count = 0L;
//    for( StructureElement neighbor : outgoing.keySet() ){
//        if( other == null || other == neighbor.getParent() || other == neighbor.getParent().getParent() ){
//            count += outgoing.get( neighbor );
//        }
//    }
//    return count;
//}
//
///**
// * Checks a selection of internal variables for consistency.
// * @return true if all subordinate checks return true
// */
//@Override
//boolean check(){
//    if( !super.check() ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: structure element internal check failed." );
//        return false;
//    }
//    if( operation == null ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation without operation." );
//        return false;
//    }
//    if( getName().isEmpty() ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation name empty." );
//        return false;
//    }
//    if( parent == null ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation " + getName() + " without component." );
//        return false;
//    }
//    if( root && !children.isEmpty() ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: root operation " + getName() + " with executions." );
//        return false;
//    }
//    if( !root && children.isEmpty() ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: non-root operation " + getName() + " without executions." );
//        return false;
//    }
//    if( getAnomalyCount() < 0 || getAnomalyCount() > getExecutionCount() ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation " + getName() + " has weird anomaly count: " + getAnomalyCount() );
//        return false;
//    }
//    if( anomalyRating < -1.0 || anomalyRating > 1.0 ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation " + getName() + " has weird anomaly rating: " + anomalyRating );
//        return false;
//    }
//    if( causeRating < -1.0 || causeRating > 1.0 ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation " + getName() + " has weird cause rating: " + causeRating );
//        return false;
//    }
//    if( percent < 0.0 || percent > 100.0 ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation " + getName() + " has weird percent rating: " + percent );
//        return false;
//    }
//    int value;
//    for( Integer opCount : incoming.values() ){
//        value = opCount.intValue();
//        if( value < 1 ){
//            Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation " + getName() + " has weird incoming connection count: " + value );
//            return false;
//        }
//    }
//    long countIncoming = getIncomingConnectionCount( null );
//    if( countIncoming != children.size() ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation " + getName() + " incoming connection count (" + countIncoming + ") does not match execution count (" + children.size() + ")." );
//        return false;
//    }
//    for( Integer opCount : outgoing.values() ){
//        value = opCount.intValue();
//        if( value < 1 ){
//            Util.writeOut( Util.LEVEL_WARNING, "< WARNING: operation " + getName() + " has weird outgoing connection count: " + value );
//            return false;
//        }
//    }
//    if( root ){
//        if( !incoming.isEmpty() || !incomingRel.isEmpty() ){
//            Util.writeOut( Util.LEVEL_WARNING, "< WARNING: root operation " + getName() + " has incoming connections." );
//        }
//        if( outgoing.isEmpty() || outgoingRel.isEmpty() ){
//            Util.writeOut( Util.LEVEL_WARNING, "< WARNING: root operation " + getName() + " has no outgoing connections." );
//            return false;
//        }
//    }
//    else if( incoming.isEmpty() || incomingRel.isEmpty() ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: non-root operation " + getName() + " has no incoming connections." );
//        return false;
//    }
//    return true;
//}
//
//}
