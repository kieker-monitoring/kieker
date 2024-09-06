/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.clustering.mtree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.clustering.mtree.query.Query;
import kieker.analysis.generic.clustering.mtree.query.ResultItem;
import kieker.analysis.generic.clustering.mtree.utils.MTreeUtils;
import kieker.analysis.generic.clustering.mtree.utils.Pair;

/**
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
class MTreeClass extends MTree<Data> {

	private static final IPromotionFunction<Data> NON_RANDOM_PROMOTION = new IPromotionFunction<>() {
		@Override
		public Pair<Data> process(final Set<Data> dataSet, final IDistanceFunction<? super Data> distanceFunction) {
			return MTreeUtils.minMax(dataSet);
		}
	};

	MTreeClass() {
		super(2, DistanceFunctionFactory.EUCLIDEAN,
				new ComposedSplitFunction<>(
						MTreeClass.NON_RANDOM_PROMOTION,
						new BalancedPartitionFunction<>()));
	}

	@Override
	public void add(final Data data) throws InternalErrorException {
		super.add(data);
		this.check();
	}

	@Override
	public boolean remove(final Data data) throws InternalErrorException {
		final boolean result = super.remove(data);
		this.check();
		return result;
	}

	@Override
	public IDistanceFunction<? super Data> getDistanceFunction() {
		return super.getDistanceFunction();
	}
}

public class MTreeTest {

	private final MTreeClass mtree = new MTreeClass();
	private final Set<Data> allData = new HashSet<>();

	public MTreeTest() {
		// default constructor
	}

	@Test
	public void testEmpty() {
		this.checkNearestByRange(new Data(1, 2, 3), 4);
		this.checkNearestByLimit(new Data(1, 2, 3), 4);
	}

	@Test
	public void test01() throws InternalErrorException, IOException {
		this.testFunction("f01");
	}

	@Test
	public void test02() throws InternalErrorException, IOException {
		this.testFunction("f02");
	}

	@Test
	public void test03() throws InternalErrorException, IOException {
		this.testFunction("f03");
	}

	@Test
	public void test04() throws InternalErrorException, IOException {
		this.testFunction("f04");
	}

	@Test
	public void test05() throws InternalErrorException, IOException {
		this.testFunction("f05");
	}

	@Test
	public void test06() throws InternalErrorException, IOException {
		this.testFunction("f06");
	}

	@Test
	public void test07() throws InternalErrorException, IOException {
		this.testFunction("f07");
	}

	@Test
	public void test08() throws InternalErrorException, IOException {
		this.testFunction("f08");
	}

	@Test
	public void test09() throws InternalErrorException, IOException {
		this.testFunction("f09");
	}

	@Test
	public void test10() throws InternalErrorException, IOException {
		this.testFunction("f10");
	}

	@Test
	public void test11() throws InternalErrorException, IOException {
		this.testFunction("f11");
	}

	@Test
	public void test12() throws InternalErrorException, IOException {
		this.testFunction("f12");
	}

	@Test
	public void test13() throws InternalErrorException, IOException {
		this.testFunction("f13");
	}

	@Test
	public void test14() throws InternalErrorException, IOException {
		this.testFunction("f14");
	}

	@Test
	public void test15() throws InternalErrorException, IOException {
		this.testFunction("f15");
	}

	@Test
	public void test16() throws InternalErrorException, IOException {
		this.testFunction("f16");
	}

	@Test
	public void test17() throws InternalErrorException, IOException {
		this.testFunction("f17");
	}

	@Test
	public void test18() throws InternalErrorException, IOException {
		this.testFunction("f18");
	}

	@Test
	public void test19() throws InternalErrorException, IOException {
		this.testFunction("f19");
	}

	@Test
	public void test20() throws InternalErrorException, IOException {
		this.testFunction("f20");
	}

	@Test
	public void testLots() throws InternalErrorException, IOException {
		this.testFunction("fLots");
	}

	@Test
	public void testRemoveNonExisting() throws InternalErrorException {
		// Empty
		assert !this.mtree.remove(new Data(99, 77));

		// With some items
		this.mtree.add(new Data(4, 44));
		assert !this.mtree.remove(new Data(99, 77));

		this.mtree.add(new Data(95, 43));
		assert !this.mtree.remove(new Data(99, 77));

		this.mtree.add(new Data(76, 21));
		assert !this.mtree.remove(new Data(99, 77));

		this.mtree.add(new Data(64, 53));
		assert !this.mtree.remove(new Data(99, 77));

		this.mtree.add(new Data(47, 3));
		assert !this.mtree.remove(new Data(99, 77));

		this.mtree.add(new Data(26, 11));
		assert !this.mtree.remove(new Data(99, 77));
	}

	@Test
	public void testGeneratedCase01() throws InternalErrorException, IOException {
		this.testFunction("fG01");
	}

	@Test
	public void testGeneratedCase02() throws InternalErrorException, IOException {
		this.testFunction("fG02");
	}

	private void assertIterator(
			final int expectedData,
			final double expectedDistance,
			final boolean expectedHasNext,
			final ResultItem<Integer> ri,
			final Iterator<ResultItem<Integer>> i) {
		Assert.assertEquals(expectedData, ri.getData().intValue());
		Assert.assertEquals(expectedDistance, ri.getDistance(), 0.0);
		Assert.assertEquals(expectedHasNext, i.hasNext());
		if (!expectedHasNext) {
			try {
				i.next();
				Assert.fail();
			} catch (final NoSuchElementException e) {
				Assert.assertTrue(true);
			}
		}
	}

	@Test
	public void testIterators() throws InternalErrorException {
		final MTree<Integer> mt = new MTree<>(
				new IDistanceFunction<Integer>() {
					@Override
					public double calculate(final Integer data1, final Integer data2) {
						return Math.abs(data1 - data2);
					}
				},
				null);

		mt.add(1);
		mt.add(2);
		mt.add(3);
		mt.add(4);

		final Query<Integer> query = mt.getNearest(0);

		// The first iterator
		final Iterator<ResultItem<Integer>> i1 = query.iterator();
		Assert.assertTrue(i1.hasNext());
		ResultItem<Integer> ri1 = i1.next();
		/*
		 * 1 2 3 4 e
		 * i1: *
		 */
		this.assertIterator(1, 1, true, ri1, i1);
		Assert.assertTrue(i1.hasNext());

		// Advance the iterator
		ri1 = i1.next();
		/*
		 * 1 2 3 4 e
		 * i1: *
		 */
		this.assertIterator(2, 2, true, ri1, i1);

		// Advance again
		ri1 = i1.next();
		/*
		 * 1 2 3 4 e
		 * i1: *
		 */
		this.assertIterator(3, 3, true, ri1, i1);

		// Begin another iteration
		final Iterator<ResultItem<Integer>> i2 = query.iterator();
		Assert.assertTrue(i2.hasNext());
		ResultItem<Integer> ri2 = i2.next();
		/*
		 * 1 2 3 4 e
		 * i1: *
		 * i2: *
		 */
		this.assertIterator(1, 1, true, ri2, i2);
		// The first iterator must not have been affected
		this.assertIterator(3, 3, true, ri1, i1);

		// Advance the second iterator
		ri2 = i2.next();
		/*
		 * 1 2 3 4 e
		 * i1: *
		 * i2: *
		 */
		this.assertIterator(2, 2, true, ri2, i2);
		// The first iterator must not have been affected
		this.assertIterator(3, 3, true, ri1, i1);

		// Now continue until the iterators reach the end
		ri1 = i1.next();
		/*
		 * 1 2 3 4 e
		 * i1: *
		 * i2: *
		 */
		this.assertIterator(4, 4, false, ri1, i1);
		this.assertIterator(2, 2, true, ri2, i2);

		ri2 = i2.next();
		/*
		 * 1 2 3 4 e
		 * i1: *
		 * i2: *
		 */
		this.assertIterator(4, 4, false, ri1, i1);
		this.assertIterator(3, 3, true, ri2, i2);

		ri2 = i2.next();
		/*
		 * 1 2 3 4 e
		 * i1: *
		 * i2: *
		 */
		this.assertIterator(4, 4, false, ri1, i1);
		this.assertIterator(4, 4, false, ri2, i2);
	}

	private void testFunction(final String fixtureName) throws InternalErrorException, IOException {
		final Fixture fixture = Fixture.load(fixtureName);
		this.testFixture(fixture);
	}

	private void testFixture(final Fixture fixture) throws InternalErrorException {
		for (final Fixture.Action action : fixture.getActions()) {
			switch (action.getCmd()) {
			case 'A':
				this.allData.add(action.getData());
				this.mtree.add(action.getData());
				break;
			case 'R':
				this.allData.remove(action.getData());
				final boolean removed = this.mtree.remove(action.getData());
				assert removed;
				break;
			default:
				throw new InternalErrorException(action.getData().toString());
			}

			this.checkNearestByRange(action.getQueryData(), action.getRadius());
			this.checkNearestByLimit(action.getQueryData(), action.getLimit());
		}
	}

	private void checkNearestByRange(final Data queryData, final double radius) {
		final List<ResultItem<Data>> results = new ArrayList<>();
		final Set<Data> strippedResults = new HashSet<>();
		final Query<Data> query = this.mtree.getNearestByRange(queryData, radius);

		for (final ResultItem<Data> ri : query) {
			results.add(ri);
			strippedResults.add(ri.getData());
		}

		double previousDistance = 0;

		for (final ResultItem<Data> result : results) {
			// Check if increasing distance
			Assert.assertTrue(previousDistance <= result.getDistance());
			previousDistance = result.getDistance();

			// Check if every item in the results came from the generated queryData
			Assert.assertTrue(this.allData.contains(result.getData()));

			// Check if every item in the results is within the range
			Assert.assertTrue(result.getDistance() <= radius);
			Assert.assertEquals(this.mtree.getDistanceFunction().calculate(result.getData(), queryData), result.getDistance(), 0.0);
		}

		for (final Data data : this.allData) {
			final double distance = this.mtree.getDistanceFunction().calculate(data, queryData);
			if (distance <= radius) {
				Assert.assertTrue(strippedResults.contains(data));
			} else {
				Assert.assertFalse(strippedResults.contains(data));
			}
		}
	}

	private void checkNearestByLimit(final Data queryData, final int limit) {
		final List<ResultItem<Data>> results = new ArrayList<>();
		final Set<Data> strippedResults = new HashSet<>();
		final Query<Data> query = this.mtree.getNearestByLimit(queryData, limit);

		for (final ResultItem<Data> ri : query) {
			results.add(ri);
			strippedResults.add(ri.getData());
		}

		if (limit <= this.allData.size()) {
			Assert.assertEquals(limit, results.size());
		} else {
			// limit > allData.size()
			Assert.assertEquals(this.allData.size(), results.size());
		}

		double farthest = 0.0;
		double previousDistance = 0.0;
		for (final ResultItem<Data> ri : results) {
			// Check if increasing distance
			Assert.assertTrue(previousDistance <= ri.getDistance());
			previousDistance = ri.getDistance();

			// Check if every item in the results came from the generated queryData
			Assert.assertTrue(this.allData.contains(ri.getData()));

			// Check if items are not repeated
			Assert.assertEquals(1, Collections.frequency(strippedResults, ri.getData()));

			final double distance = this.mtree.getDistanceFunction().calculate(ri.getData(), queryData);
			Assert.assertEquals(distance, ri.getDistance(), 0.0);
			farthest = Math.max(farthest, distance);
		}
		for (final Data data : this.allData) {
			final double distance = this.mtree.getDistanceFunction().calculate(data, queryData);
			if (distance < farthest) {
				Assert.assertTrue(strippedResults.contains(data));
			} else if (distance > farthest) {
				Assert.assertFalse(strippedResults.contains(data));
			} else { // NOCS
				// distance == farthest
			}
		}
	}
}
