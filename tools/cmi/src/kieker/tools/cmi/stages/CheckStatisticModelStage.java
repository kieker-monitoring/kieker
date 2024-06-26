/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.cmi.stages;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;

/**
 * Checks for the statistics model.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CheckStatisticModelStage extends AbstractCollector<ModelRepository> {

	@Override
	protected void execute(final ModelRepository repository) throws Exception {
		final Report report = new Report("Statistics model");

		final StatisticsModel model = repository.getModel(StatisticsPackage.Literals.STATISTICS_MODEL);

		GenericCheckUtils.checkReferences(StatisticsPackage.Literals.STATISTICS_MODEL, model.eAllContents(), report);

		this.outputPort.send(repository);
		this.reportOutputPort.send(report);
	}

}
