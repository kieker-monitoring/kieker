/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mvis;

import java.util.Locale;

import com.beust.jcommander.IStringConverter;

import kieker.analysis.generic.graph.IGraphElementSelector;
import kieker.analysis.generic.graph.selector.AllSelector;
import kieker.analysis.generic.graph.selector.AllSelectorColor;
import kieker.analysis.generic.graph.selector.DiffSelector;
import kieker.analysis.generic.graph.selector.IntersectSelector;
import kieker.analysis.generic.graph.selector.SubtractSelector;

/**
 * Convert string to selector used in graph selection.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class SelectorKindConverter implements IStringConverter<IGraphElementSelector> {

	@Override
	public IGraphElementSelector convert(final String value) {
		final String[] parts = value.split(":");
		final String command = parts[0];
		final ESelectorKind selectorKind = ESelectorKind.valueOf(command.toUpperCase(Locale.ROOT).replaceAll("-", "_"));
		switch (selectorKind) {
		case ALL:
			return new AllSelector();
		case ALL_COLOR:
			return new AllSelectorColor(parts[1].split(","), parts[2].split(","));
		case DIFF:
			return new DiffSelector(parts[1].split(","), parts[2].split(","));
		case SUBTRACT:
			return new SubtractSelector(parts[1].split(","));
		case INTERSECT:
			return new IntersectSelector(parts[1].split(","), parts[2].split(","));
		default:
			return new AllSelector();
		}
	}

}
