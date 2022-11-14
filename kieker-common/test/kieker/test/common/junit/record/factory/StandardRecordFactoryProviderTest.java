package kieker.test.common.junit.record.factory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.factory.StandardRecordFactoryProvider;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEventFactory;
import kieker.test.common.util.record.factory.TestRecord;

/**
 * Test cases for the standard record factory provider.
 * 
 * @author Holger Knoche
 * @since 2.0
 */
public class StandardRecordFactoryProviderTest {
    
    private StandardRecordFactoryProvider provider;

	/** test constructor. */
	public StandardRecordFactoryProviderTest() {
		// Nothing to do
	}

	@Before
	public void before() throws Exception {
		this.provider = new StandardRecordFactoryProvider();
	}

    /**
     * Test case: The expected factory for a record type with a factory is returned.
     */
	@Test
	public void testRecordWithFactory() {
		final IRecordFactory<?> recordFactory = this.provider.createFactoryFor(AfterOperationEvent.class);
		Assert.assertEquals(AfterOperationEventFactory.class, recordFactory.getClass());
	}

    /**
     * Test case: No record factory is returned for a record type without an according factory.
     */
	@Test
	public void testRecordWithoutFactory() {
		final IRecordFactory<?> recordFactory = this.provider.createFactoryFor(TestRecord.class);
		Assert.assertNull(recordFactory);
	}

}
