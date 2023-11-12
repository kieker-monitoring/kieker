/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.tools.mktable.stages;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import teetime.framework.AbstractConsumerStage;

import org.oceandsl.analysis.generic.Table;
import org.oceandsl.analysis.generic.data.MoveOperationEntry;

/**
 * Generate a LaTeX file for all optimizations.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class CreateLaTeXTable2 extends AbstractConsumerStage<Table<String, MoveOperationEntry>> {

    private BufferedWriter writer;
    private PrintWriter printWriter;

    public CreateLaTeXTable2(final Path outputPath) {
        try {
            this.writer = Files.newBufferedWriter(outputPath);
            this.printWriter = new PrintWriter(this.writer);
            this.printWriter.println("\\documentclass{article}");
            this.printWriter.println("\\usepackage{multirow}");
            this.printWriter.println("\\begin{document}");
        } catch (final IOException e) {
            this.logger.error("Cannot write to {}, cause {}", outputPath.toString(), e.getLocalizedMessage());
        }
    }

    @Override
    protected void execute(final Table<String, MoveOperationEntry> optimization) throws Exception {
        this.logger.info("Processing {} with {} entries\n", optimization.getLabel(), optimization.getRows().size());
        final List<MoveOperationEntry> list = optimization.getRows();

        this.printWriter.printf("\\section{%s}\n", optimization.getLabel());
        this.printWriter.println("\\begin{tabular}{|r|c|l|}");
        this.printWriter.println("\\textbf{Source} & \\textbf{Operation} & \\textbf{Target} \\\\ \\hline");

        this.computeTable(list);

        this.printWriter.println("\\end{tabular}\n\n");
        this.printWriter.println("\\newpage");
        this.printWriter.flush();
    }

    private void computeTable(final List<MoveOperationEntry> list) {

        for (final MoveOperationEntry entry : list) {
            this.printWriter.printf("%s &\n%s &\n%s \\\\\n\\hline\n",
                    this.escape(this.compactComponent(entry.getSourceComponentName())),
                    this.escape(this.compactOperation(entry.getOperationName())),
                    this.escape(this.compactComponent(entry.getTargetComponentName())));
        }
    }

    private String compactOperation(final String operationName) {
        final int last = operationName.lastIndexOf(":::");
        if (last == -1) {
            return operationName;
        } else {
            return operationName.substring(last);
        }
    }

    private String compactComponent(final String componentName) {
        final int last = componentName.lastIndexOf('/');
        if (last == -1) {
            return componentName;
        } else {
            return componentName.substring(last);
        }
    }

    private String escape(final String input) {
        return input.replace("_", "\\_");
    }

    @Override
    protected void onTerminating() {
        this.printWriter.println("\\end{document}");
        this.printWriter.close();
        try {
            this.writer.close();
        } catch (final IOException e) {
            this.logger.error("Cannot close LaTeX file. Cause: {}", e);
        }
        super.onTerminating();
    }

}
