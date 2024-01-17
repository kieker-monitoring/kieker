package kieker.tools.log.replayer.stages;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.LogManager;

import org.apache.activemq.Message;
import org.junit.internal.runners.TestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import kieker.analysis.architecture.trace.AbstractTraceProcessingStage;
import kieker.analysis.generic.TypeFilter;
import kieker.model.analysismodel.statistics.Measurement;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AbstractMessage;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;
import kieker.model.system.model.MessageTrace;
import kieker.model.system.model.Operation;
import kieker.model.system.model.util.AllocationComponentOperationPair;
import kieker.tools.settings.NoClassMapping;
import kieker.visualization.trace.call.tree.AbstractCallTreeNode;
import kieker.visualization.trace.call.tree.TraceCallTreeNode;


public class OpenTelemetryTreeStage extends AbstractTraceProcessingStage<ExecutionTrace> {

	private static final Logger LOG = LoggerFactory.getLogger(OpenTelemetryTreeStage.class);


    private AbstractCallTreeNode root;
    private Measurement measurementConfig;
    private final TestMethod test;
    private final boolean ignoreEOIs;
    private final NoClassMapping mapping;
    private final Tracer tracer;
    
    private int lastStackSize = 1;
    private AbstractCallTreeNode lastParent;
    private AbstractCallTreeNode lastAdded;
    private long testTraceId = -1;
    private ExecutionTrace lastExecutionTrace;
    




    public OpenTelemetryTreeStage(final SystemModelRepository systemModelRepository, final TestMethod test,
            final boolean ignoreEOIs, final Measurement config, final NoClassMapping mapping) {
        super(systemModelRepository);

        this.test = test;
        this.measurementConfig = config;
        this.ignoreEOIs = ignoreEOIs;
        this.mapping = mapping;

        // Initialize OpenTelemetry
        SdkTracerProvider tracerProvider = createTracerProvider();
        OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .buildAndRegisterGlobal();

        this.tracer = openTelemetry.getTracer("opentelemetry-instrumentation");
    }

