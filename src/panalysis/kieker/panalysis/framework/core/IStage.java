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

package kieker.panalysis.framework.core;


/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public interface IStage {

	/**
	 * @since 1.10
	 */
	void setId(int id);

	/**
	 * @since 1.10
	 */
	int getId();

	/**
	 * @return <code>true</code> if the execution took enough tokens from the input ports so that the stage made progress due to this execution, <code>false</code>
	 *         otherwise. The definition of <i>progress</i> depends on the semantics of the particular stage.
	 * 
	 *         <p>
	 *         Example usage:
	 *         </p>
	 * 
	 *         <pre>
	 * <code>
	 * boolean execute() {
	 * 	final Tuple token = this.tryTake(FILE_WORDCOUNT_TUPLE);
	 * 	if (token == null) {
	 * 		return false;
	 * 	}
	 * 	...
	 * 	return true;
	 * }
	 * 	</code>
	 * </pre>
	 * 
	 * @since 1.10
	 */
	boolean execute();

	// Let the method uncommented for documentation purpose:<br>
	// Since the execution can dependent on the state of one, a subset, or all input queues,<br>
	// an overloaded version with one input port is useless.
	// void execute(InputPort inputPort);
	//
	//
	// Let the method uncommented for documentation purpose:<br>
	// Since the execution can dependent on one, a subset, or all input queues,<br>
	// an overloaded version with a task bundle of one single input port is useless.
	// void execute(TaskBundle taskBundle);

	/**
	 * @since 1.10
	 */
	void cleanUp();

	/**
	 * 
	 * @return <code>true</code> if the stage may be disabled by the pipeline scheduler, <code>false</code> otherwise.
	 * 
	 * @since 1.10
	 */
	boolean mayBeDisabled();

	/**
	 * @since 1.10
	 */
	void fireSignalClosingToAllOutputPorts();

	void onPipelineStarts();

	Context<?> getContext();

	IPipeline getOwningPipeline();

	void setOwningPipeline(IPipeline owningPipeline);

	void copyAttributes(IStage stage);

	IOutputPort<?, ?> getOutputPortByIndex(int index);

	IInputPort<?, ?> getInputPortByIndex(int index);

}
