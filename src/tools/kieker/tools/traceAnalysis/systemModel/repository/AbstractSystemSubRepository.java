package kieker.tools.traceAnalysis.systemModel.repository;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractSystemSubRepository {
    public final static int ROOT_ELEMENT_ID = 0;

    private final AtomicInteger nextId = new AtomicInteger(ROOT_ELEMENT_ID+1);

    private final SystemModelRepository systemFactory;

    private AbstractSystemSubRepository(){
        this.systemFactory = null;
    }

    public AbstractSystemSubRepository(SystemModelRepository systemFactory){
        this.systemFactory = systemFactory;
    }

    protected final int getAndIncrementNextId() {
        return this.nextId.getAndIncrement();
    }

    protected final SystemModelRepository getSystemFactory() {
        return systemFactory;
    }
}