    private SdkTracerProvider createTracerProvider() {
        // Define resource information, such as service name
        Resource resource = Resource.getDefault().merge(
                Resource.create(Attributes.builder().put(AttributeKey.stringKey("service.name"), "kieker-data").build()));

        // Create a tracer provider with a BatchSpanProcessor for exporting spans to an OTLP endpoint
        return SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(OtlpHttpSpanExporter.builder()
                        .setEndpoint("http://localhost:55681/v1/traces")
                        .build())
                        .build())
                .build();
    }

    @Override
    protected void execute(final ExecutionTrace trace) throws Exception {
        try {
            LOG.info("Trace: {}", trace.getTraceId());

            // Iterate over the executions in the trace
            for (final Execution execution : trace.getTraceAsSortedExecutionSet()) {
                // Extract information about the execution
                final String fullClassname = execution.getOperation().getComponentType().getFullQualifiedName().intern();
                final String methodname = execution.getOperation().getSignature().getName().intern();
                final String call = fullClassname + "#" + methodname;

                // Convert the Operation to an AbstractMessage
             // Assuming there is a different method for converting Operation to AbstractMessage
                AbstractMessage operationAsMessage = convertOperationToAbstractMessage(execution.getOperation());

                
                // Create a MessageTrace using the execution information
                final MessageTrace kiekerPattern = new MessageTrace(execution.getTraceId(),
                        Collections.singletonList(operationAsMessage));

                // Log information about the execution
                LOG.trace("{} {}", kiekerPattern, execution.getEss());

                // Ignore synthetic Java methods
                if (!methodname.equals("class$") && !methodname.startsWith("access$")) {
                    // Add the execution to a tree structure (addExecutionToTree method is assumed to exist)
                    addExecutionToTree(execution, fullClassname, methodname, call, kiekerPattern);
                }
            }
        } catch (Exception e) {
            LOG.error("Error processing execution trace", e);
            // Handle or propagate the exception as needed
        }
    }

    private AbstractMessage convertOperationToAbstractMessage(Operation operation) {
   
        long timestamp = System.currentTimeMillis(); 
        Execution sendingExecution = getSendingExecution(); 
        Execution receivingExecution = getReceivingExecution(); 

        return new AbstractMessage(timestamp, sendingExecution, receivingExecution) {
            // Implement abstract methods if any
            @Override
            public boolean equals(Object obj) {
                // Implement equality logic if needed
                return false;
            }
        };
    }


	public final Execution getReceivingExecution() {
		return this.getReceivingExecution();
	}

	/**
	 * Delivers the object which sent the message.
	 *
	 * @return The sending object.
	 */
	public final Execution getSendingExecution() {
		return this.getSendingExecution();
	}

	
	private <T> void addExecutionToTree(final Execution execution, final String fullClassname, final String methodname,
            final String call, final MessageTrace kiekerPattern) throws Exception {
        if (test.getClass().equals(fullClassname) && test.getClass().equals(kiekerPattern))  /*getMethod().equals(methodname))*/ {
            readRoot(execution, call, kiekerPattern);
            setModule(fullClassname, root);
        } else if (root != null && execution.getTraceId() == testTraceId) {
            LOG.trace(fullClassname + " " + execution.getOperation().getSignature() + " " + execution.getEoi() + " "
                    + execution.getEss());
            LOG.trace("Last Stack: " + lastStackSize);

            callLevelDown(execution);
            callLevelUp(execution);
            LOG.trace("Parent: {} {}", lastParent.getClass(), lastParent.getEntity());

            if (execution.getEss() == lastParent.getId()) {
                final String message = "Trying to add " + call + "(" + execution.getEss() + ")" + " to "
                        + lastParent.getClass() + "(" + lastParent.getId()
                        + "), but parent ess always needs to be child ess -1";
                LOG.error(message);
                throw new RuntimeException(message);
            }

            boolean hasEqualNode = false;
            for (Object candidateObj : lastParent.getChildEdges()) {
                AbstractCallTreeNode<T> candidate = (AbstractCallTreeNode<T>) candidateObj;
                
                // Assuming equals() method is appropriately overridden in AbstractCallTreeNode
                if (candidate.getEntity().equals(kiekerPattern)) {
                    hasEqualNode = true;
                    lastAdded = candidate;
                    break; // No need to continue checking once a match is found
                }
            }

            if (!ignoreEOIs || !hasEqualNode) {
                lastAdded = lastParent.newCall(call, kiekerPattern, null);
                setModule(fullClassname, lastAdded);

                // Start OpenTelemetry span for the added node
                startOpenTelemetrySpan(lastAdded, execution);
            }
        }
    }

    private void startOpenTelemetrySpan(AbstractCallTreeNode node, Execution execution) throws Exception {
        Span span = tracer.spanBuilder(node.getIdentifier())
                .setStartTimestamp(Instant.ofEpochMilli(execution.getTin()))
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("customAttribute", "5");
        }
    }

    private void setModule(final String fullClassname, final AbstractCallTreeNode node) {
       
    	final Class<TypeFilter> outerClassName = TypeFilter.class;
    	
    	//final String outerClazzName = TypeFilter.getOuterClass(fullClassname);
        final Class<? extends NoClassMapping> moduleOfClass = mapping.getClass();
       // final String moduleOfClass = mapping.getModuleOfClass(outerClazzName);
       // node.setModule(moduleOfClass);
        node.isRootNode();
     }

    
     private void callLevelUp(final Execution execution) {
        while (execution.getEss() < lastStackSize) {
           LOG.trace("Level up: " + execution.getEss() + " " + lastStackSize);
           lastParent = (AbstractCallTreeNode) ((AbstractCallTreeNode) lastParent).getChildEdges();
          // lastParent = lastParent.getParent();
           lastStackSize--;
        }
     }

     private void callLevelDown(final Execution execution) {
        if (execution.getEss() > lastStackSize) {
           LOG.trace("Level down: " + execution.getEss() + " " + lastStackSize);
           lastParent = lastAdded;
           // lastStackSize++;
           if (lastStackSize + 1 != lastParent.getId() + 1) {
              LOG.error("Down caused wrong lastStackSize: {} {}", lastStackSize, lastParent.getId());
           }
           lastStackSize = lastParent.getId() + 1;
          // lastStackSize = lastParent.getEss() + 1;
           LOG.trace("Stack size after going down: {} Measured: {}", lastParent.getId(), lastStackSize);
        }
     }

     private void readRoot(final Execution execution, final String call, final MessageTrace kiekerPattern) {
    	    // Assuming AllocationComponentOperationPair has a constructor that takes necessary parameters
    	    AllocationComponentOperationPair allocationComponentOperationPair =
    	            new AllocationComponentOperationPair(lastStackSize, null, null);

    	    // Convert the call string to an int using hashCode
    	    int callId = call.hashCode();

    	    root = new TraceCallTreeNode(callId, allocationComponentOperationPair, true, kiekerPattern, null);
    	    lastParent = root;
    	    testTraceId = execution.getTraceId();
    	}

     }
