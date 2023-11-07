/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.mop;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import com.beust.jcommander.JCommander;

import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

/**
 * Architecture analysis main class.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class ModelOperationMain extends AbstractService<TeetimeConfiguration, Settings> {

    public static void main(final String[] args) {
        final ModelOperationMain main = new ModelOperationMain();
        try {
            final int exitCode = main.run("architecture model operations", "mop", args, new Settings());
            System.exit(exitCode);
        } catch (final IllegalArgumentException e) {
            LoggerFactory.getLogger(ModelOperationMain.class).error("Configuration error: {}", e.getLocalizedMessage());
            System.exit(1);
        }
    }

    @Override
    protected TeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new TeetimeConfiguration(this.settings);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected Path getConfigurationPath() {
        // we do not use a configuration file
        return null;
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        return true;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        if (!this.areAllInputFilesReadable(commander)) {
            return false;
        }
        if (!ParameterEvaluationUtils.checkDirectory(this.settings.getOutputDirectory().toFile(), "output model",
                commander)) {
            return false;
        }
        switch (this.settings.getOperation()) {
        case MERGE:
        case NEAREST_MERGE:
        case FUNCTION_MERGE:
            break;
        case SELECT:
            if (!ParameterEvaluationUtils.isFileReadable(this.settings.getSelectionCriteriaPath().toFile(),
                    "criteria file", commander)) {
                return false;
            }
            return this.readPatterns(this.settings.getSelectionCriteriaPath());
        default:
            return false;
        }
        return true;
    }

    private boolean areAllInputFilesReadable(final JCommander commander) {
        return this.settings.getInputModelPaths().stream()
                .allMatch(path -> ParameterEvaluationUtils.checkDirectory(path.toFile(), "input model", commander));
    }

    @Override
    protected void shutdownService() {
        // No special operation necessary.
    }

    private boolean readPatterns(final Path selectionCriteriaPath) {
        try (BufferedReader inputStream = Files.newBufferedReader(selectionCriteriaPath)) {
            String line;
            while ((line = inputStream.readLine()) != null) {
                this.settings.getSelectionCriteriaPatterns().add(Pattern.compile(line));
            }
            inputStream.close();
            return true;
        } catch (final IOException e) {
            this.logger.error("Cannot read selection criteria from {}", selectionCriteriaPath.toString());
            return false;
        }
    }
}
