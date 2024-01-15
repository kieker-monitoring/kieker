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
package kieker.analysis.architecture.recovery.events;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class StorageEvent extends GenericElementEvent {

	private final String storageSignature;
	private final String storageType;

	public StorageEvent(final String hostname, final String componentSignature, final String storageSignature, final String storageType) {
		super(hostname, componentSignature);
		this.storageSignature = storageSignature;
		this.storageType = storageType;
	}

	public String getStorageSignature() {
		return this.storageSignature;
	}

	public String getStorageType() {
		return this.storageType;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof StorageEvent) {
			final StorageEvent storage = (StorageEvent) obj;
			return this.compare(this.getComponentSignature(), storage.getComponentSignature())
					&& this.compare(this.storageSignature, storage.getStorageSignature())
					&& this.compare(this.storageType, storage.getStorageSignature())
					&& this.compare(this.getHostname(), storage.getHostname());
		} else {
			return super.equals(obj);
		}
	}

	private boolean compare(final String a, final String b) {
		if ((a == null) && (b == null)) {
			return true;
		} else if (a != null) {
			return a.equals(b);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.storageSignature.hashCode() ^ this.storageType.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s>%s:%s", this.getHostname(), this.getComponentSignature(), this.storageSignature);
	}

}
