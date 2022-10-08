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

import kieker.analysis.behavior.mtree.ComposedSplitFunction;
import kieker.analysis.behavior.mtree.DistanceFunction;
import kieker.analysis.behavior.mtree.DistanceFunctions;
import kieker.analysis.behavior.mtree.MTree;
import kieker.analysis.behavior.mtree.PartitionFunctions;
import kieker.analysis.behavior.mtree.PromotionFunction;
import kieker.analysis.behavior.mtree.utils.Pair;
import kieker.analysis.behavior.mtree.utils.Utils;

class MTreeClass extends MTree<Data> {

	private static final PromotionFunction<Data> nonRandomPromotion = new PromotionFunction<Data>() {
		@Override
		public Pair<Data> process(final Set<Data> dataSet, final DistanceFunction<? super Data> distanceFunction) {
			return Utils.minMax(dataSet);
		}
	};

	MTreeClass() {
		super(2, DistanceFunctions.EUCLIDEAN,
				new ComposedSplitFunction<>(
						nonRandomPromotion,
						new PartitionFunctions.BalancedPartition<Data>()));
	}

	@Override
	public void add(final Data data) {
		super.add(data);
		this._check();
	}

	@Override
	public boolean remove(final Data data) {
		final boolean result = super.remove(data);
		this._check();
		return result;
	}

	DistanceFunction<? super Data> getDistanceFunction() {
		return this.distanceFunction;
	}
};

public class MTreeTest {

	@Test
	public void testEmpty() {
		this._checkNearestByRange(new Data(1, 2, 3), 4);
		this._checkNearestByLimit(new Data(1, 2, 3), 4);
	}

	@Test
	public void test01() {
		this._test("f01");
	}

	@Test
	public void test02() {
		this._test("f02");
	}

	@Test
	public void test03() {
		this._test("f03");
	}

	@Test
	public void test04() {
		this._test("f04");
	}

	@Test
	public void test05() {
		this._test("f05");
	}

	@Test
	public void test06() {
		this._test("f06");
	}

	@Test
	public void test07() {
		this._test("f07");
	}

	@Test
	public void test08() {
		this._test("f08");
	}

	@Test
	public void test09() {
		this._test("f09");
	}

	@Test
	public void test10() {
		this._test("f10");
	}

	@Test
	public void test11() {
		this._test("f11");
	}

	@Test
	public void test12() {
		this._test("f12");
	}

	@Test
	public void test13() {
		this._test("f13");
	}

	@Test
	public void test14() {
		this._test("f14");
	}

	@Test
	public void test15() {
		this._test("f15");
	}

	@Test
	public void test16() {
		this._test("f16");
	}

	@Test
	public void test17() {
		this._test("f17");
	}

	@Test
	public void test18() {
		this._test("f18");
	}

	@Test
	public void test19() {
		this._test("f19");
	}

	@Test
	public void test20() {
		this._test("f20");
	}

	@Test
	public void testLots() {
		this._test("fLots");
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
		this._test("fG01");
	}

	@Test
	public void testGeneratedCase02() {
		this._test("fG02");
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
		this._test(fixtureName);
	}

	private void assertIterator(
			final int expectedData,
			final double expectedDistance,
			final boolean expectedHasNext,
			final MTree<Integer>.ResultItem ri,
			final Iterator<MTree<Integer>.ResultItem> i) {
		Assert.assertEquals(expectedData, ri.data.intValue());
		Assert.assertEquals(expectedDistance, ri.distance, 0.0);
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
				new DistanceFunction<Integer>() {
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

	private final MTreeClass mtree = new MTreeClass();
	private final Set<Data> allData = new HashSet<>();

	private void _test(final String fixtureName) {
		final Fixture fixture = Fixture.load(fixtureName);
		this._testFixture(fixture);
	}

	private void _testFixture(final Fixture fixture) {
		for (final Fixture.Action action : fixture.actions) {
			switch (action.cmd) {
			case 'A':
				this.allData.add(action.data);
				this.mtree.add(action.data);
				break;
			case 'R':
				this.allData.remove(action.data);
				final boolean removed = this.mtree.remove(action.data);
				assert removed;
				break;
			default:
				throw new RuntimeException(Character.toString(action.cmd));
			}

			this._checkNearestByRange(action.queryData, action.radius);
			this._checkNearestByLimit(action.queryData, action.limit);
		}
	}

	private void _checkNearestByRange(final Data queryData, final double radius) {
		final List<MTreeClass.ResultItem> results = new ArrayList<>();
		final Set<Data> strippedResults = new HashSet<>();
		final MTreeClass.Query query = this.mtree.getNearestByRange(queryData, radius);

		for (final MTreeClass.ResultItem ri : query) {
			results.add(ri);
			strippedResults.add(ri.data);
		}

		double previousDistance = 0;

		for (final MTreeClass.ResultItem result : results) {
			// Check if increasing distance
			Assert.assertTrue(previousDistance <= result.distance);
			previousDistance = result.distance;

			// Check if every item in the results came from the generated queryData
			Assert.assertTrue(this.allData.contains(result.data));

			// Check if every item in the results is within the range
			Assert.assertTrue(result.distance <= radius);
			Assert.assertEquals(this.mtree.getDistanceFunction().calculate(result.data, queryData), result.distance, 0.0);
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

	private void _checkNearestByLimit(final Data queryData, final int limit) {
		final List<MTreeClass.ResultItem> results = new ArrayList<>();
		final Set<Data> strippedResults = new HashSet<>();
		final MTreeClass.Query query = this.mtree.getNearestByLimit(queryData, limit);

		for (final MTreeClass.ResultItem ri : query) {
			results.add(ri);
			strippedResults.add(ri.data);
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
			Assert.assertTrue(previousDistance <= ri.distance);
			previousDistance = ri.distance;

			// Check if every item in the results came from the generated queryData
			Assert.assertTrue(this.allData.contains(ri.data));

			// Check if items are not repeated
			Assert.assertEquals(1, Collections.frequency(strippedResults, ri.data));

			final double distance = this.mtree.getDistanceFunction().calculate(ri.data, queryData);
			Assert.assertEquals(distance, ri.distance, 0.0);
			farthest = Math.max(farthest, distance);
		}
		for (final Data data : this.allData) {
			final double distance = this.mtree.getDistanceFunction().calculate(data, queryData);
			if (distance < farthest) {
				Assert.assertTrue(strippedResults.contains(data));
			} else if (distance > farthest) {
				Assert.assertFalse(strippedResults.contains(data));
			} else {
				// distance == farthest
			}
		}
	}
}
