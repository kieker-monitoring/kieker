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
package kieker.tools.mt;

import java.io.IOException;
import java.nio.file.Path;

import com.beust.jcommander.JCommander;

import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

/**
 * Manipulate tables main class
 *
 * @author Reiner Jung
 * @since 1.4.0
 */
public class ManipulateTablesMain extends AbstractService<TeetimeConfiguration, Settings> {

    public static void main(final String[] args) {
        final ManipulateTablesMain main = new ManipulateTablesMain();
        try {
            final int exitCode = main.run("Manipulate tables", "mt", args, new Settings());
            System.exit(exitCode);
        } catch (final IllegalArgumentException e) {
            LoggerFactory.getLogger(ManipulateTablesMain.class).error("Configuration error: {}",
                    e.getLocalizedMessage());
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
        return true;
    }

    @Override
    protected void shutdownService() {
        // No special operation necessary.
    }

}
