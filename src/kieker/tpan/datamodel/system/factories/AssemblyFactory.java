package kieker.tpan.datamodel.system.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.system.AssemblyComponentInstance;
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
public class AssemblyFactory extends AbstractSystemSubFactory{
    private final Hashtable<String, AssemblyComponentInstance>
            assemblyComponentInstancesByName =
            new Hashtable<String, AssemblyComponentInstance>();
    private final ArrayList<AssemblyComponentInstance>
            assemblyComponentInstancesById = new ArrayList<AssemblyComponentInstance>();

    public AssemblyFactory(SystemEntityFactory systemFactory){
        super(systemFactory);
    }

    /** Returns the instance for the passed name; null if no instance
     *  with this name.
     */
    public final AssemblyComponentInstance getAssemblyComponentInstanceByName(final String name){
        return this.assemblyComponentInstancesByName.get(name);
    }

    public final AssemblyComponentInstance createAndRegisterAssemblyComponentInstance(
            final String name,
            final ComponentType componentType){
            AssemblyComponentInstance newInst;
            if (this.assemblyComponentInstancesByName.containsKey(name)){
                throw new IllegalArgumentException("Element with name " + name + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new AssemblyComponentInstance(id, name, componentType);
            this.assemblyComponentInstancesById.add(id, newInst);
            this.assemblyComponentInstancesByName.put(name, newInst);
            return newInst;
    }

    public final Collection<AssemblyComponentInstance> getAssemblyComponentInstances(){
        return this.assemblyComponentInstancesById;
    }
}
