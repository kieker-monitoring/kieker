/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util.emf;

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
 * @author S�ren Henning
 *
 * @since 1.13
 */
public class EReferenceIndex<K, V extends EObject> {

	protected final BiMap<K, V> index;

	private final EObject object;

	protected final EReference reference;

	protected final Collection<EStructuralFeature> observedReferenceAttributes;

	protected final Function<V, K> keyCreator;

	private EReferenceIndex(final EObject object, final EReference reference, final Collection<EStructuralFeature> observedReferenceAttributes,
			final Function<V, K> keyCreator, final Collection<V> values) {
		this.object = object;
		this.reference = reference;
		this.observedReferenceAttributes = observedReferenceAttributes;
		this.keyCreator = keyCreator;

		if ((values == null) || (values.size() == 0)) {
			this.index = HashBiMap.create();
		} else {
			this.index = values.stream().collect(Collectors.toMap(
					this.keyCreator, Function.identity(), (a, b) -> b, HashBiMap::create));
		}

		final Adapter changedListener = new ChangedListener();
		this.object.eAdapters().add(changedListener);
	}

	public boolean contains(final K key) {
		return this.index.containsKey(key);
	}

	public V get(final K key) {
		return this.index.get(key);
	}

	private class ChangedListener extends EReferenceChangedListener<V> {

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

	private class ElementChangedListener extends EStructuralFeatureChangedListener<V> {

		public ElementChangedListener() {
			super(EReferenceIndex.this.observedReferenceAttributes);
		}

		@Override
		protected void notifyChanged(final V object) {
			final K key = EReferenceIndex.this.keyCreator.apply(object);
			EReferenceIndex.this.index.forcePut(key, object);
		}

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

}
