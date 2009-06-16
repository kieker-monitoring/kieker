package kieker.tpmon.monitoringRecords;

import java.io.Serializable;
import kieker.tpmon.annotations.TpmonInternal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.RemoteCallMetaData
 * 
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project 
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
 * @author Matthias Rohr
 */
public class RemoteCallMetaData implements Serializable {

    private static final long serialVersionUID = 512771634L;    // random number
    private static final Log log = LogFactory.getLog(RemoteCallMetaData.class);

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
        log.info("RemoteCallMetaData: "+traceid+" "+eoi+" "+ess);
    }
}
