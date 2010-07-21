package kieker.tools.traceAnalysis.analysisPlugins.visualization.util.dot;

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
StructureElement( StructureElement parent, boolean isRoot ){
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
boolean addChild( StructureElement child ){
    return children.add( child );
}

/**
 * Adds an element that depends on this element.
 * @param neighbor neighbor element that calls services of this operation
 */
void addIncomingDependency( StructureElement neighbor ){
    assert getClass().isInstance( neighbor );
    Integer count = incoming.get( neighbor );
    incoming.put( neighbor, count == null ? 1 : count + 1 );
}

/**
 * Adds an element that this element depends on.
 * The elements have to be on the same level in hierarchy, e.g. both must be Operations, or both Components.
 * Also connects respective parent elements, if applicable.
 * @param neighbor neighbor element whose services are called by this element
 */
void addOutgoingDependency( StructureElement neighbor ){
    assert getClass().isInstance( neighbor );
    Integer count = outgoing.get( neighbor );
    if( count == null ){                                // not knowing this yet?
        StructureElement par = neighbor.getParent();
        if( par != null && !par.equals( parent ) ){     // children of different parents ?
            parent.addOutgoingDependency( par );        // link
            par.addIncomingDependency( parent );        // backlink
        }
    }
    outgoing.put( neighbor, count == null ? 1 : count + 1 );
}

/**
 * Adds relative edge weights to all connections.
 * Don't call before the absolute connections are complete!
 */
void calculateRelativeEdgeWeights(){
    long connectionSum = getIncomingConnectionCount( null );
    for( StructureElement neighbor : incoming.keySet() ){
        incomingRel.put( neighbor, (double) incoming.get( neighbor ) / connectionSum );
    }
    connectionSum = getOutgoingConnectionCount( null );
    for( StructureElement neighbor : outgoing.keySet() ){
        outgoingRel.put( neighbor, (double) outgoing.get( neighbor ) / connectionSum );
    }
}

/**
 * Fetches the directly connected element that is on the hierarchy level above the current level.
 * E.g. deployment contexts are parents of components.
 * @return parent element
 */
StructureElement getParent(){
    return parent;
}

/**
 * Fetches the directly connected elements that are on the hierarchy level below the current level.
 * E.g. operations are children of components.
 * @return child elements
 */
Set<StructureElement> getChildren(){
    return children;
}

/**
 * Fetches the directly connected elements that call this element, aka incoming connections.
 * @return calling elements
 */
Set<StructureElement> getCallers(){
    return incoming.keySet();
}

/**
 * Fetches the directly connected elements that are called by this element, aka outgoing connections.
 * @return called elements
 */
Set<StructureElement> getCallees(){
    return outgoing.keySet();
}

public double getAnomalyRating(){
    return anomalyRating;
}

void setAnomalyRating( double anomalyRating ){
    this.anomalyRating = anomalyRating;
}

public double getCauseRating(){
    return causeRating;
}

void setCauseRating( double causeRating ){
    this.causeRating = causeRating;
}

public double getPercent(){
	return percent;
}

void setPercent( double percent ){
	this.percent = percent;
}

/**
 * @param max wether this is the component with the highest cause rating
 */
void setMax( boolean max ){
    this.max = max;
}

boolean isMax(){
    return max;
}

boolean isRoot(){
    return root;
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
    if( !outgoing.isEmpty() || incoming.size() > 1 ){   // anybody out there?
        return false;                                   // I am not alone!
    }
    StructureElement potentialNeighbor = incoming.keySet().iterator().next();
    return potentialNeighbor.isRoot() && potentialNeighbor.outgoing.size() == 1;
}

/**
 * Fetches all sibling elements: they are &quot;besides&quot; the current element,
 * on the same structure level, but not directly connected.
 * Note: Neither tested nor used yet. Maybe helpful for future use.
 * @return set of sibling elements
 */
private Set<StructureElement> getSiblings(){
    Set<StructureElement> siblings = new HashSet<StructureElement>();
    for( StructureElement in : incoming.keySet() ){
        for( StructureElement sibling : in.outgoing.keySet() ){
            if( this != sibling ){
                siblings.add( sibling );
            }
        }
    }
    for( StructureElement out : outgoing.keySet() ){
        for( StructureElement sibling : out.incoming.keySet() ){
            if( this != sibling ){
                siblings.add( sibling );
            }
        }
    }
    return siblings;
}

long getOutlierCount(){
    long count = 0L;
    for( StructureElement child : children ){
        count += child.getOutlierCount();
    }
    return count;
}

long getAnomalyCount(){
    long count = 0L;
    for( StructureElement child : children ){
        count += child.getAnomalyCount();
    }
    return count;
}

long getExecutionCount(){
    long count = 0L;
    for( StructureElement child : children ){
        count += child.getExecutionCount();
    }
    return count;
}

/**
 * Fetches all anomaly scores of all child elements.
 * @return list of childrens' anomaly scores
 */
List<Double> getChildrensAnomalyScores(){
    List<Double> result = new ArrayList<Double>();
    for( StructureElement child : children ){
        result.addAll( child.getChildrensAnomalyScores() );
    }
    return result;
}

/**
 * Fetches all response times of all child elements.
 * @return list of childrens' response times
 */
List<Long> getChildrensResponseTimes(){
    List<Long> result = new ArrayList<Long>();
    for( StructureElement child : children ){
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
@Deprecated
private double[] getHistogram( int width, int type ){
    double[] values = new double[ width ];
    if( isRoot() ){
        return values;                                  // return all zeroes
    }
    int column;
    double perc;
    switch( type ){
        case 1:
            for( Double rating : getChildrensAnomalyScores() ){
                perc = Util.scaleRatingToPercent( rating );     // stretch to [ 0.0, 1.0 ]
                column = (int) ( perc * ( width - 1 ) );        // find column in [ 0, width-1 ]
                values[column]++;                               // increase column
            }
            break;
        case 2:
            List<Double> rts = Util.longListToDoubleList( getChildrensResponseTimes() );
            double low = Util.getMin( rts );
            double high = Util.getMax( rts );
            for( Double rt : rts ){
                perc = ( rt - low ) / ( high - low );           // stretch to [ 0.0, 1.0 ]
                column = (int) ( perc * ( width - 1 ) );        // find column in [ 0, width-1 ]
                values[column]++;                               // increase column
            }
            break;
        default:
            throw new IllegalArgumentException( "Invalid histogram type: " + type );
    }
    double maxValue = Util.getMax( values );
    for( column = 0; column < width; column++ ){
        values[column] /= maxValue;                     // stretch to [ 0.0, 1.0 ]
    }
    return values;
}

/**
 * Calculates the number of connections from the specified element to this element;
 * or <em>all</em> incoming connections.
 * @param other element to be questioned (supposed to be a component, or deployment context), or null
 * @return number of connections from the specified element; or all connections, if other == null
 */
long getIncomingConnectionCount( StructureElement other ){
    long count = 0L;
    for( StructureElement child : children ){
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
long getOutgoingConnectionCount( StructureElement other ){
    long count = 0L;
    for( StructureElement child : children ){
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
    for( StructureElement child : children ){
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
    if( !incoming.keySet().equals( incomingRel.keySet() ) ){
        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: incoming connection pools unequal in element " + getName() );
        return false;
    }
    if( !outgoing.keySet().equals( outgoingRel.keySet() ) ){
        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: outgoing connection pools unequal in element " + getName() );
        return false;
    }
    return true;
}

/**
 * Compares two StructureElement objects by their causeRating.
 * @param other the StructureElement to be compared.
 * @return 1 if this' causeRating greater than other's; -1 else. 0 is returned if both have same causeRating.
 */
public int compareTo( StructureElement other ){
    if( this.getCauseRating() < other.getCauseRating() ){
        return -1;
    }
    if( this.getCauseRating() > other.getCauseRating() ){
        return 1;
    }    
    return 0;
}

}
