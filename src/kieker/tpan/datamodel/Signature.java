package kieker.tpan.datamodel;

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
public class Signature {
    private final String name;

    private final String returnType;
    private final String[] paramTypeList;

    private Signature(){
        this.name = null;
        this.returnType = null;
        this.paramTypeList = null;
    }

    public Signature(final String name, final String returnType,
            final String[] paramTypeList){
        this.name = name;
        this.returnType = returnType;
        this.paramTypeList = paramTypeList;
    }

    public final String getName() {
        return this.name;
    }

    public final String[] getParamTypeList() {
        return this.paramTypeList;
    }

    public final String getReturnType() {
        return this.returnType;
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(this.name).append("(")
                .append(this.paramTypeList)
                .append(")")
                .append(":")
                .append(this.returnType);

        return strBuild.toString();
    }
}
