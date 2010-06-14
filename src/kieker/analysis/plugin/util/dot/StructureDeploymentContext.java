package kieker.analysis.plugin.util.dot;

/**
 * This class implements unique functionality for deployment contexts,
 * which work as normal {@link StructureElement}s otherwise.
 *
 * Deployment contexts usually have no parent,
 * they have {@link StructureComponent}s as children,
 * and they are linked among each other with a <em>uses</em> relation.
 *
 * @author Nina
 */
class StructureDeploymentContext extends StructureElement {

private String vmname;          // root == -1

StructureDeploymentContext( String vmname, boolean isRoot ){
    super( null, isRoot );
	this.vmname = vmname;
}

public String getName(){
	return "Virtual Machine '" + vmname + "'";
}

public String getShortName(){
    return vmname;
}

/**
 * Checks a selection of internal variables for consistency.
 * @return true if all subordinate checks return true
 */
@Override
boolean check(){
    boolean result = true;
    if( !super.check() ){
        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: structure element internal check failed." );
        return false;
    }
	if( causeRating < -1.0 || causeRating > 1.0 ){
		Util.writeOut( Util.LEVEL_WARNING, "< WARNING: deployment context " + getName() + " has weird cause rating: " + causeRating );
		result = false;
	}
    if( percent < 0.0 || percent > 100.0 ){
        Util.writeOut( Util.LEVEL_WARNING, "< WARNING: deployment context " + getName() + " has weird percent rating: " + percent );
        result = false;
    }
	int value;
	for( Integer opCount : incoming.values() ){
		value = opCount.intValue();
		if( value < 1 ){
			Util.writeOut( Util.LEVEL_WARNING, "< WARNING: deployment context " + getName() + " has weird incoming connection count: " + value );
			result = false;
		}
	}
	for( Integer opCount : outgoing.values() ){
		value = opCount.intValue();
		if( value < 1 ){
			Util.writeOut( Util.LEVEL_WARNING, "< WARNING: deployment context " + getName() + " has weird outgoing connection count: " + value );
			result = false;
		}
	}
    if( root ){
        if( outgoing.isEmpty() ){
            Util.writeOut( Util.LEVEL_WARNING, "< WARNING: deployment context " + getName() + " has no outgoing connections." );
            result = false;
        }
    }
    else if( incoming.isEmpty() ){
		Util.writeOut( Util.LEVEL_WARNING, "< WARNING: non-root deployment context " + getName() + " has no incoming connections." );
		result = false;
	}
	for( StructureElement elem : children ){
		if( !elem.check() ){
			Util.writeOut( Util.LEVEL_WARNING, "< WARNING: component " + elem.getName() + " internal check failed." );
			result = false;
		}
	}
	return result;
}

}
