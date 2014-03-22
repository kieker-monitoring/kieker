package kieker.panalysis;

import kieker.panalysis.base.Filter;

public class RecordAggregationFilter extends Filter<RecordAggregationFilter.PORTS> {

	static public enum PORTS {
	}

	public RecordAggregationFilter(final long id) {
		super(id, PORTS.class);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}

	public void onPortChanged(final Object record) {
		// TODO Auto-generated method stub

	}

}
