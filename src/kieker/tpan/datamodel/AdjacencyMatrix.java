package kieker.tpan.datamodel;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn, Lena St&ouml;ver
 */
public class AdjacencyMatrix {
    private static final Log LOG = LogFactory.getLog(AdjacencyMatrix.class);

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
        this.matrix = copyOf(this.matrix, newComponentId+1);
        // increment dimension of old columns
        for (int i=0; i<newComponentId; i++){
            this.matrix[i] = copyOf(this.matrix[i], newComponentId+1);
        }
        this.matrix[newComponentId] = new long[newComponentId+1];
        this.componentId2NameMap = copyOf(this.componentId2NameMap, newComponentId+1);
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
    
    //XXX the following is copied from JDK 1.6 java.util.Arrays 
    
    /**
     * Copies the specified array, truncating or padding with zeros (if necessary)
     * so the copy has the specified length.  For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values.  For any indices that are valid in the
     * copy but not the original, the copy will contain <tt>0L</tt>.
     * Such indices will exist if and only if the specified length
     * is greater than that of the original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with zeros
     *     to obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     */
    private long[] copyOf(long[] original, int newLength) {
        long[] copy = new long[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }
    
    // Cloning
    /**
     * Copies the specified array, truncating or padding with nulls (if necessary)
     * so the copy has the specified length.  For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values.  For any indices that are valid in the
     * copy but not the original, the copy will contain <tt>null</tt>.
     * Such indices will exist if and only if the specified length
     * is greater than that of the original array.
     * The resulting array is of exactly the same class as the original array.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls
     *     to obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @since 1.6
     */
    public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }

    /**
     * Copies the specified array, truncating or padding with nulls (if necessary)
     * so the copy has the specified length.  For all indices that are
     * valid in both the original array and the copy, the two arrays will
     * contain identical values.  For any indices that are valid in the
     * copy but not the original, the copy will contain <tt>null</tt>.
     * Such indices will exist if and only if the specified length
     * is greater than that of the original array.
     * The resulting array is of the class <tt>newType</tt>.
     *
     * @param original the array to be copied
     * @param newLength the length of the copy to be returned
     * @param newType the class of the copy to be returned
     * @return a copy of the original array, truncated or padded with nulls
     *     to obtain the specified length
     * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
     * @throws NullPointerException if <tt>original</tt> is null
     * @throws ArrayStoreException if an element copied from
     *     <tt>original</tt> is not of a runtime type that can be stored in
     *     an array of class <tt>newType</tt>
     */
    public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        T[] copy = ((Object)newType == (Object)Object[].class)
            ? (T[]) new Object[newLength]
            : (T[]) Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }
}