package kieker.test.tools.junit.traceAnalysis.util;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public class ExecutionFactory {

    private final static String DEFAULT_STRING = "N/A";
    private final SystemModelRepository systemEntityFactory;

    private ExecutionFactory() {
        this.systemEntityFactory = null;
    }

    public ExecutionFactory(final SystemModelRepository systemEntityFactory) {
        this.systemEntityFactory = systemEntityFactory;
    }

    /**
     * Creates an Execution object initialized with the passed values.
     * The remaining values of the Execution object are assigned default
     * values.
     *
     * @param componentTypeName
     * @param componentInstanceName
     * @param operationName
     * @param traceId
     * @param tin
     * @param tout
     * @param eoi
     * @param ess
     * @throws NullPointerException iff one of the String args has the value null.
     * @return
     */
    public Execution genExecution(
            final String componentTypeName,
            final String componentInstanceName,
            final String operationName,
            final long traceId,
            final long tin,
            final long tout,
            final int eoi,
            final int ess) {
        if (componentTypeName == null
                || componentInstanceName == null
                || operationName == null) {
            throw new NullPointerException("None of the String args must be null.");
        }

        /* Register component type (if it hasn't been registered before) */
        ComponentType componentTypeA =
                this.systemEntityFactory.getTypeRepositoryFactory().lookupComponentTypeByNamedIdentifier(componentTypeName);
        if (componentTypeA == null) {
            componentTypeA =
                    this.systemEntityFactory.getTypeRepositoryFactory().createAndRegisterComponentType(componentTypeName, componentTypeName);
        }
        /* Register operation (if it hasn't been registered before) */
        Operation operationAa =
                this.systemEntityFactory.getOperationFactory().lookupOperationByNamedIdentifier(operationName);
        if (operationAa == null) {
            operationAa =
                    this.systemEntityFactory.getOperationFactory().createAndRegisterOperation(
                    operationName,
                    componentTypeA,
                    new Signature(
                    operationName,
                    DEFAULT_STRING,
                    new String[]{DEFAULT_STRING}));
        }
        if (componentTypeA.getOperations().contains(operationAa)) {
            componentTypeA.addOperation(operationAa);
        }

        /* Register assembly component (if it hasn't been registered before) */
        AssemblyComponent assemblyComponentA =
                this.systemEntityFactory.getAssemblyFactory().lookupAssemblyComponentInstanceByNamedIdentifier(componentInstanceName);
        if (assemblyComponentA == null) {
            assemblyComponentA =
                    this.systemEntityFactory.getAssemblyFactory().createAndRegisterAssemblyComponentInstance(
                    componentInstanceName,
                    componentTypeA);
        }

        /* Register execution container (if it hasn't been registered before) */
        ExecutionContainer containerC =
                this.systemEntityFactory.getExecutionEnvironmentFactory().lookupExecutionContainerByNamedIdentifier(DEFAULT_STRING);
        if (containerC == null) {
            containerC =
                    this.systemEntityFactory.getExecutionEnvironmentFactory().createAndRegisterExecutionContainer(
                    DEFAULT_STRING, DEFAULT_STRING);
        }

        /* Register allocation container (if it hasn't been registered before) */
        AllocationComponent allocationComponentA =
                this.systemEntityFactory.getAllocationFactory().lookupAllocationComponentInstanceByNamedIdentifier(DEFAULT_STRING);
        if (allocationComponentA == null) {
            allocationComponentA =
                    this.systemEntityFactory.getAllocationFactory().createAndRegisterAllocationComponentInstance(
                    DEFAULT_STRING,
                    assemblyComponentA,
                    containerC);
        }

        return new Execution(
                operationAa, allocationComponentA, traceId,
                DEFAULT_STRING, eoi, ess, tin, tout);
    }

    /**
     * Creates an Execution object initialized with the passed values.
     * The remaining values of the Execution object are assigned default
     * values.
     *
     * @param traceId
     * @param tin
     * @param tout
     * @param eoi
     * @param ess
     * @return
     */
    public Execution genExecution(
            final long traceId,
            final long tin,
            final long tout,
            final int eoi,
            final int ess) {
        return this.genExecution(
                DEFAULT_STRING, // component type
                DEFAULT_STRING, // component instance
                DEFAULT_STRING, // operation name
                traceId, tin, tout, eoi, ess);
    }
}
