/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.test.common.junit.record.factory;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.factory.IRecordFactoryProvider;
import kieker.common.record.factory.RecordFactory;
import kieker.common.record.factory.RecordFactoryResolver;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEventFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Christian Wulf
 *
 * @since 1.11
 */
public class RecordFactoryResolverTest extends AbstractKiekerTest {

	/** test constructor. */
	public RecordFactoryResolverTest() {
		// Nothing to do
	}

	/**
	 * Test case: The expected factory is returned for a record with an according factory using the standard resolution
	 * strategy.
	 */
	@Test
	public void testRecordWithFactoryUsingStandardResolution() {
		final String recordClassName = AfterOperationEvent.class.getName();
		
		final RecordFactoryResolver resolver = new RecordFactoryResolver();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = resolver.get(recordClassName);
		
		Assert.assertEquals(AfterOperationEventFactory.class, recordFactory.getClass());
	}

	/**
	 * Test case: No factory is returned for a record that does not exist.
	 */
	@Test
	public void testNotExistingRecord() {
		final String recordClassName = "record.that.does.not.exist";

		final RecordFactoryResolver resolver = new RecordFactoryResolver();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = resolver.get(recordClassName);
		
		Assert.assertNull(recordFactory);
	}

	/**
	 * Test case: The expected factory is returned for a record that is explicitly annotated with a factory.
	 */
	@Test
	public void testAnnotatedFactory() {
		final String recordClassName = RecordTypeWithAnnotatedFactory.class.getName();

		final RecordFactoryResolver resolver = new RecordFactoryResolver();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = resolver.get(recordClassName);
		
		Assert.assertNotNull(recordFactory);
		Assert.assertEquals(AnnotatedRecordFactory.class, recordFactory.getClass());
	}

	/**
	 * Test case: The appropriate factories are returned when one or more specific providers are given.
	 */
	@Test
	public void testGivenFactoryProviders() {
		final String recordClassNameA = RecordTypeA.class.getName();
		final String recordClassNameB = RecordTypeB.class.getName();
		final String fallbackClassName = AfterOperationEvent.class.getName();

		final RecordFactoryResolver resolver = new RecordFactoryResolver(Arrays.asList(new ProviderA(), new ProviderB()));
		final IRecordFactory<? extends IMonitoringRecord> factoryForRecordA = resolver.get(recordClassNameA);
		final IRecordFactory<? extends IMonitoringRecord> factoryForRecordB = resolver.get(recordClassNameB);		

		// Assert that the factories returned by the providers match the expectations
		Assert.assertNotNull(factoryForRecordA);
		Assert.assertEquals(RecordTypeFactoryA.class, factoryForRecordA.getClass());
		Assert.assertNotNull(factoryForRecordB);
		Assert.assertEquals(RecordTypeFactoryB.class, factoryForRecordB.getClass());

		// Make sure that the default resolution is used when no provider matches
		final IRecordFactory<? extends IMonitoringRecord> factoryForFallback = resolver.get(fallbackClassName);
		Assert.assertNotNull(factoryForFallback);
		Assert.assertEquals(AfterOperationEventFactory.class, factoryForFallback.getClass());
	}

	/**
	 * First record type for the provider test.
	 */
	private static class RecordTypeA extends AbstractDummyRecord {

		public RecordTypeA() {
			// Empty constructor
		}

	}

	/**
	 * Record factory for RecordTypeA. The name is explicitly chosen not to match the default pattern.
	 */
	public static class RecordTypeFactoryA extends AbstractDummyRecordFactory<RecordTypeA> {

		public RecordTypeFactoryA() {
			// Empty constructor
		}

		@Override
		public RecordTypeA create(final IValueDeserializer deserializer) {
			return new RecordTypeA();
		}

	}

	/**
	 * Provider for RecordTypeA.
	 */
	private static class ProviderA implements IRecordFactoryProvider {

		public ProviderA() {
			// Empty constructor
		}

		@Override
		public boolean isApplicableTo(final Class<?> recordClass) {
			return (recordClass == RecordTypeA.class);
		}

		@Override
		public IRecordFactory<?> createFactoryFor(final Class<?> recordClass) {
			return new RecordTypeFactoryA();
		}
		
	}

	/**
	 * Second record type for the provider test.
	 */
	private static class RecordTypeB extends AbstractDummyRecord {

		public RecordTypeB() {
			// Empty constructor
		}

	}

	/**
	 * Record factory for RecordTypeB. The name is explicitly chosen not to match the default pattern.
	 */
	public static class RecordTypeFactoryB extends AbstractDummyRecordFactory<RecordTypeB> {

		public RecordTypeFactoryB() {
			// Empty constructor
		}

		@Override
		public RecordTypeB create(final IValueDeserializer deserializer) {
			return new RecordTypeB();
		}

	}

	/**
	 * Provider for RecordTypeB.
	 */
	private static class ProviderB implements IRecordFactoryProvider {

		public ProviderB() {
			// Empty constructor
		}

		@Override
		public boolean isApplicableTo(final Class<?> recordClass) {
			return (recordClass == RecordTypeB.class);
		}

		@Override
		public IRecordFactory<?> createFactoryFor(final Class<?> recordClass) {
			return new RecordTypeFactoryB();
		}
		
	}

	/**
	 * Test record type with an explicitly annotated factory.
	 */
	@RecordFactory(AnnotatedRecordFactory.class)
	private static class RecordTypeWithAnnotatedFactory extends AbstractDummyRecord {

		public RecordTypeWithAnnotatedFactory() {
			// Empty constructor
		}

	}

	/**
	 * Record factory for the annotation test.
	 */
	public static class AnnotatedRecordFactory extends AbstractDummyRecordFactory<AbstractDummyRecord> {

		public AnnotatedRecordFactory() {
			// Empty constructor
		}

		@Override
		public AbstractDummyRecord create(final IValueDeserializer deserializer) {
			return new RecordTypeWithAnnotatedFactory();
		}

	}

	/**
	 * Convenience superclass for dummy records.
	 */
	private abstract static class AbstractDummyRecord extends AbstractMonitoringRecord {

		@Override
		public void serialize(final IValueSerializer serializer) {
			// Do nothing			
		}

		@Override
		public Class<?>[] getValueTypes() {
			return new Class<?>[0];
		}

		@Override
		public String[] getValueNames() {
			return new String[0];
		}

		@Override
		public int getSize() {
			return 0;
		}

	}

	/**
	 * Convenience superclass for dummy record factories.
	 * 
	 * @param <T> The record type created by this factory
	 */
	private abstract static class AbstractDummyRecordFactory<T extends AbstractDummyRecord> implements IRecordFactory<T> {

		@Override
		public String[] getValueNames() {
			return new String[0];
		}

		@Override
		public Class<?>[] getValueTypes() {
			return new Class<?>[0];
		}

		@Override
		public int getRecordSizeInBytes() {
			return 0;
		}

	}

}
