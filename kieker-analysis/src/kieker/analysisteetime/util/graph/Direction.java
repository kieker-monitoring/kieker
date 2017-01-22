package kieker.analysisteetime.util.graph;

/**
 * Direction is used to denote the direction of an edge or location of a vertex on an edge.
 *
 * @author Sören Henning
 *
 */
public enum Direction {

	OUT, IN, BOTH;

	public Direction opposite() {
		if (this.equals(OUT)) {
			return IN;
		} else if (this.equals(IN)) {
			return OUT;
		} else {
			return BOTH;
		}
	}
}
