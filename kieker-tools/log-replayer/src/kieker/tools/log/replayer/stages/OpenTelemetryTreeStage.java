package kieker.tools.log.replayer.stages;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import kieker.analysis.architecture.trace.AbstractTraceProcessingStage;
import kieker.analysisteetime.plugin.filter.select.TypeFilter;
import kieker.model.analysismodel.statistics.Measurement;
import kieker.model.analysismodel.statistics.impl.MeasurementImpl;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;
import kieker.tools.settings.NoClassMapping;
import kieker.visualization.trace.call.tree.TraceCallTreeNode;

public class OpenTelemetryTreeStage extends AbstractTraceProcessingStage<ExecutionTrace> {

    private final Tracer tracer;
    private TraceCallTreeNode root;
    private MeasurementImpl measurementConfig;
    private final NoClassMapping mapping;
    private boolean ignoreEOIs;

    public OpenTelemetryTreeStage(SystemModelRepository systemModelRepository, Tracer tracer, MeasurementImpl config,
                                  NoClassMapping mapping, boolean ignoreEOIs) {
        super(systemModelRepository);
        this.tracer = tracer;
        this.measurementConfig = config;
        this.mapping = mapping;
        this.ignoreEOIs = ignoreEOIs;
    }

    public TraceCallTreeNode getRoot() {
        return root;
    }

    private TraceCallTreeNode lastParent = null;
    private TraceCallTreeNode lastAdded = null;
    private int lastStackSize = 1;
    private long testTraceId = -1;

    @Override
    protected void execute(ExecutionTrace trace) throws Exception {
        for (Execution execution : trace.getTraceAsSortedExecutionSet()) {
            String fullClassname = execution.getOperation().getComponentType().getFullQualifiedName().intern();
            String methodname = execution.getOperation().getSignature().getName().intern();
            String call = fullClassname + "#" + methodname;

            // Ignore synthetic java methods
            if (!methodname.equals("class$") && !methodname.startsWith("access$")) {
                addExecutionToTree(execution, fullClassname, methodname, call);
            }
        }
    }

    private void addExecutionToTree(Execution execution, String fullClassname, String methodname, String call) {
        if (execution.getEss() == 1) {
            readRoot(execution, call);
            setModule(fullClassname, root);
        } else if (root != null && execution.getTraceId() == testTraceId) {
            callLevelDown(execution);
            callLevelUp(execution);

            boolean hasEqualNode = false;
            for (TraceCallTreeNode candidate : lastParent.getChildren()) {
                if (candidate.getClass().equals(call)) {
                    hasEqualNode = true;
                    lastAdded = candidate;
                    break;
                }
            }
         
            if (!ignoreEOIs || !hasEqualNode) {
                lastAdded = lastParent.addChild(call);
                setModule(fullClassname, lastAdded);
            }
        }
    }

    private void setModule(String fullClassname, TraceCallTreeNode node) {
    	 final String outerClazzName = TypeFilter.getOuterClass(fullClassname);
         final String moduleOfClass = mapping.getModuleOfClass(outerClazzName);
         node.setModule(moduleOfClass);
    }

    private void callLevelUp(Execution execution) {
        while (execution.getEss() < lastStackSize) {
            lastParent = lastParent.getParent();
            lastStackSize--;
        }
    }

    private void callLevelDown(Execution execution) {
        if (execution.getEss() > lastStackSize) {
            lastParent = lastAdded;
            lastStackSize = execution.getEss();
        }
    }
    
    private void readRoot(Execution execution, String call) {
        SpanBuilder spanBuilder = tracer.spanBuilder(call);
        Span span = spanBuilder.startSpan();
        try (Scope scope = span.makeCurrent()) {
            root =  new TraceCallTreeNode(call, null, measurementConfig);
            		
            setModule(null, root); // Set module for the root node
            lastParent = root;
            testTraceId = execution.getTraceId();
        } finally {
            span.end();
        }
    }
}
