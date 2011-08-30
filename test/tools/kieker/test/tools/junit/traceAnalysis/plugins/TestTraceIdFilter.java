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

package kieker.test.tools.junit.traceAnalysis.plugins;

import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;
import kieker.tools.traceAnalysis.plugins.executionFilter.TraceIdFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public class TestTraceIdFilter extends TestCase {

    //private static final Log log = LogFactory.getLog(TestTraceIdFilter.class);

    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory eFactory = new ExecutionFactory(this.systemEntityFactory);

    /**
     * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
     * assert that an Execution object <i>exec</i> with traceId not element of
     * <i>idsToPass</i> is not passed through the filter.
     */
    public void testAssertIgnoreTraceId() {
        final TreeSet<Long> idsToPass = new TreeSet<Long>();
        idsToPass.add(5l);
        idsToPass.add(7l);
        
        final TraceIdFilter filter =
                new TraceIdFilter(idsToPass);

        final Execution exec = this.eFactory.genExecution(
                11l,     // traceId (must not be element of idsToPass)
                5,      // tin (value not important)
                10,     // tout (value not important)
                0, 0);  // eoi, ess (values not important)
        Assert.assertTrue("Testcase invalid", !idsToPass.contains(exec.getTraceId()));

        final AtomicReference<Boolean> filterPassedRecord =
                new AtomicReference<Boolean>(Boolean.FALSE);

        filter.getExecutionOutputPort().subscribe(new AbstractInputPort<Execution>("Execution input") {

            /**
             * In this test, this method should not be called.
             */
            @Override
			public void newEvent(final Execution event) {
                filterPassedRecord.set(Boolean.TRUE);
            }
        });
        filter.getExecutionInputPort().newEvent(exec);
        Assert.assertFalse("Filter passed execution " + exec
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
        final TreeSet<Long> idsToPass = new TreeSet<Long>();
        idsToPass.add(5l);
        idsToPass.add(7l);

        final TraceIdFilter filter =
                new TraceIdFilter(idsToPass);

        final Execution exec = this.eFactory.genExecution(
                7l,     // traceId (must be element of idsToPass)
                5,      // tin (value not important)
                10,     // tout (value not important)
                0, 0);  // eoi, ess (values not important)
        Assert.assertTrue("Testcase invalid", idsToPass.contains(exec.getTraceId()));

        final AtomicReference<Boolean> filterPassedRecord =
                new AtomicReference<Boolean>(Boolean.FALSE);

        filter.getExecutionOutputPort().subscribe(new AbstractInputPort<Execution>("Execution input") {

            /**
             * In this test, this method must be called.
             */
            @Override
			public void newEvent(final Execution event) {
                filterPassedRecord.set(Boolean.TRUE);
                Assert.assertSame(exec, event);
            }
        });
        filter.getExecutionInputPort().newEvent(exec);
        Assert.assertTrue("Filter didn't pass execution " + exec
            + " although traceId element of " +
            idsToPass,
            filterPassedRecord.get());
    }
}