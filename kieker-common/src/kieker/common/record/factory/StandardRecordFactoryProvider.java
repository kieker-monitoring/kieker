/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import kieker.common.util.classpath.ClassForNameResolver;

/**
 * This factory provider implements the default factory resolution strategy by appending "Factory" to the record class name
 * and loading the respective class.
 * 
 * @author Holger Knoche
 * @since 2.0
 */
@SuppressWarnings("rawtypes")
public class StandardRecordFactoryProvider implements IRecordFactoryProvider {

    private final ClassForNameResolver<IRecordFactory> classForNameResolver = new ClassForNameResolver<>(IRecordFactory.class);

    public StandardRecordFactoryProvider() {
        // Empty default constructor
    }

    @Override
    public boolean isApplicableTo(final Class<?> recordClass) {
        return true;
    }

    private String buildRecordFactoryClassName(final String recordClassName) {
		return recordClassName + "Factory";
	}

    @Override
    public IRecordFactory<?> createFactoryFor(final Class<?> recordClass) {
        final String recordFactoryClassName = this.buildRecordFactoryClassName(recordClass.getName());

		try {
			final Class<? extends IRecordFactory> recordFactoryClass = this.classForNameResolver.classForName(recordFactoryClassName);
            final Constructor<? extends IRecordFactory> defaultConstructor = recordFactoryClass.getConstructor();
			return defaultConstructor.newInstance();
		} catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return null;
		}
    }
    
}
