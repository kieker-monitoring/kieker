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
import java.util.concurrent.atomic.AtomicReference;
import junit.framework.TestCase;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.traceAnalysis.executionFilter.TimestampFilter;
import kieker.tests.junit.analysis.plugins.traceAnalysis.util.ExecutionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TestTimestampFilter extends TestCase {

    private static final Log log = LogFactory.getLog(TestTimestampFilter.class);
    private volatile long IGNORE_EXECUTIONS_BEFORE_TIMESTAMP = 50;
    private volatile long IGNORE_EXECUTIONS_AFTER_TIMESTAMP = 100;

    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory eFactory = new ExecutionFactory(systemEntityFactory);

    /**
     * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
     * assert that a record <i>r</i> with <i>r.tin &lt; a</i> and <i>r.tout
     * &gt; a </i>, <i>r.tout &lt; b</i> does not pass the filter.
     */
    public void testRecordTinBeforeToutWithinIgnored() {
        TimestampFilter filter =
                new TimestampFilter(
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        Execution exec = eFactory.genExecution(
                77, // traceId (value not important)
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP-1, // tin
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP-1,  // tout
                0, 0); // eoi, ess

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
            + " although tin timestamp before" + IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
            filterPassedRecord.get());
    }

    /**
     * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
     * assert that a record <i>r</i> with <i>r.tin &gt; a</i>, <i>r.tin
     * &lt; b</i> and <i>r.tout &gt; b </i> does not pass the filter.
     */
    public void testRecordTinWithinToutAfterIgnored() {
        TimestampFilter filter =
                new TimestampFilter(
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        Execution exec = eFactory.genExecution(
                15, // traceId (value not important)
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP+1, // tin
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP+1,  // tout
                0, 0); // eoi, ess

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
            + " although tout timestamp after" + IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
            filterPassedRecord.get());
    }

    /**
     * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
     * assert that a record <i>r</i> with <i>r.tin == a</i> and <i>r.tout == b </i>
     * does pass the filter.
     */
    public void testRecordTinToutOnBordersPassed() {
        TimestampFilter filter =
                new TimestampFilter(
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        final Execution exec = eFactory.genExecution(
                159, // traceId (value not important)
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, // tin
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP,  // tout
                0, 0); // eoi, ess

        final AtomicReference<Boolean> filterPassedRecord =
                new AtomicReference<Boolean>(Boolean.FALSE);

        filter.getExecutionOutputPort().subscribe(new AbstractInputPort<Execution>("Execution input") {

            /**
             * In this test, this method MUST be called exactly once.
             */
            public void newEvent(Execution event) {
                filterPassedRecord.set(Boolean.TRUE);
                assertSame(event, exec);
            }
        });
        filter.getExecutionInputPort().newEvent(exec);
        assertTrue("Filter didn't pass execution " + exec
            + " although timestamps within range [" +
            IGNORE_EXECUTIONS_BEFORE_TIMESTAMP + "," +
            IGNORE_EXECUTIONS_AFTER_TIMESTAMP + "]",
            filterPassedRecord.get());
    }

    /**
     * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
     * assert that a record <i>r</i> with <i>r.tin &gt; a</i>, <i>r.tin &lt; b</i>
     * and <i>r.tout &lt; b </i>, <i>r.tout &gt; a </i>  does pass the filter.
     */
    public void testRecordTinToutWithinRangePassed() {
        TimestampFilter filter =
                new TimestampFilter(
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        final Execution exec = eFactory.genExecution(
                159, // traceId (value not important)
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP+1, // tin
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP-1,  // tout
                0, 0); // eoi, ess

        final AtomicReference<Boolean> filterPassedRecord =
                new AtomicReference<Boolean>(Boolean.FALSE);

        filter.getExecutionOutputPort().subscribe(new AbstractInputPort<Execution>("Execution input") {

            /**
             * In this test, this method MUST be called exactly once.
             */
            public void newEvent(Execution event) {
                filterPassedRecord.set(Boolean.TRUE);
                assertSame(event, exec);
            }
        });
        filter.getExecutionInputPort().newEvent(exec);
        assertTrue("Filter didn't pass execution " + exec
            + " although timestamps within range [" +
            IGNORE_EXECUTIONS_BEFORE_TIMESTAMP + "," +
            IGNORE_EXECUTIONS_AFTER_TIMESTAMP + "]",
            filterPassedRecord.get());
    }
}