package kieker.common.logReader;

/*
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
 * This reader allows one to read a folder or an single tpmon file and
 * transforms it to monitoring events that are stored in the file system
 * again, written to a database, or whatever tpmon is configured to do
 * with the monitoring data.
 */

/**
 *
 * @author Andre van Hoorn
 */
public class LogReaderExecutionException extends Exception {

    public static final long serialVersionUID = 1L;

    public LogReaderExecutionException(String messString) {
        super(messString);
    }

    public LogReaderExecutionException(String messString, Throwable cause) {
        super(messString, cause);
    }
}
