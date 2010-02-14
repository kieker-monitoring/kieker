package kieker.tpan.datamodel.system.factories;

import java.util.ArrayList;
import java.util.Hashtable;
import kieker.tpan.datamodel.system.ComponentType;

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
    private final ArrayList<ComponentType> componentTypesById = new ArrayList<ComponentType>();

    public TypeRepositoryFactory(SystemEntityFactory systemFactory) {
        super(systemFactory);
    }

    /** Returns the instance for the passed name; null if no instance
     *  with this name.
     */
    public final ComponentType getComponentTypeByName(final String name) {
        return this.componentTypesByName.get(name);
    }

    public final ComponentType createAndRegisterComponentType(
            final String fullqualifiedName) {
        ComponentType newInst;
        if (this.componentTypesByName.containsKey(fullqualifiedName)) {
            throw new IllegalArgumentException("Element with name " + fullqualifiedName + "exists already");
        }
        int id = this.getAndIncrementNextId();
        newInst = new ComponentType(id, fullqualifiedName);
        this.componentTypesById.add(id, newInst);
        this.componentTypesByName.put(fullqualifiedName, newInst);
        return newInst;
    }
}
