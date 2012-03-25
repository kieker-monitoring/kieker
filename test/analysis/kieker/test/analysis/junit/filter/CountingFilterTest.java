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

package kieker.test.analysis.junit.filter;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.common.configuration.Configuration;
import kieker.test.analysis.junit.plugin.SimpleSourcePlugin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This test is for the class {@link CountingFilter}.
 * 
 * @author Nils Christian Ehmke, Jan Waller
 */
public class CountingFilterTest {

	private CountingFilter consumer;
	volatile SimpleSourcePlugin src; // NOPMD (package visible for inner class)

	@Before
	public void before() {
		/* Establish the connection. */
		this.consumer = new CountingFilter(new Configuration());
		this.src = new SimpleSourcePlugin(new Configuration());
		final AnalysisController controller = new AnalysisController();
		controller.registerFilter(this.consumer);
		controller.registerFilter(this.src);
		Assert.assertTrue(controller.connect(this.src, SimpleSourcePlugin.OUTPUT_PORT_NAME, this.consumer, CountingFilter.INPUT_PORT_NAME_EVENTS));
	}

	@Test
	public void testNormal() {
		Assert.assertEquals(0, this.consumer.getMessageCount());
		this.src.deliver(new Object());
		this.src.deliver(new Object());
		this.src.deliver(new Object());
		Assert.assertEquals(3, this.consumer.getMessageCount());
	}

	@Test
	public void testConcurrently() {
		Assert.assertEquals(0, this.consumer.getMessageCount());
		/* Now start some data concurrently. */
		final Thread t1 = new Thread() {

			@Override
			public void run() {
				CountingFilterTest.this.src.deliver(new Object());
			}
		};
		final Thread t2 = new Thread() {

			@Override
			public void run() {
				CountingFilterTest.this.src.deliver(new Object());
			}
		};
		final Thread t3 = new Thread() {

			@Override
			public void run() {
				CountingFilterTest.this.src.deliver(new Object());
			}
		};
		t1.start();
		t2.start();
		t3.start();
		/* Join the threads. */
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (final InterruptedException e) {
			Assert.fail();
		}
		Assert.assertEquals(3, this.consumer.getMessageCount());
	}

	@Test
	public void testDifferentClasses() {
		Assert.assertEquals(0, this.consumer.getMessageCount());
		/* Now send some other data. */
		this.src.deliver(Long.valueOf(10));
		this.src.deliver(null); // should not be delivered
		this.src.deliver("");
		Assert.assertEquals(2, this.consumer.getMessageCount());
	}
}
