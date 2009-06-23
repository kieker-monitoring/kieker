package kieker.loganalysis.datamodel;

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
    public boolean callMessage;
    public long timestamp;
    public String sender; // TODO: vm
    public String receiver; // TODO: vm
    public KiekerExecutionRecord execution;
     
    public Message(boolean callMessage, long timestamp, String sender, 
            String receiver, KiekerExecutionRecord execution){
        this.callMessage = callMessage;
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
        this.execution = execution;
    }
    
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(this.timestamp); strBuild.append(':');
        strBuild.append((this.sender!=null)?this.sender:"$");
       if(!this.callMessage&&this.sender!=null){
            strBuild.append("."+this.execution.opname);
        }
        strBuild.append(this.callMessage?"|-send->":"|-return->");
        strBuild.append((this.receiver!=null)?this.receiver:"$");
        if(this.callMessage&&this.receiver!=null){
            strBuild.append("."+this.execution.opname);
        }
        return strBuild.toString();
    }
}
