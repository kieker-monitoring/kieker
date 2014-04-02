package kieker.panalysis.concurrent;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class StageInfo<InputPort extends Enum<InputPort>> {

	final Map<InputPort, Deque<?>> inputQueues = new HashMap<InputPort, Deque<?>>();
}
