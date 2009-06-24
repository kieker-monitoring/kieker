package kieker.loganalysis.datamodel;

import java.util.Arrays;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn, Lena St&ouml;ver
 */
public class AdjacencyMatrix {
    private static final Log log = LogFactory.getLog(AdjacencyMatrix.class);

    // Maps component ids (matrix indices) to component name and vice versa
    private String[] componentId2NameMap = new String[0];
    private Hashtable<String,Integer> componentName2IdMap = new Hashtable<String,Integer>();

    // Dynamically resizable adjacency matrix
    // matrix[Sender][Receiver] holds the weights of the edges of the dependency graph
    //private ArrayList<ArrayList<Long>> matrix = new ArrayList<ArrayList<Long>>();
    private long[][] matrix = new long[0][0];

    public long[][] getMatrixAsArray(){
        return this.matrix;
    }

    public String[] getComponentNames(){
        return this.componentId2NameMap;
    }

    /**
     * Adds a new component to the adjacency matric and
     * returns the id of this newly added component.
     *
     * @param componentName
     * @return
     */
    private int addNewComponentToMatrix(String componentName){
        int newComponentId = this.matrix.length;
        this.matrix = Arrays.copyOf(this.matrix, newComponentId+1);
        // increment dimension of old columns
        for (int i=0; i<newComponentId; i++){
            this.matrix[i]=Arrays.copyOf(this.matrix[i], newComponentId+1);
        }
        this.matrix[newComponentId] = new long[newComponentId+1];
        this.componentId2NameMap=Arrays.copyOf(this.componentId2NameMap, newComponentId+1);
        this.componentId2NameMap[newComponentId]=componentName;
        this.componentName2IdMap.put(componentName,newComponentId);
        return newComponentId;
    }

    public void addDependency(String senderComponentName, String receiverComponentName){
        /* lookup or create component ids */
        Integer senderComponentId = this.componentName2IdMap.get(senderComponentName);
        Integer receiverComponentId = this.componentName2IdMap.get(receiverComponentName);
        if(senderComponentId == null){
            senderComponentId = new Integer(addNewComponentToMatrix(senderComponentName));
        }
        if(receiverComponentId == null){
            receiverComponentId = new Integer(addNewComponentToMatrix(receiverComponentName));
        }

        /* increment dependency value */
        this.matrix[senderComponentId][receiverComponentId]++;
    }

    public String toString(){
        StringBuilder strBuilder = new StringBuilder("\n");
        for (int i=0; i<this.matrix.length; i++){ // forall sender
            String curSenderComponentName = this.componentId2NameMap[i];
            long[] receiverArray = this.matrix[i];
            for (int j=0; j<receiverArray.length; j++){ // forall receivers
                if (receiverArray[j]>0){
                    String curReceiverComponentName = this.componentId2NameMap[j];
                    strBuilder.append(curSenderComponentName+"|("+receiverArray[j]+")->"+curReceiverComponentName+"\n");
                }
            }
        }
        return strBuilder.toString();
    }
}