
package kieker.tpmon.aspects;

import java.util.HashMap;
import java.util.Random;

/**
 * This is a central place for realizing a random registry 
 * of instrumented operations.
 * 
 * A random subset of all operations will receive a isMonitored(..) == true
 * 
 * This method is used by the experimental aspect TpmonRandomPartialnstumentationServletRemote.
 * 
 * The overhead that results form "deactivated" operations should be evaluated.
 * 
 * @author matthias
 */
public class TpmonRandomInstrumentationController {
    
    private static HashMap<String, Boolean> monitoredOperations = new HashMap<String,Boolean>();
    private static Random randomGen = new Random();
    private static double probabilityToInstrument = randomGen.nextDouble(); 
    
    @TpmonInternal
    public static synchronized Boolean isMonitored(String signature) {        
        Boolean hashedObject = monitoredOperations.get(signature);
        if (hashedObject != null) {
            return hashedObject;
        } else { //
            if (randomGen.nextDouble() <= probabilityToInstrument) {
                monitoredOperations.put(signature, true);
                return true;
            } else {
                monitoredOperations.put(signature, false);
                return false;
            }
        }                    
    }
    
   
}
