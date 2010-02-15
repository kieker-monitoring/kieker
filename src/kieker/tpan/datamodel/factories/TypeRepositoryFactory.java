package kieker.tpan.datamodel.factories;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.ComponentType;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */
/**
 *
 * @author Andre van Hoorn
 */
public class TypeRepositoryFactory extends AbstractSystemSubFactory {

    private final Hashtable<String, ComponentType> componentTypesByName =
            new Hashtable<String, ComponentType>();
    private final Hashtable<Integer,ComponentType> componentTypesById = new Hashtable<Integer, ComponentType>();

    public final ComponentType rootComponent;

    public TypeRepositoryFactory(final SystemEntityFactory systemFactory,
            final ComponentType rootComponent) {
        super(systemFactory);
        this.rootComponent = rootComponent;
    }

    /** Returns the instance for the passed factoryIdentifier; null if no instance
     *  with this factoryIdentifier.
     */
    public final ComponentType getComponentTypeByFactoryIdentifier(final String factoryIdentifier) {
        return this.componentTypesByName.get(factoryIdentifier);
    }

    public final ComponentType createAndRegisterComponentType(
            final String factoryIdentifier,
            final String fullqualifiedName) {
        ComponentType newInst;
        if (this.componentTypesByName.containsKey(factoryIdentifier)) {
            throw new IllegalArgumentException("Element with name " + factoryIdentifier + "exists already");
        }
        int id = this.getAndIncrementNextId();
        newInst = new ComponentType(id, fullqualifiedName);
        this.componentTypesById.put(id, newInst);
        this.componentTypesByName.put(factoryIdentifier, newInst);
        return newInst;
    }

    public final Collection<ComponentType> getComponentTypes(){
        return this.componentTypesById.values();
    }
}
