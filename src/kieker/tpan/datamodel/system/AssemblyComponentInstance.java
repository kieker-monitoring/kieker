package kieker.tpan.datamodel.system;

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
public class AssemblyComponentInstance {
    private final int id;
    private final String name;
    private final ComponentType type;

    private AssemblyComponentInstance(){
        this.id = -1;
        this.name = null;
        this.type = null;
    }

    public AssemblyComponentInstance(
            final int id, final String name,
            final ComponentType type){
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public final int getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final ComponentType getType() {
        return this.type;
    }

    @Override
    public final String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(this.name).append(":")
                .append(this.type.getFullQualifiedName());
        return strBuild.toString();
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AssemblyComponentInstance)){
            return false;
        }
        AssemblyComponentInstance other = (AssemblyComponentInstance)obj;
        return other.id == this.id;
    }
}
