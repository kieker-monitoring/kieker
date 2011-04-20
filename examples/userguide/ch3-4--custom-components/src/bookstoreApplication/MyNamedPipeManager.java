package bookstoreApplication;

import java.util.HashMap;

public class MyNamedPipeManager {

    private static final MyNamedPipeManager PIPE_MGR_INSTANCE = new MyNamedPipeManager();

    /* Not synchronized! */
    private final HashMap<String, MyPipe> pipeMap = new HashMap<String, MyPipe>();

    public static MyNamedPipeManager getInstance() {
        return MyNamedPipeManager.PIPE_MGR_INSTANCE;
    }

    /**
     * Returns a pipe with name pipeName. If a pipe with this name does not
     * exist prior to the call, it will be created.
     *
     * @param pipeName name of the (new) pipe.
     * @return the pipe
     * @throws IllegalArgumentException
     *             if the given name is null or has length zero.
     */
    public synchronized MyPipe acquirePipe(final String pipeName)
            throws IllegalArgumentException {
        if ((pipeName == null) || (pipeName.length() == 0)) {
            throw new IllegalArgumentException("Invalid connection name: '" + pipeName + "'");
        }
        MyPipe conn = this.pipeMap.get(pipeName);
        if (conn == null) {
            conn = new MyPipe(pipeName);
            this.pipeMap.put(pipeName, conn);
        }
        return conn;
    }
}
