package kieker.tests.junit.analysis.datamodel;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import junit.framework.TestCase;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.traceAnalysis.executionFilter.TraceIdFilter;
import kieker.tests.junit.analysis.plugins.traceAnalysis.util.ExecutionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TestExecutionTrace extends TestCase {

    private static final Log log = LogFactory.getLog(TestExecutionTrace.class);

    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory eFactory = new ExecutionFactory(systemEntityFactory);

    public void testMessageTraceTransformationValidTrace() {
        final Execution exec0_0 = eFactory.genExecution(
                69898l,     // traceId
                1,      // tin
                10,     // tout 
                0, 0);  // eoi, ess
        final Execution exec1_1 = eFactory.genExecution(
                69898l,     // traceId
                2,      // tin
                4,     // tout
                1, 1);  // eoi, ess
        final Execution exec2_1 = eFactory.genExecution(
                69898l,     // traceId
                5,      // tin
                8,     // tout
                2, 1);  // eoi, ess
        final Execution exec3_2 = eFactory.genExecution(
                69898l,     // traceId
                6,      // tin
                7,     // tout
                3, 2);  // eoi, ess
    }
}