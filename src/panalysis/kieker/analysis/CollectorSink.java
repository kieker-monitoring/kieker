/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis;

import java.util.Collection;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

public class CollectorSink<T> extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "input";

	private static final int THRESHOLD = 10000;

	private Collection<T> objects;

	public CollectorSink(final Configuration configuration, final IProjectContext projectContext, final Collection<T> collection) {
		super(configuration, projectContext);
		this.objects = collection;
	}

	@InputPort(name = CollectorSink.INPUT_PORT_NAME)
	public void execute(final T object) {
		this.objects.add(object);
		if ((this.objects.size() % THRESHOLD) == 0) {
			System.out.println("size: " + this.objects.size());
		}
	}

	public Collection<T> getObjects() {
		return this.objects;
	}

	public void setObjects(final Collection<T> objects) {
		this.objects = objects;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

}
