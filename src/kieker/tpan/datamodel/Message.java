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
            KiekerExecutionRecord receiver, KiekerExecutionRecord execution) {
        this.callMessage = callMessage;
        this.timestamp = timestamp;
        //this.senderComponentName = sender;
        //this.receiverComponentName = receiver;
        this.sender = sender;
        this.receiver = receiver;
        this.execution = execution;
    }

    public String toString() {
        StringBuilder strBuild = new StringBuilder();

        strBuild.append(this.callMessage ? "SND" : "RCV").append(" ");
        strBuild.append(this.timestamp);
        strBuild.append(" ");
        strBuild.append(this.getSenderLabel(true)); // include vmName
        if (this.sender != null) {
            strBuild.append(".").append(this.sender.opname);
            strBuild.append("[").append(this.sender.eoi).append(",").append(this.sender.ess).append("]");
        }
        strBuild.append(" --> ");
        strBuild.append(this.getReceiverLabel(true)); // include vmName
        if (this.receiver != null) {
            strBuild.append(".").append(this.receiver.opname);
            strBuild.append("[").append(this.receiver.eoi).append(",").append(this.receiver.ess).append("]");
        }

//        strBuild.append(this.timestamp); strBuild.append(':');
//        strBuild.append(this.getSenderLabel());
//       if(!this.callMessage&&this.senderComponentName!=null){
//            strBuild.append("."+this.execution.opname);
//        }
//        strBuild.append(this.callMessage?"|-send->":"|-return->");
//        strBuild.append(this.getReceiverLabel());
//        if(this.callMessage&&this.receiverComponentName!=null){
//            strBuild.append("."+this.execution.opname);
//        }
        return strBuild.toString();
    }

    /** Convenience function which returns "$" in case 'senderComponentName' field is null */
    public String getSenderLabel(final boolean includeHostname) {
        if (this.sender == null) {
            return "$";
        }
        if (includeHostname) {
            return this.sender.vmName + "::" + this.sender.componentName;
        } else {
            return this.sender.componentName;
        }
    }

    /** Convenience function which returns "$" in case 'senderComponentName' field is null */
    public String getReceiverLabel(final boolean includeHostname) {
        if (this.receiver == null) {
            return "$";
        }
        if (includeHostname) {
            return this.receiver.vmName + "::" + this.receiver.componentName;
        } else {
            return this.receiver.componentName;
        }
    }
}
