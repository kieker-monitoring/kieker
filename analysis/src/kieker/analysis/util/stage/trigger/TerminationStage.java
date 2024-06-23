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
package kieker.analysis.util.stage.trigger;

import teetime.framework.AbstractProducerStage;

public class TerminationStage<O> extends AbstractProducerStage<O> {

	private final O value;

	public TerminationStage(final O value) {
		this.value = value;
	}

	@Override
	protected void execute() throws Exception {
		this.workCompleted();
	}

	@Override
	protected void onTerminating() {
		this.outputPort.send(this.value);
		super.onTerminating();
	}
}
