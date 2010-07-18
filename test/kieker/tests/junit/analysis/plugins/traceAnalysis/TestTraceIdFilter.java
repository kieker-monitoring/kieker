package kieker.tests.junit.analysis.plugins.traceAnalysis;

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
import kieker.analysis.plugin.traceAnalysis.executionFilter.TimestampFilter;
import kieker.analysis.plugin.traceAnalysis.executionFilter.TraceIdFilter;
import kieker.tests.junit.analysis.plugins.traceAnalysis.util.ExecutionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TestTraceIdFilter extends TestCase {

    private static final Log log = LogFactory.getLog(TestTraceIdFilter.class);

    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory eFactory = new ExecutionFactory(systemEntityFactory);

    /**
     * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
     * assert that an Execution object <i>exec</i> with traceId not element of
     * <i>idsToPass</i> is not passed through the filter.
     */
    public void testAssertIgnoreTraceId() {
        TreeSet<Long> idsToPass = new TreeSet<Long>();
        idsToPass.add(5l);
        idsToPass.add(7l);
        
        TraceIdFilter filter =
                new TraceIdFilter(idsToPass);

        final Execution exec = eFactory.genExecution(
                11l,     // traceId (must not be element of idsToPass)
                5,      // tin (value not important)
                10,     // tout (value not important)
                0, 0);  // eoi, ess (values not important)
        assertTrue("Testcase invalid", !idsToPass.contains(exec.getTraceId()));

        final AtomicReference<Boolean> filterPassedRecord =
                new AtomicReference<Boolean>(Boolean.FALSE);

        filter.getExecutionOutputPort().subscribe(new AbstractInputPort<Execution>("Execution input") {

            /**
             * In this test, this method should not be called.
             */
            public void newEvent(Execution event) {
                filterPassedRecord.set(Boolean.TRUE);
            }
        });
        filter.getExecutionInputPort().newEvent(exec);
        assertFalse("Filter passed execution " + exec
            + " although traceId not element of " +
            idsToPass,
            filterPassedRecord.get());
    }

    /**
     * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
     * assert that an Execution object <i>exec</i> with traceId element of
     * <i>idsToPass</i> is passed through the filter.
     */
    public void testAssertPassTraceId() {
        TreeSet<Long> idsToPass = new TreeSet<Long>();
        idsToPass.add(5l);
        idsToPass.add(7l);

        TraceIdFilter filter =
                new TraceIdFilter(idsToPass);

        final Execution exec = eFactory.genExecution(
                7l,     // traceId (must be element of idsToPass)
                5,      // tin (value not important)
                10,     // tout (value not important)
                0, 0);  // eoi, ess (values not important)
        assertTrue("Testcase invalid", idsToPass.contains(exec.getTraceId()));

        final AtomicReference<Boolean> filterPassedRecord =
                new AtomicReference<Boolean>(Boolean.FALSE);

        filter.getExecutionOutputPort().subscribe(new AbstractInputPort<Execution>("Execution input") {

            /**
             * In this test, this method must be called.
             */
            public void newEvent(Execution event) {
                filterPassedRecord.set(Boolean.TRUE);
                assertSame(exec, event);
            }
        });
        filter.getExecutionInputPort().newEvent(exec);
        assertTrue("Filter didn't pass execution " + exec
            + " although traceId element of " +
            idsToPass,
            filterPassedRecord.get());
    }
}