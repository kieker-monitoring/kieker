package kieker.tpan.plugins.traceReconstructionPlugin;

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
 */

import kieker.tpan.plugins.util.event.EventProcessingException;

/**
 *
 * @author Andre van Hoorn
 */
public class TraceProcessingException extends EventProcessingException {
    private static final long serialVersionUID = 189899L;

    public TraceProcessingException (String msg){
        super(msg);
    }

    public TraceProcessingException (String msg, Throwable t){
        super(msg, t);
    }
}
