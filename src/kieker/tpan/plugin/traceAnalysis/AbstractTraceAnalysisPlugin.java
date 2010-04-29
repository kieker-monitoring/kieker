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

package kieker.tpan.plugin.traceAnalysis;

import kieker.tpan.plugins.IAnalysisPlugin;
import kieker.tpan.datamodel.factories.SystemEntityFactory;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractTraceAnalysisPlugin implements IAnalysisPlugin {
    private final String name;
    private final SystemEntityFactory systemEntityFactory;

    private AbstractTraceAnalysisPlugin(){
        this.name = "no name";
        this.systemEntityFactory = null;
    }

    public AbstractTraceAnalysisPlugin (final String name,
            final SystemEntityFactory systemEntityFactory){
        this.systemEntityFactory = systemEntityFactory;
        this.name = name;
    }

    protected void printMessage(final String[] lines){
        System.out.println("");
        System.out.println("#");
        System.out.println("# Plugin: " + this.name);
        for (String l : lines){
            System.out.println(l);
        }
    }

   protected final SystemEntityFactory getSystemEntityFactory() {
        return this.systemEntityFactory;
    }
}
