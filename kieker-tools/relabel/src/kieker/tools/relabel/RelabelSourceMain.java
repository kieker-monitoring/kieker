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
package kieker.tools.relabel;

import java.nio.file.Files;
import java.nio.file.Path;

import com.beust.jcommander.JCommander;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

/**
 * Architecture analysis main class.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class RelabelSourceMain extends AbstractService<TeetimeConfiguration, Settings> {

    /** logger for all tools. */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName()); // NOPMD

    public static void main(final String[] args) {
        final RelabelSourceMain main = new RelabelSourceMain();
        try {
            final int exitCode = main.run("relabel source information", "relabel", args, new Settings());
            System.exit(exitCode);
        } catch (final IllegalArgumentException e) {
            LoggerFactory.getLogger(RelabelSourceMain.class).error("Configuration error: {}", e.getLocalizedMessage());
            System.exit(1);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        if (!Files.isDirectory(this.settings.getInputDirectory())) {
            this.logger.error("Input path {} is not a directory", this.settings.getInputDirectory());
            return false;
        }

        if (!Files.isDirectory(this.settings.getOutputDirectory())) {
            this.logger.error("Output path {} is not directory", this.settings.getOutputDirectory());
            return false;
        }

        if (this.settings.getReplacements().size() == 0) {
            this.logger.error("Need to specify at least one replacement rule.");
            return false;
        }

        return true;
    }

    @Override
    protected TeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
        return new TeetimeConfiguration(this.settings);
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
    protected void shutdownService() {
        // nothing to do here
    }

}
