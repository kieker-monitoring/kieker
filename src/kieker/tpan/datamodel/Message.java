package kieker.tpan.datamodel;

import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

/**
 * kieker.loganalysis.datamodel.Message
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
 * @author Andre van Hoorn
 */

public class Message {
    public final boolean callMessage;
    public final long timestamp;
    //public String senderComponentName; // TODO: remove
    //public String receiverComponentName; // TODO: remove
    public final KiekerExecutionRecord sender, receiver, execution;
     
    public Message(boolean callMessage, long timestamp, KiekerExecutionRecord sender, 
            KiekerExecutionRecord receiver, KiekerExecutionRecord execution){
        this.callMessage = callMessage;
        this.timestamp = timestamp;
        //this.senderComponentName = sender;
        //this.receiverComponentName = receiver;
        this.sender = sender;
        this.receiver = receiver;
        this.execution = execution;
    }
    
    public String toString(){
        StringBuilder strBuild = new StringBuilder();

        strBuild.append(this.callMessage?"S":"R").append(" ");
        strBuild.append(this.timestamp); strBuild.append(" ");
       if(this.sender != null){   
            strBuild.append("[").append(this.sender.eoi)
                    .append(",").append(this.sender.ess).append("]");
            strBuild.append(this.sender.vmName).append("::");
        }
        strBuild.append(this.getSenderComponentName());
       if(this.sender != null){           
            strBuild.append(".").append(this.sender.opname);
        }
        strBuild.append(" --> ");
       if(this.receiver != null){           
            strBuild.append("[").append(this.receiver.eoi)
                    .append(",").append(this.receiver.ess).append("]");
            strBuild.append(this.receiver.vmName).append("::");
        }
        strBuild.append(this.getReceiverComponentName());
        if(this.receiver != null){
            strBuild.append(".").append(this.receiver.opname);
        }
        
//        strBuild.append(this.timestamp); strBuild.append(':');
//        strBuild.append(this.getSenderComponentName());
//       if(!this.callMessage&&this.senderComponentName!=null){
//            strBuild.append("."+this.execution.opname);
//        }
//        strBuild.append(this.callMessage?"|-send->":"|-return->");
//        strBuild.append(this.getReceiverComponentName());
//        if(this.callMessage&&this.receiverComponentName!=null){
//            strBuild.append("."+this.execution.opname);
//        }
        return strBuild.toString();
    }

    /** Convenience function which returns "$" in case 'senderComponentName' field is null */
    public String getSenderComponentName(){
        return (this.sender==null)?"$":this.sender.componentName;
    }

    /** Convenience function which returns "$" in case 'senderComponentName' field is null */
    public String getReceiverComponentName(){
        return (this.receiver==null)?"$":this.receiver.componentName;
    }
}
