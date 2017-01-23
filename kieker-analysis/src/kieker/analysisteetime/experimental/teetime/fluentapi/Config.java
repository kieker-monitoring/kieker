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

package kieker.analysisteetime.experimental.teetime.fluentapi;

import java.util.Arrays;
import java.util.function.Function;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.AbstractProducerStage;
import teetime.framework.AbstractStage;
import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.InitialElementProducer;
import teetime.stage.basic.AbstractTransformation;
import teetime.stage.basic.ITransformation;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.io.Printer;
import teetime.stage.string.ToLowerCase;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class Config<P> {

	private final Configuration configuration = new Configuration();
	private final Out<P> firstOut;

	private Config(final AbstractProducerStage<P> stage) {
		final OutputPort<P> outputPort = stage.getOutputPort();
		this.firstOut = new Out<>(outputPort);
	}

	public static <O> Config<O>.Out<O> from(final AbstractProducerStage<O> stage) {
		final Config<O> config = new Config<>(stage);
		return config.firstOut;
	}

	public class Out<I> {

		private final OutputPort<I> lastPort;

		private Out(final OutputPort<I> lastPort) {
			this.lastPort = lastPort;
		}

		public <O> Out<O> to(final ITransformation<I, O> stage) {
			final InputPort<I> inputPort = stage.getInputPort();
			final OutputPort<O> outputPort = stage.getOutputPort();

			Config.this.configuration.connectPorts(this.lastPort, inputPort, 4); // TODO 4 hard coded

			return new Out<>(outputPort);
		}

		public <O> Out<O> to(final TransfomerStage<I, O> stage) {
			final InputPort<I> inputPort = stage.inputPort;
			final OutputPort<O> outputPort = stage.outputPort;

			Config.this.configuration.connectPorts(this.lastPort, inputPort, 4); // TODO 4 hard coded

			return new Out<>(outputPort);
		}

		public void distribute(final IDistributorStrategy strategy, final BranchX<I>... branches) {

			final Distributor<I> distributor = new Distributor<>(strategy);

			Config.this.configuration.connectPorts(this.lastPort, distributor.getInputPort(), 4); // TODO 4 hard coded

			for (final BranchX<I> branch : branches) {
				final InputPort<I> inputPort = branch.inputPort;
				Config.this.configuration.connectPorts(distributor.getNewOutputPort(), inputPort, 4); // TODO 4 hard coded
			}

		}

		public Configuration end(final AbstractConsumerStage<I> stage) {
			final InputPort<I> inputPort = stage.getInputPort();
			Config.this.configuration.connectPorts(this.lastPort, inputPort, 4); // TODO 4 hard coded

			return this.config();
		}

		public Configuration config() {
			return Config.this.configuration;
		}

	}

	public static class TransfomerStage<I, O> {

		private final InputPort<I> inputPort;
		private final OutputPort<O> outputPort;

		private TransfomerStage(final InputPort<I> inputPort, final OutputPort<O> outputPort) {
			this.inputPort = inputPort;
			this.outputPort = outputPort;
		}

		public static <S extends AbstractStage, I, O> TransfomerStage<I, O> of(final S stage, final Function<S, InputPort<I>> inputPort,
				final Function<S, OutputPort<O>> outputPort) {
			return new TransfomerStage<>(inputPort.apply(stage), outputPort.apply(stage));
		}

	}

	public static class Branch<P> {

	}

	public class BranchX<I> extends Config<I> {

		InputPort<I> inputPort;

		private BranchX(final AbstractConsumerStage<I> stage) {
			super(null);
			this.inputPort = stage.getInputPort();
		}

	}

	///////// EXAMPLE /////////

	public static void main(final String[] args) {

		final Configuration config = Config.from(new InitialElementProducer<>(Arrays.asList("uno", "dos", "tres")))
				.to(new ToUpperCaseStage())
				.to(TransfomerStage.of(new ToLowerCase(), s -> s.getInputPort(), s -> s.getOutputPort()))
				.to(new StringLengthStage())
				.end(new Printer<>());

		final Execution<Configuration> execution = new Execution<Configuration>(config);
		execution.executeBlocking();

	}

	private static class ToUpperCaseStage extends AbstractTransformation<String, String> {

		@Override
		protected void execute(final String string) {
			final String upperCaseString = string.toUpperCase();
			this.getOutputPort().send(upperCaseString);
		}

	}

	private static class StringLengthStage extends AbstractTransformation<String, Integer> {

		@Override
		protected void execute(final String string) {
			final int length = string.length();
			this.getOutputPort().send(length);
		}

	}

}
