/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon;

import java.io.Serializable;
import kieker.tpmon.annotations.TpmonInternal;

/**
 * This data has to be transported with the request data if a remote call is made.
 * Required for monitoring in distributed systems.
 * @author matthias
 */
public class RemoteCallMetaData implements Serializable{

    private static final long serialVersionUID = 512771634L;    // random number

    public int ess;
    public int eoi;
    public long traceid;

    public RemoteCallMetaData(long traceid, int eoi, int ess) {
        this.ess = ess;
        this.eoi = eoi;
        this.traceid = traceid;
    }
   
    @TpmonInternal()
    public void printinfo(){
        System.out.println("RemoteCallMetaData: "+traceid+" "+eoi+" "+ess);
    }
}