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

package kieker.test.tools.junit;

import kieker.test.tools.junit.currentTimeEventGeneratorFilter.TestCurrentTimeEventGenerator;
import kieker.test.tools.junit.traceAnalysis.filter.TestTimestampFilter;
import kieker.test.tools.junit.traceAnalysis.filter.TestTraceIdFilter;
import kieker.test.tools.junit.traceAnalysis.filter.TestTraceReconstructionFilter;
import kieker.test.tools.junit.traceAnalysis.systemModel.TestExecutionTraceBookstore;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite is just used to run all available tools test with one
 * call. It should call all tests within <i>test/tools</i>.
 * 
 * @author Nils Christian Ehmke
 */
@RunWith(Suite.class)
@SuiteClasses({
	TestCurrentTimeEventGenerator.class,
	TestTimestampFilter.class,
	TestTraceIdFilter.class,
	TestTraceReconstructionFilter.class,
	TestExecutionTraceBookstore.class
})
public class ToolsTest { // NOPMD (empty TestSuite class)
}
