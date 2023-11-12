/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.fxca.utils;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Henning Schnoor
 *
 * @param <T>
 *            element type
 *
 * @since 1.3.0
 */
public class IndexIterator<T> implements Iterator<T> {

    private final Supplier<Integer> getLength;
    private final Function<Integer, T> getElement;
    private int index;

    public IndexIterator(final Supplier<Integer> getLength, final Function<Integer, T> getElement) {
        this.getLength = getLength;
        this.getElement = getElement;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return this.index < this.getLength.get();
    }

    @Override
    public T next() {
        return this.getElement.apply(this.index++);
    }
}
