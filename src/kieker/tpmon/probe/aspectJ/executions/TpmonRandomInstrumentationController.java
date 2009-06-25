package kieker.tpmon.probe.aspectJ.executions;

import kieker.tpmon.annotation.TpmonInternal;
import java.util.HashMap;
import java.util.Random;

/**
 * kieker.tpmon.aspects.TpmonRandomInstrumentationController
 *
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * ==================================================
 *
 * This is a central place for realizing a random registry 
 * of instrumented operations.
 * 
 * A random subset of all operations will receive a isMonitored(..) == true
 * 
 * This method is used by the experimental aspect TpmonRandomPartialnstumentationServletRemote.
 * 
 * The overhead that results form "deactivated" operations should be evaluated.
 * 
 * @author Matthias Rohr
 */
public class TpmonRandomInstrumentationController {
    
    private static HashMap<String, Boolean> monitoredOperations = new HashMap<String,Boolean>();
    private static Random randomGen = new Random();
    private static double probabilityToInstrument = randomGen.nextDouble(); 
    
    @TpmonInternal()
    public static synchronized Boolean isMonitored(String signature) {        
        Boolean hashedObject = monitoredOperations.get(signature);
        if (hashedObject != null) {
            return hashedObject;
        }
        if (randomGen.nextDouble() <= probabilityToInstrument) {
            monitoredOperations.put(signature, true);
            return true;
        } else {
            monitoredOperations.put(signature, false);
            return false;
        }
    }
}
