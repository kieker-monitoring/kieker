/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.kdm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.ClassOperationSignaturePair;
import kieker.common.util.Signature;
import kieker.tools.kdm.repository.KDMRepository;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;

/**
 * This class represents a {@code Kieker} filter to extract information for a KDM instance from monitored data. It enriches a given instance of {@link KDMRepository}
 * and adds information about the monitored project to the instance. Those are for example the existence of packages, classes, methods, their
 * types etc. The monitored data can either be send to this filter as instances of {@code OperationExecutionRecord} or as traces of the type {@code MessageTrace}.
 * The received data won't be filtered, which means there is no reason for forwarding the data.
 * 
 * @author Benjamin Harms, Nils Christian Ehmke
 * @version 1.0
 */
@Plugin(name = "Dynamic KDM Extractor", description = "A plugin extracting information for an instance of the KDM from monitored data",
		repositoryPorts = @RepositoryPort(name = KDMExtractorFilter.REPOSITORY_PORT_NAME_KDM_MODEL, repositoryType = KDMRepository.class))
public final class KDMExtractorFilter extends AbstractFilterPlugin {
	/**
	 * This is the name of the configuration used to determine the output file, which will be used to save the resulting KDM instance.
	 */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FILE = "outputFile";
	/**
	 * The name of the input port for message traces.
	 */
	public static final String INPUT_PORT_NAME_MSG_TRACE = "msgTraces";
	/**
	 * The name of the input port for execution records.
	 */
	public static final String INPUT_PORT_NAME_OPERATION_EXECUTION_RECORD = "executionRecords";
	/**
	 * The name of the port for the repository to be enriched.
	 */
	public static final String REPOSITORY_PORT_NAME_KDM_MODEL = "kdmRepository";
	/**
	 * This object can be used to log exceptions etc.
	 */
	private static final Log LOG = LogFactory.getLog(KDMExtractorFilter.class);
	/**
	 * This is the default value for the output file property.
	 */
	private static final String DEFAULT_PROPERTY_FILE_NAME = "";
	/**
	 * This field contains the repository to be enriched, directly after the method {@link #init()} has been called.
	 */
	private KDMRepository repository;
	/**
	 * This is the output file used to save the resulting model.
	 */
	private final String outputFileName;

	/**
	 * Creates a new instance of this class using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration used to initialize the object.
	 */
	public KDMExtractorFilter(final Configuration configuration) {
		super(configuration);

		this.outputFileName = configuration.getStringProperty(KDMExtractorFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILE);
	}

	/**
	 * Delivers the current configuration of this instance. This is usually an empty configuration.
	 * 
	 * @return A configuration object with the current configuration.
	 */
	// @Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(KDMExtractorFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILE, this.outputFileName);

