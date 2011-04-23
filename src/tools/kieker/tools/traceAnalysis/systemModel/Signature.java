package kieker.tools.traceAnalysis.systemModel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Andre van Hoorn
 */
public class Signature {
    private final String name;

    private final String returnType;
    private final String[] paramTypeList;

    private Signature(){
        this.name = null;
        this.returnType = null;
        this.paramTypeList = null;
    }

    public Signature(final String name, final String returnType,
            final String[] paramTypeList){
        this.name = name;
        this.returnType = returnType;
        this.paramTypeList = paramTypeList;
    }

    public final String getName() {
        return this.name;
    }

    public final String[] getParamTypeList() {
        return this.paramTypeList;
    }

    public final String getReturnType() {
        return this.returnType;
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(this.name).append("(");
        boolean first = true;
        for (String t : this.paramTypeList){
            if (!first){
                strBuild.append(",");
            } else {
                first = false;
            }
            strBuild.append(t);
        }
        strBuild.append(")")
                .append(":")
                .append(this.returnType);
        return strBuild.toString();
    }
}
