package kieker.analysisteetime.util.graph;

import java.util.Set;

public interface Element {

	public <T> T getProperty(String key);

	public Set<String> getPropertyKeys();

	public void setProperty(String key, Object value);

	public void setPropertyIfAbsent(String key, Object value);

	public <T> T removeProperty(String key);

}
