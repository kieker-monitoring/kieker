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
package kieker.analysis.behavior.mtree;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.behavior.mtree.utils.Pair;
import kieker.analysis.behavior.mtree.utils.Utils;

class MTreeClass extends MTree<Data> {

	private static final IPromotionFunction<Data> NON_RANDOM_PROMOTION = new IPromotionFunction<Data>() {
		@Override
		public Pair<Data> process(final Set<Data> dataSet, final IDistanceFunction<? super Data> distanceFunction) {
			return Utils.minMax(dataSet);
		}
	};

	MTreeClass() {
		super(2, DistanceFunctions.EUCLIDEAN,
				new ComposedSplitFunction<>(
						NON_RANDOM_PROMOTION,
						new PartitionFunctions.BalancedPartition<Data>()));
	}

	@Override
	public void add(final Data data) {
		super.add(data);
		this.check();
	}

	@Override
	public boolean remove(final Data data) {
		final boolean result = super.remove(data);
		this.check();
		return result;
	}

	IDistanceFunction<? super Data> getDistanceFunction() {
		return this.distanceFunction;
	}
};

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
	public void test01() {
		this.testFunction("f01");
	}

	@Test
	public void test02() {
		this.testFunction("f02");
	}

	@Test
	public void test03() {
		this.testFunction("f03");
	}

	@Test
	public void test04() {
		this.testFunction("f04");
	}

	@Test
	public void test05() {
		this.testFunction("f05");
	}

	@Test
	public void test06() {
		this.testFunction("f06");
	}

	@Test
	public void test07() {
		this.testFunction("f07");
	}

	@Test
	public void test08() {
		this.testFunction("f08");
	}

	@Test
	public void test09() {
		this.testFunction("f09");
	}

	@Test
	public void test10() {
		this.testFunction("f10");
	}

	@Test
	public void test11() {
		this.testFunction("f11");
	}

	@Test
	public void test12() {
		this.testFunction("f12");
	}

	@Test
	public void test13() {
		this.testFunction("f13");
	}

	@Test
	public void test14() {
		this.testFunction("f14");
	}

	@Test
	public void test15() {
		this.testFunction("f15");
	}

	@Test
	public void test16() {
		this.testFunction("f16");
	}

	@Test
	public void test17() {
		this.testFunction("f17");
	}

	@Test
	public void test18() {
		this.testFunction("f18");
	}

	@Test
	public void test19() {
		this.testFunction("f19");
	}

	@Test
	public void test20() {
		this.testFunction("f20");
	}

	@Test
	public void testLots() {
		this.testFunction("fLots");
	}

	@Test
	public void testRemoveNonExisting() {
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
	public void testGeneratedCase01() {
		this.testFunction("fG01");
	}

	@Test
	public void testGeneratedCase02() {
		this.testFunction("fG02");
	}

	@Test
	public void testNotRandom() {
		/*
		 * To generate a random test, execute the following commands:
		 * py/mtree/tests/fixtures/generator.py -a500 -r0.2 > py/mtree/tests/fixtures/fNotRandom.py
		 * cpp/convert-fixture-to-cpp.py fNotRandom > cpp/tests/fixtures/fNotRandom.txt
		 */

		final String fixtureName = "fNotRandom";
		final String fixtureFileName = Fixture.path(fixtureName);
		final File fixtureFile = new File(fixtureFileName);
		if (!fixtureFile.exists()) {
			throw new RuntimeException("The file " + fixtureFile + " does not exist");
		}
		this.testFunction(fixtureName);
	}

	private void assertIterator(
			final int expectedData,
			final double expectedDistance,
			final boolean expectedHasNext,
			final MTree<Integer>.ResultItem ri,
			final Iterator<MTree<Integer>.ResultItem> i) {
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
	public void testIterators() {
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

		final MTree<Integer>.Query query = mt.getNearest(0);

		// The first iterator
		final Iterator<MTree<Integer>.ResultItem> i1 = query.iterator();
		Assert.assertTrue(i1.hasNext());
		MTree<Integer>.ResultItem ri1 = i1.next();
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
		final Iterator<MTree<Integer>.ResultItem> i2 = query.iterator();
		Assert.assertTrue(i2.hasNext());
		MTree<Integer>.ResultItem ri2 = i2.next();
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

	private void testFunction(final String fixtureName) {
		final Fixture fixture = Fixture.load(fixtureName);
		this.testFixture(fixture);
	}

	private void testFixture(final Fixture fixture) {
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
				throw new RuntimeException(action.getData().toString());
			}

			this.checkNearestByRange(action.getQueryData(), action.getRadius());
			this.checkNearestByLimit(action.getQueryData(), action.getLimit());
		}
	}

	private void checkNearestByRange(final Data queryData, final double radius) {
		final List<MTreeClass.ResultItem> results = new ArrayList<>();
		final Set<Data> strippedResults = new HashSet<>();
		final MTreeClass.Query query = this.mtree.getNearestByRange(queryData, radius);

		for (final MTreeClass.ResultItem ri : query) {
			results.add(ri);
			strippedResults.add(ri.getData());
		}

		double previousDistance = 0;

		for (final MTreeClass.ResultItem result : results) {
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
		final List<MTreeClass.ResultItem> results = new ArrayList<>();
		final Set<Data> strippedResults = new HashSet<>();
		final MTreeClass.Query query = this.mtree.getNearestByLimit(queryData, limit);

		for (final MTreeClass.ResultItem ri : query) {
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
		for (final MTreeClass.ResultItem ri : results) {
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
