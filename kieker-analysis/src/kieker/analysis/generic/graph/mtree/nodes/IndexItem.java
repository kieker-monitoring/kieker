package kieker.analysis.generic.graph.mtree.nodes;

public class IndexItem<T> { // NOCS cannot be declared final
	protected double radius;
	private final T data;
	private double distanceToParent;

	public IndexItem(final T data) {
		this.data = data;
		this.radius = 0;
		this.distanceToParent = -1;
	}

	public T getData() {
		return this.data;
	}

	public double getDistanceToParent() {
		return this.distanceToParent;
	}

	public void setDistanceToParent(final double distance) {
		this.distanceToParent = distance;
	}

	int check() {
		this.checkRadius();
		this.checkDistanceToParent();

		return 1;
	}

	private void checkRadius() {
		assert this.radius >= 0;
	}

	protected void checkDistanceToParent() {
		assert !(this instanceof RootLeafNode);
		assert !(this instanceof RootNode);
		assert this.distanceToParent >= 0;
	}

	public double getRadius() {
		return this.radius;
	}

}