		return configuration;
	}

	/**
	 * Delivers the default configuration of this class. This is usually an empty configuration.
	 * 
	 * @return A configuration object with the default configuration.
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(KDMExtractorFilter.CONFIG_PROPERTY_NAME_OUTPUT_FILE, KDMExtractorFilter.DEFAULT_PROPERTY_FILE_NAME);

		return configuration;
	}

	/**
	 * This method initializes the plugin before the analysis. It uses this method to get the connected repository. If this fails (if the repository isn't available)
	 * the initialize-method returns false.
	 * 
	 * @return true if and only if the extractor filter has been initialized properly.
	 */
	@Override
	public boolean init() {
		// Get our repository and check whether it is valid
		this.repository = (KDMRepository) super.getRepository(KDMExtractorFilter.REPOSITORY_PORT_NAME_KDM_MODEL);
		return this.repository != null;
	}

	/**
	 * This method terminates the plugin after the analysis. In this case it uses the given output file (if configured) to save the state of the repository.
	 * 
	 * @param error
	 *            Determines whether the plugin is terminated as result of an error or not.
	 */
	@Override
	public void terminate(final boolean error) {
		try {
			if (this.outputFileName.length() > 0) {
				this.repository.saveToFile(this.outputFileName);
			}
		} catch (final IOException ex) {
			KDMExtractorFilter.LOG.error("Could not save repository state.", ex);
		}
	}

	/**
	 * This is the input port for the instances of {@link MessageTrace}.
	 * 
	 * @param trace
	 *            The message trace which should be used to enrich the KDM instance.
	 */
	@InputPort(name = KDMExtractorFilter.INPUT_PORT_NAME_MSG_TRACE, description = "Input Port for the MessageTraces", eventTypes = MessageTrace.class)
	public void execute(final MessageTrace trace) {
		// Run through the available messages
		final List<AbstractMessage> messages = trace.getSequenceAsVector();

		for (final AbstractMessage message : messages) {
			// Get the method information
			final MethodInformation methodInfoRec = MethodInformation.extractMethodInformation(message.getReceivingExecution());
			final MethodInformation methodInfoSnd = MethodInformation.extractMethodInformation(message.getSendingExecution());

			// Add the methods and the method call to the repository
			this.addMethod(methodInfoRec);
			this.addMethod(methodInfoSnd);
			this.addMethodCall(methodInfoSnd, methodInfoRec);
		}
	}

	/**
	 * This is the input port for the instances of {@link OperationExecutionRecord}.
	 * 
	 * @param record
	 *            The record which should be used to enrich the KDM instance.
	 */
	@InputPort(name = KDMExtractorFilter.INPUT_PORT_NAME_OPERATION_EXECUTION_RECORD,
			description = "Input Port for the OperationExecutionRecords",
			eventTypes = OperationExecutionRecord.class)
	public void execute(final OperationExecutionRecord record) {
		final ClassOperationSignaturePair pair = ClassOperationSignaturePair.splitOperationSignatureStr(record.getOperationSignature());
		final MethodInformation methodInfo = MethodInformation.extractMethodInformation(pair);
		this.addMethod(methodInfo);
	}

	/**
	 * This method adds a method to the repository using the given method information.
	 * 
	 * @param methodInfo
	 *            The object containing the necessary information about the method.
	 */
	private void addMethod(final MethodInformation methodInfo) {
		this.repository.addPackage(methodInfo.packageNames);

		this.repository.addClass(methodInfo.packageNames, methodInfo.classNames);
		this.repository.addMethod(methodInfo.isStatic(), methodInfo.isConstructor(), methodInfo.getReturnType(), methodInfo.getVisibilityModifier(),
				methodInfo.getParameters(), methodInfo.getMethodName(), methodInfo.getClassNames(), methodInfo.getPackageNames());
	}

	/**
	 * Adds a method call to the repository using the given method information objects.
	 * 
	 * @param methodInfoSnd
	 *            The object containing the information about the sending method.
	 * @param methodInfoRcv
	 *            The object containing the information about the receiving method.
	 */
	private void addMethodCall(final MethodInformation methodInfoSnd, final MethodInformation methodInfoRcv) {
		this.repository.addMethodCall(methodInfoSnd.isStatic(), methodInfoSnd.isConstructor(), methodInfoSnd.getReturnType(), methodInfoSnd.getVisibilityModifier(),
				methodInfoSnd.getParameters(), methodInfoSnd.getMethodName(), methodInfoSnd.getClassNames(), methodInfoSnd.getPackageNames(),
				methodInfoRcv.isStatic(), methodInfoRcv.isConstructor(), methodInfoRcv.getReturnType(), methodInfoRcv.getVisibilityModifier(),
				methodInfoRcv.getParameters(), methodInfoRcv.getMethodName(), methodInfoRcv.getClassNames(), methodInfoRcv.getPackageNames());
	}

	/**
	 * This class is a write-once container for all important information about a method. As it is used only within the context of the class
	 * {@link KDMExtractorFilter} we can use it without getter-methods.
	 * 
	 * @author Nils Christian Ehmke
	 * @version 1.0
	 */
	private static final class MethodInformation {
		/**
		 * The single package names, forming the package and subpackages of the classes which is the parent of the method.
		 */
		private final String[] packageNames;
		/**
		 * The single class names, forming the classes and nested classes in which the method lies.
		 */
		private final String[] classNames;
		/**
		 * The parameters of the method.
		 */
		private final String[] parameters;
		/**
		 * The method name.
		 */
		private final String methodName;
		/**
		 * The return type of the method.
		 */
		private final String returnType;
		/**
		 * A flag determining whether the method is static.
		 */
		private final boolean staticFlag;
		/**
		 * The visibility modifier.
		 */
		private final String visibilityModifier;
		/**
		 * A flag determining whether the method is static.
		 */
		private final boolean constructor;

		/**
		 * Creates a new instance of this class, initializing the container with the given values.
		 * 
		 * @param packageNames
		 *            The package names.
		 * @param classNames
		 *            The class names.
		 * @param parameters
		 *            The parameters.
		 * @param methodName
		 *            The method's name.
		 * @param returnType
		 *            The return type.
		 * @param isStatic
		 *            A flag whether the method is static.
		 * @param isConstructor
		 *            A flag whether the method is a constructor.
		 * @param visibilityModifier
		 *            The visibility modifier.
		 */
		public MethodInformation(final String[] packageNames, final String[] classNames, final String[] parameters, final String methodName,
				final String returnType, final boolean isStatic, final boolean isConstructor, final String visibilityModifier) {
			this.packageNames = packageNames.clone();
			this.classNames = classNames.clone();
			this.parameters = parameters.clone();
			this.methodName = methodName;
			this.returnType = returnType;
			this.staticFlag = isStatic;
			this.constructor = isConstructor;
			this.visibilityModifier = visibilityModifier;
		}

		/**
		 * Determines whether the method is a constructor.
		 * 
		 * @return true iff the method is a constructor.
		 */
		public boolean isConstructor() {
			return this.constructor;
		}

		/**
		 * Extracts the method information from a given execution.
		 * 
		 * @param execution
		 *            The execution used to extract the method information.
		 * @return The information about the method stored in the given object.
		 */
		public static MethodInformation extractMethodInformation(final Execution execution) {
			final ComponentType compType = execution.getOperation().getComponentType();
			return KDMExtractorFilter.MethodInformation.extractMethodInformation(execution.getOperation().getSignature(), compType.getPackageName(),
					compType.getFullQualifiedName());
		}

		/**
		 * Extracts the method information from the given pair.
		 * 
		 * @param pair
		 *            The pair used to extract the method information.
		 * @return The method information stored in the pair.
		 */
		public static MethodInformation extractMethodInformation(final ClassOperationSignaturePair pair) {
			return KDMExtractorFilter.MethodInformation.extractMethodInformation(pair.getSignature(), pair.getPackageName(), pair.getFqClassname());
		}

		/**
		 * Delivers the package names.
		 * 
		 * @return The package names.
		 */
		public String[] getPackageNames() {
			return this.packageNames.clone();
		}

		/**
		 * Delivers the class names.
		 * 
		 * @return The class names.
		 */
		public String[] getClassNames() {
			return this.classNames.clone();
		}

		/**
		 * Delivers the parameters.
		 * 
		 * @return The parameters of the method.
		 */
		public String[] getParameters() {
			return this.parameters.clone();
		}

		/**
		 * Delivers the method's name.
		 * 
		 * @return The name of the method.
		 */
		public String getMethodName() {
			return this.methodName;
		}

		/**
		 * Delivers the return type.
		 * 
		 * @return The return type of the method.
		 */
		public String getReturnType() {
			return this.returnType;
		}

		/**
		 * Delivers the flag whether the method is static.
		 * 
		 * @return true iff the method is static.
		 */
		public boolean isStatic() {
			return this.staticFlag;
		}

		/**
		 * Delivers the visibility modifier.
		 * 
		 * @return The visibility modifier of the method.
		 */
		public String getVisibilityModifier() {
			return this.visibilityModifier;
		}

		/**
		 * Extracts the method information from the given objects.
		 * 
		 * @param signature
		 *            The signature used for the method itself.
		 * @param packageName
		 *            The name of the (nested) package in which the owner-class of the method lies.
		 * @param fqClassName
		 *            The full qualified name of the class, which is the owner of the method.
		 * @return The resulting method information.
		 */
		private static MethodInformation extractMethodInformation(final Signature signature, final String packageName, final String fqClassName) {
			boolean isStatic = false;
			String visibilityModifier = "";

			// Get the obvious properties
			final String methodName = signature.getName();
			final String returnType = signature.getReturnType();
			final String[] parameters = signature.getParamTypeList();
			final boolean isConstructor = Character.isUpperCase(methodName.charAt(0));

			// Run through the modifiers
			for (final String modifier : signature.getModifier()) {
				final String lModifier = modifier.toLowerCase(Locale.getDefault());
				if ("static".equals(lModifier)) {
					isStatic = true;
				} else {
					visibilityModifier = lModifier;
				}
			}

			// Now the classes and packages
			final String[] packageNames = packageName.split("\\.");
			final StringTokenizer tokenizer = new StringTokenizer(fqClassName, "\\.");
			final List<String> classes = new ArrayList<String>();
			while (tokenizer.hasMoreElements()) {
				final String token = tokenizer.nextToken();
				if (Character.isUpperCase(token.charAt(0))) {
					classes.add(token);
				}
			}
			if (isConstructor) {
				classes.add(methodName);
			}
			final String[] classNames = classes.toArray(new String[classes.size()]);

			return new MethodInformation(packageNames, classNames, parameters, methodName, returnType, isStatic, isConstructor, visibilityModifier);
		}

	}
}
