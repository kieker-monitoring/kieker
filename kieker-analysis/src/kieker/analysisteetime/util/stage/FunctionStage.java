/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysisteetime.util.stage;

import java.util.function.Function;

import teetime.stage.basic.AbstractTransformation;

/**
 * Stage that maps the elements from its input port to its output port by using a {@link Function}.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class FunctionStage<I, O> extends AbstractTransformation<I, O> {

	private Function<I, O> function;

	public FunctionStage(final Function<I, O> function) {
		super();
		this.function = function;
	}

	public Function<I, O> getFunction() {
		return this.function;
	}

	public void setFunction(final Function<I, O> function) {
		this.function = function;
	}

	@Override
	protected void execute(final I element) {
		final O transformedElement = this.function.apply(element);
		this.getOutputPort().send(transformedElement);
	}

}
