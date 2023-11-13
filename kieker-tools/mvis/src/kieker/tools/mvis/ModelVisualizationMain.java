/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mvis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import com.beust.jcommander.JCommander;

import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.analysis.generic.graph.EGraphGenerationMode;
import kieker.tools.common.AbstractService;

/**
 * Architecture analysis main class.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ModelVisualizationMain extends AbstractService<TeetimeConfiguration, Settings> {

    public static void main(final String[] args) {
        final ModelVisualizationMain main = new ModelVisualizationMain();
        try {
            final int exitCode = main.run("architecture modeler", "arch-mod", args, new Settings());
            System.exit(exitCode);
        } catch (final IllegalArgumentException e) {
            LoggerFactory.getLogger(ModelVisualizationMain.class).error("Configuration error: {}",
                    e.getLocalizedMessage());
            System.exit(1);
        }
    }

    @Override
    protected TeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new TeetimeConfiguration(this.settings);
        } catch (final IOException e) {
            this.logger.error("Error reading files. Cause: {}", e.getLocalizedMessage());
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
        if (this.settings.getGraphGenerationMode() == null) {
            this.logger.error("You need to specify a graph generation mode: {}",
                    this.createModeList(EGraphGenerationMode.values()));
            return false;
        }
        if (this.settings.getSelector() == null) {
            this.logger.error("No valid node and edge selector specificed. Valid types are: {}",
                    this.createSelectorList(ESelectorKind.values()));
            return false;
        }
        if (!Files.isDirectory(this.settings.getOutputDirectory())) {
            this.logger.error("Output path {} is not directory", this.settings.getOutputDirectory());
            return false;
        }

        return true;
    }

    private Object createSelectorList(final ESelectorKind[] values) {
        String list = null;
        for (final ESelectorKind value : values) {
            if (list == null) {
                list = value.name().toLowerCase(Locale.ROOT);
            } else {
                list += "," + value.name().toLowerCase(Locale.ROOT);
            }
        }
        return list;
    }

    private String createModeList(final EGraphGenerationMode[] values) {
        String list = null;
        for (final EGraphGenerationMode value : values) {
            if (list == null) {
                list = value.getKey();
            } else {
                list += "," + value.getKey();
            }
        }
        return list;
    }

    @Override
    protected void shutdownService() {
        // No special shutdown function required
    }

}
