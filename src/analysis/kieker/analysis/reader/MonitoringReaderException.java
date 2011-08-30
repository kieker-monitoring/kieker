/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.analysis.reader;

import kieker.common.record.MonitoringRecordReceiverException;

/**
 *
 * @author Andre van Hoorn
 */
public class MonitoringReaderException extends MonitoringRecordReceiverException {

    private static final long serialVersionUID = 14537L;

    public MonitoringReaderException(String messString) {
        super(messString);
    }

    public MonitoringReaderException(String messString, Throwable cause) {
        super(messString, cause);
    }
}
