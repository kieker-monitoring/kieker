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
package org.oceandsl.tools.fxca.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Reiner Jung
 * @since 1.3.0
 */
public class ContainmentHashMap<K, V extends IContainable> extends HashMap<K, V> {

    private static final long serialVersionUID = -599934940656656150L;
    private final MMObject parent;

    public ContainmentHashMap(final MMObject parent) {
        this.parent = parent;
    }

    @Override
    public V put(final K key, final V value) {
        value.setParent(this.parent);
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        m.values().forEach(value -> value.setParent(this.parent));
        super.putAll(m);
    }

    @Override
    public V putIfAbsent(final K key, final V value) {
        value.setParent(this.parent);
        return super.putIfAbsent(key, value);
    }
}
