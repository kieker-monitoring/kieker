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
