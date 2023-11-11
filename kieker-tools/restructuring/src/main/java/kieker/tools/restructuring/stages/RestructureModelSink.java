/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring.stages;

import java.nio.file.Path;

import kieker.tools.restructuring.restructuremodel.TransformationModel;
import kieker.tools.restructuring.util.WriteModelUtils;

import teetime.framework.AbstractConsumerStage;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class RestructureModelSink extends AbstractConsumerStage<TransformationModel> {
	private final Path outputPath;

	public RestructureModelSink(final Path outputPath) {
		this.outputPath = outputPath;
	}

	@Override
	protected void execute(final TransformationModel element) throws Exception {
		WriteModelUtils.writeModelRepository(this.outputPath, element.getName(), element);
	}

}
