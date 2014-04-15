package kieker.panalysis.base;

/**
 * 
 * @author Christian Wulf
 * 
 * @param <S>
 *            the stage, this port belongs to<br>
 *            <i>(used for ensuring type safety)</i>
 * @param <T>
 */
public interface IOutputPort<S extends IStage, T> extends IPort<T> {

}
