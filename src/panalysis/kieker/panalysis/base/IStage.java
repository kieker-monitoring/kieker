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

package kieker.panalysis.base;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param I
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
	 * @since 1.10
	 */
	void execute();

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

	void cleanUp();

}
