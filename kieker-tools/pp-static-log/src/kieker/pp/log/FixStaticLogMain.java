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
package kieker.pp.log;

import java.io.IOException;
import java.nio.file.Path;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

/**
 * Fix static log which misses the component value of the callee..
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class FixStaticLogMain extends AbstractService<FixStaticLogTeetimeConfiguration, Settings> {

    public static void main(final String[] args) {
        final FixStaticLogMain main = new FixStaticLogMain();
        final int exitCode = main.run("preprocess static log", "pp-static-log", args, new Settings());

        System.exit(exitCode);
    }

    @Override
    protected FixStaticLogTeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new FixStaticLogTeetimeConfiguration(this.settings);
        } catch (final IOException e) {
            this.logger.error("IO error. Cause: {}", e.getLocalizedMessage());
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
        if (!this.settings.getInputPath().toFile().isFile()) {
            this.logger.error("Input file {} is not file", this.settings.getInputPath());
            return false;
        }
        for (final Path path : this.settings.getMapPaths()) {
            if (!path.toFile().isFile()) {
                this.logger.error("Map file {} is not file", path);
                return false;
            }
        }
        if (!this.settings.getOutputFile().getParentFile().isDirectory()) {
            this.logger.error("Directory for output file {} does not exists", this.settings.getOutputFile());
            return false;
        }

        return true;
    }

    @Override
    protected void shutdownService() {
        // nothing to do here
    }

}
