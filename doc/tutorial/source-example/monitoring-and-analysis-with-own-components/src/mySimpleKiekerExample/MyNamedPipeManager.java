package mySimpleKiekerExample;

import java.util.HashMap;

public class MyNamedPipeManager {

	private static final MyNamedPipeManager INSTANCE = new MyNamedPipeManager();

	/* Not synchronized! */
	private HashMap<String, MyPipe> pipeMap = new HashMap<String, MyPipe>();

	public static MyNamedPipeManager getInstance() {
		return MyNamedPipeManager.INSTANCE;
	}

	/**
	 * This method returns a connection with name @a pipeName. If a connection
	 * with this name does not exist prior to the call, it is created.
	 * 
	 * @param pipeName
	 *            The name of the (new) pipe.
	 * @throws IllegalArgumentException
	 *             If the given name is null or has length zero.
	 */
	public synchronized MyPipe acquirePipe(final String pipeName)
			throws IllegalArgumentException {
		if (pipeName == null || pipeName.length() == 0) {
			throw new IllegalArgumentException("Invalid connection name "
					+ pipeName);
		}
		MyPipe conn = this.pipeMap.get(pipeName);
		if (conn == null) {
			conn = new MyPipe(pipeName);
			this.pipeMap.put(pipeName, conn);
		}
		return conn;
	}

}
