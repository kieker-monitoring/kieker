/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.cmi;

import java.util.ArrayList;

import kieker.analysis.architecture.ModelRepositoryProducerStage;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.tools.cmi.stages.CheckAssemblyModelStage;
import kieker.tools.cmi.stages.CheckDeploymentModelStage;
import kieker.tools.cmi.stages.CheckExecutionModelStage;
import kieker.tools.cmi.stages.CheckSourceMissingLabelStage;
import kieker.tools.cmi.stages.CheckSourceWithoutModelElementStage;
import kieker.tools.cmi.stages.CheckStatisticModelStage;
import kieker.tools.cmi.stages.CheckTypeModelStage;
import kieker.tools.cmi.stages.PrintReportStage;
import kieker.tools.cmi.stages.Report;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.basic.merger.Merger;

/**
 * Pipe and Filter configuration for the model integrity checker.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class TeetimeConfiguration extends Configuration {

	public TeetimeConfiguration(final Settings settings) {
		final ModelRepositoryProducerStage readerStage = new ModelRepositoryProducerStage(settings.getInputDirectory());

		OutputPort<ModelRepository> outputPort = readerStage.getOutputPort();
		final Merger<Report> merger = new Merger<>();
		merger.declareActive();
		final PrintReportStage reportStage = new PrintReportStage();
		this.connectPorts(merger.getOutputPort(), reportStage.getInputPort());

		if (settings.getChecks() == null) {
			settings.setChecks(new ArrayList<ECheck>());
		}
		if (settings.getChecks().isEmpty()) {
			settings.getChecks().add(ECheck.TYPE);
		}

		if (settings.getChecks().contains(ECheck.TYPE)) {
			outputPort = this.createTypeCheck(outputPort, merger);
		}
		if (settings.getChecks().contains(ECheck.ASSEMBLY)) {
			outputPort = this.createAssemblyCheck(outputPort, merger);
		}
		if (settings.getChecks().contains(ECheck.DEPLOYMENT)) {
			outputPort = this.createDeploymentCheck(outputPort, merger);
		}
		if (settings.getChecks().contains(ECheck.EXECUTION)) {
			outputPort = this.createExecutionCheck(outputPort, merger);
		}
		if (settings.getChecks().contains(ECheck.STATISTICS)) {
			outputPort = this.createStatisticsCheck(outputPort, merger);
		}
		if (settings.getChecks().contains(ECheck.SOURCE)) {
			outputPort = this.createSourceCheck(outputPort, merger); // NOPMD
		}
	}

	private OutputPort<ModelRepository> createTypeCheck(final OutputPort<ModelRepository> outputPort,
			final Merger<Report> merger) {
		final CheckTypeModelStage stage = new CheckTypeModelStage();
		this.connectPorts(outputPort, stage.getInputPort());
		this.connectPorts(stage.getReportOutputPort(), merger.getNewInputPort());

		return stage.getOutputPort();
	}

	private OutputPort<ModelRepository> createAssemblyCheck(final OutputPort<ModelRepository> outputPort,
			final Merger<Report> merger) {
		final CheckAssemblyModelStage stage = new CheckAssemblyModelStage();
		this.connectPorts(outputPort, stage.getInputPort());
		this.connectPorts(stage.getReportOutputPort(), merger.getNewInputPort());

		return stage.getOutputPort();
	}

	private OutputPort<ModelRepository> createDeploymentCheck(final OutputPort<ModelRepository> outputPort,
			final Merger<Report> merger) {
		final CheckDeploymentModelStage stage = new CheckDeploymentModelStage();
		this.connectPorts(outputPort, stage.getInputPort());
		this.connectPorts(stage.getReportOutputPort(), merger.getNewInputPort());

		return stage.getOutputPort();
	}

	private OutputPort<ModelRepository> createExecutionCheck(final OutputPort<ModelRepository> outputPort,
			final Merger<Report> merger) {
		final CheckExecutionModelStage stage = new CheckExecutionModelStage();
		this.connectPorts(outputPort, stage.getInputPort());
		this.connectPorts(stage.getReportOutputPort(), merger.getNewInputPort());

		return stage.getOutputPort();
	}

	private OutputPort<ModelRepository> createStatisticsCheck(final OutputPort<ModelRepository> outputPort,
			final Merger<Report> merger) {
		final CheckStatisticModelStage stage = new CheckStatisticModelStage();
		this.connectPorts(outputPort, stage.getInputPort());
		this.connectPorts(stage.getReportOutputPort(), merger.getNewInputPort());

		return stage.getOutputPort();
	}

	private OutputPort<ModelRepository> createSourceCheck(final OutputPort<ModelRepository> outputPort,
			final Merger<Report> merger) {
		final CheckSourceMissingLabelStage checkSourceModelStage = new CheckSourceMissingLabelStage();
		final CheckSourceWithoutModelElementStage checkSourceWithoutModelElementStage = new CheckSourceWithoutModelElementStage();

		this.connectPorts(outputPort, checkSourceModelStage.getInputPort());
		this.connectPorts(checkSourceModelStage.getOutputPort(), checkSourceWithoutModelElementStage.getInputPort());

		this.connectPorts(checkSourceModelStage.getReportOutputPort(), merger.getNewInputPort());
		this.connectPorts(checkSourceWithoutModelElementStage.getReportOutputPort(), merger.getNewInputPort());

		return checkSourceWithoutModelElementStage.getOutputPort();
	}

}
