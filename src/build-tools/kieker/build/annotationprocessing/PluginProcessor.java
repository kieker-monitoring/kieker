/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.build.annotationprocessing;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Annotation processor for the Kieker Plugin annotation. This processor asserts that every concrete plugin has
 * a constructor which requires a configuration as its only parameter.
 * 
 * @author Holger Knoche
 * 
 */
@SupportedAnnotationTypes("kieker.analysis.plugin.annotation.Plugin")
public class PluginProcessor extends AbstractPluginProcessor {

	private static final String HANDLED_TYPE_NAME = "plugin";

	@Override
	protected void handleElement(final Element element, final RoundEnvironment environment) {
		if (!element.getModifiers().contains(Modifier.ABSTRACT)) {
			this.mandatoryConstructorExists(element, HANDLED_TYPE_NAME);
		}
	}

}
