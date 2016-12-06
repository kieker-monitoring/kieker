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

package kieker.analysisteetime.modeltests.adjusted;

import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class EReferenceIndex<K, V extends EObject> {

	protected final BiMap<K, V> index;

	private final EObject object;

	protected final EReference reference;

	protected final Function<V, K> keyCreator;

	/*
	 * public EReferenceIndex(final EObject object, final EReference reference, final Function<V, K> keyCreator) {
	 *
	 * // TODO Bad
	 * // EList<V> values = (EList<V>) object.eGet(reference);
	 *
	 * // this(object, reference, keyCreator, values);
	 * }
	 */

	public EReferenceIndex(final EObject object, final EReference reference, final Function<V, K> keyCreator, final List<V> values) {
		this.object = object;
		this.reference = reference;
		this.keyCreator = keyCreator;

		// TODO Temp
		this.index = HashBiMap.create();
		// this.index = values.stream().collect(Collectors.toMap(keyCreator, Function.identity()));

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

		// TODO react also on changes of name of operation
		@Override
		protected void notifyElementAdded(final V element) {
			final K key = EReferenceIndex.this.keyCreator.apply(element);
			EReferenceIndex.this.index.put(key, element); // TODO forcePut?

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

	private class ElementChangedListener extends AdapterImpl {

		public ElementChangedListener() {}

		@Override
		public void notifyChanged(final Notification notification) {

			// TODO Only react on specific event types and features

			final V element = (V) notification.getNotifier();
			final K key = EReferenceIndex.this.keyCreator.apply(element);

			EReferenceIndex.this.index.forcePut(key, element);
		}

	}

}
