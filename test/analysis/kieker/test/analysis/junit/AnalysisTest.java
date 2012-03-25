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

package kieker.test.analysis.junit;

import kieker.test.analysis.junit.filter.CountingFilterTest;
import kieker.test.analysis.junit.plugin.GeneralPluginTest;
import kieker.test.analysis.junit.reader.namedRecordPipe.TestPipeReader;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This test suite is just used to run all available analysis test with one
 * call. It should call all tests within <i>test/analysis</i>.
 * 
 * @author Nils Christian Ehmke
 */
@RunWith(Suite.class)
@SuiteClasses({
	TestPipeReader.class,
	GeneralPluginTest.class,
	CountingFilterTest.class })
public class AnalysisTest { // NOPMD (empty TestSuideClass)
}
