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

package kieker.analysisteetime.util.emf;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EReference;

/**
 *
 *
 * @param <T>
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public abstract class AbstractEReferenceChangedListener<T> extends AdapterImpl {

	private final EReference listenedFeature;

	public AbstractEReferenceChangedListener(final EReference listenedFeature) {
		this.listenedFeature = listenedFeature;
	}

	@Override
	public final void notifyChanged(final Notification notification) {
		if (notification.getFeature() == this.listenedFeature) {
			switch (notification.getEventType()) {
			case Notification.ADD:
				this.notifyElementAddedIntern(notification.getNewValue());
				break;
			case Notification.ADD_MANY:
				final List<?> addedOperationTypes = (List<?>) notification.getNewValue();
				addedOperationTypes.forEach(o -> this.notifyElementAddedIntern(o));
				break;
			case Notification.REMOVE:
				this.notifyElementRemovedIntern(notification.getOldValue());
				break;
			case Notification.REMOVE_MANY:
				final List<?> removedOperationTypes = (List<?>) notification.getOldValue();
				removedOperationTypes.forEach(o -> this.notifyElementRemovedIntern(o));
				break;
			default:
				break;
			}
		}

		super.notifyChanged(notification);
	}

	@SuppressWarnings("unchecked")
	private void notifyElementAddedIntern(final Object element) {
		this.notifyElementAdded((T) element);
	}

	@SuppressWarnings("unchecked")
	private void notifyElementRemovedIntern(final Object element) {
		this.notifyElementRemoved((T) element);
	}

	protected abstract void notifyElementAdded(final T element);

	protected abstract void notifyElementRemoved(final T element);

}
