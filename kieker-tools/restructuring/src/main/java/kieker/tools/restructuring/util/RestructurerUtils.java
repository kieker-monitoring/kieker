package kieker.tools.restructuring.util;

import java.util.Map.Entry;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public final class RestructurerUtils {

    private static final AssemblyFactory FACTORY = AssemblyFactory.eINSTANCE;

    private RestructurerUtils() {
        // ensure that utility class is not instantiated
    }

    public static AssemblyModel cloneModel(final AssemblyModel model) {
        final AssemblyModel result = RestructurerUtils.FACTORY.createAssemblyModel();

        for (final Entry<String, AssemblyComponent> e : model.getComponents().entrySet()) {
            final AssemblyComponent comp = RestructurerUtils.FACTORY.createAssemblyComponent();
            result.getComponents().put(e.getKey(), comp);
            for (final Entry<String, AssemblyOperation> op : e.getValue().getOperations().entrySet()) {
                final AssemblyOperation o = RestructurerUtils.FACTORY.createAssemblyOperation();
                result.getComponents().get(e.getKey()).getOperations().put(op.getKey(), o);
            }
        }

        return result;
    }

    public static AssemblyModel alterComponentNames(final AssemblyModel model) {
        final String prefix = "_";
        final AssemblyModel result = RestructurerUtils.FACTORY.createAssemblyModel();

        for (final Entry<String, AssemblyComponent> e : model.getComponents().entrySet()) {
            final AssemblyComponent comp = RestructurerUtils.FACTORY.createAssemblyComponent();
            result.getComponents().put(prefix + e.getKey(), comp);
            for (final Entry<String, AssemblyOperation> op : e.getValue().getOperations().entrySet()) {
                final AssemblyOperation o = RestructurerUtils.FACTORY.createAssemblyOperation();
                result.getComponents().get("_" + e.getKey()).getOperations().put(op.getKey(), o);
            }
        }

        return result;
    }
}
