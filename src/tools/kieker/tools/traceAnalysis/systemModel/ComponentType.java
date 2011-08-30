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

package kieker.tools.traceAnalysis.systemModel;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Andre van Hoorn
 */
public class ComponentType {
    private final int id;
    private final String packageName;
    private final String typeName;
    private Collection<Operation> operations = new ArrayList<Operation>();

    public ComponentType(final int id, final String packageName,
            final String typeName) {
        this.id = id;
        this.packageName = packageName;
        this.typeName = typeName;
    }

    public ComponentType(final int id, final String fullqualifiedTypeName) {
        this.id = id;
        String tmpPackagName;
        String tmpTypeName;
        if (fullqualifiedTypeName.indexOf('.') != -1) {
            String tmpComponentName = fullqualifiedTypeName;
            int index = 0;
            for (index = tmpComponentName.length() - 1; index > 0; index--) {
                if (tmpComponentName.charAt(index) == '.') {
                    break;
                }
            }
            tmpPackagName = tmpComponentName.substring(0, index);
            tmpTypeName = tmpComponentName.substring(index + 1);
        } else {
            tmpPackagName = "";
            tmpTypeName = fullqualifiedTypeName;
        }
        this.packageName = tmpPackagName;
        this.typeName = tmpTypeName;
    }

    public final int getId() {
        return this.id;
    }

    public final String getTypeName() {
        return this.typeName;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final String getFullQualifiedName() {
        return this.packageName + "." + typeName;
    }

    public final Collection<Operation> getOperations() {
        return this.operations;
    }

    public final Operation addOperation(Operation op){
        this.operations.add(op);
        return op;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ComponentType)){
            return false;
        }
        ComponentType other = (ComponentType)obj;
        return other.id == this.id;
    }
}
