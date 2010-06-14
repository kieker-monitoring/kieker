package kieker.analysis.plugin.util.dot;

//package kieker.tpan.plugins.util.dot;
//
//import org.trustsoft.timingBehaviorAnomalyDetection.EvaluatedExecution;
//
///**
// * This class implements unique functionality for executions,
// * which work as normal {@link StructureElement}s otherwise.
// *
// * Executions usually have a {@link StructureOperation} as parent,
// * they have no children,
// * and are not linked among each other.
// *
// * @author Nina
// */
//class StructureExecution extends StructureElement {
//
//private EvaluatedExecution execution;
//
//StructureExecution( EvaluatedExecution exec, StructureElement parent ){
//    super( parent, false );
//    this.execution = exec;
//    this.anomalyRating = exec.getAnomalyScore();
//    this.causeRating = anomalyRating;               // ATTENTION !  other rating because of "renaming" between levels
//}
//
//public String getName(){
//    return execution.getIndexNotation();
//}
//
//public String getShortName(){
//    return execution.getShortIndexNotation();
//}
//
//@Override
//boolean isOutlier(){
//    return execution.isConsideredOutlier();
//}
//
//@Override
//long getOutlierCount(){
//    return execution.isConsideredOutlier() ? 1L : 0L;
//}
//
//@Override
//long getAnomalyCount(){
//    return execution.isConsideredAnomaly() ? 1L : 0L;
//}
//
//@Override
//int getTrueAnomalyStatus(){
//    return execution.getTrueAnomalyStatus();
//}
//
//long getResponseTime(){
//    return execution.getResponseTime();
//}
//
///**
// * Checks a selection of internal variables for consistency.
// * @return true if all subordinate checks return true
// */
//@Override
//boolean check(){
//    boolean result = true;
//    if( !super.check() ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: structure element internal check failed." );
//        return false;
//    }
//    if( execution == null ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: execution without execution." );
//        result = false;
//    }
//    if( parent == null ){
//        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: execution " + getName() + " without operation." );
//        return false;
//    }
//    return result;
//}
//
//}
