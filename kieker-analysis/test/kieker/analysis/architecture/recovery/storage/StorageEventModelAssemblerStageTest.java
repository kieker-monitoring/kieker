/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.architecture.recovery.storage;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import kieker.analysis.architecture.recovery.events.StorageEvent;

import teetime.framework.test.StageTester;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
class StorageEventModelAssemblerStageTest {

	private static final String HOST_NAME = "host";
	private static final String COMPONENT_NAME = "component";
	private static final String STORAGE_NAME = "storage";
	private static final String STORAGE_TYPE = "type";

	@Test
	void test() {
		final StorageEventModelAssemblerStage stage = new StorageEventModelAssemblerStage(new IStorageEventAssembler() {

			@Override
			public void addStorage(final StorageEvent event) {
				// nothing to be done here
			}
		});

		final StorageEvent storageEvent = new StorageEvent(StorageEventModelAssemblerStageTest.HOST_NAME,
				StorageEventModelAssemblerStageTest.COMPONENT_NAME, StorageEventModelAssemblerStageTest.STORAGE_NAME,
				StorageEventModelAssemblerStageTest.STORAGE_TYPE);

		StageTester.test(stage).and().send(storageEvent, storageEvent).to(stage.getInputPort()).start();

		MatcherAssert.assertThat("Must produce 2 storage events", stage.getOutputPort(),
				StageTester.produces(storageEvent, storageEvent));

	}

}
