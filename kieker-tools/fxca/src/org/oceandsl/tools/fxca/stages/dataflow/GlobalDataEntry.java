package org.oceandsl.tools.fxca.stages.dataflow;

import lombok.Getter;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class GlobalDataEntry {

    @Getter
    private final String name;
    @Getter
    private final String files;
    @Getter
    private final String modules;
    @Getter
    private final String variables;

    public GlobalDataEntry(final String name, final String files, final String modules, final String variables) {
        this.name = name;
        this.files = files;
        this.modules = modules;
        this.variables = variables;
    }

}
