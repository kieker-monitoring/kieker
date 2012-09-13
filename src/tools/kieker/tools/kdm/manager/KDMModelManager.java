/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.kdm.manager; // NOPMD (long class)

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.gmt.modisco.omg.kdm.code.AbstractCodeElement;
import org.eclipse.gmt.modisco.omg.kdm.code.ArrayType;
import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeFactory;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeItem;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.CodeRelationship;
import org.eclipse.gmt.modisco.omg.kdm.code.CompilationUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Datatype;
import org.eclipse.gmt.modisco.omg.kdm.code.ExportKind;
import org.eclipse.gmt.modisco.omg.kdm.code.IndexUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.ItemUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodKind;
import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Module;
import org.eclipse.gmt.modisco.omg.kdm.code.Namespace;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterKind;
import org.eclipse.gmt.modisco.omg.kdm.code.ParameterUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;
import org.eclipse.gmt.modisco.omg.kdm.code.impl.CodeFactoryImpl;
import org.eclipse.gmt.modisco.omg.kdm.core.KDMEntity;
import org.eclipse.gmt.modisco.omg.kdm.core.ModelElement;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Attribute;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KDMModel;
import org.eclipse.gmt.modisco.omg.kdm.kdm.KdmFactory;
import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;
import org.eclipse.gmt.modisco.omg.kdm.kdm.impl.KdmFactoryImpl;
import org.eclipse.gmt.modisco.omg.kdm.kdm.impl.KdmPackageImpl;
import org.eclipse.gmt.modisco.omg.kdm.source.AbstractInventoryElement;
import org.eclipse.gmt.modisco.omg.kdm.source.Directory;
import org.eclipse.gmt.modisco.omg.kdm.source.InventoryModel;
import org.eclipse.gmt.modisco.omg.kdm.source.SourceFile;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.kdm.manager.exception.InvalidClassException;
import kieker.tools.kdm.manager.exception.InvalidInterfaceException;
import kieker.tools.kdm.manager.exception.InvalidMethodException;
import kieker.tools.kdm.manager.exception.InvalidNamespaceException;
import kieker.tools.kdm.manager.exception.InvalidPackageException;
import kieker.tools.kdm.manager.util.AttributeIterator;
import kieker.tools.kdm.manager.util.ClassNameIterator;
import kieker.tools.kdm.manager.util.DependencyIterator;
import kieker.tools.kdm.manager.util.InterfaceNameIterator;
import kieker.tools.kdm.manager.util.MethodCallIterator;
import kieker.tools.kdm.manager.util.MethodNameIterator;
import kieker.tools.kdm.manager.util.NamespaceNameIterator;
import kieker.tools.kdm.manager.util.PackageNameIterator;
import kieker.tools.kdm.manager.util.ParameterIterator;
import kieker.tools.kdm.manager.util.descriptions.AttributeDescription;
import kieker.tools.kdm.manager.util.descriptions.DependencyDescription;
import kieker.tools.kdm.manager.util.descriptions.MethodDescription;
import kieker.tools.kdm.manager.util.descriptions.ParameterDescription;

/**
 * This class is a manager for an instance of the KDM (Knowledge-Discovery-Metamodel) specified by the OMG. The used implementation in this class originates from
 * MoDisco. The class itself is thread-safe and supplies therefore methods to access, manipulate and enrich the KDM instance atomically. The {@link KDMModelManager}
 * has mostly been developed to store Java-Code within the Code-Model of the instance.
 * 
 * @author Nils Christian Ehmke, Benjamin Harms
 */
public final class KDMModelManager { // NOPMD (long class)
	private static final Log LOG = LogFactory.getLog(KDMModelManager.class);

	/***************************************************************************************************************************************************************
	 * The following part should only contain the members of the class.
	 **************************************************************************************************************************************************************/

	/**
	 * The factory which should be used to create new model instances of the KDM-package.
	 */
	private final KdmFactory kdmFactory = new KdmFactoryImpl();
	/**
	 * The factory which should be used to create new model instances of the code-package.
	 */
	private final CodeFactory codeFactory = new CodeFactoryImpl();
	/**
	 * This is the root-segment of the whole managed KDM instance.
	 */
	private final Segment segment;
	/**
	 * This is (for the moment the only) instance of {@link CodeModel} within the segment. It is stored in this field for fast access.
	 */
	private final CodeModel codeModel;
	/**
	 * This string contains the name of the used programming language, if it could be determined.
	 */
	private String languageName;
	/**
	 * This hash map contains additionally all available packages within the KDM instance via the full qualified name. This has two reasons: First we can check very
	 * fast whether a package exists already and second we can access the package itself very fast, if we want for example add a class.
	 */
	private final ConcurrentHashMap<String, Package> packages = new ConcurrentHashMap<String, Package>();
	/**
	 * This hash map contains additionally all available classes within the KDM instance via the full qualified name.
	 */
	private final ConcurrentHashMap<String, ClassUnit> classes = new ConcurrentHashMap<String, ClassUnit>();
	/**
	 * This hash map contains additionally all available methods within the KDM instance via a special full qualified name. To assemble a method-name to access this
	 * hash map use {@link #assembleMethodQualifier(boolean, String, String, String[], String, String[], String[])}.
	 */
	private final ConcurrentHashMap<String, MethodUnit> methods = new ConcurrentHashMap<String, MethodUnit>();
	/**
	 * This hash map contains additionally all available interfaces within the KDM instance, making sure that we can check very fast whether an interface exists
	 * already.
	 */
	private final ConcurrentHashMap<String, InterfaceUnit> interfaces = new ConcurrentHashMap<String, InterfaceUnit>();
	/**
	 * This hash map is filled during initializing and contains some basic java types like void and boolean (to map those values correctly).
	 */
	private final ConcurrentHashMap<String, Datatype> javaDatatypes = new ConcurrentHashMap<String, Datatype>();
	/**
	 * This hash map contains the available arrays within the KDM instance for fast access.
	 */
	private final ConcurrentHashMap<String, ArrayType> arrays = new ConcurrentHashMap<String, ArrayType>();
	/**
	 * This hash map contains all call relationships between methods, making sure that we can check very fast whether a relationship exists already.
	 */
	private final ConcurrentHashMap<String, CodeRelationship> methodCalls = new ConcurrentHashMap<String, CodeRelationship>();
	/**
	 * This hash map contains all namespaces used in C#-models, making shure that we can check very fast whether a namespace exists already.
	 */
	private final ConcurrentHashMap<String, Namespace> namespaces = new ConcurrentHashMap<String, Namespace>();

	/***************************************************************************************************************************************************************
	 * The following part should only contain constructors
	 **************************************************************************************************************************************************************/

