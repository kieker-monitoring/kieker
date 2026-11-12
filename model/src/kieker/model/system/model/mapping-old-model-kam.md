# ComponentType

kieker.model.system.model.ComponentType -> analysismodel.type.ComponentType
id -> signature (id is a number representing a unique value, the signature is a string holding the FQN)
typeName -> name
packageName -> package
operations -> providedOperations

# AssemblyComponent

kieker.model.system.model.AssemblyComponent -> analysismodel.assembly.AssemblyComponent
id -> signature (int id unique, signature string also unique)
name -> signature
type -> componentType

kieker.model.system.model.AllocationComponent -> analysismodel.deployment.DeploymentComponent
assemblyComponent -> assemblyComponent
executionContainer -> deploymentContext
id -> signature

kieker.model.system.model.Execution -> analysismodel.trace.OperationCall
allocationComponent
assumed
eoi
ess
operation -> operation
sessionId
tin
tout
traceId
          -> duration
          -> parent
          -> children
          -> start
          -> durRatioToParent
          -> durRatioToRootParent
          -> stackDepth
          -> orderIndex
          -> failed
          -> failedCause
       
kieker.model.system.model.ExecutionTrace -> analysismodel.trace.Trace
- minEoi
- maxEoi
- minTin
- maxTin
- trace (collection of Execution)
- trace[0].traceId -> traceId
          -> rootOperationCall






