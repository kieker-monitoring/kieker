package kieker.tpan.datamodel.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

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
public class ComponentType {

    private static final AtomicInteger nextId = new AtomicInteger(0);

    private final int id = nextId.getAndIncrement();
    private final String packageName;
    private final String typeName;
    private Collection<Operation> operations = new ArrayList<Operation>();

    public ComponentType(final String packageName,
            final String typeName) {
        this.packageName = packageName;
        this.typeName = typeName;
    }

    public ComponentType(final String fullqualifiedTypeName) {
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

    public final Operation newOperation(Signature signature){
        Operation op = new Operation(this, signature);
        this.operations.add(op);
        return op;
    }
}
