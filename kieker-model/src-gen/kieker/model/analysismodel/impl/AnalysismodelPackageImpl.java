/**
 */
package kieker.model.analysismodel.impl;

import java.time.Duration;
import java.time.Instant;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import kieker.model.analysismodel.AnalysismodelFactory;
import kieker.model.analysismodel.AnalysismodelPackage;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.impl.ExecutionPackageImpl;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.source.impl.SourcePackageImpl;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl;
import kieker.model.analysismodel.trace.TracePackage;
import kieker.model.analysismodel.trace.impl.TracePackageImpl;
import kieker.model.analysismodel.type.TypePackage;
import kieker.model.analysismodel.type.impl.TypePackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class AnalysismodelPackageImpl extends EPackageImpl implements AnalysismodelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EDataType instantEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EDataType durationEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see kieker.model.analysismodel.AnalysismodelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AnalysismodelPackageImpl() {
		super(eNS_URI, AnalysismodelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>
	 * This method is used to initialize {@link AnalysismodelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AnalysismodelPackage init() {
		if (isInited) {
			return (AnalysismodelPackage) EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredAnalysismodelPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final AnalysismodelPackageImpl theAnalysismodelPackage = registeredAnalysismodelPackage instanceof AnalysismodelPackageImpl
				? (AnalysismodelPackageImpl) registeredAnalysismodelPackage
				: new AnalysismodelPackageImpl();

		isInited = true;

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI);
		final StatisticsPackageImpl theStatisticsPackage = (StatisticsPackageImpl) (registeredPackage instanceof StatisticsPackageImpl ? registeredPackage
				: StatisticsPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);
		final TypePackageImpl theTypePackage = (TypePackageImpl) (registeredPackage instanceof TypePackageImpl ? registeredPackage : TypePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);
		final AssemblyPackageImpl theAssemblyPackage = (AssemblyPackageImpl) (registeredPackage instanceof AssemblyPackageImpl ? registeredPackage
				: AssemblyPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		final DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl) (registeredPackage instanceof DeploymentPackageImpl ? registeredPackage
				: DeploymentPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		final ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl) (registeredPackage instanceof ExecutionPackageImpl ? registeredPackage
				: ExecutionPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		final TracePackageImpl theTracePackage = (TracePackageImpl) (registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcePackage.eNS_URI);
		final SourcePackageImpl theSourcePackage = (SourcePackageImpl) (registeredPackage instanceof SourcePackageImpl ? registeredPackage
				: SourcePackage.eINSTANCE);

		// Create package meta-data objects
		theAnalysismodelPackage.createPackageContents();
		theStatisticsPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcePackage.createPackageContents();

		// Initialize created meta-data
		theAnalysismodelPackage.initializePackageContents();
		theStatisticsPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAnalysismodelPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AnalysismodelPackage.eNS_URI, theAnalysismodelPackage);
		return theAnalysismodelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EDataType getInstant() {
		return this.instantEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EDataType getDuration() {
		return this.durationEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AnalysismodelFactory getAnalysismodelFactory() {
		return (AnalysismodelFactory) this.getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void createPackageContents() {
		if (this.isCreated) {
			return;
		}
		this.isCreated = true;

		// Create data types
		this.instantEDataType = this.createEDataType(INSTANT);
		this.durationEDataType = this.createEDataType(DURATION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void initializePackageContents() {
		if (this.isInitialized) {
			return;
		}
		this.isInitialized = true;

		// Initialize package
		this.setName(eNAME);
		this.setNsPrefix(eNS_PREFIX);
		this.setNsURI(eNS_URI);

		// Obtain other dependent packages
		final StatisticsPackage theStatisticsPackage = (StatisticsPackage) EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI);
		final TypePackage theTypePackage = (TypePackage) EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);
		final AssemblyPackage theAssemblyPackage = (AssemblyPackage) EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);
		final DeploymentPackage theDeploymentPackage = (DeploymentPackage) EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		final ExecutionPackage theExecutionPackage = (ExecutionPackage) EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		final TracePackage theTracePackage = (TracePackage) EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		final SourcePackage theSourcePackage = (SourcePackage) EPackage.Registry.INSTANCE.getEPackage(SourcePackage.eNS_URI);

		// Add subpackages
		this.getESubpackages().add(theStatisticsPackage);
		this.getESubpackages().add(theTypePackage);
		this.getESubpackages().add(theAssemblyPackage);
		this.getESubpackages().add(theDeploymentPackage);
		this.getESubpackages().add(theExecutionPackage);
		this.getESubpackages().add(theTracePackage);
		this.getESubpackages().add(theSourcePackage);

		// Initialize data types
		this.initEDataType(this.instantEDataType, Instant.class, "Instant", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		this.initEDataType(this.durationEDataType, Duration.class, "Duration", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		this.createResource(eNS_URI);
	}

} // AnalysismodelPackageImpl
