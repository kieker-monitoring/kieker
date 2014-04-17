/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis.framework.concurrent;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.sequential.Pipeline;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class WorkerThread extends Thread {

	private Pipeline<?> pipeline;
	private PipelineScheduler pipelineScheduler;

	private long duration;

	private volatile TerminationPolicy terminationPolicy;
	private volatile boolean shouldTerminate = false;

	@Override
	public void run() {
		this.initDatastructures();

		final long start = System.currentTimeMillis();

		while (this.pipelineScheduler.isAnyStageActive()) {
			final IStage stage = this.pipelineScheduler.get();
			if ("startThread".equals(this.getName())) {
				System.out.println("shouldTerminate: " + this.shouldTerminate);
			}

			this.startStageExecution();
			final boolean executedSuccessfully = stage.execute();
			this.finishStageExecution();

			if (this.shouldTerminate) {
				this.executeTerminationPolicy(stage, executedSuccessfully);
			}
		}

		final long end = System.currentTimeMillis();
		this.duration = end - start;
	}

	private void executeTerminationPolicy(final IStage executedStage, final boolean executedSuccessfully) {
		// System.out.println("WorkerThread.executeTerminationPolicy(): " + this.terminationPolicy);
		switch (this.terminationPolicy) {
		case TERMINATE_STAGE_AFTER_EXECUTION:
			if (executedStage.mayBeDisabled()) {
				this.pipelineScheduler.disable(executedStage);
			}
			break;
		case TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION:
			if (!executedSuccessfully) {
				if (executedStage.mayBeDisabled()) {
					this.pipelineScheduler.disable(executedStage);
				}
			}
			break;
		case TERMINATE_STAGE_NOW:
			for (final IStage stage : this.pipeline.getStages()) {
				this.pipelineScheduler.disable(stage);
			}
			break;
		default:
			break;
		}
	}

	private void initDatastructures() {
		this.pipelineScheduler = new PipelineScheduler(this.pipeline);
	}

	private void startStageExecution() {
		// TODO Auto-generated method stub

	}

	private void finishStageExecution() {
		// TODO Auto-generated method stub

	}

	public long getDuration() {
		return this.duration;
	}

	// BETTER move initialization to the constructor
	public void setPipeline(final Pipeline<?> pipeline) {
		this.pipeline = pipeline;
	}

	public Pipeline<?> getPipeline() {
		return this.pipeline;
	}

	public void terminate(final TerminationPolicy terminationPolicyToUse) {
		for (final AbstractFilter<?> startStage : this.pipeline.getStartStages()) {
			startStage.fireSignalClosingToAllInputPorts();
		}

		this.setTerminationPolicy(terminationPolicyToUse);
	}

	/**
	 * If not set, this thread will run infinitely.
	 * 
	 * @param terminationPolicyToUse
	 */
	public void setTerminationPolicy(final TerminationPolicy terminationPolicyToUse) {
		this.terminationPolicy = terminationPolicyToUse;
		this.shouldTerminate = true;
	}
}
