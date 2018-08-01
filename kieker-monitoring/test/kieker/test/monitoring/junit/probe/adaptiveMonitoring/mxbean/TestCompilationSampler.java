/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.probe.adaptiveMonitoring.mxbean;

import java.util.List;

import org.junit.Assert;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.jvm.CompilationRecord;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.sampler.mxbean.CompilationSampler;

/**
 * @author Micky Singh Multani
 * 
 * @since 1.10
 */
public class TestCompilationSampler extends AbstractJVMSamplerTest {

	private static final String LISTNAME = TestCompilationSampler.class.getName();
	private static final String SIGNATURE = SignatureFactory.createJVMCompilationSignature();
	private static final CompilationSampler SAMPLER = new CompilationSampler();

	public TestCompilationSampler() {
		super(LISTNAME, SIGNATURE, SAMPLER);
	}

	@Override
	protected void isInstanceOf(final List<IMonitoringRecord> recordList) {
		final boolean isInstanceOf = recordList.get(0) instanceof CompilationRecord;
		Assert.assertTrue("Unexpected instance of IMonitoringRecord", isInstanceOf);
	}

	@Override
	protected void checkNumEventsBeforeMonitoringDisabled(final int records) {
		Assert.assertEquals("Unexpected number of triggering events before disabling", 4, records);
	}

}
