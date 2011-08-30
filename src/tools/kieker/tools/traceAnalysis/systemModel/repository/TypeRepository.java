/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.systemModel.repository;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tools.traceAnalysis.systemModel.ComponentType;

/**
 *
 * @author Andre van Hoorn
 */
public class TypeRepository extends AbstractSystemSubRepository {

    private final Hashtable<String, ComponentType> componentTypesByName =
            new Hashtable<String, ComponentType>();
    private final Hashtable<Integer,ComponentType> componentTypesById =
            new Hashtable<Integer, ComponentType>();

    public final ComponentType rootComponent;

    public TypeRepository(final SystemModelRepository systemFactory,
            final ComponentType rootComponent) {
        super(systemFactory);
        this.rootComponent = rootComponent;
    }

    /**
     * Returns the instance for the passed namedIdentifier; null if no instance
     *  with this namedIdentifier.
     */
    public synchronized final ComponentType lookupComponentTypeByNamedIdentifier(final String namedIdentifier) {
        return this.componentTypesByName.get(namedIdentifier);
    }

    /**
     * Creates and registers a component type that has not been registered yet.
     *
     * @param namedIdentifier
     * @param fullqualifiedName
     * @return the created component type
     * @throws IllegalArgumentException if a component type with the given 
     * namedIdentifier has already been registered
     */
    public synchronized final ComponentType createAndRegisterComponentType(
            final String namedIdentifier,
            final String fullqualifiedName) {
        ComponentType newInst;
        if (this.componentTypesByName.containsKey(namedIdentifier)) {
            throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
        }
        int id = this.getAndIncrementNextId();
        newInst = new ComponentType(id, fullqualifiedName);
        this.componentTypesById.put(id, newInst);
        this.componentTypesByName.put(namedIdentifier, newInst);
        return newInst;
    }

    /**
     * Returns a collection of all registered component types.
     *
     * @return a collection of all registered component types.
     */
    public synchronized  final Collection<ComponentType> getComponentTypes(){
        return this.componentTypesById.values();
    }
}
