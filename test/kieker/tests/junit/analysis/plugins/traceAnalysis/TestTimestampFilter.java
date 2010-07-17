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
import kieker.analysis.datamodel.AllocationComponent;
import kieker.analysis.datamodel.AssemblyComponent;
import kieker.analysis.datamodel.ComponentType;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionContainer;
import kieker.analysis.datamodel.Operation;
import kieker.analysis.datamodel.Signature;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.traceAnalysis.executionFilter.TimestampFilter;

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

    private final static String DEFAULT_STRING = "N/A";

    private final SystemModelRepository systemEntityFactory = new SystemModelRepository();

    public void testRecordTinBeforeToutWithinIgnored() {
        TimestampFilter filter =
                new TimestampFilter(
                IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
                IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

        Execution exec = genExecution(
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
            + " although timestamp before" + IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
            filterPassedRecord.get());
    }

    private Execution genExecution(
            final long tin,
            final long tout,
            final int eoi,
            final int ess){
        ComponentType componentTypeA =
                this.systemEntityFactory.getTypeRepositoryFactory().createAndRegisterComponentType(
                DEFAULT_STRING, DEFAULT_STRING);
        Operation operationAa =
                this.systemEntityFactory.getOperationFactory().createAndRegisterOperation(
                DEFAULT_STRING,
                componentTypeA,
                new Signature(
                DEFAULT_STRING,
                DEFAULT_STRING,
                new String[] {DEFAULT_STRING}));
        componentTypeA.addOperation(operationAa);
        AssemblyComponent assemblyComponentA =
                this.systemEntityFactory.getAssemblyFactory().createAndRegisterAssemblyComponentInstance(
                DEFAULT_STRING,
                componentTypeA);
        ExecutionContainer containerC =
                this.systemEntityFactory.getExecutionEnvironmentFactory().createAndRegisterExecutionContainer(
                DEFAULT_STRING, DEFAULT_STRING);
        AllocationComponent allocationComponentA =
                this.systemEntityFactory.getAllocationFactory().createAndRegisterAllocationComponentInstance(
                DEFAULT_STRING,
                assemblyComponentA,
                containerC);
        return new Execution(
                operationAa, allocationComponentA, 1,
                DEFAULT_STRING, eoi, ess, tin, tout);
    }
}