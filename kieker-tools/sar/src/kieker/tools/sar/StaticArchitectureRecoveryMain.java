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
package kieker.tools.sar;

import java.io.IOException;
import java.lang.invoke.WrongMethodTypeException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.ParameterEvaluationUtils;

import teetime.framework.Configuration;
import teetime.framework.Execution;

import org.oceandsl.analysis.architecture.ArchitectureModelManagementUtils;

/**
 * Architecture analysis main class.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class StaticArchitectureRecoveryMain {

    public static final String CALLTABLE_FILENAME = "calltable.csv";
    public static final String CALLER_CALLEE_DATAFLOW_FILENAME = "dataflow-cc.csv";
    public static final String STORAGE_DATAFLOW_FILENAME = "dataflow-cb.csv";
    public static final String STORAGE_FILENAME = "common-blocks.csv";

    /** Exit code for successful operation. */
    public static final int SUCCESS_EXIT_CODE = 0;
    /** An runtime error happened. */
    public static final int RUNTIME_ERROR = 1;
    /** There was an configuration error. */
    public static final int CONFIGURATION_ERROR = 2;
    /** There was a parameter error. */
    public static final int PARAMETER_ERROR = 3;
    /** Displayed the usage message. */
    public static final int USAGE_EXIT_CODE = 4;

    /** logger for all tools. */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName()); // NOPMD

    /** true if help should be displayed. */
    protected boolean help = false; // NOPMD this is set to false for documentation purposes
    /** configuration specified as parameters. */
    protected Settings settings;
    /** configuration provided as kieker configuration file. */
    protected kieker.common.configuration.Configuration kiekerConfiguration;

    private ModelRepository repository;

    public static void main(final String[] args) {
        final StaticArchitectureRecoveryMain main = new StaticArchitectureRecoveryMain();
        try {
            final int exitCode = main.run("static architecture recovery", "sar", args, new Settings());
            System.exit(exitCode);
        } catch (final IllegalArgumentException e) {
            LoggerFactory.getLogger(StaticArchitectureRecoveryMain.class).error("Configuration error: {}",
                    e.getLocalizedMessage());
            System.exit(1);
        }
    }

    /**
     * Execute the tool.
     *
     * @param label
     *            printed to the debug log about what application is running.
     */
    private void execute(final String label) throws ConfigurationException {
        this.repository = ArchitectureModelManagementUtils.createModelRepository(String.format("%s-%s",
                this.settings.getExperimentName(), this.settings.getComponentMapFiles() != null ? "map" : "file"));

        if (this.containsValue(this.settings.getInputMode(), EInputMode.CALL, EInputMode.BOTH)) {
            this.executeConfiguration("call", label, this.createTeetimeCallConfiguration());
        }
        if (this.containsValue(this.settings.getInputMode(), EInputMode.DATAFLOW, EInputMode.BOTH)) {
            this.executeConfiguration("storage", label, this.createTeetimeStorageConfiguration());
            this.executeConfiguration("dataflow", label, this.createTeetimeDataflowConfiguration());
        }

        this.shutdownService();

        this.logger.info("Done");
    }

    private boolean containsValue(final EInputMode hasMode, final EInputMode... modes) {
        for (final EInputMode mode : modes) {
            if (mode.equals(hasMode)) {
                return true;
            }
        }
        return false;
    }

    private <T extends Configuration> void executeConfiguration(final String type, final String label,
            final T createTeetimeConfiguration) {
        final Execution<T> execution = new Execution<>(createTeetimeConfiguration);

        final Thread shutdownThread = this.shutdownHook(execution);
        this.logger.debug("Running {} {}", type, label);
        execution.executeBlocking();
        if (!shutdownThread.isAlive()) {
            Runtime.getRuntime().removeShutdownHook(shutdownThread);
        }
    }

    /**
     * General shutdown hook for services and tools.
     *
     * @param execution
     *            teetime execution
     * @return shutdown thread
     */
    private Thread shutdownHook(final Execution<?> execution) {
        final Thread shutdownThread = new Thread(new Runnable() { // NOPMD is not a web app
            /**
             * Thread to gracefully terminate service.
             */
            @Override
            public void run() {
                synchronized (execution) {
                    execution.abortEventually();
                    StaticArchitectureRecoveryMain.this.shutdownService();
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownThread);
        return shutdownThread;
    }

    private TeetimeCallConfiguration createTeetimeCallConfiguration() throws ConfigurationException {
        try {
            return new TeetimeCallConfiguration(this.logger, this.settings, this.repository);
        } catch (final IOException e) {
            this.logger.error("Error reading files. Cause: {}", e.getLocalizedMessage());
            throw new ConfigurationException(e);
        }
    }

    private TeetimeStorageConfiguration createTeetimeStorageConfiguration() throws ConfigurationException {
        try {
            return new TeetimeStorageConfiguration(this.logger, this.settings, this.repository);
        } catch (final IOException e) {
            this.logger.error("Error reading files. Cause: {}", e.getLocalizedMessage());
            throw new ConfigurationException(e);
        }
    }

    private TeetimeDataflowConfiguration createTeetimeDataflowConfiguration() throws ConfigurationException {
        try {
            return new TeetimeDataflowConfiguration(this.logger, this.settings, this.repository);
        } catch (final IOException e) {
            this.logger.error("Error reading files. Cause: {}", e.getLocalizedMessage());
            throw new ConfigurationException(e);
        }
    }

    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        if (this.containsValue(this.settings.getInputMode(), EInputMode.CALL, EInputMode.BOTH)
                && !ParameterEvaluationUtils.isFileReadable(this.settings.getInputFile()
                        .resolve(StaticArchitectureRecoveryMain.CALLTABLE_FILENAME).toFile(), "", commander)) {
            return false;
        }
        if (this.containsValue(this.settings.getInputMode(), EInputMode.DATAFLOW, EInputMode.BOTH)) {
            if (!ParameterEvaluationUtils.isFileReadable(this.settings.getInputFile()
                    .resolve(StaticArchitectureRecoveryMain.CALLER_CALLEE_DATAFLOW_FILENAME).toFile(), "", commander)) {
                return false;
            }
            if (!ParameterEvaluationUtils.isFileReadable(this.settings.getInputFile()
                    .resolve(StaticArchitectureRecoveryMain.STORAGE_DATAFLOW_FILENAME).toFile(), "", commander)) {
                return false;
            }
            if (!ParameterEvaluationUtils.isFileReadable(
                    this.settings.getInputFile().resolve(StaticArchitectureRecoveryMain.STORAGE_FILENAME).toFile(), "",
                    commander)) {
                return false;
            }
        }

        return ParameterEvaluationUtils.checkDirectory(
                this.settings.getOutputDirectory().toAbsolutePath().getParent().toFile(), "output directory",
                commander);
    }

    /**
     * Configure and execute the evaluation tool utilizing an external configuration.
     *
     * @param title
     *            start up label for debug messages
     * @param label
     *            label used during execution to indicate the running service
     * @param args
     *            arguments are ignored
     * @param stettings
     *            configuration object
     *
     * @return returns exit code
     */
    public int run(final String title, final String label, final String[] args, final Settings stettings) {
        this.settings = stettings;
        this.logger.debug(title);

        final JCommander commander = new JCommander(stettings);
        try {
            commander.parse(args);
            if (this.checkParameters(commander)) {
                if (this.help) {
                    commander.usage();
                    return StaticArchitectureRecoveryMain.USAGE_EXIT_CODE;
                } else {
                    this.execute(label);
                    return StaticArchitectureRecoveryMain.SUCCESS_EXIT_CODE;
                }
            } else {
                this.logger.error("Configuration Error"); // NOPMD
                return StaticArchitectureRecoveryMain.CONFIGURATION_ERROR;
            }
        } catch (final WrongMethodTypeException | ParameterException e) {
            this.logger.error(e.getLocalizedMessage()); // NOPMD
            commander.usage();
            return StaticArchitectureRecoveryMain.PARAMETER_ERROR;
        } catch (final ConfigurationException e) {
            this.logger.error(e.getLocalizedMessage()); // NOPMD
            commander.usage();
            return StaticArchitectureRecoveryMain.CONFIGURATION_ERROR;
        }
    }

    private void shutdownService() {
        try {
            ArchitectureModelManagementUtils.writeModelRepository(this.settings.getOutputDirectory(), this.repository);
        } catch (final IOException e) {
            this.logger.error("Error saving model: {}", e.getLocalizedMessage());
        }
    }

}
