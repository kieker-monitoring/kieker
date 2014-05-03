package kieker.panalysis.framework.concurrent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.framework.core.ISink;
import kieker.panalysis.framework.core.ISource;
import kieker.panalysis.framework.core.IStage;

public class ConcurrentPipeline {

	private final Map<IOutputPort<?, ?>, IInputPort<?, ?>> connections = new HashMap<IOutputPort<?, ?>, IInputPort<?, ?>>();
	private final Set<IStage> ioStages = new HashSet<IStage>();
	private final Map<IStage, List<IStage>> standardStages = new HashMap<IStage, List<IStage>>();

	private final int numCores = Runtime.getRuntime().availableProcessors();
	private int defaultBundleSize = 64;

	public <T> void connect(final IOutputPort<?, T> sourcePort, final IInputPort<?, T> targetPort, final int bundleSize) {
		this.connections.put(sourcePort, targetPort);
		this.addStage(sourcePort.getOwningStage());
		this.addStage(targetPort.getOwningStage());
	}

	/**
	 * Connects to ports using the default bundle size
	 * 
	 * @param sourcePort
	 * @param targetPort
	 */
	public <T> void connect(final IOutputPort<?, T> sourcePort, final IInputPort<?, T> targetPort) {
		this.connect(sourcePort, targetPort, this.defaultBundleSize);
	}

	private void addStage(final IStage owningStage) {
		if (owningStage instanceof IoStage) {
			this.ioStages.add(owningStage);
		} else {
			this.standardStages.put(owningStage, null);
		}
	}

	public void start() {
		this.cloneNonIoStages();
		this.connectConcurrentStages();
		this.instantiatePipes();
	}

	private void cloneNonIoStages() {
		for (final Entry<IStage, List<IStage>> entry : this.standardStages.entrySet()) {
			final List<IStage> concurrentStages = this.createConcurrentStages(entry.getKey());
			entry.setValue(concurrentStages);
		}
	}

	private List<IStage> createConcurrentStages(final IStage stage) {
		final List<IStage> concurrentStages = new LinkedList<IStage>();
		for (int i = 0; i < this.numCores; i++) {
			final Class<? extends IStage> stageClazz = stage.getClass();
			// final Constructor<? extends IStage> constructor = stageClazz.getConstructor(Configuration.class);
			// final IStage copiedStage = constructor.newInstance(stage.getConfiguration()); // copy by reference since the configuration is read-only

			IStage copiedStage;
			try {
				copiedStage = stageClazz.newInstance();
			} catch (final InstantiationException e) {
				throw new IllegalStateException("The stage to be copied requires a constructor without any parameters.", e);
			} catch (final IllegalAccessException e) {
				throw new IllegalStateException("The stage to be copied requires a constructor without any parameters.", e);
			}
			copiedStage.copyAttributes(stage);
			concurrentStages.add(copiedStage);
		}
		return concurrentStages;
	}

	private void connectConcurrentStages() {
		for (final Entry<IOutputPort<?, ?>, IInputPort<?, ?>> entry : this.connections.entrySet()) {
			final IOutputPort<?, ?> sourcePort = entry.getKey();
			final IInputPort<?, ?> targetPort = entry.getValue();

			final boolean isSourceIoStage = sourcePort.getOwningStage() instanceof IoStage;
			final boolean isTargetIoStage = targetPort.getOwningStage() instanceof IoStage;

			if (isSourceIoStage && !isTargetIoStage) {
				final List<IStage> concurrentStages = this.standardStages.get(targetPort.getOwningStage());
				final IStage owningSourceStage = sourcePort.getOwningStage();
				for (final IStage concurrentTargetStage : concurrentStages) {
					this.connections.put(owningSourceStage.getNewOutputPort(), concurrentTargetStage.getInputPortByIndex(targetPort.getIndex()));
				}
			} else if (!isSourceIoStage && isTargetIoStage) {
				final List<IStage> concurrentStages = this.standardStages.get(sourcePort.getOwningStage());
				final IStage owningTargetStage = targetPort.getOwningStage();
				for (final IStage s : concurrentStages) {
					this.connections.put(s.getOutputPortByIndex(sourcePort.getIndex()), owningTargetStage.getNewInputPort());
				}
			} else {
				final List<IStage> concurrentSourceStages = this.standardStages.get(sourcePort.getOwningStage());
				final List<IStage> concurrentTargetStages = this.standardStages.get(targetPort.getOwningStage());
				for (int i = 0; i < concurrentSourceStages.size(); i++) {
					final IStage sourceStage = concurrentSourceStages.get(i);
					final IOutputPort<?, ?> otherSourcePort = sourceStage.getOutputPortByIndex(sourcePort.getIndex());

					final IStage targetStage = concurrentTargetStages.get(i);
					final IInputPort<?, ?> otherTargetPort = targetStage.getInputPortByIndex(targetPort.getIndex());

					this.connections.put(otherSourcePort, otherTargetPort);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T, S0 extends ISource, S1 extends ISink<S1>> void instantiatePipes() {
		for (final Entry<IOutputPort<?, ?>, IInputPort<?, ?>> entry : this.connections.entrySet()) {
			final IOutputPort<S0, T> sourcePort = (IOutputPort<S0, T>) entry.getKey();
			final IInputPort<S1, T> targetPort = (IInputPort<S1, T>) entry.getValue();
			this.instantiatePipe(sourcePort, targetPort);
		}
	}

	private <T, P extends IPipe<T>, S0 extends ISource, S1 extends ISink<S1>>
			void instantiatePipe(final IOutputPort<S0, T> sourcePort, final IInputPort<S1, T> targetPort) {
		final boolean isSourceIoStage = sourcePort.getOwningStage() instanceof IoStage;
		final boolean isTargetIoStage = targetPort.getOwningStage() instanceof IoStage;

		IPipe<T> pipe;
		if (isSourceIoStage && !isTargetIoStage) {
			pipe = new ConcurrentWorkStealingPipe<T>();
		} else if (!isSourceIoStage && isTargetIoStage) {
			pipe = new SingleProducerSingleConsumerPipe<T>();
		} else if (isSourceIoStage && isTargetIoStage) {
			throw new IllegalStateException("It is not allowed to connect two I/O stages.");
		} else {
			pipe = new ConcurrentWorkStealingPipe<T>();
		}

		pipe.setSourcePort(sourcePort);
		pipe.setTargetPort(targetPort);
	}

	public void setDefaultBundleSize(final int defaultBundleSize) {
		this.defaultBundleSize = defaultBundleSize;
	}

	public int getDefaultBundleSize() {
		return this.defaultBundleSize;
	}
}
