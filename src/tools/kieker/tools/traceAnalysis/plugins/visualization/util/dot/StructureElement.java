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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class serves as a super class for all classes that are part of the
 * dependency structure resp. that each form a node of the dependency graph.
 * 
 * Methods are provided to build up and access the dependency structure.
 * 
 * Instances of this class can be automatically sorted by their cause rating.
 * 
 * @author Nina
 */
public abstract class StructureElement implements Comparable<StructureElement> {

StructureElement parent;
Map<StructureElement, Integer> incoming;	// these depend on me (they call me), and how often
Map<StructureElement, Integer> outgoing;	// I depend on these  (I call them), and how often
Map<StructureElement, Double> incomingRel;
Map<StructureElement, Double> outgoingRel;
Set<StructureElement> children;
double anomalyRating;
double causeRating;
double percent;
boolean max;                                // I am the component with the highest causeRating percent
boolean root;

/**
 * This default constructor initializes several attributes.
 * @param parent parent object in the dependency structure
 * @param isRoot whether this element is considered a root element
 */
StructureElement( final StructureElement parent, final boolean isRoot ){
    this.parent = parent;
    this.incoming = new HashMap<StructureElement, Integer>();   // initialization instead above does not work !
    this.outgoing = new HashMap<StructureElement, Integer>();
    this.incomingRel = new HashMap<StructureElement, Double>();
    this.outgoingRel = new HashMap<StructureElement, Double>();
    this.children = new HashSet<StructureElement>();
    this.anomalyRating = 0.0;
    this.causeRating = 0.0;
    this.percent = 0.0;
    this.max = false;
    this.root = isRoot;
}

/**
 * Fetches the name of this element.
 * @return name of this element
 */
public abstract String getName();

/**
 * Fetches a short name of this element.
 * Some parts may be cut off.
 * @return short version of the name of this element
 */
public abstract String getShortName();

/**
 * Adds an element that is part of this element.
 * @return true if actually added, false if set is unchanged
 */
boolean addChild( final StructureElement child ){
    return this.children.add( child );
}

/**
 * Adds an element that depends on this element.
 * @param neighbor neighbor element that calls services of this operation
 */
void addIncomingDependency( final StructureElement neighbor ){
    assert this.getClass().isInstance( neighbor );
    final Integer count = this.incoming.get( neighbor );
    this.incoming.put( neighbor, count == null ? 1 : count + 1 );
}

/**
 * Adds an element that this element depends on.
 * The elements have to be on the same level in hierarchy, e.g. both must be Operations, or both Components.
 * Also connects respective parent elements, if applicable.
 * @param neighbor neighbor element whose services are called by this element
 */
void addOutgoingDependency( final StructureElement neighbor ){
    assert this.getClass().isInstance( neighbor );
    final Integer count = this.outgoing.get( neighbor );
    if( count == null ){                                // not knowing this yet?
        final StructureElement par = neighbor.getParent();
        if( (par != null) && !par.equals( this.parent ) ){     // children of different parents ?
            this.parent.addOutgoingDependency( par );        // link
            par.addIncomingDependency( this.parent );        // backlink
        }
    }
    this.outgoing.put( neighbor, count == null ? 1 : count + 1 );
}

/**
 * Adds relative edge weights to all connections.
 * Don't call before the absolute connections are complete!
 */
void calculateRelativeEdgeWeights(){
    long connectionSum = this.getIncomingConnectionCount( null );
    for( final StructureElement neighbor : this.incoming.keySet() ){
        this.incomingRel.put( neighbor, (double) this.incoming.get( neighbor ) / connectionSum );
    }
    connectionSum = this.getOutgoingConnectionCount( null );
    for( final StructureElement neighbor : this.outgoing.keySet() ){
        this.outgoingRel.put( neighbor, (double) this.outgoing.get( neighbor ) / connectionSum );
    }
}

/**
 * Fetches the directly connected element that is on the hierarchy level above the current level.
 * E.g. deployment contexts are parents of components.
 * @return parent element
 */
StructureElement getParent(){
    return this.parent;
}

/**
 * Fetches the directly connected elements that are on the hierarchy level below the current level.
 * E.g. operations are children of components.
 * @return child elements
 */
Set<StructureElement> getChildren(){
    return this.children;
}

/**
 * Fetches the directly connected elements that call this element, aka incoming connections.
 * @return calling elements
 */
Set<StructureElement> getCallers(){
    return this.incoming.keySet();
}

/**
 * Fetches the directly connected elements that are called by this element, aka outgoing connections.
 * @return called elements
 */
Set<StructureElement> getCallees(){
    return this.outgoing.keySet();
}

public double getAnomalyRating(){
    return this.anomalyRating;
}

void setAnomalyRating( final double anomalyRating ){
    this.anomalyRating = anomalyRating;
}

public double getCauseRating(){
    return this.causeRating;
}

void setCauseRating( final double causeRating ){
    this.causeRating = causeRating;
}

public double getPercent(){
	return this.percent;
}

void setPercent( final double percent ){
	this.percent = percent;
}

/**
 * @param max wether this is the component with the highest cause rating
 */
void setMax( final boolean max ){
    this.max = max;
}

boolean isMax(){
    return this.max;
}

boolean isRoot(){
    return this.root;
}

/**
 * This method is not fully implemented in this super class,
 * it has to be overridden by a class that need this function.
 * @return false
 */
boolean isOutlier(){
    return false;
}

/**
 * Tests if this element is the only one on its level (op,comp,dc).
 * @return true if this element has no outgoing connections,
 * and the only incoming connection itself has root flag set,
 * and it has only one outgoing connection (that should be me).
 */
boolean isLonely(){
    if( !this.outgoing.isEmpty() || (this.incoming.size() > 1) ){   // anybody out there?
        return false;                                   // I am not alone!
    }
    final StructureElement potentialNeighbor = this.incoming.keySet().iterator().next();
    return potentialNeighbor.isRoot() && (potentialNeighbor.outgoing.size() == 1);
}

///**
// * Fetches all sibling elements: they are &quot;besides&quot; the current element,
// * on the same structure level, but not directly connected.
// * Note: Neither tested nor used yet. Maybe helpful for future use.
// * @return set of sibling elements
// */
//private Set<StructureElement> getSiblings(){
//    final Set<StructureElement> siblings = new HashSet<StructureElement>();
//    for( final StructureElement in : this.incoming.keySet() ){
//        for( final StructureElement sibling : in.outgoing.keySet() ){
//            if( this != sibling ){
//                siblings.add( sibling );
//            }
//        }
//    }
//    for( final StructureElement out : this.outgoing.keySet() ){
//        for( final StructureElement sibling : out.incoming.keySet() ){
//            if( this != sibling ){
//                siblings.add( sibling );
//            }
//        }
//    }
//    return siblings;
//}

long getOutlierCount(){
    long count = 0L;
    for( final StructureElement child : this.children ){
        count += child.getOutlierCount();
    }
    return count;
}

long getAnomalyCount(){
    long count = 0L;
    for( final StructureElement child : this.children ){
        count += child.getAnomalyCount();
    }
    return count;
}

long getExecutionCount(){
    long count = 0L;
    for( final StructureElement child : this.children ){
        count += child.getExecutionCount();
    }
    return count;
}

/**
 * Fetches all anomaly scores of all child elements.
 * @return list of childrens' anomaly scores
 */
List<Double> getChildrensAnomalyScores(){
    final List<Double> result = new ArrayList<Double>();
    for( final StructureElement child : this.children ){
        result.addAll( child.getChildrensAnomalyScores() );
    }
    return result;
}

/**
 * Fetches all response times of all child elements.
 * @return list of childrens' response times
 */
List<Long> getChildrensResponseTimes(){
    final List<Long> result = new ArrayList<Long>();
    for( final StructureElement child : this.children ){
        result.addAll( child.getChildrensResponseTimes() );
    }
    return result;
}

/**
 * Constructs a histogram of the anomaly scores or response times of all executions of this element.
 * The values are stretched to full scale, i.e. the highest value always is set to 1.0.
 * @param width number of columns in the histogram.
 * @param type select 1 for anomaly score, or 2 for response times
 * @return array of percentages [0.0,1.0], all zeroes for root
 */
//@Deprecated
//private double[] getHistogram( int width, int type ){
//    double[] values = new double[ width ];
//    if( isRoot() ){
//        return values;                                  // return all zeroes
//    }
//    int column;
//    double perc;
//    switch( type ){
//        case 1:
//            for( Double rating : getChildrensAnomalyScores() ){
//                perc = Util.scaleRatingToPercent( rating );     // stretch to [ 0.0, 1.0 ]
//                column = (int) ( perc * ( width - 1 ) );        // find column in [ 0, width-1 ]
//                values[column]++;                               // increase column
//            }
//            break;
//        case 2:
//            List<Double> rts = Util.longListToDoubleList( getChildrensResponseTimes() );
//            double low = Util.getMin( rts );
//            double high = Util.getMax( rts );
//            for( Double rt : rts ){
//                perc = ( rt - low ) / ( high - low );           // stretch to [ 0.0, 1.0 ]
//                column = (int) ( perc * ( width - 1 ) );        // find column in [ 0, width-1 ]
//                values[column]++;                               // increase column
//            }
//            break;
//        default:
//            throw new IllegalArgumentException( "Invalid histogram type: " + type );
//    }
//    double maxValue = Util.getMax( values );
//    for( column = 0; column < width; column++ ){
//        values[column] /= maxValue;                     // stretch to [ 0.0, 1.0 ]
//    }
//    return values;
//}

/**
 * Calculates the number of connections from the specified element to this element;
 * or <em>all</em> incoming connections.
 * @param other element to be questioned (supposed to be a component, or deployment context), or null
 * @return number of connections from the specified element; or all connections, if other == null
 */
long getIncomingConnectionCount( final StructureElement other ){
    long count = 0L;
    for( final StructureElement child : this.children ){
        count += child.getIncomingConnectionCount( other );
    }
    return count;
}

/**
 * Calculates the number of connections from this element to the specified element;
 * or <em>all</em> outgoing connections.
 * @param other element to be questioned (supposed to be a component, or deployment context), or null
 * @return number of connections to the specified element; or all connections, if other == null
 */
long getOutgoingConnectionCount( final StructureElement other ){
    long count = 0L;
    for( final StructureElement child : this.children ){
        count += child.getOutgoingConnectionCount( other );
    }
    return count;
}

/**
 * It returns:
 * <ul>
 * <li>-1 : if it is unknown whether this execution was during an anomaly injection period
 * or not (the anomalyInjection intervall data was not combined with the monitoring data yet)</li>
 * <li>0 : if it is not an anomaly (=true normaly) (= was not during an anomaly injection period)</li>
 * <li>1 : if it is an anomaly (=true anomaly) (= was during an anomaly injection period)</li>
 * <li>2 : if it is unclear whether it is an anomaly or not (at injection period boundry).</li>
 * </ul>
 * (Description copied from EvaluatedExecution.)
 * @return status -1 if unknown yet, 0 if not anomaly, 1 if anomaly, 2 if unclear
 */
int getTrueAnomalyStatus(){
    int status = -1;
    childrenLoop:
    for( final StructureElement child : this.children ){
        switch( child.getTrueAnomalyStatus() ){
            case -1:
                continue childrenLoop;  // ignore
            case 0:
                if( status < 0 ){
                    status = 0;         // increase if lower
                }
                continue childrenLoop;
            case 1:
                status = 1;
                break childrenLoop;     // abort search
            case 2:
                if( status < 0 ){
                    status = 2;         // increase if lower
                }
                continue childrenLoop;
            default:
                throw new RuntimeException( "Anomaly status unknown: " + child.getTrueAnomalyStatus() );
        }
    }
    return status;
}

/**
 * Checks a selection of internal variables for consistency.
 * @return true if all subordinate checks return true
 */
boolean check(){
    if( !this.incoming.keySet().equals( this.incomingRel.keySet() ) ){
        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: incoming connection pools unequal in element " + this.getName() );
        return false;
    }
    if( !this.outgoing.keySet().equals( this.outgoingRel.keySet() ) ){
        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: outgoing connection pools unequal in element " + this.getName() );
        return false;
    }
    return true;
}

/**
 * Compares two StructureElement objects by their causeRating.
 * @param other the StructureElement to be compared.
 * @return 1 if this' causeRating greater than other's; -1 else. 0 is returned if both have same causeRating.
 */
@Override
public int compareTo( final StructureElement other ){
    if( this.getCauseRating() < other.getCauseRating() ){
        return -1;
    }
    if( this.getCauseRating() > other.getCauseRating() ){
        return 1;
    }    
    return 0;
}

}
