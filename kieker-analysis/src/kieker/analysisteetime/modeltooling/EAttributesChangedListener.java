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

package kieker.analysisteetime.modeltooling;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public abstract class EAttributesChangedListener<T> extends AdapterImpl {

	private final Collection<EAttribute> listenedFeatures;

	public EAttributesChangedListener(final Collection<EAttribute> listenedFeatures) {
		this.listenedFeatures = listenedFeatures;
	}

	@Override
	final public void notifyChanged(final Notification notification) {

		if (this.listenedFeatures.contains(notification.getFeature())) {
			if ((notification.getEventType() == Notification.SET) || (notification.getEventType() == Notification.UNSET)) {
				this.notifyChangedIntern(notification.getNotifier());
			}
		}

		super.notifyChanged(notification);
	}

	@SuppressWarnings("unchecked")
	private void notifyChangedIntern(final Object object) {
		this.notifyChanged((T) object);
	}

	protected abstract void notifyChanged(final T object);

}
