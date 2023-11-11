/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.generic.sink;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;

import teetime.framework.AbstractConsumerStage;

/**
 * Sink creating a yaml file.
 *
 * @param <T>
 *            row data type.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class YamlSink<T> extends AbstractConsumerStage<T> {

	private final Path path;

	public YamlSink(final Path path) {
		this.path = path;
	}

	@Override
	protected void execute(final T object) throws Exception {
		final Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		try (final Writer writer = Files.newBufferedWriter(this.path)) {
			writer.write(yaml.dumpAs(object, Tag.MAP, null));
		}
	}

}