	/**
	 * Creates a new instance of this class, starting with an empty model instance.
	 */
	public KDMModelManager() {
		// Start with an empty model
		this.segment = this.kdmFactory.createSegment();
		this.codeModel = this.codeFactory.createCodeModel();
		this.segment.getModel().add(this.codeModel);

		this.initializeJavaDatatypes();
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param fileName
	 *            The name of the file used to load the current state of the model instance. If something goes wrong during loading, an empty model will be used
	 *            instead.
	 */
	public KDMModelManager(final String fileName) {
		// Delegate to the other constructor
		this(new File(fileName).getAbsoluteFile());
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param file
	 *            The file used to load the current state of the model instance. If something goes wrong during loading, an empty model will be used instead.
	 */
	public KDMModelManager(final File file) {
		List<CodeModel> codeModels = Collections.emptyList();

		// This is what we use in the case of an exception
		Segment newSegment = this.kdmFactory.createSegment();
		CodeModel newCodeModel = this.codeFactory.createCodeModel();
		newSegment.getModel().add(newCodeModel);

		// Try to load the model
		try {
			// We put both elements in temporal variables to make sure that they are completely correct, before assigning them. This avoids an inconsistent model.
			final Segment tempSegment = KDMModelManager.loadFromFile(file);
			// Get all code models within the segment
			codeModels = KDMModelManager.getAllCodeModels(tempSegment);
			// Use the first code model as a base and merge the remaining into it.
			final CodeModel tempCodeModel = codeModels.get(0);

			// Seems like everything worked. Assign the values
			newSegment = tempSegment;
			newCodeModel = tempCodeModel;

		} catch (final IOException ex) {
			// Something went wrong - log the error
			KDMModelManager.LOG.error("The file with the name '" + file.getAbsolutePath() + "' could not be loaded.", ex);
		} catch (final ArrayIndexOutOfBoundsException ex) {
			// Something went wrong - log the error
			KDMModelManager.LOG.error("The loaded file doesn't contain a CodeModel.", ex);
		} catch (final ClassCastException ex) {
			// Something went wrong - log the error
			KDMModelManager.LOG.error("The loaded file doesn't contain a CodeModel.", ex);
		}

		// Assign whatever value we have now for the instances
		this.segment = newSegment;
		this.codeModel = newCodeModel;

		// Initialize the hash maps
		this.initializeJavaDatatypes();
		this.clearHashMaps();
		// Use all code models to initialize
		for (final CodeModel cModel : codeModels) {
			this.initializeHashMaps(cModel);
		}
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param newSegment
	 *            The segment which is used to initialize the KDM instance. The given segment is <b>not</b> copied, which means that exterior modifications of the
	 *            models within the segment can destroy the thread-safe-property. If the segment doesn't contain a code model, an empty model will be used instead.
	 */
	public KDMModelManager(final Segment newSegment) {
		// Get all code models within the segment
		final List<CodeModel> codeModels = KDMModelManager.getAllCodeModels(newSegment);

		if (codeModels.isEmpty()) {
			// Initialize an empty model
			this.segment = this.kdmFactory.createSegment();
			this.codeModel = this.codeFactory.createCodeModel();
		} else {
			this.segment = newSegment;

			// Use the first code model as a base
			this.codeModel = codeModels.get(0);
		}

		// Initialize the hash maps
		this.initializeJavaDatatypes();
		this.clearHashMaps();
		for (final CodeModel cModel : codeModels) {
			this.initializeHashMaps(cModel);
		}
	}

	/***************************************************************************************************************************************************************
	 * The following part should only contain methods for I/O
	 **************************************************************************************************************************************************************/

	/**
	 * This method tries to load an instance of {@link Segment} from the given file. The given file should probably be a xml-representation of the
	 * MoDisco-KDM-Implementation.
	 * 
	 * @param file
	 *            The file used to load the model.
	 * @return The instance of {@link Segment} loaded from the model.
	 * @throws IOException
	 *             If something went wrong during loading.
	 */
	private static Segment loadFromFile(final File file) throws IOException {
		// Create a resource set to work with.
		final ResourceSet resourceSet = new ResourceSetImpl();
		// Initialize the package information
		KdmPackageImpl.init();
		// Set OPTION_RECORD_UNKNOWN_FEATURE prior to calling getResource.
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new EcoreResourceFactoryImpl() {

			@Override
			public Resource createResource(final URI uri) {
				final XMIResourceImpl resource = (XMIResourceImpl) super.createResource(uri);
				resource.getDefaultLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
				return resource;
			}
		});
		// Try to load the resource.
		try {
			final XMIResource resource = (XMIResource) resourceSet.getResource(URI.createFileURI(file.toString()), true);
			final EList<EObject> content;
			resource.load(Collections.EMPTY_MAP);
			content = resource.getContents();
			if (content.size() == 1) { // The first (and only) element should be the project.
				return (Segment) content.get(0);
			} else {
				throw new IOException("No segment found in file '" + file.getAbsolutePath() + "'.");
			}
			// RuntimeException org.eclipse.emf.common.util.WrappedException
		} catch (final IOException ex) {
			final IOException newEx = new IOException("Error loading file '" + file.getAbsolutePath() + "'.");
			newEx.initCause(ex);
			throw newEx; // NOPMD (stack trace preserved)
		} catch (final Exception ex) { // NOCS NOPMD (catch Exception)
			// Some exceptions like the XMIException can be thrown during loading although it cannot be seen. Catch this situation.
			final IOException newEx = new IOException("The given file '" + file.getAbsolutePath() + "' is not a valid model file.");
			newEx.initCause(ex);
			throw newEx; // NOPMD (stack trace preserved)
		}
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
		// Delegate to the other saveToFile-Method
		this.saveToFile(new File(fileName).getAbsoluteFile());
	}

	/**
	 * This method saves the current state of the model manager within the given file.
	 * 
	 * @param file
	 *            The file in which the model instance should be stored.
	 * @throws IOException
	 *             If something went wrong during saving.
	 * @see #saveToFile(String)
	 *      TODO Check whether we have to copy the segment-instance atomically or something, as the segment could theoretically be modified during the storage.
	 */
	public void saveToFile(final File file) throws IOException {
		// Create a resource and put the given project into it.
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
		resource.getContents().add(this.segment);

		// Make sure that the method uses utf8 instead of ascii.
		final Map<String, String> options = new HashMap<String, String>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");

		// Now try to save the resource.
		resource.save(options);
	}

	/***************************************************************************************************************************************************************
	 * The following part should only contain methods to enrich the KDM instance
	 **************************************************************************************************************************************************************/

	/**
	 * This method adds the given package atomically to the current model. It is interpreted as nested packages, if there is more than one element in the given
	 * array. Each element in the array should be only one single package though, like for example {@code myPackage} and <b>not</b> like
	 * {@code myPackage.mySubPackage}. If a package with the same name exists already, it won't be added to the model.
	 * 
	 * @param packageName
	 *            The name of the package(s) to be added to the model.
	 */
	public void addPackage(final String[] packageName) {
		final String fullPackageName = KDMModelManager.assembleFullQualifiedName(packageName);
		// Avoid the empty package
		if ("".equals(fullPackageName)) {
			return;
		}

		final int len = packageName.length;
		Package parent = null;
		final StringBuilder fullName = new StringBuilder();

		// Run through the whole package-array and add the packages to our model if necessary
		for (int i = 0; i < len; i++) {
			// Get the qualifier
			final String myName = packageName[i];
			if (fullName.length() != 0) {
				fullName.append('.');
			}
			fullName.append(myName);

			// Create the corresponding model instance
			final Package pack = this.codeFactory.createPackage();
			pack.setName(myName);

			// Check whether the package exists already or not
			final Package existPackage = this.packages.putIfAbsent(fullName.toString(), pack);
			if (existPackage == null) {
				// It doesn't. Put it into our model. Synchronize on the codeElement-list to access the list
				if (parent == null) {
					synchronized (this.codeModel.getCodeElement()) {
						this.codeModel.getCodeElement().add(pack);
					}
				} else {
					synchronized (parent.getCodeElement()) {
						parent.getCodeElement().add(pack);
					}
				}
			}

			// Use the right package as a parent. If the package already existed we musn't use the newly created.
			if (existPackage == null) {
				parent = pack;
			} else {
				parent = existPackage;
			}
		}
	}

	/**
	 * This method adds the given class to the kdm model. If a class with the same name exists already, it won't be added to the model. The array is
	 * interpreted as nested classes.
	 * 
	 * @param className
	 *            The name of the class.
	 * @see #addClass(String[], String[])
	 */
	public void addClass(final String[] className) {
		this.addClass(className, new String[0]);
	}

	/**
	 * This method adds the given class to the given package. If a class with the same name exists already, it won't be added to the model. The arrays are
	 * interpreted as nested packages or nested classes respectively. Since classes can also be nested within interfaces you can use this method to add a
	 * class to an interface. Just add the name of the interface to the class name-array. But be careful. In that case the interface <b>must</b> already exist.
	 * Otherwise this method will add a new class with the same name to the model.
	 * 
	 * @param className
	 *            The name of the class. It may also contain the name of an interface, if the class is nested in it.
	 * @param packageName
	 *            The name of the package. This can be empty, but <b>not</b> null.
	 */
	public void addClass(final String[] className, final String[] packageName) {
		// What we do:
		// 1) We try to find a package with the given name. If it doesn't exist, we throw an exception.
		// 2) We run through the class-array and add the nested classes to the model if necessary. Keep in mind that a class can be either in a package, a class or
		// directly within the code model, if it isn't in an package at all.

		// Assemble the full qualified package name and try to find the package
		final String fullPackageName = KDMModelManager.assembleFullQualifiedName(packageName);
		final Package pack;

		// Check whether the package exists or not - but keep in mind that the "empty" package exists always!
		if (!"".equals(fullPackageName)) {
			this.addPackage(packageName);
			pack = this.packages.get(fullPackageName);
		} else {
			pack = null;
		}

		// It does exist. We can continue
		final int len = className.length;
		Datatype parent = null;
		final StringBuilder myFullName = new StringBuilder(fullPackageName);

		// Run through the whole class-array and add the classes to our model if necessary
		for (int i = 0; i < len; i++) {
			// Get the qualifier
			final String myName = className[i];
			if (myFullName.length() != 0) {
				myFullName.append('.');
			}
			myFullName.append(myName);

			// Create the corresponding model instance
			final ClassUnit clazz = this.codeFactory.createClassUnit();
			clazz.setName(myName);

			// Check also whether it is an interface
			final InterfaceUnit existInterface = this.interfaces.get(myFullName.toString());
			ClassUnit existClass = null;
			if (existInterface == null) {
				// Check whether the class exists already or not
				existClass = this.classes.putIfAbsent(myFullName.toString(), clazz);
			}
			if ((existClass == null) && (existInterface == null)) {
				// It is neither a class nor an interface. Use a class as default here and put it into our model. Synchronize on the codeElement-list to access the
				// list
				if (parent == null) {
					// Check for the "empty" package
					if (pack == null) {
						synchronized (this.codeModel.getCodeElement()) {
							this.codeModel.getCodeElement().add(clazz);
						}
					} else {
						synchronized (pack.getCodeElement()) {
							pack.getCodeElement().add(clazz);
						}
					}
				} else {
					// Add the class to the parents list. Check whether it is a ClassUnit or an InterfaceUnit
					if (parent instanceof ClassUnit) {
						final ClassUnit classUnit = (ClassUnit) parent;
						synchronized (classUnit.getCodeElement()) {
							classUnit.getCodeElement().add(clazz);
						}
					} else if (parent instanceof InterfaceUnit) {
						final InterfaceUnit interfaceUnit = (InterfaceUnit) parent;
						synchronized (interfaceUnit.getCodeElement()) {
							interfaceUnit.getCodeElement().add(clazz);
						}
					} else {
						KDMModelManager.LOG.error("Invalid parent type '" + parent.getClass().toString() + "' while adding a class.");
					}
				}
			}

			// Use the right element as a parent. If the class already existed we musn't use the newly created.
			if (existClass != null) {
				parent = existClass;
			} else if (existInterface != null) {
				parent = existInterface;
			} else {
				parent = clazz;
			}
		}
	}

	/**
	 * This method adds the given interface to the kdm model. If an interface with the same name exists already, it won't be added to the model.
	 * The array is interpreted as nested interfaces.
	 * 
	 * @param interfaceName
	 *            The name of the interface.
	 * @see #addInterface(String[], String[])
	 */
	public void addInterface(final String[] interfaceName) {
		this.addInterface(interfaceName, new String[0]);
	}

	/**
	 * This method adds the given interface to the given package. If an interface with the same name exists already, it won't be added to the model. The arrays are
	 * interpreted as nested packages or nested interfaces respectively. Since interfaces can also be nested within classes you can use this method to add an
	 * interface to a class. Just add the name of the class to the interface name-array. But be careful. In that case the class <b>must</b> already exist.
	 * Otherwise this method will add a new interface with the same name to the model.
	 * 
	 * @param interfaceName
	 *            The name of the interface. It may also contain the name of a class, if the class is nested in it.
	 * @param packageName
	 *            The name of the package.
	 */
	public void addInterface(final String[] interfaceName, final String[] packageName) {
		// Compute full package name
		final String fullPackageName = KDMModelManager.assembleFullQualifiedName(packageName);
		final Package pack;

		// Check the package
		if (!"".equals(fullPackageName)) {
			this.addPackage(packageName);
			pack = this.packages.get(fullPackageName);
		} else {
			pack = null;
		}

		// It does exist. We can continue
		final int len = interfaceName.length;
		Datatype parent = null;
		final StringBuilder myFullName = new StringBuilder(fullPackageName);

		// Run through the whole interface-array and add the interfaces to our model if necessary
		for (int i = 0; i < len; i++) {
			// Get the qualifier
			final String myName = interfaceName[i];
			if (myFullName.length() != 0) {
				myFullName.append('.');
			}
			myFullName.append(myName);

			// Create the corresponding model instance
			final InterfaceUnit interfaze = this.codeFactory.createInterfaceUnit();
			interfaze.setName(myName);

			// Check whether it is an existing class
			final ClassUnit existClass = this.classes.get(myFullName.toString());
			InterfaceUnit existInterface = null;
			if (existClass == null) {
				// Check whether the interface exists already or not
				existInterface = this.interfaces.putIfAbsent(myFullName.toString(), interfaze);
			}
			if ((existInterface == null) && (existClass == null)) {
				// It is neither an interface nor a class. Use an interface as default here and put it into our model. Synchronize on the codeElement-list to access
				// the list
				if (parent == null) {
					// Check for the "empty" package
					if (pack == null) {
						synchronized (this.codeModel.getCodeElement()) {
							this.codeModel.getCodeElement().add(interfaze);
						}
					} else {
						synchronized (pack.getCodeElement()) {
							pack.getCodeElement().add(interfaze);
						}
					}
				} else {
					// Add the interface to the parents list. Check whether it is a ClassUnit or an InterfaceUnit
					if (parent instanceof InterfaceUnit) {
						final InterfaceUnit interfaceUnit = (InterfaceUnit) parent;
						synchronized (interfaceUnit.getCodeElement()) {
							interfaceUnit.getCodeElement().add(interfaze);
						}
					} else if (parent instanceof ClassUnit) {
						final ClassUnit classUnit = (ClassUnit) parent;
						synchronized (classUnit.getCodeElement()) {
							classUnit.getCodeElement().add(interfaze);
						}
					} else {
						KDMModelManager.LOG.error("Invalid parent type '" + parent.getClass().toString() + "' while adding an interface.");
					}
				}
			}

			// Use the right element as a parent. If the element already existed we musn't use the newly created.
			if (existInterface != null) {
				parent = existInterface;
			} else if (existClass != null) {
				parent = existClass;
			} else {
				parent = interfaze;
			}
		}
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
		// Assemble both method qualifiers
		final String srcMethodQualifier = KDMModelManager.assembleMethodQualifier(srcVisibilityModifier, srcParameters, srcMethodName,
				srcClassName, srcPackageName);
		final String dstMethodQualifier = KDMModelManager.assembleMethodQualifier(dstVisibilityModifier, dstParameters, dstMethodName,
				dstClassName, dstPackageName);
		// Assemble relationship qualifier
		final String relationshipQualifier = this.assembleMethodCallQualifier(srcMethodQualifier, dstMethodQualifier);

		final CodeRelationship codeRelationship = this.codeFactory.createCodeRelationship();
		if (this.methodCalls.putIfAbsent(relationshipQualifier, codeRelationship) == null) {
			// Try to find the methods
			MethodUnit srcMethod = this.methods.get(srcMethodQualifier);
			MethodUnit dstMethod = this.methods.get(dstMethodQualifier);

			// Add the methods if necessary
			if (srcMethod == null) {
				this.addMethod(srcIsStatic, srcIsConstructor, srcReturnType, srcVisibilityModifier, srcParameters, srcMethodName, srcClassName, srcPackageName);
				srcMethod = this.methods.get(srcMethodQualifier);
			}
			if (dstMethod == null) {
				this.addMethod(dstIsStatic, dstIsConstructor, dstReturnType, dstVisibilityModifier, dstParameters, dstMethodName, dstClassName, dstPackageName);
				dstMethod = this.methods.get(dstMethodQualifier);
			}

			// Build relationship
			codeRelationship.setFrom(srcMethod);
			codeRelationship.setTo(dstMethod);

			// Put the relationship to the model. Synchronize on the CodeRelation-list.
			synchronized (srcMethod.getCodeRelation()) {
				srcMethod.getCodeRelation().add(codeRelationship);
			}
		}
	}

	/**
	 * This method adds the given function to the kdm model.
	 * 
	 * @param isStatic
	 *            Determines whether the function is static.
	 * @param returnType
	 *            The return type of the function. May be empty, but <b>not</b> null.
	 * @param visibilityModifier
	 *            The visibility modifier of the method. May be empty, but <b>not</b> null.
	 * @param parameters
	 *            The parameters of the function. May be empty, but <b>not</b> null.
	 * @param functionName
	 *            The name of the function.
	 */

	public void addFunction(final boolean isStatic, final String returnType, final String visibilityModifier, final String[] parameters, final String functionName) {
		this.addMethod(isStatic, false, returnType, visibilityModifier, parameters, functionName, new String[0], new String[0]);
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
		// Assemble the full class name
		final String fullClassName = KDMModelManager.assembleFullQualifiedName(className);
		ClassUnit clazz;

		// Check the class
		if (!"".equals(fullClassName)) {
			this.addClass(className, packageName);
			// Assemble the full qualified name of the class to find it
			final String fullClassQualifier = KDMModelManager.assembleFullQualifiedName(packageName, className);
			clazz = this.classes.get(fullClassQualifier);
		} else {
			clazz = null; // NOPMD (null) // this smells...
		}

		// Looks like we could add the method. Check whether it exists already by using the parameters and everything to identify the method uniquely.
		final String methodQualifier = KDMModelManager.assembleMethodQualifier(visibilityModifier, parameters, methodName, className, packageName);

		final MethodUnit method = this.codeFactory.createMethodUnit();
		if (this.methods.putIfAbsent(methodQualifier, method) == null) {
			// It didn't exist already. We can safely add the properties and add it to our model
			method.setExport(KDMModelManager.translateExportKind(visibilityModifier));
			if (isConstructor) {
				method.setKind(MethodKind.CONSTRUCTOR);
			} else {
				method.setKind(MethodKind.METHOD);
			}
			method.setName(methodName);

			final Signature signature = this.codeFactory.createSignature();
			method.setType(signature);
			method.getCodeElement().add(signature);
			signature.setName(methodName);

			// Add the return type if necessary
			if (returnType.length() > 0) {
				final ParameterUnit parameterUnit = this.codeFactory.createParameterUnit();
				parameterUnit.setType(this.translateType(returnType));
				parameterUnit.setKind(ParameterKind.RETURN);

				signature.getParameterUnit().add(parameterUnit);
			}

			// Add the parameters
			int pos = 0;
			for (final String parameter : parameters) {
				if (parameter.length() == 0) {
					continue;
				}

				final ParameterUnit parameterUnit = this.codeFactory.createParameterUnit();
				parameterUnit.setType(this.translateType(parameter));
				parameterUnit.setPos(pos);
				// The name of the parameter
				final int splitPos = parameter.trim().lastIndexOf(" ");
				String parameterName = "";
				if (splitPos > 0) {
					parameterName = parameter.substring(splitPos + 1);
				}
				parameterUnit.setName(parameterName);
				if (this.isPrimitive(parameter)) {
					parameterUnit.setKind(ParameterKind.BY_VALUE);
				} else {
					parameterUnit.setKind(ParameterKind.BY_REFERENCE);
				}
				signature.getParameterUnit().add(parameterUnit);

				pos++;
			}

			// Is it a method within a class?
			if (clazz != null) {
				synchronized (clazz.getCodeElement()) {
					clazz.getCodeElement().add(method);
				}
			} else {
				// It is a global function
				synchronized (this.codeModel.getCodeElement()) {
					this.codeModel.getCodeElement().add(method);
				}
			}
		}
	}

	/***************************************************************************************************************************************************************
	 * The following part should only contain methods to iterate through the KDM instance
	 **************************************************************************************************************************************************************/

	/**
	 * This method returns an iterator for the packages directly within the kdm instance.
	 * 
	 * @return
	 *         An iterator for the packages directly within the kdm instance.
	 */
	public Iterator<String> iteratePackages() {
		// Use all elements from all code models
		final List<List<AbstractCodeElement>> elememtList = new ArrayList<List<AbstractCodeElement>>();
		for (final CodeModel modelElement : KDMModelManager.getAllCodeModels(this.segment)) {
			elememtList.add(modelElement.getCodeElement());
		}

		return new PackageNameIterator(elememtList);
	}

	/**
	 * This method returns an iterator for the packages within the given one.
	 * 
	 * @param fullPackageName
	 *            The full name of the package to iterate on.
	 * @return
	 *         An iterator for the packages within the given one.
	 * @throws InvalidPackageException
	 *             If the package does not exist.
	 */
	public Iterator<String> iteratePackages(final String fullPackageName) throws InvalidPackageException {
		return this.iteratePackages(fullPackageName, false);
	}

	/**
	 * This method returns an iterator for the packages within the given one.
	 * 
	 * @param fullPackageName
	 *            The full name of the package to iterate on.
	 * @param depthFirstSearch
	 *            If true, the iterator performs depth-first search and tries to get more information by iterating nested packages, classes and interfaces.
	 * @return
	 *         An iterator for the packages within the given one.
	 * @throws InvalidPackageException
	 *             If the package does not exist.
	 */
	public Iterator<String> iteratePackages(final String fullPackageName, final boolean depthFirstSearch) throws InvalidPackageException {
		// Does the package exists?
		final Package pack = this.packages.get(fullPackageName);
		if (pack == null) {
			throw new InvalidPackageException("A package with the name '" + fullPackageName + "' does not exist.");
		}

		// Return new iterator
		return new PackageNameIterator(pack, fullPackageName, depthFirstSearch);
	}

	/**
	 * This method returns an iterator for all classes directly within the kdm instance.
	 * 
	 * @return
	 *         An iterator for all classes directly within the kdm instance.
	 */
	public Iterator<String> iterateClasses() {
		return new ClassNameIterator(this.codeModel.getCodeElement());
	}

	/**
	 * This method returns an iterator for all classes within the given package.
	 * 
	 * @param fullPackageName
	 *            The name of the package to iterate on. Can be empty to iterate "global" classes, but <b>not</b> null.
	 * @return
	 *         An iterator for all classes within the given package.
	 * @throws InvalidPackageException
	 *             If the package does not exist.
	 */
	public Iterator<String> iterateClasses(final String fullPackageName) throws InvalidPackageException {
		return this.iterateClasses(fullPackageName, false);
	}

	/**
	 * This method returns an iterator for all classes within the given package.
	 * 
	 * @param fullPackageName
	 *            The name of the package to iterate on.
	 * @param depthFirstSearch
	 *            If true, the iterator performs depth-first search and tries to get more information by iterating nested packages, classes and interfaces.
	 * @return
	 *         An iterator for all classes within the given package.
	 * @throws InvalidPackageException
	 *             If the package does not exist.
	 */
	public Iterator<String> iterateClasses(final String fullPackageName, final boolean depthFirstSearch) throws InvalidPackageException {
		Iterator<String> iterator;
		// Is the package name empty?
		if ("".equals(fullPackageName)) {
			// Create iterator for "global" classes
			iterator = this.iterateClasses();
		} else {
			// Does the package exists?
			final Package pack = this.packages.get(fullPackageName);
			if (pack == null) {
				throw new InvalidPackageException("A package with the name '" + fullPackageName + "' does not exist.");
			}
			// Create Iterator for the given package
			iterator = new ClassNameIterator(pack, fullPackageName, depthFirstSearch);
		}

		// Return the iterator
		return iterator;
	}

	/**
	 * This method returns an iterator for all interfaces directly nested within the KDM.
	 * 
	 * @return
	 *         An iterator for all interfaces directly nested within the KDM.
	 */
	public Iterator<String> iterateInterfaces() {
		return new InterfaceNameIterator(this.codeModel.getCodeElement());
	}

	/**
	 * This method returns an iterator for all interfaces within the given package.
	 * 
	 * @param fullPackageName
	 *            The name of the package to iterate on. Can be empty to iterate "global" interfaces, but <b>not</b> null.
	 * @return
	 *         An iterator for all interfaces within the given package.
	 * @throws InvalidPackageException
	 *             If the package does not exist.
	 */
	public Iterator<String> iterateInterfaces(final String fullPackageName) throws InvalidPackageException {
		return this.iterateInterfaces(fullPackageName, false);
	}

	/**
	 * This method returns an iterator for all interfaces within the given package.
	 * 
	 * @param fullPackageName
	 *            The name of the package to iterate on.
	 * @param depthFirstSearch
	 *            If true, the iterator performs depth-first search and tries to get more information by iterating nested packages, classes and interfaces.
	 * @return
	 *         An iterator for all interfaces within the given package.
	 * @throws InvalidPackageException
	 *             If the package does not exist.
	 */
	public Iterator<String> iterateInterfaces(final String fullPackageName, final boolean depthFirstSearch) throws InvalidPackageException {
		Iterator<String> iterator;
		// Is the package name empty?
		if ("".equals(fullPackageName)) {
			// Create iterator for "global" interfaces
			iterator = this.iterateInterfaces();
		} else {
			// Does the package exists?
			final Package pack = this.packages.get(fullPackageName);
			if (pack == null) {
				throw new InvalidPackageException("A package with the name '" + fullPackageName + "' does not exist.");
			}
			// Create iterator for the given package
			iterator = new InterfaceNameIterator(pack, fullPackageName, depthFirstSearch);
		}

		// Return new iterator
		return iterator;
	}

	/**
	 * This method returns an iterator for all "global" function.
	 * 
	 * @return
	 *         An iterator for all "global" function.
	 */
	public Iterator<MethodDescription> iterateFunction() {
		// Only methods directly within the kdm model
		return new MethodNameIterator(this.codeModel.getCodeElement());
	}

	/**
	 * This method returns an iterator for all methods within the given class.
	 * 
	 * @param fullClassName
	 *            The name of the class to iterate on.
	 * @return
	 *         An iterator for all methods within the given class.
	 * @throws InvalidClassException
	 *             If the class does not exist.
	 */
	public Iterator<MethodDescription> iterateMethodsFromClass(final String fullClassName) throws InvalidClassException {
		// Does the class exists?
		final ClassUnit clazz = this.classes.get(fullClassName);
		if (clazz == null) {
			throw new InvalidClassException("A class with the name '" + fullClassName + "' does not exist.");
		}

		// Return new iterator
		return new MethodNameIterator(clazz, fullClassName);
	}

	/**
	 * This method returns an iterator for all methods within the given interface.
	 * 
	 * @param fullInterfaceName
	 *            The full name of the interface.
	 * @return
	 *         An Iterator for all methods within the given interface.
	 * @throws InvalidInterfaceException
	 *             If the interface does not exist.
	 */
	public Iterator<MethodDescription> iterateMethodsFromInterface(final String fullInterfaceName) throws InvalidInterfaceException {
		// Does the interface exists?
		final InterfaceUnit interfaze = this.interfaces.get(fullInterfaceName);
		if (interfaze == null) {
			throw new InvalidInterfaceException("An interface with the name '" + fullInterfaceName + "' does not exist.");
		}

		// Return new iterator
		return new MethodNameIterator(interfaze, fullInterfaceName);
	}

	/**
	 * This method returns an iterator for all parameter of the given method.
	 * 
	 * @param fullMethodName
	 *            The full name of the method. You have to use the unique method qualifier provided by a {@link MethodDescription}. See also
	 *            {@link #iterateMethodsFromClass(String)} and {@link #iterateMethodsFromInterface(String)}.
	 * @return
	 *         An iterator for all parameter of the given method.
	 * @throws InvalidMethodException
	 *             If the method does not exist.
	 */
	public Iterator<ParameterDescription> iterateParameter(final String fullMethodName) throws InvalidMethodException {
		// Does the method exists?
		final MethodUnit method = this.methods.get(fullMethodName);
		if (method == null) {
			throw new InvalidMethodException("A method with the name '" + fullMethodName + "' does not exist.");
		}

		// Return new Iterator
		return new ParameterIterator(method);
	}

	/**
	 * This method returns an iterator for all method calls of the given method.
	 * 
	 * @param fullMethodName
	 *            The full name of the method. You have to use the unique method qualifier provided by a {@link MethodDescription}. See also
	 *            {@link #iterateMethodsFromClass(String)} and {@link #iterateMethodsFromInterface(String)}.
	 * @return
	 *         An iterator for all method calls of the given method.
	 * @throws InvalidMethodException
	 *             If the method does not exist.
	 */
	public Iterator<String> iterateMethodCalls(final String fullMethodName) throws InvalidMethodException {
		// Does the method exists?
		final MethodUnit methodUnit = this.methods.get(fullMethodName);
		if (methodUnit == null) {
			throw new InvalidMethodException("A method with the name '" + fullMethodName + "' does not exist.");
		}

		// Return new Iterator
		return new MethodCallIterator(methodUnit);
	}

	/**
	 * This method creates an iterator for dependencies of the given {@link Package}.
	 * 
	 * @param fullPackageName
	 *            The full name of the {@link Package}.
	 * @return
	 *         An iterator for all dependencies of the given package.
	 * @throws InvalidPackageException
	 *             If the package does nocht exist.
	 */
	public Iterator<DependencyDescription> iterateDependenciesFromPackage(final String fullPackageName) throws InvalidPackageException {
		// Get the package and check whether it exist or not
		final Package pack = this.getPackage(fullPackageName);

		return new DependencyIterator(pack);
	}

	/**
	 * This method returns an iterator for all dependencies of the given class.
	 * 
	 * @param fullClassName
	 *            The full name of the class.
	 * @return
	 *         An iterator for all dependencies of the given class.
	 * @throws InvalidClassException
	 *             If the class does not exist.
	 */
	public Iterator<DependencyDescription> iterateDependenciesFromClass(final String fullClassName) throws InvalidClassException {
		// Does the class exists?
		final ClassUnit classUnit = this.classes.get(fullClassName);
		if (classUnit == null) {
			throw new InvalidClassException("A class with the name '" + fullClassName + "' does not exist.");
		}

		// Return new Iterator
		return new DependencyIterator(classUnit);
	}

	/**
	 * This method returns an iterator for all dependencies of the given interface.
	 * 
	 * @param fullInterfaceName
	 *            The full name of the interface.
	 * @return
	 *         An iterator for all dependencies of the given interface.
	 * @throws InvalidInterfaceException
	 *             If the interface does not exist.
	 */
	public Iterator<DependencyDescription> iterateDependenciesFromInterface(final String fullInterfaceName) throws InvalidInterfaceException {
		// Does the interface exists?
		final InterfaceUnit interfaceUnit = this.interfaces.get(fullInterfaceName);
		if (interfaceUnit == null) {
			throw new InvalidInterfaceException("An interface with the name '" + fullInterfaceName + "' does not exist.");
		}

		// Return new Iterator
		return new DependencyIterator(interfaceUnit);
	}

	/**
	 * This method returns an iterator for all dependencies of the given namespace.
	 * 
	 * @param fullNamespaceName
	 *            The full name of the namespace.
	 * @return
	 *         An iterator for all dependencies of the given namespace.
	 * @throws InvalidNamespaceException
	 *             If the namespace does not exist.
	 */
	public Iterator<DependencyDescription> iterateDependenciesFromNamespace(final String fullNamespaceName) throws InvalidNamespaceException {
		final Namespace namespaze = this.getNamespace(fullNamespaceName);

		return new DependencyIterator(namespaze);
	}

	/**
	 * This method returns an iterator for all attributes of the given class.
	 * 
	 * @param fullClassName
	 *            The full name of the class.
	 * @return
	 *         An iterator for all attributes of the given class.
	 * @throws InvalidClassException
	 *             If the class does not exist.
	 */
	public Iterator<AttributeDescription> iterateAttributesFromClass(final String fullClassName) throws InvalidClassException {
		final ClassUnit clazz = this.getClassUnit(fullClassName);

		return new AttributeIterator(clazz);
	}

	/**
	 * This method returns an iterator for all attributes of the given interface.
	 * 
	 * @param fullInterfaceName
	 *            The full name of the interface.
	 * @return
	 *         An iterator for all attributes of the given interface.
	 * @throws InvalidInterfaceException
	 *             If the interface does not exist.
	 */
	public Iterator<AttributeDescription> iterateAttributesFromInterface(final String fullInterfaceName) throws InvalidInterfaceException {
		final InterfaceUnit interfaze = this.getInterfaceUnit(fullInterfaceName);

		return new AttributeIterator(interfaze);
	}

	/**
	 * This method return an iterator for all {@link Namespace} directly within the code model.
	 * 
	 * @return
	 *         An iterator for all {@link Namespace} directly within the code model.
	 * @throws NoSuchElementException
	 *             If no namespaces exist.
	 */
	public Iterator<String> iterateNamespaces() throws NoSuchElementException {
		// Namespaces directly within the model
		final Module module = this.getNamespaceModule();
		return new NamespaceNameIterator(module.getCodeElement());
	}

	/**
	 * This method return an iterator for all {@link Namespace} within the given namespace.
	 * 
	 * @param fullNamespaceName
	 *            The full name of the namespace.
	 * @return
	 *         An iterator for all {@link Namespace} within the given namespace.
	 * @throws InvalidNamespaceException
	 *             If the namespace does not exist.
	 */
	public Iterator<String> iterateNamespaces(final String fullNamespaceName) throws InvalidNamespaceException {
		final Namespace namespaze = this.getNamespace(fullNamespaceName);

		return new NamespaceNameIterator(namespaze, fullNamespaceName, false);
	}

	/**
	 * This method return an iterator for all {@link Namespace} within the given namespace.
	 * 
	 * @param fullNamespaceName
	 *            The full name of the namespace.
	 * @param depthFirstSearch
	 *            If true the iterator performs a depth-first search and tries to get more information by iterating nested namespaces.
	 * @return
	 *         An iterator for all {@link Namespace} within the given namespace.
	 * @throws InvalidNamespaceException
	 *             If the namespace does not exist.
	 */
	public Iterator<String> iterateNamespaces(final String fullNamespaceName, final boolean depthFirstSearch) throws InvalidNamespaceException {
		final Namespace namespaze = this.getNamespace(fullNamespaceName);

		return new NamespaceNameIterator(namespaze, fullNamespaceName, depthFirstSearch);
	}

	/**
	 * This method returns an iterator for all classes within the given {@link Namespace}.
	 * 
	 * @param fullNamespaceName
	 *            The full name of the {@link Namespace}.
	 * @return
	 *         An iterator for all classes within the given {@link Namespace}.
	 * @throws InvalidNamespaceException
	 *             If the namespace does not exist.
	 */
	public Iterator<String> iterateClassesFromNamespace(final String fullNamespaceName) throws InvalidNamespaceException {
		final Namespace namepsaze = this.getNamespace(fullNamespaceName);
		return new ClassNameIterator(namepsaze, fullNamespaceName);
	}

	/**
	 * This method returns an iterator for all interfaces within the given {@link Namespace}.
	 * 
	 * @param fullNamespaceName
	 *            The full name of the {@link Namespace}.
	 * @return
	 *         An iterator for all interfaces within the given {@link Namespace}.
	 * @throws InvalidNamespaceException
	 *             If the namespace does not exist.
	 */
	public Iterator<String> iterateInterfacesFromNamespace(final String fullNamespaceName) throws InvalidNamespaceException {
		final Namespace namespaze = this.getNamespace(fullNamespaceName);
		return new InterfaceNameIterator(namespaze, fullNamespaceName);
	}

	/***************************************************************************************************************************************************************
	 * The following part should only contain public getter
	 **************************************************************************************************************************************************************/

	/**
	 * This method returns the name of the project.
	 * 
	 * @return
	 *         The name of the project.
	 */
	public String getProjectName() {
		String name = "";

		if (this.codeModel != null) {
			name = this.codeModel.getName();
		}

		return name;
	}

	/**
	 * This method tries to find out the language of the project from an {@link InventoryModel}.
	 * 
	 * @return
	 *         The language of the project if it could be determined, otherwise an empty string.
	 */
	public String getProjectLanguage() {
		// If the language is determined just return it
		if ((this.languageName != null) && (this.languageName.length() > 0)) {
			return this.languageName;
		} else { // Try to find out from the InventoryModel
			try {
				final InventoryModel inventoryModel = KDMModelManager.getInventoryModel(this.segment);
				Iterator<AbstractInventoryElement> inventoryIterator = inventoryModel.getInventoryElement().iterator();
				// Check a SourceFile-element to use the language attribute.
				while (inventoryIterator.hasNext()) {
					final AbstractInventoryElement element = inventoryIterator.next();
					// Directory? ==> look into
					if (element instanceof Directory) {
						final Directory directory = (Directory) element;
						inventoryIterator = directory.getInventoryElement().iterator();
					} else if (element instanceof SourceFile) {
						final SourceFile sourceFile = (SourceFile) element;
						final String language = sourceFile.getLanguage();
						if (language.length() > 0) {
							// Set up the language and break
							this.languageName = language;
							break;
						}
					}
				}
			} catch (final NoSuchElementException ex) {
				// No InventoryModel exist
				this.languageName = "";
			}
		}
		return this.languageName;
	}

	/**
	 * This method returns the kdm {@link Package} element of the package with the given name. <b>Attention! Use at your own risk! Use it read only!</b>
	 * 
	 * @param fullPackageName
	 *            The full name of the package.
	 * @return
	 *         The kdm {@link Package} element.
	 * @throws InvalidPackageException
	 *             If the package does not exist.
	 */
	public Package getPackage(final String fullPackageName) throws InvalidPackageException {
		final Package pack = this.packages.get(fullPackageName);
		if (pack == null) {
			throw new InvalidPackageException("A package with the name '" + fullPackageName + "' does not exist.");
		}

		return pack;
	}

	/**
	 * This method returns the kdm {@link ClassUnit} element of the class with the given name. <b>Attention! Use at your own risk! Use it read only!</b>
	 * 
	 * @param fullClassName
	 *            The full name of the class.
	 * @return
	 *         The kdm {@link ClassUnit} element.
	 * @throws InvalidClassException
	 *             If the class does not exist.
	 */
	public ClassUnit getClassUnit(final String fullClassName) throws InvalidClassException {
		final ClassUnit classUnit = this.classes.get(fullClassName);
		if (classUnit == null) {
			throw new InvalidClassException("A class with the name '" + fullClassName + "' does not exist.");
		}

		return classUnit;
	}

	/**
	 * This method returns the kdm {@link InterfaceUnit} element for the interface with the given name. <b>Attention! Use at your own risk! Use it read only!</b>
	 * 
	 * @param fullInterfaceName
	 *            The full name of the interface.
	 * @return
	 *         The kdm {@link InterfaceUnit} element.
	 * @throws InvalidInterfaceException
	 *             If the interface does not exist.
	 */
	public InterfaceUnit getInterfaceUnit(final String fullInterfaceName) throws InvalidInterfaceException {
		final InterfaceUnit interfaceUnit = this.interfaces.get(fullInterfaceName);
		if (interfaceUnit == null) {
			throw new InvalidInterfaceException("An interface with the name '" + fullInterfaceName + "' does not exist.");
		}

		return interfaceUnit;
	}

	/**
	 * This method returns the kdm {@link MethodUnit} element for the method with the given qualifier. <b>Attention! Use at your own risk! Use it read only!</b>
	 * 
	 * @param methodQualifier
	 *            The unique qualifier to identify the method. See {@link MethodDescription}.
	 * @return
	 *         The kdm {@link MethodUnit} element.
	 * @throws InvalidMethodException
	 *             If the method does not exist.
	 */
	public MethodUnit getMethodUnit(final String methodQualifier) throws InvalidMethodException {
		final MethodUnit methodUnit = this.methods.get(methodQualifier);
		if (methodUnit == null) {
			throw new InvalidMethodException("A method with the name '" + methodQualifier + "' does not exist.");
		}

		return methodUnit;
	}

	/**
	 * This method returns the kdm {@link MethodUnit} element for the method matching the given {@link MethodDescription}. <b>Attention! Use at your own risk! Use it
	 * read only!</b>
	 * 
	 * @param description
	 *            The {@link MethodDescription} for the method.
	 * @return
	 *         The kdm {@link MethodUnit} element.
	 * @throws InvalidMethodException
	 *             If the method does not exist.
	 * @see #iterateMethodsFromClass(String), {@link #iterateMethodsFromInterface(String)} and {@link #iterateFunction()}.
	 */
	public MethodUnit getMethodUnit(final MethodDescription description) throws InvalidMethodException {
		return this.getMethodUnit(description.getMethodQualifier());
	}

	/**
	 * This method returns the kdm {@link Namespace} element for the namespace matching the given name. <b>Attention! Use at your own risk! Use it read only!</b>
	 * 
	 * @param fullNamespaceName
	 *            The full name of the namespace.
	 * @return
	 *         The kdm {@link Namespace} element.
	 * @throws InvalidNamespaceException
	 *             If the namespace does not exist.
	 */
	public Namespace getNamespace(final String fullNamespaceName) throws InvalidNamespaceException {
		final Namespace namespaze = this.namespaces.get(fullNamespaceName);
		if (namespaze == null) {
			throw new InvalidNamespaceException("A namespace with the name '" + fullNamespaceName + "' does not exist.");
		}
		return namespaze;
	}

	/***************************************************************************************************************************************************************
	 * The following part should only contain private and static methods for subtasks
	 **************************************************************************************************************************************************************/

	/**
	 * Translates a given visibility modifier into an instance of the enum {@link ExportKind}. It is assumed that the given parameter is in lower case.
	 * 
	 * @param visibilityModifier
	 *            The textual representation of a visibility modifier in lower cases.
	 * @return The corresponding {@link ExportKind} if it exists, unknown otherwise.
	 */
	private static ExportKind translateExportKind(final String visibilityModifier) {
		final ExportKind result;
		if ("private".equals(visibilityModifier)) {
			result = ExportKind.PRIVATE;
		} else if ("public".equals(visibilityModifier)) {
			result = ExportKind.PUBLIC;
		} else if ("protected".equals(visibilityModifier)) {
			result = ExportKind.PROTECTED;
		} else {
			result = ExportKind.UNKNOWN;
		}

		return result;
	}

	/**
	 * This method uses the given array to assemble a full qualified name, by appending the strings and separating them with dots.
	 * 
	 * @param array
	 *            The array containing the single names for the full qualified name
	 * @return The full qualified name.
	 */
	private static String assembleFullQualifiedName(final String[] array) {
		if (array.length == 0) {
			return "";
		}
		final StringBuilder buffer = new StringBuilder();

		// Run through the array and assemble the name
		for (final String elem : array) {
			buffer.append(elem).append('.');
		}

		// There is now one dot too much. Remove it
		buffer.deleteCharAt(buffer.length() - 1);

		return buffer.toString();
	}

	/**
	 * This method uses the given arrays to assemble a full qualified name, by appending the strings and separating them with dots.
	 * 
	 * @param arrayFst
	 *            The first array containing the single names for the full qualified name
	 * @param arraySnd
	 *            The second array containing the single names for the full qualified name
	 * @return The full qualified name consisting of the full qualified names of both arrays.
	 */
	private static String assembleFullQualifiedName(final String[] arrayFst, final String[] arraySnd) {
		final StringBuilder result = new StringBuilder();

		result.append(KDMModelManager.assembleFullQualifiedName(arrayFst));
		if (result.length() != 0) {
			result.append('.');
		}
		result.append(KDMModelManager.assembleFullQualifiedName(arraySnd));

		return result.toString();
	}

	/**
	 * This method assembles the given values to a method qualifier which is used to identify methods within the hash map.
	 * 
	 * @param visibilityModifier
	 *            The visibility modifier of the method.
	 * @param parameters
	 *            The parameters of the method.
	 * @param methodName
	 *            The method's name.
	 * @param className
	 *            The class name of the method.
	 * @param packageName
	 *            The package method of the method.
	 * @return The full qualified name of the method.
	 */
	private static String assembleMethodQualifier(final String visibilityModifier, final String[] parameters, final String methodName, final String[] className,
			final String[] packageName) {
		final StringBuilder result = new StringBuilder();

		result.append(visibilityModifier).append(' ');

		final String fullQualifiedName = KDMModelManager.assembleFullQualifiedName(packageName, className);
		// In case of a global function there is no package and no class.
		// So there is no dot needed if the full qualified name is an empty string
		if (fullQualifiedName.length() > 0) {
			result.append(fullQualifiedName).append('.');
		}
		result.append(methodName);
		result.append(Arrays.toString(parameters));

		return result.toString();
	}

	/**
	 * Delivers all instances of {@link CodeModel} within the given segment. It doesn't modify the given instance in a destructive way though.
	 * 
	 * @param segment
	 *            The segment with the model instances.
	 * @return A list with all available code models within the given segment.
	 */
	private static List<CodeModel> getAllCodeModels(final Segment segment) {
		final List<CodeModel> result = new ArrayList<CodeModel>();

		for (final KDMModel model : segment.getModel()) {
			if (model instanceof CodeModel) {
				result.add((CodeModel) model);
			}
		}
		return result;
	}

	/**
	 * Searches for the first {@link InventoryModel} in the given {@link Segment}.
	 * 
	 * @param segment
	 *            The segment with the model instances.
	 * @return
	 *         The first {@link InventoryModel} found within the given segment.
	 * @throws NoSuchElementException
	 *             If the segment does nocht contain an {@link InventoryModel}.
	 */
	private static InventoryModel getInventoryModel(final Segment segment) throws NoSuchElementException {
		for (final KDMModel model : segment.getModel()) {
			if (model instanceof InventoryModel) {
				return (InventoryModel) model;
			}
		}

		throw new NoSuchElementException("The given segment does not contain an InvertoryModel.");
	}

	/**
	 * This method is used to reassemble a method qualifier from a method unit within the KDM model. It is necessary to initialize the hash maps.
	 * 
	 * @param methodUnit
	 *            The method unit containing all necessary information about the method.
	 * @param fullParentName
	 *            The full name of the parent (class, interface, ...).
	 * @return
	 *         The full qualified name of the method.
	 */
	private static String reassembleMethodQualifierFromModel(final MethodUnit methodUnit, final String fullParentName) {
		final StringBuilder qualifier = new StringBuilder();
		// Export kind
		qualifier.append(methodUnit.getExport().toString()).append(" ");

		qualifier.append(fullParentName);
		qualifier.append(methodUnit.getName());

		// Parameters
		qualifier.append("[");
		final Signature signature = KDMModelManager.getSignature(methodUnit);
		if (signature != null) {
			final StringBuilder parameters = new StringBuilder();
			final Iterator<ParameterUnit> iterator = signature.getParameterUnit().iterator();
			while (iterator.hasNext()) {
				// Next element
				final ParameterUnit parameterUnit = iterator.next();
				// Ignore return type
				if (!ParameterKind.RETURN.equals(parameterUnit.getKind())) {
					final Datatype type = parameterUnit.getType();
					// Check for array type
					if (type instanceof ArrayType) {
						final ArrayType arrayType = (ArrayType) type;
						final ItemUnit itemUnit = arrayType.getItemUnit();
						// The item unit describes the items, so its type is the type of the array
						parameters.append(itemUnit.getType().getName());
						// It is an array, so add some brackets
						parameters.append("[] ");
						// Else primitive type
					} else {
						// Only the name of the type must be added
						parameters.append(type.getName());
						parameters.append(" ");
					}

					// Do not forget the name of the parameter
					parameters.append(parameterUnit.getName());
					// Separator only needed, if there are more elements
					if (iterator.hasNext()) {
						parameters.append(", ");
					}
				}
			}

			// Add the parameters
			qualifier.append(parameters.toString());
		}

		qualifier.append("]");

		return qualifier.toString();
	}

	/**
	 * This method returns the <b>first</b> Signature within the given method unit or null if it does not exist.
	 * 
	 * @param methodUnit
	 *            The method unit to get the signature of.
	 * @return
	 *         The first signature if it exists or null.
	 */
	private static Signature getSignature(final MethodUnit methodUnit) {
		for (final AbstractCodeElement element : methodUnit.getCodeElement()) {
			if (element instanceof Signature) {
				return (Signature) element;
			}
		}

		return null;
	}

	/**
	 * This method tries to get an {@link Attribute} with the given tag and returns the value.
	 * 
	 * @param item
	 *            The {@link CodeItem} to get the attribute from.
	 * @param tag
	 *            The value for the tag-attribute of the {@link Attribute}.
	 * @return
	 *         The {@link Attribute} with the tag value 'FullyQualifiedName'.
	 * @throws NoSuchElementException
	 *             If no such attribute exist.
	 */
	private static String getValueFromAttribute(final CodeItem item, final String tag) throws NoSuchElementException {
		if ((tag != null) && (tag.length() > 0)) {
			for (final Attribute attribute : item.getAttribute()) {
				if (attribute.getTag().equals(tag)) {
					return attribute.getValue();
				}
			}
		}
		throw new NoSuchElementException();
	}

	/**
	 * This method removes the prefix 'global' used in the C#-models.
	 * 
	 * @param value
	 *            The value to remove the prefix.
	 * @return
	 *         The String without the prefix or an empty string if value is null.
	 */
	private static String removeGlobalCSharpPrefix(final String value) {
		final String prefix = "global.";
		String result = "";

		// Avoid NullPointerException
		if (value == null) {
			return result;
		}
		// Remove the prefix if it exist
		if (value.startsWith(prefix)) {
			result = value.substring(prefix.length());
		} else {
			result = value;
		}

		return result;
	}

	/***************************************************************************************************************************************************************
	 * The following part should only contain private and non-static methods for subtasks
	 **************************************************************************************************************************************************************/

	/**
	 * Checks whether the given type is a java primitive.
	 * 
	 * @param type
	 *            The type to be checked.
	 * @return
	 *         true if and only if the given type represents a java primitive.
	 */
	private boolean isPrimitive(final String type) {
		return this.javaDatatypes.containsKey(type);
	}

	/**
	 * This method tries to translate the given string into an instance of {@link Datatype}. This means that we either have this type already in our KDM instance and
	 * can link to it or we don't know it (yet) and have to find a substitute.
	 * 
	 * @param typeName
	 *            The name of the type to be translated.
	 * @return A "translated" version of the given type.
	 */
	private Datatype translateType(final String typeName) {
		// We have following possibilities:
		// 1) The given typeName is a normal class or a primitive type in which case we try to find it in our existing classes and primitive types. If this fails, we
		// have to create it.
		// 2) The given typeName is an array. In which case we have the possibility, that it already exists in our array map or if not it can be an array of an
		// already known class or primitive type. In the latter cases we have to create this new array type and put it into our map.

		// Check whether we have an array or not.
		final boolean isArray;
		if (typeName.contains("[]")) {
			isArray = true;
		} else {
			isArray = false;
		}

		String pureTypeName;
		if (typeName.contains(" ")) {
			final int splitPos = typeName.lastIndexOf(' ');
			pureTypeName = typeName.substring(0, splitPos);
		} else {
			pureTypeName = typeName;
		}

		if (isArray) {
			// Array type
			final Datatype arrayType = this.arrays.get(pureTypeName);
			if (arrayType != null) {
				return arrayType;
			}

			// The array doesn't exist yet. We have to create it
			this.createDatatype(pureTypeName);
			// Now it should exist
			return this.arrays.get(pureTypeName);
		} else {

			// No array type
			final Datatype primType = this.javaDatatypes.get(pureTypeName);
			if (primType != null) {
				return primType;
			}

			// Not a primitive type either
			final Datatype classType = this.classes.get(pureTypeName);
			if (classType != null) {
				return classType;
			}

			// It is a class, but we don't have it yet.
			this.createDatatype(pureTypeName);
			// Now the class should exist
			return this.classes.get(pureTypeName);
		}
	}

	/**
	 * This method creates the given typeName as data type, if it doesn't exist already. It can also handle arrays.
	 * 
	 * @param typeName
	 *            The typeName to be created.
	 */
	private void createDatatype(final String typeName) {
		final String pureTypeName;

		if (typeName.contains("[]")) {
			// Array type
			pureTypeName = typeName.replace("[]", "");

			final ArrayType arrayType = this.codeFactory.createArrayType();
			final ItemUnit itemUnit = this.codeFactory.createItemUnit();
			final IndexUnit indexUnit = this.codeFactory.createIndexUnit();
			boolean foundType = false;
			indexUnit.setType(this.javaDatatypes.get("int"));

			arrayType.setItemUnit(itemUnit);
			arrayType.setIndexUnit(indexUnit);

			final Datatype primType = this.javaDatatypes.get(pureTypeName);

			if (primType != null) {
				itemUnit.setType(primType);
				foundType = true;
			}

			// Not a primitive type either
			if (!foundType) {
				final Datatype classType = this.classes.get(pureTypeName);
				if (classType != null) {
					itemUnit.setType(classType);
					foundType = true;
				}
			}

			// We have to create the classes etc.
			if (!foundType) {
				this.createDatatype(pureTypeName);
				itemUnit.setType(this.classes.get(pureTypeName));
			}

			final ArrayType last = this.arrays.putIfAbsent(typeName, arrayType);
			if (last == null) {
				// We have to add it to our model as well
				synchronized (this.codeModel.getCodeElement()) {
					this.codeModel.getCodeElement().add(arrayType);
				}
			}
		} else {
			// No array type
			pureTypeName = typeName;

			final String[] subStrings = pureTypeName.split("\\.");
			int upperCaseCounter = 0;
			for (final String string : subStrings) {
				if (Character.isUpperCase(string.charAt(0))) {
					upperCaseCounter++;
				}
			}
			final String[] packageNames = new String[subStrings.length - upperCaseCounter];
			final String[] classNames = new String[upperCaseCounter];
			int i = 0;
			int k = 0;
			for (final String string : subStrings) {
				if (Character.isUpperCase(string.charAt(0))) {
					classNames[i] = string;
					i++;
				} else {
					packageNames[k] = string;
					k++;
				}
			}
			// Create the packages and classes
			this.addClass(classNames, packageNames);
		}
	}

	/**
	 * This method clears all concurrent hash maps, but javaDatatypes.
	 */
	private void clearHashMaps() {
		// Clear the hash maps
		this.packages.clear();
		this.classes.clear();
		this.interfaces.clear();
		this.methods.clear();
		this.methodCalls.clear();
		this.arrays.clear();
	}

	/**
	 * This method initializes them based on the current code model. This method should be called directly after a file has been loaded and
	 * only once for a code model. It is <b>not</b> thread safe, as it doesn't look the code model or something. It is assumed that the primitive
	 * java types have already been initialized when calling this method.
	 * 
	 * @param cModel
	 *            Elements from this {@link CodeModel} will be added to the hash maps.
	 */
	private void initializeHashMaps(final CodeModel cModel) {
		// Check the model first
		if ((cModel == null) || (cModel.getCodeElement() == null)) {
			KDMModelManager.LOG.error("Hash map initialization faild because the code model is null.");
			return;
		}
		// Fill them again
		final Stack<Iterator<? extends ModelElement>> iteratorStack = new Stack<Iterator<? extends ModelElement>>();
		final Stack<String> nameStack = new Stack<String>();
		Iterator<? extends ModelElement> currentIterator = cModel.getCodeElement().iterator();
		ModelElement currentElement;
		boolean condition = currentIterator.hasNext();
		// Constant name used on the top level
		final String globalConstant = "global";
		final String nameAttributeKey = "FullyQualifiedName";

		while (condition) {
			// Next element
			currentElement = currentIterator.next();
			final StringBuilder name = new StringBuilder();
			// Assemble full name
			if (!nameStack.isEmpty()) {
				name.append(nameStack.peek());
				if (!name.toString().endsWith(".")) {
					name.append('.');
				}
			}

			// What is the current element?
			if (currentElement instanceof Package) {
				final Package pack = (Package) currentElement;
				// Assemble name
				name.append(pack.getName());
				// Put the package into the hash map
				this.packages.put(name.toString(), pack);
				// Keep child elements in mind
				iteratorStack.push(currentIterator);
				currentIterator = pack.getCodeElement().iterator();
				// Save current name
				nameStack.push(name.toString());
			} else if (currentElement instanceof ClassUnit) {
				final ClassUnit clazz = (ClassUnit) currentElement;
				// Assemble name, used for Java
				name.append(clazz.getName());
				String key;
				// In C#-models we must use the name from the attribute, so try to find it
				try {
					final String value = KDMModelManager.getValueFromAttribute(clazz, nameAttributeKey);
					key = KDMModelManager.removeGlobalCSharpPrefix(value);
				} catch (final NoSuchElementException ex) {
					// Else use the name
					key = name.toString();
				}
				// Put the class into the hash map
				this.classes.put(key, clazz);
				// Keep child elements in mind
				iteratorStack.push(currentIterator);
				currentIterator = clazz.getCodeElement().iterator();
				// Save current name
				nameStack.push(name.toString());
			} else if (currentElement instanceof InterfaceUnit) {
				final InterfaceUnit interfaze = (InterfaceUnit) currentElement;
				// Assemble name
				name.append(interfaze.getName());
				String key;
				// In C#-models we must use the name from the attribute, so try to find it
				try {
					final String value = KDMModelManager.getValueFromAttribute(interfaze, nameAttributeKey);
					key = KDMModelManager.removeGlobalCSharpPrefix(value);
				} catch (final NoSuchElementException ex) {
					// Else use the name
					key = name.toString();
				}
				// Put the interface into the hash map
				this.interfaces.put(key, interfaze);
				// Keep child elements in mind
				iteratorStack.push(currentIterator);
				currentIterator = interfaze.getCodeElement().iterator();
				// Save current name
				nameStack.push(name.toString());
			} else if (currentElement instanceof MethodUnit) {
				final MethodUnit method = (MethodUnit) currentElement;
				// Assemble name
				String qualifier;
				// If we are in a C#-model we must use the value from the 'FullyQualifiedName'-attribute.
				try {
					// Get the parent
					final EObject parentObject = method.eContainer();
					String pName;
					if (parentObject instanceof ClassUnit) {
						final ClassUnit classUnit = (ClassUnit) parentObject;
						pName = KDMModelManager.getValueFromAttribute(classUnit, nameAttributeKey);
					} else if (parentObject instanceof InterfaceUnit) {
						final InterfaceUnit interfaceUnit = (InterfaceUnit) parentObject;
						pName = KDMModelManager.getValueFromAttribute(interfaceUnit, nameAttributeKey);
					} else {
						// Use the normal way
						pName = name.toString();
					}
					// Keep the 'global' prefix in mind which is used in C#-models
					pName = KDMModelManager.removeGlobalCSharpPrefix(pName);
					qualifier = KDMModelManager.reassembleMethodQualifierFromModel(method, pName);
				} catch (final NoSuchElementException ex) {
					qualifier = KDMModelManager.reassembleMethodQualifierFromModel(method, name.toString());
				}
				// Put the method into the hash map
				this.methods.put(qualifier, method);
				// Keep child elements in mind, necessary to get all code relations (method calls)
				iteratorStack.push(currentIterator);
				currentIterator = method.getCodeRelation().iterator();
				// don't forget the name stack!
				nameStack.push(name.toString());
			} else if (currentElement instanceof ArrayType) {
				final ArrayType arrayType = (ArrayType) currentElement;
				final StringBuilder arrayTypeName = new StringBuilder();
				final ItemUnit itemUnit = arrayType.getItemUnit();
				// Assemble array type name
				arrayTypeName.append(itemUnit.getType().getName());
				arrayTypeName.append("[]");
				this.arrays.put(arrayTypeName.toString(), arrayType);
			} else if (currentElement instanceof CodeRelationship) {
				final CodeRelationship codeRelationship = (CodeRelationship) currentElement;
				// Get the relationship elements
				final CodeItem fromObject = codeRelationship.getFrom();
				final KDMEntity toObject = codeRelationship.getTo();
				// Ensure the elements are methods
				if ((fromObject instanceof MethodUnit) && (toObject instanceof MethodUnit)) {
					final MethodUnit srcMethodUnit = (MethodUnit) fromObject;
					final MethodUnit dstMethodUnit = (MethodUnit) toObject;
					// Assemble method qualifier
					final String srcQualifier = KDMModelManager.reassembleMethodQualifierFromModel(srcMethodUnit, name.toString());
					final String dstParentName = KDMModelManager.reassembleFullParentName(dstMethodUnit);
					final String dstQualifier = KDMModelManager.reassembleMethodQualifierFromModel(dstMethodUnit, dstParentName);
					final StringBuilder relName = new StringBuilder();
					relName.append(srcQualifier).append(' ');
					relName.append(dstQualifier);
					// Put the relation ship into the hash map
					this.methodCalls.put(relName.toString(), codeRelationship);
				}
			} else if (currentElement instanceof Module) { // used in C#-models
				final Module module = (Module) currentElement;
				// Keep child elements in mind
				iteratorStack.push(currentIterator);
				currentIterator = module.getCodeElement().iterator();
			} else if (currentElement instanceof Namespace) { // Used in C#-models
				final Namespace namespace = (Namespace) currentElement;
				final String namespaceName = namespace.getName();
				// Keep the constant name 'global' in mind, don't use it!
				if (!globalConstant.equals(namespaceName) || !nameStack.isEmpty()) {
					try {
						// Use the full qualified name within the attribute
						final String nameAttrValue = KDMModelManager.getValueFromAttribute(namespace, nameAttributeKey);
						this.namespaces.put(nameAttrValue, namespace);
					} catch (final NoSuchElementException ex) {
						KDMModelManager.LOG.error("The attribute '" + nameAttributeKey + "' of the namespace with the name '" + namespaceName + "' does not exist.");
					}
				}
				// Keep the flat order in mind and don't look for child elements
			} else if (currentElement instanceof CompilationUnit) {
				final CompilationUnit compilationUnit = (CompilationUnit) currentElement;
				// Keep child elements in mind
				iteratorStack.push(currentIterator);
				currentIterator = compilationUnit.getCodeElement().iterator();
			}

			// Are there more elements?
			while (!currentIterator.hasNext() && !iteratorStack.isEmpty()) {
				currentIterator = iteratorStack.pop();

				if (!nameStack.isEmpty()) {
					nameStack.pop();
				}
			}

			condition = currentIterator.hasNext();
		}
	}

	/**
	 * This method returns a unique identifier for method calls.
	 * 
	 * @param srcQualifier
	 *            The unique identifier for the calling method.
	 * @param dstQualifier
	 *            The unique identifier for the called method.
	 * @return
	 *         A unique identifier for method calls.
	 */
	private String assembleMethodCallQualifier(final String srcQualifier, final String dstQualifier) {
		final String connector = " ";
		final StringBuilder result = new StringBuilder();
		result.append(srcQualifier);
		result.append(connector);
		result.append(dstQualifier);

		return result.toString();
	}

	/**
	 * This method tries to get a {@link Module} out of the list of Abstract code elements from the code model.
	 * 
	 * @return
	 *         The Module if one exist.
	 * @throws NoSuchElementException
	 *             If no Module exist.
	 */
	private Module getNamespaceModule() throws NoSuchElementException {
		for (final AbstractCodeElement element : this.codeModel.getCodeElement()) {
			if (element instanceof Module) {
				return (Module) element;
			}
		}

		throw new NoSuchElementException();
	}

	/**
	 * This method should be called to initialize the hash map containing the java basic datatypes. It is threadsafe, but it should only be called in the
	 * constructor.
	 */
	private void initializeJavaDatatypes() {
		this.javaDatatypes.clear();

		final Datatype voidType = this.codeFactory.createVoidType();
		voidType.setName("void");
		this.javaDatatypes.put("void", voidType);

		final Datatype booleanType = this.codeFactory.createBooleanType();
		booleanType.setName("boolean");
		this.javaDatatypes.put("boolean", booleanType);

		final Datatype byteType = this.codeFactory.createIntegerType();
		byteType.setName("byte");
		this.javaDatatypes.put("byte", byteType);

		final Datatype shortType = this.codeFactory.createIntegerType();
		shortType.setName("short");
		this.javaDatatypes.put("short", shortType);

		final Datatype intType = this.codeFactory.createIntegerType();
		intType.setName("int");
		this.javaDatatypes.put("int", intType);

		final Datatype longType = this.codeFactory.createIntegerType();
		longType.setName("long");
		this.javaDatatypes.put("long", longType);

		final Datatype floatType = this.codeFactory.createFloatType();
		floatType.setName("float");
		this.javaDatatypes.put("float", floatType);

		final Datatype doubleType = this.codeFactory.createFloatType();
		doubleType.setName("double");
		this.javaDatatypes.put("double", doubleType);

		final Datatype charType = this.codeFactory.createCharType();
		charType.setName("char");
		this.javaDatatypes.put("char", charType);

		// Put the java types into the model instance as well
		synchronized (this.codeModel.getCodeElement()) {
			final Enumeration<Datatype> enumer = this.javaDatatypes.elements();
			while (enumer.hasMoreElements()) {
				this.codeModel.getCodeElement().add(enumer.nextElement());
			}
		}
	}

	/***************************************************************************************************************************************************************
	 * The following part should only contain public static methods for subtasks
	 **************************************************************************************************************************************************************/

	/**
	 * This method reassembles the full parent name of a method by running through the parent elements and collecting the names.
	 * 
	 * @param element
	 *            The element to reassemble the full parent name of. It should be a {@link Package}, a {@link ClassUnit}, an {@link InterfaceUnit} or a
	 *            {@link MethodUnit}.
	 * @return
	 *         The full parent name.
	 */
	public static String reassembleFullParentName(final CodeItem element) {
		final StringBuilder fullName = new StringBuilder();
		final List<String> parts = new LinkedList<String>();
		// Get first container
		EObject currentElement = element.eContainer();

		// As long as there is an other package, class or interface container we must go on
		while (currentElement != null) {
			if (currentElement instanceof Package) {
				final Package pack = (Package) currentElement;
				// Add the name
				parts.add(pack.getName());
				// Get the next container
				currentElement = pack.eContainer();
			} else if (currentElement instanceof ClassUnit) {
				final ClassUnit clazz = (ClassUnit) currentElement;
				// Add the name
				parts.add(clazz.getName());
				// Get the next container
				currentElement = clazz.eContainer();
			} else if (currentElement instanceof InterfaceUnit) {
				final InterfaceUnit interfaze = (InterfaceUnit) currentElement;
				// Add the name
				parts.add(interfaze.getName());
				// Get the next container
				currentElement = interfaze.eContainer();
			} else {
				break;
			}
		}
		// Keep the wrong order in mind
		Collections.reverse(parts);
		// Assemble name
		for (final String part : parts) {
			fullName.append(part).append('.');
		}

		return fullName.toString();
	}

	/**
	 * This method reassembles the full parent package name by running through the parent elements and collecting the names.
	 * 
	 * @param item
	 *            The element to reassemble the full parent package name of. It should be a {@link Package}, a {@link ClassUnit}, an {@link InterfaceUnit} or a
	 *            {@link MethodUnit}.
	 * @return
	 *         The full parent name.
	 */
	public static String reassembleParentPackageName(final CodeItem item) {
		final StringBuilder name = new StringBuilder();

		if (item != null) {
			final List<String> parts = new LinkedList<String>();
			// Get first container
			EObject currentItem = item.eContainer();

			while (currentItem != null) {
				if (currentItem instanceof Package) {
					final Package pack = (Package) currentItem;
					parts.add(pack.getName());
					currentItem = pack.eContainer();
				} else if (currentItem instanceof ClassUnit) {
					currentItem = currentItem.eContainer();
				} else if (currentItem instanceof InterfaceUnit) {
					currentItem = currentItem.eContainer();
				} else { // TODO: This is no check on != null but != any of the above!
					break;
				}
			}
			// Keep the wrong order in mind
			Collections.reverse(parts);
			// Assemble name
			for (final String part : parts) {
				name.append(part).append('.');
			}
			// Remove the last dot
			if (name.toString().endsWith(".")) {
				name.deleteCharAt(name.length() - 1);
			}
		}

		return name.toString();
	}

	/**
	 * This method returns the unique method qualifier used within the {@link KDMModelManager}.
	 * 
	 * @param methodUnit
	 *            The method unit to create the identifier.
	 * @return
	 *         The unique qualifier used within the {@link KDMModelManager}.
	 */
	public static String reassembleMethodQualifier(final MethodUnit methodUnit) {
		String parentName;
		try {
			final EObject parentObject = methodUnit.eContainer();
			if ((parentObject instanceof ClassUnit) || (parentObject instanceof InterfaceUnit)) {
				final CodeItem parent = (CodeItem) parentObject;
				final String value = KDMModelManager.getValueFromAttribute(parent, "FullyQualifiedName");
				parentName = KDMModelManager.removeGlobalCSharpPrefix(value);
			} else {
				parentName = KDMModelManager.reassembleFullParentName(methodUnit);
			}
		} catch (final NoSuchElementException ex) {
			parentName = KDMModelManager.reassembleFullParentName(methodUnit);
		}

		final String qualifier = KDMModelManager.reassembleMethodQualifierFromModel(methodUnit, parentName);

		return qualifier;
	}

	/***************************************************************************************************************************************************************
	 * The following part should only contain methods for tests or debugging
	 **************************************************************************************************************************************************************/

	/**
	 * This method creates a list containing all hash map keys for packages, classes, interfaces, methods and array types.
	 * 
	 * @return
	 *         A list containing all hash map keys.
	 */
	public List<String> logHashMapKeys() {
		final List<String> keys = new LinkedList<String>();
		// keys.add("packages");
		// keys.addAll(this.packages.keySet());
		//
		// keys.add("");
		// keys.add("classes");
		// keys.addAll(this.classes.keySet());
		//
		// keys.add("");
		// keys.add("interfaces");
		// keys.addAll(this.interfaces.keySet());
		//
		// keys.add("");
		// keys.add("methods");
		keys.addAll(this.methods.keySet());

		// keys.add("");
		// keys.add("arrays");
		// keys.addAll(this.arrays.keySet());
		//
		// keys.add("");
		// keys.add("methodCalls");
		// keys.addAll(this.methodCalls.keySet());
		//
		// keys.add("");
		// keys.add("namespaces");
		// keys.addAll(this.namespaces.keySet());

		return keys;
	}
}
