package kieker.panalysis.base;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public abstract class AbstractPipe implements IPipe {

	public <O extends Enum<O>, I extends Enum<I>> void connect(final ISource<O> sourceStage, final O sourcePort, final ISink<I> targetStage, final I targetPort) {
		sourceStage.setPipeForOutputPort(sourcePort, this);
		targetStage.setPipeForInputPort(targetPort, this);
		System.out.println("Connected " + sourceStage.getClass().getSimpleName() + " with " + targetStage.getClass().getSimpleName());
	}
}
