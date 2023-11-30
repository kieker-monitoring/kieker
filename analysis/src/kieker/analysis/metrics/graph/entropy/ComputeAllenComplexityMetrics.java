/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.metrics.graph.entropy;

import java.util.HashMap;
import java.util.Map;

import org.mosim.refactorlizar.architecture.evaluation.CalculationMode;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.CodeMetric;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Cohesion;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Complexity;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Coupling;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.HyperGraphSize;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.LinesOfCode;
import org.mosim.refactorlizar.architecture.evaluation.cohesion.HyperGraphCohesionCalculator;
import org.mosim.refactorlizar.architecture.evaluation.complexity.HyperGraphComplexityCalculator;
import org.mosim.refactorlizar.architecture.evaluation.coupling.HyperGraphInterModuleCouplingGenerator;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;
import org.mosim.refactorlizar.architecture.evaluation.graphs.SystemGraphUtils;
import org.mosim.refactorlizar.architecture.evaluation.size.HyperGraphSizeCalculator;

import com.google.common.graph.Graph;

import teetime.stage.basic.AbstractTransformation;

/**
 * Compute the Allen metrics for a given modular graph.
 *
 * @param <T>
 *            node element type
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ComputeAllenComplexityMetrics<T>
		extends AbstractTransformation<Graph<Node<T>>, Map<Class<? extends CodeMetric>, CodeMetric>> {

	private final Class<? extends CodeMetric>[] metrics;
	private final SystemGraphUtils<T> systemGraphUtils;

	@SafeVarargs
	public ComputeAllenComplexityMetrics(final SystemGraphUtils<T> systemGraphUtils, // NOPMD
			final Class<? extends CodeMetric>... metrics) { // NOPMD array storage
		this.metrics = metrics; // NOPMD
		this.systemGraphUtils = systemGraphUtils;
	}

	@Override
	protected void execute(final Graph<Node<T>> graph) throws Exception {
		final CalculationMode mode = CalculationMode.NO_OFFSET;
		final Map<Class<? extends CodeMetric>, CodeMetric> result = new HashMap<>();

		for (final Class<? extends CodeMetric> metricClass : this.metrics) {
			final CodeMetric metric;
			if (metricClass.equals(HyperGraphSize.class)) {
				metric = this.calculateHyperGraphSize(mode, this.systemGraphUtils, graph);
			} else if (metricClass.equals(Complexity.class)) {
				metric = new HyperGraphComplexityCalculator<>(mode, this.systemGraphUtils).calculate(graph);
			} else if (metricClass.equals(Coupling.class)) {
				metric = new HyperGraphInterModuleCouplingGenerator<>(mode, this.systemGraphUtils).calculate(graph);
			} else if (metricClass.equals(Cohesion.class)) {
				metric = new HyperGraphCohesionCalculator<>(mode, this.systemGraphUtils).calculate(graph);
			} else if (metricClass.equals(LinesOfCode.class)) {
				metric = new LinesOfCode(graph.nodes().size());
			} else {
				metric = null;
			}
			result.put(metricClass, metric);
		}

		this.outputPort.send(result);

	}

	private HyperGraphSize calculateHyperGraphSize(final CalculationMode mode,
			final SystemGraphUtils<T> internalSystemGraphUtils, final Graph<Node<T>> graph) {
		return new HyperGraphSize(
				new HyperGraphSizeCalculator<T>(mode).calculate(internalSystemGraphUtils.convertToSystemGraph(graph)));
	}

}
