/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.maa.stages;

import org.csveed.annotations.CsvCell;

public class ProvidedInterfaceEntry {

    @CsvCell(columnIndex = 1, columnName = "component-type")
    private String componentType;
    @CsvCell(columnIndex = 2, columnName = "provided-interface")
    private String providedInterface;
    @CsvCell(columnIndex = 3, columnName = "operation")
    private String operation;
    @CsvCell(columnIndex = 4, columnName = "caller-component-types")
    private String callerComponentTypes;

    public ProvidedInterfaceEntry() {
        // dummy for csveed
    }

    public ProvidedInterfaceEntry(final String componentType, final String providedInterface, final String operation,
            final String callerComponentTypes) {
        this.componentType = componentType;
        this.providedInterface = providedInterface;
        this.operation = operation;
        this.callerComponentTypes = callerComponentTypes;
    }

    public String getComponentType() {
        return this.componentType;
    }

    public void setComponentType(final String componentType) {
        this.componentType = componentType;
    }

    public String getProvidedInterface() {
        return this.providedInterface;
    }

    public void setProvidedInterface(final String providedInterface) {
        this.providedInterface = providedInterface;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public String getCallerComponentTypes() {
        return this.callerComponentTypes;
    }

    public void setCallerComponentTypes(final String callerComponentTypes) {
        this.callerComponentTypes = callerComponentTypes;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof ProvidedInterfaceEntry) {
            final ProvidedInterfaceEntry other = (ProvidedInterfaceEntry) object;
            return this.checkString(this.callerComponentTypes, other.getCallerComponentTypes())
                    && this.checkString(this.componentType, other.getComponentType())
                    && this.checkString(this.operation, other.getOperation())
                    && this.checkString(this.providedInterface, other.getProvidedInterface());
        } else {
            return false;
        }
    }

    private boolean checkString(final String left, final String right) {
        if (left == null && right == null) {
            return true;
        } else if (left != null && right != null) {
            return left.equals(right);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.callerComponentTypes.hashCode() ^ this.componentType.hashCode() ^ this.operation.hashCode()
                ^ this.providedInterface.hashCode();
    }
}
