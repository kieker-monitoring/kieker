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

import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;
import kieker.tools.traceAnalysis.plugins.executionFilter.TimestampFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public class TestTimestampFilter extends TestCase {

    //private static final Log log = LogFactory.getLog(TestTimestampFilter.class);
    
    private volatile long IGNORE_EXECUTIONS_BEFORE_TIMESTAMP = 50;
    private volatile long IGNORE_EXECUTIONS_AFTER_TIMESTAMP = 100;

    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();
    private final ExecutionFactory eFactory = new ExecutionFactory(this.systemEntityFactory);

    /**
     * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
     * assert that a record <i>r</i> with <i>r.tin &lt; a</i> and <i>r.tout
     * &gt; a </i>, <i>r.tout &lt; b</i> does not pass the filter.
     */
    public void testRecordTinBeforeToutWithinIgnored() {
        final TimestampFilter filter =
                new TimestampFilter(
                this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        final Execution exec = this.eFactory.genExecution(
                77, // traceId (value not important)
                this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP-1, // tin
                this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP-1,  // tout
                0, 0); // eoi, ess

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
            + " although tin timestamp before" + this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
            filterPassedRecord.get());
    }

    /**
     * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
     * assert that a record <i>r</i> with <i>r.tin &gt; a</i>, <i>r.tin
     * &lt; b</i> and <i>r.tout &gt; b </i> does not pass the filter.
     */
    public void testRecordTinWithinToutAfterIgnored() {
        final TimestampFilter filter =
                new TimestampFilter(
                this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        final Execution exec = this.eFactory.genExecution(
                15, // traceId (value not important)
                this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP+1, // tin
                this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP+1,  // tout
                0, 0); // eoi, ess

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
            + " although tout timestamp after" + this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
            filterPassedRecord.get());
    }

    /**
     * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
     * assert that a record <i>r</i> with <i>r.tin == a</i> and <i>r.tout == b </i>
     * does pass the filter.
     */
    public void testRecordTinToutOnBordersPassed() {
        final TimestampFilter filter =
                new TimestampFilter(
                this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        final Execution exec = this.eFactory.genExecution(
                159, // traceId (value not important)
                this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, // tin
                this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP,  // tout
                0, 0); // eoi, ess

        final AtomicReference<Boolean> filterPassedRecord =
                new AtomicReference<Boolean>(Boolean.FALSE);

        filter.getExecutionOutputPort().subscribe(new AbstractInputPort<Execution>("Execution input") {

            /**
             * In this test, this method MUST be called exactly once.
             */
            @Override
			public void newEvent(final Execution event) {
                filterPassedRecord.set(Boolean.TRUE);
                Assert.assertSame(event, exec);
            }
        });
        filter.getExecutionInputPort().newEvent(exec);
        Assert.assertTrue("Filter didn't pass execution " + exec
            + " although timestamps within range [" +
            this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP + "," +
            this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP + "]",
            filterPassedRecord.get());
    }

    /**
     * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
     * assert that a record <i>r</i> with <i>r.tin &gt; a</i>, <i>r.tin &lt; b</i>
     * and <i>r.tout &lt; b </i>, <i>r.tout &gt; a </i>  does pass the filter.
     */
    public void testRecordTinToutWithinRangePassed() {
        final TimestampFilter filter =
                new TimestampFilter(
                this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        final Execution exec = this.eFactory.genExecution(
                159, // traceId (value not important)
                this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP+1, // tin
                this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP-1,  // tout
                0, 0); // eoi, ess

        final AtomicReference<Boolean> filterPassedRecord =
                new AtomicReference<Boolean>(Boolean.FALSE);

        filter.getExecutionOutputPort().subscribe(new AbstractInputPort<Execution>("Execution input") {

            /**
             * In this test, this method MUST be called exactly once.
             */
            @Override
			public void newEvent(final Execution event) {
                filterPassedRecord.set(Boolean.TRUE);
                Assert.assertSame(event, exec);
            }
        });
        filter.getExecutionInputPort().newEvent(exec);
        Assert.assertTrue("Filter didn't pass execution " + exec
            + " although timestamps within range [" +
            this.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP + "," +
            this.IGNORE_EXECUTIONS_AFTER_TIMESTAMP + "]",
            filterPassedRecord.get());
    }
}