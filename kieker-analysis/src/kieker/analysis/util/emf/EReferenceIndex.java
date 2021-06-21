/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.util.emf;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @param <K>
 *
 * @param <V>
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public final class EReferenceIndex<K, V extends EObject> {

	protected final BiMap<K, V> index; // NOPMD (default visibility contradicts checkstyle)

	protected final EReference reference; // NOPMD (default visibility contradicts checkstyle)

	protected final Collection<EStructuralFeature> observedReferenceAttributes; // NOPMD (default visibility contradicts checkstyle)

	protected final Function<V, K> keyCreator; // NOPMD (default visibility contradicts checkstyle)

	private EReferenceIndex(final EObject object, final EReference reference, final Collection<EStructuralFeature> observedReferenceAttributes,
			final Function<V, K> keyCreator, final Collection<V> values) {
		this.reference = reference;
		this.observedReferenceAttributes = observedReferenceAttributes;
		this.keyCreator = keyCreator;

		if ((values == null) || values.isEmpty()) {
			this.index = HashBiMap.create();
		} else {
			this.index = values.stream().collect(Collectors.toMap(
					this.keyCreator, Function.identity(), (a, b) -> b, HashBiMap::create));
		}

		final Adapter changedListener = new ChangedListener();
		object.eAdapters().add(changedListener);
	}

	public boolean contains(final K key) {
		return this.index.containsKey(key);
	}

	public V get(final K key) {
		return this.index.get(key);
	}

	public static <K, V extends EObject> EReferenceIndex<K, V> createEmpty(final EObject object, final EReference reference,
			final Collection<EStructuralFeature> observedReferenceAttributes, final Function<V, K> keyCreator) {
		return new EReferenceIndex<>(object, reference, observedReferenceAttributes, keyCreator, null);
	}

	public static <K, V extends EObject> EReferenceIndex<K, V> create(final EObject object, final EReference reference,
			final Collection<EStructuralFeature> observedReferenceAttributes, final Function<V, K> keyCreator) {
		@SuppressWarnings("unchecked")
		final EList<V> values = (EList<V>) object.eGet(reference);
		return new EReferenceIndex<>(object, reference, observedReferenceAttributes, keyCreator, values);
	}

	public static <K, V extends EObject> EReferenceIndex<K, V> create(final EObject object, final EReference reference,
			final Collection<EStructuralFeature> observedReferenceAttributes, final Function<V, K> keyCreator, final Collection<V> values) {
		return new EReferenceIndex<>(object, reference, observedReferenceAttributes, keyCreator, values);
	}

	/**
	 *
	 */
	private class ChangedListener extends AbstractEReferenceChangedListener<V> {

		private final ElementChangedListener elementChangedListener = new ElementChangedListener();

		public ChangedListener() {
			super(EReferenceIndex.this.reference);
		}

		@Override
		protected void notifyElementAdded(final V element) {
			final K key = EReferenceIndex.this.keyCreator.apply(element);
			EReferenceIndex.this.index.forcePut(key, element);

			// Add attribute listener
			element.eAdapters().add(this.elementChangedListener);
		}

		@Override
		protected void notifyElementRemoved(final V element) {
			final K key = EReferenceIndex.this.keyCreator.apply(element);
			EReferenceIndex.this.index.remove(key);

			// Remove attribute listener
			element.eAdapters().remove(this.elementChangedListener);
		}
	}

	/**
	 *
	 */
	private class ElementChangedListener extends AbstractEStructuralFeatureChangedListener<V> {

		public ElementChangedListener() {
			super(EReferenceIndex.this.observedReferenceAttributes);
		}

		@Override
		protected void notifyChanged(final V object) {
			final K key = EReferenceIndex.this.keyCreator.apply(object);
			EReferenceIndex.this.index.forcePut(key, object);
		}

	}
}
