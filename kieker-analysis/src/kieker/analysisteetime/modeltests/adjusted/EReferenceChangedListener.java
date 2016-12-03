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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EReference;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public abstract class EReferenceChangedListener<T> extends AdapterImpl {

	private final EReference listenedFeature;

	public EReferenceChangedListener(final EReference listenedFeature) {
		this.listenedFeature = listenedFeature;
	}

	@Override
	public void notifyChanged(final Notification notification) {

		if (notification.getFeature() == this.listenedFeature) {
			switch (notification.getEventType()) {
			case Notification.ADD:
				// TODO Check casting
				this.notifyOperationTypeAdded((T) notification.getNewValue());
				break;
			case Notification.ADD_MANY:
				// TODO Check casting
				final List<T> addedOperationTypes = (List<T>) notification.getNewValue();
				addedOperationTypes.forEach(o -> this.notifyOperationTypeAdded(o));
				break;
			case Notification.REMOVE:
				// TODO Check casting
				this.notifyOperationTypeRemoved((T) notification.getOldValue());
				break;
			case Notification.REMOVE_MANY:
				// TODO Check casting
				final List<T> removedOperationTypes = (List<T>) notification.getOldValue();
				removedOperationTypes.forEach(o -> this.notifyOperationTypeRemoved(o));
				break;
			default:
				break;
			}
		}

		super.notifyChanged(notification);
	}

	protected abstract void notifyOperationTypeAdded(final T operationType);

	protected abstract void notifyOperationTypeRemoved(final T operationType);

}
