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

package kieker.tools.kdm.repository;

import java.io.File;
import java.io.IOException;

import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;
import kieker.tools.kdm.manager.KDMModelManager;

/**
 * This is an extension of the {@link AbstractRepository} from {@code Kieker} to manage a KDM instance during the analysis. It supplies methods that can for example
 * used by filters to enrich the KDM instance. Internally it uses an instance of {@link KDMModelManager}.
 * 
 * @author Benjamin Harms, Nils Christian Ehmke
 * @version 1.0
 */
@Repository(name = "KDM Repository", description = "A repository containing and managing an instance of the KDM")
public final class KDMRepository extends AbstractRepository {

	/**
	 * This is the name of the configuration used to determine the initial input file, which will be used to load the initial KDM instance.
	 */
	public static final String CONFIG_PROPERTY_NAME_INPUT_FILE = "inputFile";
	/**
	 * This is the default value for the input file property.
	 */
	private static final String DEFAULT_PROPERTY_FILE_NAME = "";
	/**
	 * This field contains the KDMModelManager which is responsible for managing the KDM instance.
	 */
	private final KDMModelManager kdmModelManager;
	/**
	 * This is the input file used to load the initial model.
	 */
	private final String inputFileName;

	/**
	 * Creates a new instance of this class, using the given configuration object to initialize the object. If the configuration contains the property for the
	 * initial file, the constructor will try to load a KDM instance from this file. If this fails or the property doesn't exist, the object starts with an empty
	 * model instance.
	 * 
	 * @param configuration
	 *            The configuration used for loading.
	 */
	public KDMRepository(final Configuration configuration) {
		super(configuration);

		// Try to initialize the kdm instance with the potential given file
		this.inputFileName = configuration.getStringProperty(KDMRepository.CONFIG_PROPERTY_NAME_INPUT_FILE);
		final File inputFile = new File(this.inputFileName).getAbsoluteFile();

		if (inputFile.isFile()) {
			// Try to load from the model
			this.kdmModelManager = new KDMModelManager(inputFile);
		} else {
			// Initialize model normally
			this.kdmModelManager = new KDMModelManager();
		}
	}

	/**
	 * Delivers the current configuration of this instance.
	 * 
	 * @return A configuration object with the current configuration.
	 */
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(KDMRepository.CONFIG_PROPERTY_NAME_INPUT_FILE, this.inputFileName);

		return configuration;
	}

	/**
	 * Delivers the default configuration of this class.
	 * 
	 * @return A configuration object with the default configuration.
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(KDMRepository.CONFIG_PROPERTY_NAME_INPUT_FILE, KDMRepository.DEFAULT_PROPERTY_FILE_NAME);

		return configuration;
	}

	/**
	 * This method adds the given package atomically to the current model. It is interpreted as nested packages, if there is more than one element in the given
	 * array. Each element in the array should be only one single package though, like for example {@code myPackage} and <b>not</b> like
	 * {@code myPackage.mySubPackage}. If a package with the same name exists already, it won't be added to the model.
	 * 
	 * @param packageName
	 *            The name of the package(s) to be added to the model.
	 */
	public void addPackage(final String[] packageName) {
		this.kdmModelManager.addPackage(packageName);
	}

	/**
	 * This method adds the given class to the given package. If a class with the same name exists already, it won't be added to the model. The arrays are
	 * interpreted as nested packages or nested classes respectively.
	 * 
	 * @param className
	 *            The name of the class.
	 * @param packageName
	 *            The name of the package. This can be empty, but <b>not</b> null.
	 */
	public void addClass(final String[] packageName, final String[] className) {
		this.kdmModelManager.addClass(className, packageName);
	}

	/**
	 * This method adds the given method to the given class in the given package.
	 * 
	 * @param isStatic
	 *            Determines whether the new method is static.
	 * @param isConstructor
	 *            Determines whether the new method is a constructor.
	 * @param returnType
	 *            The return type of the method. If the method is a constructor, this variable will be ignored.
	 * @param visibilityModifier
	 *            The visibility modifier of the method.
	 * @param parameters
	 *            The parameters of the method.
	 * @param methodName
	 *            The name of the method.
	 * @param className
	 *            The name of the class. Multiple entries within the array are interpreted as nested classes.
	 * @param packageName
	 *            The name of the package. Multiple entries within the array are interpreted as nested packages.
	 */
	public void addMethod(final boolean isStatic, final boolean isConstructor, final String returnType, final String visibilityModifier, final String[] parameters,
			final String methodName, final String[] className, final String[] packageName) {
		this.kdmModelManager.addMethod(isStatic, isConstructor, returnType, visibilityModifier, parameters, methodName, className, packageName);
	}

	/**
	 * Adds a method call to the model using the given parameters to identify the methods.
	 * 
	 * @param srcIsStatic
	 *            Determines whether the source method is static.
	 * @param srcIsConstructor
	 *            Determines whether the source method is a constructor.
	 * @param srcReturnType
	 *            The return type of the source method.
	 * @param srcVisibilityModifier
	 *            The visibility modifier of the source method.
	 * @param srcParameters
	 *            The parameters of the source method.
	 * @param srcMethodName
	 *            The source method's name.
	 * @param srcClassName
	 *            The classes of the source method.
	 * @param srcPackageName
	 *            The packages of the source method.
	 * @param dstIsStatic
	 *            Determines whether the destination method is static.
	 * @param dstIsConstructor
	 *            Determines whether the destination method is a constructor.
	 * @param dstReturnType
	 *            The return type of the destination method.
	 * @param dstVisibilityModifier
	 *            The visibility modifier of the destination method.
	 * @param dstParameters
	 *            The parameters of the destination method.
	 * @param dstMethodName
	 *            The destination method's name.
	 * @param dstClassName
	 *            The classes of the destination method.
	 * @param dstPackageName
	 *            The packages of the destination method.
	 */
	public void addMethodCall(final boolean srcIsStatic, final boolean srcIsConstructor, final String srcReturnType, final String srcVisibilityModifier,
			final String[] srcParameters, final String srcMethodName, final String[] srcClassName, final String[] srcPackageName, final boolean dstIsStatic,
			final boolean dstIsConstructor, final String dstReturnType, final String dstVisibilityModifier, final String[] dstParameters,
			final String dstMethodName, final String[] dstClassName, final String[] dstPackageName) {
		this.kdmModelManager.addMethodCall(srcIsStatic, srcIsConstructor, srcReturnType, srcVisibilityModifier, srcParameters, srcMethodName, srcClassName,
				srcPackageName, dstIsStatic, dstIsConstructor, dstReturnType, dstVisibilityModifier, dstParameters, dstMethodName, dstClassName, dstPackageName);
	}

	/**
	 * This method saves the current state of the model manager under the given name.
	 * 
	 * @param fileName
	 *            The name under which the model instance should be stored.
	 * @throws IOException
	 *             If something went wrong during saving.
	 * @see #saveToFile(File)
	 */
	public void saveToFile(final String fileName) throws IOException {
		this.kdmModelManager.saveToFile(fileName);
	}

	/**
	 * This method saves the current state of the model manager within the given file.
	 * 
	 * @param file
	 *            The file in which the model instance should be stored.
	 * @throws IOException
	 *             If something went wrong during saving.
	 * @see #saveToFile(String)
	 */
	public void saveToFile(final File file) throws IOException {
		this.kdmModelManager.saveToFile(file);
	}
}
