/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.source;

import teetime.framework.AbstractProducerStage;

/**
 * Generates a series of integer numbers from start to end.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class NumberGeneratorProducer extends AbstractProducerStage<Integer> {

	private final Integer start;
	private final Integer end;

	public NumberGeneratorProducer(final Integer start, final Integer end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected void execute() throws Exception {
		for (int i = this.start; i <= this.end; i++) {
			this.outputPort.send(i);
		}
		this.workCompleted();
	}

}
