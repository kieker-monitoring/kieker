/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author matthias
 */
public class FaultInjectionRegistry {
    // this is a hashmap in the tpmon library that allows to share an atomic boolean that can be used
    // as fault injection switch
    private static ConcurrentHashMap<String, AtomicBoolean> faultInjectionActiveSwitches = new  ConcurrentHashMap<String, AtomicBoolean>();
    

    public static synchronized AtomicBoolean getSwitch(String switchName){
        AtomicBoolean mySwitch = faultInjectionActiveSwitches.get(switchName);
        if (mySwitch == null) {
            mySwitch = new AtomicBoolean(false);
            faultInjectionActiveSwitches.put(switchName, mySwitch);
        }
        return mySwitch;
    }
    
}
