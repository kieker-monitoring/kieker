/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring.util;

import java.util.Map.Entry;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public final class RestructurerUtils {

	private static final AssemblyFactory FACTORY = AssemblyFactory.eINSTANCE;

	private RestructurerUtils() {
		// ensure that utility class is not instantiated
	}

	public static AssemblyModel cloneModel(final AssemblyModel model) {
		final AssemblyModel result = RestructurerUtils.FACTORY.createAssemblyModel();

		for (final Entry<String, AssemblyComponent> e : model.getComponents().entrySet()) {
			final AssemblyComponent comp = RestructurerUtils.FACTORY.createAssemblyComponent();
			result.getComponents().put(e.getKey(), comp);
			for (final Entry<String, AssemblyOperation> op : e.getValue().getOperations().entrySet()) {
				final AssemblyOperation o = RestructurerUtils.FACTORY.createAssemblyOperation();
				result.getComponents().get(e.getKey()).getOperations().put(op.getKey(), o);
			}
		}

		return result;
	}

	public static AssemblyModel alterComponentNames(final AssemblyModel model) {
		final String prefix = "_";
		final AssemblyModel result = RestructurerUtils.FACTORY.createAssemblyModel();

		for (final Entry<String, AssemblyComponent> e : model.getComponents().entrySet()) {
			final AssemblyComponent comp = RestructurerUtils.FACTORY.createAssemblyComponent();
			result.getComponents().put(prefix + e.getKey(), comp);
			for (final Entry<String, AssemblyOperation> op : e.getValue().getOperations().entrySet()) {
				final AssemblyOperation o = RestructurerUtils.FACTORY.createAssemblyOperation();
				result.getComponents().get("_" + e.getKey()).getOperations().put(op.getKey(), o);
			}
		}

		return result;
	}
}
