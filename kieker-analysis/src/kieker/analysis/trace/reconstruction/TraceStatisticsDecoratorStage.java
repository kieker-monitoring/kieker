/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.trace.reconstruction;

import java.util.Arrays;
import java.util.List;

import kieker.analysis.trace.traversal.IOperationCallVisitor;
import kieker.analysis.trace.traversal.TraceTraverser;
import kieker.model.analysismodel.trace.Trace;

import teetime.stage.basic.AbstractFilter;

/**
 * This class is a {@code TeeTime} stage adding statistics to instances of
 * {@link TraceRoot}. The traces are forwarded to the output port.
 *
 * @author Nils Christian Ehmke, Sören Henning
 *
 * @since 1.14
 */
public final class TraceStatisticsDecoratorStage extends AbstractFilter<Trace> {

	private final TraceTraverser traverser = new TraceTraverser();
	private final IOperationCallVisitor durRatioToParentCalculator = new DurRatioToParentCalculator();

	public TraceStatisticsDecoratorStage() {
		super();
	}

	@Override
	public void execute(final Trace trace) {
		final IOperationCallVisitor durRatioToRootParentCalculator = new DurRatioToRootParentCalculator(
				trace.getRootOperationCall());
		final List<IOperationCallVisitor> visitors = Arrays.asList(this.durRatioToParentCalculator,
				durRatioToRootParentCalculator);

		this.traverser.traverse(trace, visitors);

		super.getOutputPort().send(trace);
	}

}
